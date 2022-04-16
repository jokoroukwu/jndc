package io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum InformationId implements NdcComponent {
    EPP_NUMBER_SIGNATURE('1', "EPP serial number and signature ('1')"),
    EPP_KEY_SIGNATURE('2', "EPP public key and signature ('2')"),
    NEW_KVV('3', "New Key Verification Value ('3')"),
    KEY_STATUS('4', "Keys status ('4')"),
    PUBLIC_KEY_LOADED('5', "Public Key loaded ('5')"),
    KEY_ENTRY_MODE('6', "Key Entry Mode ('6')"),
    RSA_ENCRYPTION_KVV('7', "Certificate RSA encryption KVV ('7')"),
    SST_CERTIFICATE('8', "SST certificate ('8')"),
    SST_RANDOM_NUMBER('9', "SST random number ('9')"),
    PKCS7_KEY_LOADED('A', "PKCS7 key loaded ('A')"),
    ENCRYPTOR_CAPABILITIES_STATE('B', "Encryptor capabilities and state ('B')"),
    KEY_DELETED('C', "Key deleted ('C')"),
    EPP_ATTRIBUTES('D', "EPP attributes ('D')"),
    VL_EPP_NUMBER_SIGNATURE('E', "Variable‐length EPP serial number and signature ('E')"),
    RESERVED_F('F', "Reserved ('F')"),
    RESERVED_G('G', "Reserved ('G')"),
    HOST_CERTIFICATE('H', "Host Certificate ('H')"),
    EPP_UNBOUND('I', "EPP Unbound ('I')"),
    EXTENDED_CAPABILITIES('J', "Extended Capabilities ('J')"),
    EXTENDED_KEY_STATUS('K', "Extended Key Status ('K')"),
    AUTHENTICATION_TOKEN('L', "Authentication Token ('L')");

    private final String displayedName;
    private final char value;

    InformationId(char value, String displayedName) {
        this.displayedName = displayedName;
        this.value = value;
    }

    public static DescriptiveOptional<InformationId> forValue(char value) {
        switch (value) {
            case '1':
                return DescriptiveOptional.of(EPP_NUMBER_SIGNATURE);
            case '2':
                return DescriptiveOptional.of(EPP_KEY_SIGNATURE);
            case '3':
                return DescriptiveOptional.of(NEW_KVV);
            case '4':
                return DescriptiveOptional.of(KEY_STATUS);
            case '5':
                return DescriptiveOptional.of(PUBLIC_KEY_LOADED);
            case '6':
                return DescriptiveOptional.of(KEY_ENTRY_MODE);
            case '7':
                return DescriptiveOptional.of(RSA_ENCRYPTION_KVV);
            case '8':
                return DescriptiveOptional.of(SST_CERTIFICATE);
            case '9':
                return DescriptiveOptional.of(SST_RANDOM_NUMBER);
            case 'A':
                return DescriptiveOptional.of(PKCS7_KEY_LOADED);
            case 'B':
                return DescriptiveOptional.of(ENCRYPTOR_CAPABILITIES_STATE);
            case 'C':
                return DescriptiveOptional.of(KEY_DELETED);
            case 'D':
                return DescriptiveOptional.of(EPP_ATTRIBUTES);
            case 'E':
                return DescriptiveOptional.of(VL_EPP_NUMBER_SIGNATURE);
            case 'F':
                return DescriptiveOptional.of(RESERVED_F);
            case 'G':
                return DescriptiveOptional.of(RESERVED_G);
            case 'H':
                return DescriptiveOptional.of(HOST_CERTIFICATE);
            case 'I':
                return DescriptiveOptional.of(EPP_UNBOUND);
            case 'J':
                return DescriptiveOptional.of(EXTENDED_CAPABILITIES);
            case 'K':
                return DescriptiveOptional.of(EXTENDED_KEY_STATUS);
            case 'L':
                return DescriptiveOptional.of(AUTHENTICATION_TOKEN);
            default: {
                return DescriptiveOptional.empty(() -> String.format("value '%c' is not a valid 'Information Identifier'", value));
            }
        }
    }

    public static DescriptiveOptional<InformationId> forValue(int value) {
        return forValue((char) value);
    }


    public char value() {
        return value;
    }


    @Override
    public String toString() {
        return displayedName;
    }

    @Override
    public String toNdcString() {
        return String.valueOf(value);
    }
}
