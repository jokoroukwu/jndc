package io.github.jokoroukwu.jndc.tlv;

import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public final class TransactionCategoryCode extends AbstractBerTlv<String> {
    public static final String NAME = "Transaction Category Code";
    public static final int LENGTH = 1;
    public static final int TAG = 0x9F53;
    public static final NdcComponentReader<DescriptiveOptional<BerTlv<String>>> READER = new HexStringBerTLVReader(TAG, LENGTH, NAME);
    private final String value;

    public TransactionCategoryCode(String value) {
        Integers.validateIsExactHexValue(value.length(), 2, NAME + " length");
        this.value = value;
    }

    @Override
    public String toNdcString() {
        return "9F5301" + value;
    }

    @Override
    public int getLength() {
        return LENGTH;
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
