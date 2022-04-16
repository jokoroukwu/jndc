package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cspdata;

import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.StringJoiner;

public class CspData implements IdentifiableBuffer {
    public static final char CSP_DATA_ID = 'U';
    public static final CspData EMPTY_CSP = csp(Strings.EMPTY_STRING);

    public static final char CONFIRMATION_CSP_DATA_ID = 'V';
    public static final CspData EMPTY_CONFIRMATION_CSP = confirmationCsp(Strings.EMPTY_STRING);

    private final char id;
    private final String value;

    CspData(char id, String value) {
        this.value = value;
        this.id = id;
    }

    public static CspData csp(String value) {
        validateValue(value);
        return new CspData(CSP_DATA_ID, value);
    }

    public static CspData confirmationCsp(String value) {
        validateValue(value);
        return new CspData(CONFIRMATION_CSP_DATA_ID, value);
    }

    private static void validateValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("'CSP Data' may not be null");
        }
        if (value.length() > 16) {
            throw new IllegalArgumentException("'CSP Data' may contain up to 16 characters but was: " + value);
        }
    }

    @Override
    public char getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toNdcString() {
        return id + value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CspData.class.getSimpleName() + ": {", "}")
                .add("id: " + id)
                .add("value: '" + value + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CspData cspData = (CspData) o;
        return id == cspData.id && value.equals(cspData.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }
}
