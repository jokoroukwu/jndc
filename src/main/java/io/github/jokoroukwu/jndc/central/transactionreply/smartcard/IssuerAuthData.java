package io.github.jokoroukwu.jndc.central.transactionreply.smartcard;

import io.github.jokoroukwu.jndc.tlv.AbstractBerTlv;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

public class IssuerAuthData extends AbstractBerTlv<String> {
    public static final String NAME = "Issuer Authentication Data";
    public static final int TAG = 0x91;
    private final String value;

    IssuerAuthData(String value, Void unused) {
        this.value = value;
    }

    public IssuerAuthData(String value) {
        ObjectUtils.validateNotNull(value, NAME + " value");
        Integers.validateHexRange(value.length(), 16, 32, NAME + " length");
        Integers.validateIsEven(value.length(), NAME + " length");
        Strings.validateIsHex(value, NAME + " value");
        this.value = value;
    }

    @Override
    public String toNdcString() {
        return "91" + encodeLength(value.length() / 2) + value;
    }

    @Override
    public int getTag() {
        return TAG;
    }

    @Override
    public int getLength() {
        return value.length();
    }

    @Override
    public String getValue() {
        return value;
    }
}
