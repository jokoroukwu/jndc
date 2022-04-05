package io.github.jokoroukwu.jndc.tlv;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Contains the data objects (with tags and lengths)
 * returned by the ICC in response to a command.
 */
public class ResponseFormat2 extends CompositeTlv<String> {
    public static final int TAG = 0x77;
    public static final String NAME = "Response Message Template Format 2 (77)";

    public ResponseFormat2(Collection<BerTlv<String>> value) {
        super(TAG, value);
    }

    //  is used internally by the corresponding reader
    ResponseFormat2(int length, LinkedHashMap<Integer, BerTlv<String>> value) {
        super(TAG, length, value);
    }

    @SafeVarargs
    public static ResponseFormat2 of(BerTlv<String>... berTlvs) {
        return new ResponseFormat2(Arrays.asList(berTlvs));
    }

}
