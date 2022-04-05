package io.github.jokoroukwu.jndc.tlv;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public class HexStringBerTLVReader implements NdcComponentReader<DescriptiveOptional<BerTlv<String>>> {
    public static final HexStringBerTLVReader DEFAULT = new HexStringBerTLVReader(-1, -1, "tag");
    private final String name;
    private final int tag;
    private final int length;
    private final BerTlvReader bertlvReader;

    public HexStringBerTLVReader(String name, int tag, int length, BerTlvReader berTlvReader) {
        this.name = ObjectUtils.validateNotNull(name, "Tag name");
        this.bertlvReader = ObjectUtils.validateNotNull(berTlvReader, "BerTlvReader");
        this.length = length;
        this.tag = tag;
    }

    public HexStringBerTLVReader(int tag, int length, String name) {
        this(name, tag, length, BerTlvReaderBase.INSTANCE);
    }

    @Override
    public DescriptiveOptional<BerTlv<String>> readComponent(NdcCharBuffer buffer) {
        return bertlvReader.tryReadTag(buffer)
                .mapDescription(errorMessage -> String.format("failed to read %s: %s", tag, errorMessage))
                .filter(this::isExpectedTag, actual -> () -> String.format("expected %s (%d) tag but was: %d", name, tag, actual))
                .flatMapToObject(tag -> bertlvReader.tryReadLength(buffer)
                        .mapDescription(errorMessage -> String.format("failed to read %s length: %s", tag, errorMessage))
                        .filter(this::isExpectedLength, actual -> ()
                                -> String.format("%s length must be equal to %d but was: %d", tag, length, actual))
                        .flatMapToObject(length -> toBerTlv(buffer, tag, length)));
    }

    private boolean isExpectedTag(int value) {
        //  negative tag value corresponds to any tag
        return tag < 0 || tag == value;
    }

    private boolean isExpectedLength(int value) {
        //  negative length value corresponds to variable tag length
        return length < 0 || value == length;
    }

    private DescriptiveOptional<BerTlv<String>> toBerTlv(NdcCharBuffer buffer, int tag, int length) {
        return buffer.tryReadCharSequence(length * 2)
                .<BerTlv<String>>map(value -> new HexStringBerTlv(tag, length, value))
                .wrapDescription(errorMessage -> String.format("failed to read %s value: %s", tag, errorMessage));
    }

}
