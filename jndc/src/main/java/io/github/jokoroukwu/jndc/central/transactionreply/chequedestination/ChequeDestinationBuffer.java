package io.github.jokoroukwu.jndc.central.transactionreply.chequedestination;

import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;

public enum ChequeDestinationBuffer implements IdentifiableBuffer {
    POCKET_1('1'),
    POCKET_2('2'),
    POCKET_3('3'),
    EJECT('E');

    public static final char ID = 'a';
    private final char value;

    ChequeDestinationBuffer(char value) {
        this.value = value;
    }

    public static DescriptiveOptional<ChequeDestinationBuffer> forValue(char value) {
        switch (value) {
            case '1':
                return DescriptiveOptional.of(POCKET_1);
            case '2':
                return DescriptiveOptional.of(POCKET_2);
            case '3':
                return DescriptiveOptional.of(POCKET_3);
            case 'E':
                return DescriptiveOptional.of(EJECT);
            default: {
                return DescriptiveOptional.empty(() -> String.format("'%c' is not a valid 'Cheque Destination' value", value));
            }
        }
    }

    public static DescriptiveOptional<ChequeDestinationBuffer> forValue(int value) {
        return forValue((char) value);
    }

    @Override
    public String toNdcString() {
        return String.valueOf(ID) + value;
    }

    @Override
    public char getId() {
        return ID;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ChequeDestinationBuffer.class.getSimpleName() + ": {", "}")
                .add("id: '" + ID + "'")
                .add("value: " + value)
                .toString();
    }
}
