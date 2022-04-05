package io.github.jokoroukwu.jndc.tlv;

import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.Strings;

public final class TransactionCurrencyExponent extends AbstractBerTlv<String> {
    public static final String NAME = "Transaction Currency Exponent";
    public static final int TAG = 0x5F36;
    public static final int OCTET_LENGTH = 1;
    public static final NdcComponentReader<DescriptiveOptional<BerTlv<String>>> READER = new HexStringBerTLVReader(TAG, OCTET_LENGTH, NAME);

    private final String value;

    public TransactionCurrencyExponent(String value) {
        ObjectUtils.validateNotNull(value, NAME + " value");
        Integers.validateIsExactValue(value.length(), 2, NAME + " length");
        Strings.validateIsHex(value, NAME + " value");
        this.value = value;
    }

    @Override
    public String toNdcString() {
        return "5F3601" + value;
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
