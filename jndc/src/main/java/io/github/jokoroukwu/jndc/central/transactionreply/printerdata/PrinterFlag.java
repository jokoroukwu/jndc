package io.github.jokoroukwu.jndc.central.transactionreply.printerdata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum PrinterFlag implements NdcComponent {
    NO_PRINT('0', "Do not print ('0')"),
    JOURNAL_PRINTER('1', "Print on journal printer only ('1')"),
    RECEIPT_PRINTER('2', "Print on receipt printer only ('2')"),
    RECEIPT_AND_JOURNAL('3', "Print on receipt and journal printer ('3')"),
    PDD('4', "Print on PPD ('4')"),
    PDD_AND_JOURNAL('5', "Print on PPD and journal ('5')"),
    STATEMENT_PRINTER('8', "Print on statement printer only ('8')"),
    PASSBOOK(':', "Print on passbook (':')"),
    RECEIPT_SIDEWAYS('=', "Print sideways on the receipt printer ('=')"),
    AUDIO_DATA('>', "Audio data for a voice‐guided session ('>')"),
    STAMP_CHEQUE('a', "Stamp cheque ('a')"),
    ENDORSE_CHEQUE('b', "Endorse cheque ('b')"),
    RESERVED('9', "Reserved ('9')");

    private final char value;
    private final String displayedName;

    PrinterFlag(char value, String displayedName) {
        this.value = value;
        this.displayedName = displayedName;
    }

    public static DescriptiveOptional<PrinterFlag> forValue(char value) {
        switch (value) {
            case '0':
                return DescriptiveOptional.of(NO_PRINT);
            case '1':
                return DescriptiveOptional.of(JOURNAL_PRINTER);
            case '2':
                return DescriptiveOptional.of(RECEIPT_PRINTER);
            case '3':
                return DescriptiveOptional.of(RECEIPT_AND_JOURNAL);
            case '4':
                return DescriptiveOptional.of(PDD);
            case '5':
                return DescriptiveOptional.of(PDD_AND_JOURNAL);
            case '8':
                return DescriptiveOptional.of(STATEMENT_PRINTER);
            case ':':
                return DescriptiveOptional.of(PASSBOOK);
            case '=':
                return DescriptiveOptional.of(RECEIPT_SIDEWAYS);
            case '>':
                return DescriptiveOptional.of(AUDIO_DATA);
            case 'a':
                return DescriptiveOptional.of(STAMP_CHEQUE);
            case 'b':
                return DescriptiveOptional.of(ENDORSE_CHEQUE);
            case '9':
                return DescriptiveOptional.of(RESERVED);
            default: {
                return DescriptiveOptional.empty(() -> String.format("value '%c' is not a valid 'Printer Flag'", value));
            }
        }
    }

    public static DescriptiveOptional<PrinterFlag> forValue(int value) {
        return forValue((char) value);
    }


    public char getValue() {
        return value;
    }

    @Override
    public String toString() {
        return displayedName;
    }

    @Override
    public String toNdcString() {
        return Character.toString(value);
    }
}
