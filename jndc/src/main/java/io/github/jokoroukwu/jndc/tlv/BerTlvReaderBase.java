package io.github.jokoroukwu.jndc.tlv;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;

public enum BerTlvReaderBase implements BerTlvReader {

    INSTANCE;

    @Override
    public DescriptiveOptionalInt tryReadTag(NdcCharBuffer messageStream) {
        return messageStream.tryReadHexInt(2)
                .flatMap(value -> hasSubsequentTagBytes(value)
                        ? readSubsequentTagBytes(messageStream, value, 1)
                        : DescriptiveOptionalInt.of(value));
    }

    private boolean hasSubsequentTagBytes(int value) {
        return (value & 0b00011111) == 0b00011111;
    }

    private DescriptiveOptionalInt readSubsequentTagBytes(NdcCharBuffer buffer, int highBytes, int byteNumber) {
        if (byteNumber > 3) {
            //  all EMV tags should fit in 4 bytes
            return DescriptiveOptionalInt.empty(() -> "tag overflows integer capacity");
        }
        //  read next tag byte
        return buffer.tryReadHexInt(2)
                .flatMap(value -> isLastByte(value)
                        ? DescriptiveOptionalInt.of(appendLowBytes(highBytes, value))
                        : readSubsequentTagBytes(buffer, appendLowBytes(highBytes, value), byteNumber + 1));
    }

    @Override
    public DescriptiveOptionalInt tryReadLength(NdcCharBuffer buffer) {
        return buffer.tryReadHexInt(2)
                .flatMap(value -> isLastByte(value)
                        ? DescriptiveOptionalInt.of(value & 0b01111111)
                        : mergeLength(value, buffer));
    }

    private DescriptiveOptionalInt mergeLength(int firstByte, NdcCharBuffer messageStream) {
        var byteCount = firstByte & 0b01111111;
        return messageStream.tryReadHexLong(byteCount * 2)
                //  the length may be arbitrary
                .filter(this::isWithinIntRange, length -> () -> String.format("length (%d) overflows integer capacity", length))
                .toInt();
    }

    private int appendLowBytes(int highBytes, int lowBytes) {
        return (highBytes << Byte.SIZE) | lowBytes;
    }

    private boolean isLastByte(int value) {
        return (value & 0b10000000) == 0;
    }

    private boolean isWithinIntRange(long value) {
        return value <= Integer.MAX_VALUE;
    }
}
