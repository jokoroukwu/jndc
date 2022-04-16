package io.github.jokoroukwu.jndc.central.datacommand.extendedencyrptionkeychange;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum Modifier implements NdcComponent {
    DECIPHER_MASTER_KEY_WITH_CURRENT('1'),
    DECIPHER_COMM_KEY_WITH_MASTER('2'),
    DECIPHER_COMM_KEY_WITH_COMM('3'),
    USE_LOCAL_COMM_KEY_AS_COMM('4'),
    DECIPHER_MAC_WITH_MASTER('5'),
    DECIPHER_MAC_WITH_COMM('6'),
    USE_LOCAL_COMM_KEY_AS_MAC('7'),
    DECIPHER_VISA_KEY_WITH_CURRENT('8'),
    VISA_KEY_TABLE('9'),
    DECIPHER_VISA_KEY_WITH_MASTER('A'),
    LOAD_HSM_KEY_AND_SIGNATURE('B'),
    LOAD_INIT_MASTER_KEY_WITH_RSA('C'),
    LOAD_NEW_INIT_COMM_KEY_WITH_RSA('D'),
    LOAD_NEW_INIT_VISA_KEY_WITH_RSA('E'),
    SEND_EPP_SERIAL_NUM('F'),
    SEND_EPP_PUBLIC_KEY('G'),
    SEND_ALL_KVVS('H'),
    RESERVED_I('I'),
    SET_KEY_ENTRY_MODE('J'),
    SEND_CURRENT_KEY_ENTRY_MODE('K'),
    LOAD_HOST_CERTIFICATE('L'),
    SEND_SST_CERTIFICATE('M'),
    SEND_SST_RANDOM_NUMBER('N'),
    LOAD_A_KEY_PKCS7_ENCODED('O'),
    REPLACE_CERT_AUTH_CERTIFICATE('P'),
    SEND_ENCRYPTOR_CAPABILITIES('Q'),
    LOAD_NCR_SUB_PUB_KEY('R'),
    DELETE_HSM_PUB_KEY('S'),
    DELETE_NCR_PUB_KEY('T'),
    SEND_EPP_ATTRIBUTES('U'),
    SEND_VAR_LENGTH_EPP_SERIAL_NUM('V'),
    RESERVED_W('W'),
    LOAD_HSM_PUB_KEY_WITH_TR34_CERT('X'),
    LOAD_TR31_KEY_BLOCK('Y'),
    RELOAD_HSM_PUB_KEY_REBIND('Z'),
    RELOAD_HSM_PUB_KEY_FORCE_REBIND('a'),
    DELETE_HSM_PUB_KEY_UNBIND('b'),
    DELETE_HSM_PUB_KEY_FORCE_UNBIND('c'),
    RESERVED_D('d'),
    LOAD_NEW_COMM_KEY_WITH_TR34('e'),
    LOAD_NEW_MAC_KEY_WITH_TR34('f'),
    SEND_HOST_CERT('g'),
    SEND_EXTENDED_CAPABILITIES('h'),
    SEND_EXTENDED_KEY_STATUS('i'),
    DIEBOLD_UNBIND_EPP('j'),
    START_AUTH_COMMAND('k');

    private final char value;

    Modifier(char value) {
        this.value = value;
    }

    public static DescriptiveOptional<Modifier> forValue(char value) {
        switch (value) {
            case '1':
                return DescriptiveOptional.of(DECIPHER_MASTER_KEY_WITH_CURRENT);
            case '2':
                return DescriptiveOptional.of(DECIPHER_COMM_KEY_WITH_MASTER);
            case '3':
                return DescriptiveOptional.of(DECIPHER_COMM_KEY_WITH_COMM);
            case '4':
                return DescriptiveOptional.of(USE_LOCAL_COMM_KEY_AS_COMM);
            case '5':
                return DescriptiveOptional.of(DECIPHER_MAC_WITH_MASTER);
            case '6':
                return DescriptiveOptional.of(DECIPHER_MAC_WITH_COMM);
            case '7':
                return DescriptiveOptional.of(USE_LOCAL_COMM_KEY_AS_MAC);
            case '8':
                return DescriptiveOptional.of(DECIPHER_VISA_KEY_WITH_CURRENT);
            case '9':
                return DescriptiveOptional.of(VISA_KEY_TABLE);
            case 'A':
                return DescriptiveOptional.of(DECIPHER_VISA_KEY_WITH_MASTER);
            case 'B':
                return DescriptiveOptional.of(LOAD_HSM_KEY_AND_SIGNATURE);
            case 'C':
                return DescriptiveOptional.of(LOAD_INIT_MASTER_KEY_WITH_RSA);
            case 'D':
                return DescriptiveOptional.of(LOAD_NEW_INIT_COMM_KEY_WITH_RSA);
            case 'E':
                return DescriptiveOptional.of(LOAD_NEW_INIT_VISA_KEY_WITH_RSA);
            case 'F':
                return DescriptiveOptional.of(SEND_EPP_SERIAL_NUM);
            case 'G':
                return DescriptiveOptional.of(SEND_EPP_PUBLIC_KEY);
            case 'H':
                return DescriptiveOptional.of(SEND_ALL_KVVS);
            case 'I':
                return DescriptiveOptional.of(RESERVED_I);
            case 'J':
                return DescriptiveOptional.of(SET_KEY_ENTRY_MODE);
            case 'K':
                return DescriptiveOptional.of(SEND_CURRENT_KEY_ENTRY_MODE);
            case 'L':
                return DescriptiveOptional.of(LOAD_HOST_CERTIFICATE);
            case 'M':
                return DescriptiveOptional.of(SEND_SST_CERTIFICATE);
            case 'N':
                return DescriptiveOptional.of(SEND_SST_RANDOM_NUMBER);
            case 'O':
                return DescriptiveOptional.of(LOAD_A_KEY_PKCS7_ENCODED);
            case 'P':
                return DescriptiveOptional.of(REPLACE_CERT_AUTH_CERTIFICATE);
            case 'Q':
                return DescriptiveOptional.of(SEND_ENCRYPTOR_CAPABILITIES);
            case 'R':
                return DescriptiveOptional.of(LOAD_NCR_SUB_PUB_KEY);
            case 'S':
                return DescriptiveOptional.of(DELETE_HSM_PUB_KEY);
            case 'T':
                return DescriptiveOptional.of(DELETE_NCR_PUB_KEY);
            case 'U':
                return DescriptiveOptional.of(SEND_EPP_ATTRIBUTES);
            case 'V':
                return DescriptiveOptional.of(SEND_VAR_LENGTH_EPP_SERIAL_NUM);
            case 'W':
                return DescriptiveOptional.of(RESERVED_W);
            case 'X':
                return DescriptiveOptional.of(LOAD_HSM_PUB_KEY_WITH_TR34_CERT);
            case 'Y':
                return DescriptiveOptional.of(LOAD_TR31_KEY_BLOCK);
            case 'Z':
                return DescriptiveOptional.of(RELOAD_HSM_PUB_KEY_REBIND);
            case 'a':
                return DescriptiveOptional.of(RELOAD_HSM_PUB_KEY_FORCE_REBIND);
            case 'b':
                return DescriptiveOptional.of(DELETE_HSM_PUB_KEY_UNBIND);
            case 'c':
                return DescriptiveOptional.of(DELETE_HSM_PUB_KEY_FORCE_UNBIND);
            case 'd':
                return DescriptiveOptional.of(RESERVED_D);
            case 'e':
                return DescriptiveOptional.of(LOAD_NEW_COMM_KEY_WITH_TR34);
            case 'f':
                return DescriptiveOptional.of(LOAD_NEW_MAC_KEY_WITH_TR34);
            case 'g':
                return DescriptiveOptional.of(SEND_HOST_CERT);
            case 'h':
                return DescriptiveOptional.of(SEND_EXTENDED_CAPABILITIES);
            case 'i':
                return DescriptiveOptional.of(SEND_EXTENDED_KEY_STATUS);
            case 'j':
                return DescriptiveOptional.of(DIEBOLD_UNBIND_EPP);
            case 'k':
                return DescriptiveOptional.of(START_AUTH_COMMAND);
            default: {
                return DescriptiveOptional.empty(() -> String.format("value '%c' is not a valid 'Modifier'", value));
            }
        }
    }

    public static DescriptiveOptional<Modifier> forValue(int value) {
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
        return String.format("%s ('%c')", name(), value);
    }
}
