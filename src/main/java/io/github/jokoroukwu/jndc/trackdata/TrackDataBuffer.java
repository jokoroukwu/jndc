package io.github.jokoroukwu.jndc.trackdata;

import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.StringJoiner;

public abstract class TrackDataBuffer implements IdentifiableBuffer {

    public static String validateTrackData(String trackData, int maxLength, String fieldName) {
        ObjectUtils.validateNotNull(trackData, fieldName);
        final int length = trackData.length();
        Integers.validateRange(length, 0, maxLength, fieldName + " length");
        return Strings.validateCharRange(trackData, '0', '?', fieldName);
    }

    public abstract String getValue();

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + ": {", "}")
                .add("id: '" + getId() + '\'')
                .add("value: '" + getValue() + '\'')
                .toString();
    }

    @Override
    public String toNdcString() {
        return getId() + getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackDataBuffer that = (TrackDataBuffer) o;
        return getId() == that.getId() && Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getValue());
    }

}
