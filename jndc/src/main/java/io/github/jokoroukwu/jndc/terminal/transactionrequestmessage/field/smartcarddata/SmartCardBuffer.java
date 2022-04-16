package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.smartcarddata;

import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class SmartCardBuffer implements IdentifiableBuffer {
    public static final SmartCardBuffer EMPTY = new SmartCardBuffer(null);
    public static final char ID = '5';

    private final SmartCardData smartCardData;

    public SmartCardBuffer(SmartCardData smartCardData) {
        this.smartCardData = smartCardData;
    }

    public Optional<SmartCardData> getSmartCardData() {
        return Optional.ofNullable(smartCardData);
    }

    @Override
    public char getId() {
        return ID;
    }

    @Override
    public String toNdcString() {
        if (smartCardData != null) {
            return ID + smartCardData.toNdcString();
        }
        return String.valueOf(ID);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SmartCardBuffer.class.getSimpleName() + ": {", "}")
                .add("smartCardData: " + smartCardData)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmartCardBuffer that = (SmartCardBuffer) o;
        return Objects.equals(smartCardData, that.smartCardData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(smartCardData);
    }
}
