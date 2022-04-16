package io.github.jokoroukwu.jndc.genericbuffer;

import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.StringJoiner;

public class GenericBuffer implements IdentifiableBuffer {
    private final char id;
    private final String value;

    public GenericBuffer(char id, String value) {
        this.id = id;
        this.value = ObjectUtils.validateNotNull(value, "Buffer value");
    }

    public GenericBuffer(char id) {
        this(id, Strings.EMPTY_STRING);
    }

    @Override
    public String toNdcString() {
        return id + value;
    }

    @Override
    public char getId() {
        return id;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GenericBuffer.class.getSimpleName() + ": {", "}")
                .add("id: " + id)
                .add("value: '" + value + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericBuffer genericBuffer = (GenericBuffer) o;
        return id == genericBuffer.id && value.equals(genericBuffer.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }

}
