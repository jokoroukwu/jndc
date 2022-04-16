package io.github.jokoroukwu.jndc.central.transactionreply.functionid;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum StandardFunction implements FunctionId, NdcComponent {
    DEPOSIT_PRINT('1', "Deposit and print ('1' or '7')"),

    DISPENSE_PRINT('2', "Dispense and print ('2' or '8')"),

    DISPLAY_PRINT('3', "Display and print ('3' or '9')"),

    PRINT_IMMEDIATE('4', "Print immediate ('4')"),

    SET_NEXT_STATE_PRINT('5', "Set next state and print ('5')"),

    NIGHT_SAFE_DEPOSIT_PRINT('6', "Night safe deposit and print ('6')"),

    EJECT_CARD_DISPENSE_PRINT('A', "Eject card, dispense, print ('A')"),

    PARALLEL_DISPENSE_PRINT_EJECT('B', "Parallel dispense, print, card eject ('B')"),

    CARD_PARALLEL_DISPENSE_PRINT('F', "Card before parallel dispense/print ('F')"),

    PRINT_STATEMENT_WAIT('P', "Print statement and wait ('P')"),

    PRINT_STATEMENT_SET_NEXT_STATE('Q', "Print statement and set next state ('Q')"),

    PRINT_PASSBOOK_SET_NEXT_STATE('#', "Print passbook and set next state ('#')"),

    PRINT_PASSBOOK_WAIT('%', "Print passbook and wait ('%')"),

    REFUND_NOTES_PRINT('*', "Refund notes and print ('*')"),

    DEPOSIT_NOTES_PRINT('‐', "Refund notes and print ('‐')"),

    DEPOSIT_NOTES_WAIT('’', "Deposit notes and print ('’')"),

    PROCESS_CHEQUE(':', "Process cheque (':')"),

    DISPENSE_NOTES_MEDIA_EXCHANGE('b', "Dispense notes first during valuable media exchange ('b')"),

    PROCESS_MULTI_CHEQUES('c', "Process multiple cheques ('c')"),

    PRINT_MEDIA_SET_NEXT_STATE('e', "Print valuable media and set next state ('e')"),

    DEPOSIT_MEDIA('f', "Deposit media first during valuable media exchange ('f')");


    private final char id;
    private final String displayedName;

    StandardFunction(char id, String displayedName) {
        this.id = id;
        this.displayedName = displayedName;
    }

    public static DescriptiveOptional<FunctionId> forValue(char value) {
        switch (value) {
            case '1':
            case '7':
                return DescriptiveOptional.of(DEPOSIT_PRINT);
            case '2':
            case '8':
                return DescriptiveOptional.of(DISPENSE_PRINT);
            case '3':
            case '9':
                return DescriptiveOptional.of(DISPLAY_PRINT);
            case '4':
                return DescriptiveOptional.of(PRINT_IMMEDIATE);
            case '5':
                return DescriptiveOptional.of(SET_NEXT_STATE_PRINT);
            case '6':
                return DescriptiveOptional.of(NIGHT_SAFE_DEPOSIT_PRINT);
            case 'A':
                return DescriptiveOptional.of(EJECT_CARD_DISPENSE_PRINT);
            case 'B':
                return DescriptiveOptional.of(PARALLEL_DISPENSE_PRINT_EJECT);
            case 'F':
                return DescriptiveOptional.of(CARD_PARALLEL_DISPENSE_PRINT);
            case 'P':
                return DescriptiveOptional.of(PRINT_STATEMENT_WAIT);
            case 'Q':
                return DescriptiveOptional.of(PRINT_STATEMENT_SET_NEXT_STATE);
            case '#':
                return DescriptiveOptional.of(PRINT_PASSBOOK_SET_NEXT_STATE);
            case '%':
                return DescriptiveOptional.of(PRINT_PASSBOOK_WAIT);
            case '*':
                return DescriptiveOptional.of(REFUND_NOTES_PRINT);
            case '‐':
                return DescriptiveOptional.of(DEPOSIT_NOTES_PRINT);
            case '’':
                return DescriptiveOptional.of(DEPOSIT_NOTES_WAIT);
            case ':':
                return DescriptiveOptional.of(PROCESS_CHEQUE);
            case 'b':
                return DescriptiveOptional.of(DISPENSE_NOTES_MEDIA_EXCHANGE);
            case 'c':
                return DescriptiveOptional.of(PROCESS_MULTI_CHEQUES);
            case 'e':
                return DescriptiveOptional.of(PRINT_MEDIA_SET_NEXT_STATE);
            case 'f':
                return DescriptiveOptional.of(DEPOSIT_MEDIA);
            default: {
                return DescriptiveOptional.empty(() -> String.format("value '%c' is not a valid 'Function Identifier'", value));
            }
        }
    }

    public static DescriptiveOptional<FunctionId> forValue(int value) {
        return forValue((char) value);
    }

    @Override
    public char getId() {
        return id;
    }

    @Override
    public String toNdcString() {
        return Character.toString(id);
    }

    @Override
    public String toString() {
        return displayedName;
    }
}
