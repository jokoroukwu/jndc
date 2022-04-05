package io.github.jokoroukwu.jndc.trackdata;

import io.github.jokoroukwu.jndc.util.text.Strings;

public class Track2DataBuffer extends TrackDataBuffer {
    public static final char ID = 'L';

    private final String value;

    public Track2DataBuffer(String value) {
        Strings.validateNotNullNotEmpty(value, "Track 2 Data");
        this.value = validateTrackData(value, 39, "Track 2 Data");
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public char getId() {
        return ID;
    }
}
