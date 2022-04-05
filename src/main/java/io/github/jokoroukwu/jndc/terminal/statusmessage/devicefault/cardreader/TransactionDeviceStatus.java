package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum TransactionDeviceStatus implements NdcComponent {
    NO_EXCEPTION('0', "No Exception ('0')"),
    CARD_WITHDRAWAL_TIMEOUT('1', "Card Withdrawal Timeout ('1')"),
    CARD_EJECT_FAILURE('2', "Card Eject Failure ('2')"),
    TRACK_UPDATE_FAILURE('3', "Track Update Failure ('3')"),
    INVALID_HOST_TRACK_DATA('4', "Invalid Host Track Data ('4')"),
    TRACK_DATA_ERROR('5', "Track Data Error ('5')");

    private final char value;
    private final String displayedName;

    TransactionDeviceStatus(char value, String displayedName) {
        this.value = value;
        this.displayedName = displayedName;
    }

    public static DescriptiveOptional<TransactionDeviceStatus> forValue(char value) {
        switch (value) {
            case '0':
                return DescriptiveOptional.of(NO_EXCEPTION);
            case '1':
                return DescriptiveOptional.of(CARD_WITHDRAWAL_TIMEOUT);
            case '2':
                return DescriptiveOptional.of(CARD_EJECT_FAILURE);
            case '3':
                return DescriptiveOptional.of(TRACK_UPDATE_FAILURE);
            case '4':
                return DescriptiveOptional.of(INVALID_HOST_TRACK_DATA);
            case '5':
                return DescriptiveOptional.of(TRACK_DATA_ERROR);
            default: {
                return DescriptiveOptional.empty(()
                        -> String.format("value '%c' is not a valid 'Transaction/Device Status'", value));
            }
        }
    }

    public static DescriptiveOptional<TransactionDeviceStatus> forValue(int value) {
        return forValue((char) value);
    }

    @Override
    public String toNdcString() {
        return String.valueOf(value);
    }


    @Override
    public String toString() {
        return displayedName;
    }
}
