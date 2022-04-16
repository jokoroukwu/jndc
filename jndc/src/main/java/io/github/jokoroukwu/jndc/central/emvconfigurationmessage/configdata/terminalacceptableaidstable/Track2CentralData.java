package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum Track2CentralData implements NdcComponent {
    /**
     * Use the card’s magnetic stripe data in the track 2
     * buffer field of the transaction request.
     */
    DEFAULT(0),

    /**
     * Use the card’s magnetic stripe data in the track 2 buffer field of
     * the transaction request, unless there is no track 2 data
     * available, in which case use the ICC track 2 data defined in
     * 'Track 2 Data To During ICC Transaction' field.
     */
    CARD_OR_DEFINED(1),

    /**
     * Always place the ICC track 2 data defined in
     * 'Track 2 Data To During ICC Transaction' field in the
     * track 2 buffer field.
     */
    DEFINED(2);
    private final int value;
    private final String ndcStringValue;
    private final String displayedName;

    Track2CentralData(int value) {
        this.value = value;
        ndcStringValue = Integers.toEvenLengthHexString(value);
        displayedName = String.format("%s (%d)", name(), value);
    }

    public static DescriptiveOptional<Track2CentralData> forValue(int value) {
        switch (value) {
            case 0: {
                return DescriptiveOptional.of(DEFAULT);
            }
            case 1: {
                return DescriptiveOptional.of(CARD_OR_DEFINED);
            }
            case 2: {
                return DescriptiveOptional.of(DEFINED);
            }
            default: {
                return DescriptiveOptional.empty(() -> String.format("%d is not a valid 'Track 2 Data for Central' value", value));
            }
        }
    }

    public static DescriptiveOptional<Track2CentralData> forValue(String value) {
        return forValue(Integer.parseInt(value));
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return displayedName;
    }

    @Override
    public String toNdcString() {
        return ndcStringValue;
    }
}
