package io.github.jokoroukwu.jndc.trackdata;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

public class Track1DataBuffer extends TrackDataBuffer {
    public static final int MAX_DATA_LENGTH = 78;
    public static final char TRANSACTION_REQUEST_ID = '1';
    public static final char TRANSACTION_REPLY_ID = 'K';

    private final char id;
    private final String value;

    Track1DataBuffer(char id, String value) {
        this.id = id;
        this.value = value;
    }

    public static Track1DataBuffer requestBuffer(String value) {
        return new Track1DataBuffer(TRANSACTION_REQUEST_ID, validateTrack1Data(value));
    }

    public static Track1DataBuffer replyBuffer(String value) {
        return new Track1DataBuffer(TRANSACTION_REPLY_ID, validateTrack1Data(value));
    }

    public static String validateTrack1Data(String data) {
        ObjectUtils.validateNotNull(data, "'Track 1 Data'");
        Integers.validateRange(data.length(), 1, MAX_DATA_LENGTH, "'Track 1 Data' length");
        Strings.validateCharRange(data, ' ', '_', "'Track 1 Data'");
        return data;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public char getId() {
        return id;
    }
}
