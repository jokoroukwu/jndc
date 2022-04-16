package io.github.jokoroukwu.jndc.tlv;

import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.Strings;

public final class TransactionType extends AbstractBerTlv<String> {
    public static final String NAME = "Transaction Type";
    public static final int TAG = 0x9C;
    public static final int OCTET_LENGTH = 1;
    public static final NdcComponentReader<DescriptiveOptional<BerTlv<String>>> READER = new HexStringBerTLVReader(TAG, OCTET_LENGTH, NAME);

    private final String value;

    public TransactionType(String value) {
        ObjectUtils.validateNotNull(value, NAME + " value");
        Integers.validateIsExactValue(value.length(), 2, NAME + " length");
        Strings.validateIsHex(value, NAME + " value");
        this.value = value;
    }

    @Override
    public String toNdcString() {
        return "9C01" + value;
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
