package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum Track2ICCData implements NdcComponent {
    /**
     * (Default) Use the Track 2 Equivalent Data (tag 0x57) for all
     * NDC processing, but if it is not supplied by the ICC, produce
     * an ICC level fake error and terminate the ICC processing.
     */
    DEFAULT(0),
    /**
     * Use the Track 2 Equivalent Data (tag 0x57) for all NDC
     * processing, but if it is not supplied by the ICC, use the track 2
     * data from the magnetic card.
     */
    USE_TAG_OR_CARD(1),
    /**
     * Always use the track 2 data from the magnetic card.
     */
    USE_CARD(2),
    /**
     * Use the Track 2 Equivalent Data (tag 0x57) for all NDC
     * processing, but if it is not supplied by the ICC, construct
     * simulated Track 2 data.
     */
    USE_TAG_OR_SIMULATE(3),
    /**
     * Always construct simulated track 2 data.
     */
    SIMULATE(4);

    private final int value;
    private final String ndcStringValue;
    private final String displayedName;

    Track2ICCData(int value) {
        this.value = value;
        ndcStringValue = Integers.toEvenLengthHexString(value);
        displayedName = String.format("%s (%d)", name(), value);
    }

    public static DescriptiveOptional<Track2ICCData> forValue(int value) {
        switch (value) {
            case 0: {
                return DescriptiveOptional.of(DEFAULT);
            }
            case 1: {
                return DescriptiveOptional.of(USE_TAG_OR_CARD);
            }
            case 2: {
                return DescriptiveOptional.of(USE_CARD);
            }
            case 3: {
                return DescriptiveOptional.of(USE_TAG_OR_SIMULATE);
            }
            case 4: {
                return DescriptiveOptional.of(SIMULATE);
            }
            default: {
                return DescriptiveOptional.empty(()
                        -> String.format("%s is not a valid 'Track 2 Data To Be Used During ICC Transaction' value", value));
            }
        }
    }

    public static DescriptiveOptional<Track2ICCData> forValue(String value) {
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
