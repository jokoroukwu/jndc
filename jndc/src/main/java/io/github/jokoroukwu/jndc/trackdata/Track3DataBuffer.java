package io.github.jokoroukwu.jndc.trackdata;

import io.github.jokoroukwu.jndc.util.text.Strings;

public class Track3DataBuffer extends TrackDataBuffer {
    public static final char ID = '4';
    private final String value;

    public Track3DataBuffer(String value) {
        Strings.validateNotNullNotEmpty(value, "Track 3 Data");
        this.value = validateTrackData(value, 106, "Track 3 Data");
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
