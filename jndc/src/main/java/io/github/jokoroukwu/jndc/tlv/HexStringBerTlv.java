package io.github.jokoroukwu.jndc.tlv;

import io.github.jokoroukwu.jndc.PatternValidatorBase;
import io.github.jokoroukwu.jndc.util.Integers;

public class HexStringBerTlv extends AbstractBerTlv<String> {
    private final int tag;
    private final int length;
    private final String value;

    //  no-validation constructor;
    //  is used internally by the corresponding reader
    HexStringBerTlv(int tag, int length, String value) {
        this.tag = tag;
        this.length = length;
        this.value = value;
    }

    public HexStringBerTlv(int tag, String value) {
        Integers.validateNotNegative(tag, "Tag");
        Integers.validateIsEven(value.length(), "Tag length");
        PatternValidatorBase.INSTANCE.validateIsHex(value, "Tag value");
        this.tag = tag;
        this.length = value.length() / 2;
        this.value = value;
    }


    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toNdcString() {
        return Integers.toEvenLengthHexString(tag)
                + encodeLength(length)
                + value;
    }

}
