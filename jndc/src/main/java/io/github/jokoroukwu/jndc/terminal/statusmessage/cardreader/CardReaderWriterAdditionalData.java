package io.github.jokoroukwu.jndc.terminal.statusmessage.cardreader;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public final class CardReaderWriterAdditionalData implements NdcComponent {
    private final String track1Data;
    private final String track2Data;
    private final String track3Data;

    public CardReaderWriterAdditionalData(String track1Data, String track2Data, String track3Data) {
        this.track1Data = ObjectUtils.validateNotNull(track1Data, "track1Data");
        this.track2Data = ObjectUtils.validateNotNull(track2Data, "track2Data");
        this.track3Data = ObjectUtils.validateNotNull(track3Data, "track3Data");
    }


    public String getTrack1Data() {
        return track1Data;
    }

    public String getTrack2Data() {
        return track2Data;
    }

    public String getTrack3Data() {
        return track3Data;
    }

    @Override
    public String toNdcString() {
        return track1Data
                + NdcConstants.GROUP_SEPARATOR
                + track2Data
                + NdcConstants.GROUP_SEPARATOR
                + track3Data;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CardReaderWriterAdditionalData.class.getSimpleName() + ": {", "}")
                .add("track1Data: '" + track1Data + '\'')
                .add("track2Data: '" + track2Data + '\'')
                .add("track3Data: '" + track3Data + '\'')
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardReaderWriterAdditionalData that = (CardReaderWriterAdditionalData) o;
        return track1Data.equals(that.track1Data) && track2Data.equals(that.track2Data) && track3Data.equals(that.track3Data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(track1Data, track2Data, track3Data);
    }
}
