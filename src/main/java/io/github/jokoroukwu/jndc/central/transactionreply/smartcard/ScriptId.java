package io.github.jokoroukwu.jndc.central.transactionreply.smartcard;

import io.github.jokoroukwu.jndc.tlv.HexStringBerTlv;
import io.github.jokoroukwu.jndc.util.Integers;

public class ScriptId extends HexStringBerTlv {
    public static final String NAME = "Script ID Tag";
    public static final int TAG = 0x9F18;

    public ScriptId(String value) {
        super(TAG, value);
        Integers.validateRange(value.length(), 0, 8, NAME + " length");
    }
}
