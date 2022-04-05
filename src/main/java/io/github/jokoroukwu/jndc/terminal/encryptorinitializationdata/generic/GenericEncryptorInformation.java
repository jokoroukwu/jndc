package io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.generic;

import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.EncryptorInformation;
import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.InformationId;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public class GenericEncryptorInformation implements EncryptorInformation {
    private final InformationId informationId;
    private final String value;

    public GenericEncryptorInformation(InformationId informationId, String value) {
        this.informationId = ObjectUtils.validateNotNull(informationId, "informationId");
        this.value = ObjectUtils.validateNotNull(value, "value");
    }


    @Override
    public InformationId getInformationId() {
        return informationId;
    }

    public String getValue() {
        return new NdcStringBuilder()
                .appendComponent(informationId)
                .appendFs()
                .append(value)
                .toString();
    }

    @Override
    public String toNdcString() {
        return value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GenericEncryptorInformation.class.getSimpleName() + ": {", "}")
                .add("value: '" + value + '\'')
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericEncryptorInformation that = (GenericEncryptorInformation) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
