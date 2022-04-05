package io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.newkvv;

import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.EncryptorInformation;
import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.InformationId;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public class NewKvv implements EncryptorInformation {
    private final String value;

    public NewKvv(String value) {
        this.value = validateKeyValue(value);

    }

    public static NewKvv of(String value) {
        return new NewKvv(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public InformationId getInformationId() {
        return InformationId.NEW_KVV;
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(2 + value.length())
                .appendComponent(InformationId.NEW_KVV)
                .appendFs()
                .append(value)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NewKvv.class.getSimpleName() + ": {", "}")
                .add("informationId: " + InformationId.NEW_KVV)
                .add("value: '" + value + '\'')
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewKvv newKvv = (NewKvv) o;
        return value.equals(newKvv.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(InformationId.NEW_KVV, value);
    }


    private String validateKeyValue(String value) {
        ObjectUtils.validateNotNull(value, "'New KVV'");
        if (value.length() == 6 || value.length() == 72) {
            return value;
        }
        final String message = String.format("New KVV should be 6 or 72 characters long but was %d for value: '%s'",
                value.length(), value);
        throw new IllegalArgumentException(message);
    }
}
