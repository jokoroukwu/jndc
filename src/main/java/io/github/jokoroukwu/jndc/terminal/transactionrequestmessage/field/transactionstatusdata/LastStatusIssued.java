package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

/**
 * Last Status Issued contains one byte identifying what the last known
 * status message sent from the SST was (other than download‐type
 * messages). It does not necessarily relate to the Transaction Serial Number.
 */
public enum LastStatusIssued {
    NONE_SENT('0'),
    GOOD_TERMINATION('1'),
    ERROR('2'),
    TRANSACTION_REPLY_REJECTED('3');

    private final char value;

    LastStatusIssued(char value) {
        this.value = value;
    }

    public static DescriptiveOptional<LastStatusIssued> forValue(char value) {
        switch (value) {
            case '0':
                return DescriptiveOptional.of(NONE_SENT);
            case '1':
                return DescriptiveOptional.of(GOOD_TERMINATION);
            case '2':
                return DescriptiveOptional.of(ERROR);
            case '3':
                return DescriptiveOptional.of(TRANSACTION_REPLY_REJECTED);
            default:
                return DescriptiveOptional.empty(()
                        -> String.format("'%c' does not match any 'Last Status Issued' value", value));
        }
    }

    public static DescriptiveOptional<LastStatusIssued> forValue(int value) {
        return forValue((char) value);
    }

    public char getValue() {
        return value;
    }
}
