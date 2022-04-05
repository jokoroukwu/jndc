package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum DepositTransactionDirection {
    NOT_CASH_DEPOSIT('0'),
    VAULT_DIRECTION('1'),
    REFUND_DIRECTION('2');

    private final char value;

    DepositTransactionDirection(char value) {
        this.value = value;
    }

    public static DescriptiveOptional<DepositTransactionDirection> forValue(char value) {
        switch (value) {
            case '0':
                return DescriptiveOptional.of(NOT_CASH_DEPOSIT);
            case '1':
                return DescriptiveOptional.of(VAULT_DIRECTION);
            case '2':
                return DescriptiveOptional.of(REFUND_DIRECTION);
            default: {
                final String messageTemplate = "value '%c' does not match any 'Last Cash Deposit Transaction Direction'";
                return DescriptiveOptional.empty(() -> String.format(messageTemplate, value));
            }
        }
    }

    public static DescriptiveOptional<DepositTransactionDirection> forValue(int value) {
        return forValue((char) value);
    }

    public char value() {
        return value;
    }

}
