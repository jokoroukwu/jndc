package io.github.jokoroukwu.jndc.terminal.dig;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum Dig implements NdcComponent {
    TIME_OF_DAY_CLOCK('A', "Time‐of‐Day Clock ('A')"),
    COMMUNICATIONS('B', "Communications ('B')"),
    SYSTEM_DISK('C', "System Disk ('C')"),
    MAGNETIC_CARD_READER_WRITER('D', "Magnetic Card Reader/Writer ('D')"),
    CASH_HANDLER('E', "Cash Handler ('E')"),
    ENVELOPE_DEPOSITORY('F', "Envelope Depository ('F')"),
    RECEIPT_PRINTER('G', "Receipt Printer ('G')"),
    JOURNAL_PRINTER('H', "Journal Printer ('H')"),
    RESERVED_I('I', "Reserved ('I')"),
    RESERVED_J('J', "Reserved ('J')"),
    NIGHT_SAFE_DEPOSITORY('K', "Night Safe Depository ('K')"),
    ENCRYPTOR('L', "Encryptor ('L')"),
    SECURITY_CAMERA('M', "Security Camera ('M')"),
    DOOR_ACCESS_SYSTEM('N', "Door Access System ('N')"),
    OFF_LINE_FLEX_DISK('O', "Off‐Line Flex Disk ('O')"),
    TI_BINS('P', "TI Bins (Alarms) ('P')"),
    CARDHOLDER_KEYBOARD('Q', "Cardholder Keyboard ('Q')"),
    OPERATOR_KEYBOARD('R', "Operator Keyboard ('R')"),
    CARDHOLDER_DISPLAY_VOICE('S', "Cardholder Display/Voice ('S')"),
    RESERVED_T('T', "Reserved ('T')"),
    RESERVED_U('U', "Reserved ('U')"),
    STATEMENT_PRINTER('V', "Statement Printer ('V')"),
    RESERVED_W('W', "Reserved ('W')"),
    PASSBOOK('X', "Passbook ('X')"),
    COIN_DISPENSER('Y', "Coin Dispenser ('Y')"),
    SYSTEM_DISPLAY('Z', "System Display ('Z')"),
    MEDIA_ENTRY_INDICATORS('[', "Media Entry Indicators  '['"),
    ENVELOPE_DISPENSER('\\', "Envelope Dispenser"),
    VOICE_GUIDANCE('a', "Voice Guidance ('a')"),
    CASH_HANDLER_0('d', "Cash Handler 0 ('d')"),
    CASH_HANDLER_1('e', "Cash Handler 1 ('e')"),
    BARCODE_READER('f', "Barcode Reader ('f')"),
    UPS('j', "UPS ('j')"),
    CHEQUE_PROCESSOR('q', "Cheque Processor ('q')"),
    NOTE_ACCEPTOR('w', "Note Acceptor ('w')"),
    SECONDARY_CARD_READER('y', "Secondary Card Reader ('y')");

    private final char value;
    private final String displayedName;

    Dig(char value, String displayedName) {
        this.value = value;
        this.displayedName = displayedName;
    }

    public static DescriptiveOptional<Dig> forValue(char value) {
        switch (value) {
            case 'A':
                return DescriptiveOptional.of(TIME_OF_DAY_CLOCK);
            case 'B':
                return DescriptiveOptional.of(COMMUNICATIONS);
            case 'C':
                return DescriptiveOptional.of(SYSTEM_DISK);
            case 'D':
                return DescriptiveOptional.of(MAGNETIC_CARD_READER_WRITER);
            case 'E':
                return DescriptiveOptional.of(CASH_HANDLER);
            case 'F':
                return DescriptiveOptional.of(ENVELOPE_DEPOSITORY);
            case 'G':
                return DescriptiveOptional.of(RECEIPT_PRINTER);
            case 'H':
                return DescriptiveOptional.of(JOURNAL_PRINTER);
            case 'I':
                return DescriptiveOptional.of(RESERVED_I);
            case 'J':
                return DescriptiveOptional.of(RESERVED_J);
            case 'T':
                return DescriptiveOptional.of(RESERVED_T);
            case 'U':
                return DescriptiveOptional.of(RESERVED_U);
            case 'W':
                return DescriptiveOptional.of(RESERVED_W);
            case 'K':
                return DescriptiveOptional.of(NIGHT_SAFE_DEPOSITORY);
            case 'L':
                return DescriptiveOptional.of(ENCRYPTOR);
            case 'M':
                return DescriptiveOptional.of(SECURITY_CAMERA);
            case 'N':
                return DescriptiveOptional.of(DOOR_ACCESS_SYSTEM);
            case 'O':
                return DescriptiveOptional.of(OFF_LINE_FLEX_DISK);
            case 'P':
                return DescriptiveOptional.of(TI_BINS);
            case 'Q':
                return DescriptiveOptional.of(CARDHOLDER_KEYBOARD);
            case 'R':
                return DescriptiveOptional.of(OPERATOR_KEYBOARD);
            case 'S':
                return DescriptiveOptional.of(CARDHOLDER_DISPLAY_VOICE);
            case 'V':
                return DescriptiveOptional.of(STATEMENT_PRINTER);
            case 'X':
                return DescriptiveOptional.of(PASSBOOK);
            case 'Y':
                return DescriptiveOptional.of(COIN_DISPENSER);
            case 'Z':
                return DescriptiveOptional.of(SYSTEM_DISPLAY);
            case '[':
                return DescriptiveOptional.of(MEDIA_ENTRY_INDICATORS);
            case '\\':
                return DescriptiveOptional.of(ENVELOPE_DISPENSER);
            case 'a':
                return DescriptiveOptional.of(VOICE_GUIDANCE);
            case 'd':
                return DescriptiveOptional.of(CASH_HANDLER_0);
            case 'e':
                return DescriptiveOptional.of(CASH_HANDLER_1);
            case 'f':
                return DescriptiveOptional.of(BARCODE_READER);
            case 'j':
                return DescriptiveOptional.of(UPS);
            case 'q':
                return DescriptiveOptional.of(CHEQUE_PROCESSOR);
            case 'w':
                return DescriptiveOptional.of(NOTE_ACCEPTOR);
            case 'y':
                return DescriptiveOptional.of(SECONDARY_CARD_READER);
            default: {
                return DescriptiveOptional.empty(() -> String.format("value '%c' is not a valid 'DIG'", value));
            }
        }
    }

    public static DescriptiveOptional<Dig> forValue(int value) {
        return forValue((char) value);
    }

    public char getValue() {
        return value;
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
