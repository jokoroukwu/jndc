package io.github.jokoroukwu.jndc.tlv;

import io.github.jokoroukwu.jndc.util.ByteUtils;
import io.github.jokoroukwu.jndc.util.Integers;

import java.util.Objects;
import java.util.StringJoiner;

public abstract class AbstractBerTlv<V> implements BerTlv<V> {

    protected String encodeLength(int length) {
        if (length <= 127) {
            //  length is encoded within a single byte
            return Integers.toZeroPaddedHexString(length, 2);
        }
        //  first byte defines the number of length bytes
        final int octetCount = ByteUtils.numberOfOctets(length);
        return Integers.toZeroPaddedHexString(0b10000000 | octetCount, 2) + Integers.toEvenLengthHexString(length);
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + ": {", "}")
                .add("tag: " + Integers.toEvenLengthHexString(getTag()))
                .add("length: " + getLength())
                .add("value: " + getValue())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BerTlv)) return false;
        BerTlv<?> that = (BerTlv<?>) o;
        return getTag() == that.getTag() &&
                getLength() == that.getLength() &&
                Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTag(), getLength(), getValue());
    }
}
