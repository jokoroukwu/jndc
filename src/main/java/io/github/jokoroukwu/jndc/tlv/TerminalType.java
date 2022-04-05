package io.github.jokoroukwu.jndc.tlv;

import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.Strings;

public final class TerminalType extends AbstractBerTlv<String> {
    public static final int TAG = 0x9F35;
    public static final int OCTET_LENGTH = 1;
    public static final String NAME = "Terminal Type";
    public static final NdcComponentReader<DescriptiveOptional<BerTlv<String>>> READER = new HexStringBerTLVReader(TAG, OCTET_LENGTH, NAME);
    private final String value;

    public TerminalType(String value) {
        ObjectUtils.validateNotNull(value, NAME + " value");
        Integers.validateIsExactValue(value.length(), 2, NAME + " length");
        Strings.validateIsHex(value, NAME + " value");
        this.value = value;
    }

    @Override
    public String toNdcString() {
        return "9F3501" + value;
    }

    @Override
    public int getLength() {
        return OCTET_LENGTH;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public int getTag() {
        return TAG;
    }
}
