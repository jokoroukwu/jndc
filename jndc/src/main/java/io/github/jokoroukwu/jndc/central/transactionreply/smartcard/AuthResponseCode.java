package io.github.jokoroukwu.jndc.central.transactionreply.smartcard;

import io.github.jokoroukwu.jndc.tlv.AbstractBerTlv;
import io.github.jokoroukwu.jndc.util.text.Strings;

public class AuthResponseCode extends AbstractBerTlv<String> {
    public static final String NAME = "Authorisation Response Code";
    public static final int TAG = 0x8A;
    public static final int OCTET_LENGTH = 2;

    private final String value;

    AuthResponseCode(String value, Void unused) {
        this.value = value;
    }

    public AuthResponseCode(String value) {
        Strings.validateLength(value, 4, NAME + " length");
        Strings.validateIsHex(value, NAME + " value");
        this.value = value;
    }

    @Override
    public String toNdcString() {
        return "8A02" + value;
    }

    @Override
    public int getTag() {
        return TAG;
    }

    @Override
    public int getLength() {
        return OCTET_LENGTH;
    }

    @Override
    public String getValue() {
        return value;
    }
}
