package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;

public enum AppSelectionIndicator implements NdcComponent {
    /**
     * (Default) During the candidate list build processing, any
     * application which is a partial match will be added to the
     * candidate list.
     * Note: A partial match occurs when the AID used in the
     * SELECT command is shorter than the DF Name and exactly
     * matches for all the characters present. The DF name is
     * returned in the response to a SELECT command and can be
     * the same length or longer than the AID used in the SELECT
     * command.
     * For example, if a card contains these applications:
     * ● A0000000001231
     * ● A0000000001232
     * ● A0000000004321
     * An AID of A00000000012, will select two applications, if the
     * card itself supports partial AID selection. An AID of
     * A000000000, will select all three.
     */
    PARTIAL_MATCH(0),
    /**
     * Use the card’s magnetic stripe data in the track 2 buffer field of
     * the transaction request, unless there is no track 2 data
     * available, in which case use the ICC track 2 data defined in
     * field f19.
     */
    FULL_MATCH(1);

    private final int value;
    private final String stringValue;

    AppSelectionIndicator(int value) {
        this.value = value;
        stringValue = Integers.toEvenLengthHexString(value);
    }

    public static DescriptiveOptional<AppSelectionIndicator> forValue(int value) {
        if (value == 0) {
            return DescriptiveOptional.of(PARTIAL_MATCH);
        }
        if (value == 1) {
            return DescriptiveOptional.of(FULL_MATCH);
        }
        return DescriptiveOptional.empty(() -> String.format("%d is not a valid 'Application Selection Indicator' value", value));
    }

    public static DescriptiveOptional<AppSelectionIndicator> forValue(String value) {
        return forValue(Integer.parseInt(value));
    }


    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "Application Selection Indicator: {", "}")
                .add("value: " + value)
                .toString();
    }

    @Override
    public String toNdcString() {
        return stringValue;
    }
}
