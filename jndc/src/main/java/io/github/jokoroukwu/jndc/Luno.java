package io.github.jokoroukwu.jndc;

import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class Luno implements NdcComponent {
    /**
     * LUNO with no device ID
     */
    public static final Luno DEFAULT = new Luno("000", Strings.EMPTY_STRING);
    public static final String FIELD_NAME = "LUNO";

    private final String value;
    private final String terminalNumber;

    public Luno(String value, String terminalNumber) {
        this.value = ObjectUtils.validateNotNull(value, "value");
        this.terminalNumber = ObjectUtils.validateNotNull(terminalNumber, "terminalNumber");
    }

    public static Luno ofValue(String value) {
        return new Luno(value, Strings.EMPTY_STRING);
    }

    public static Luno ofDeviceId(String deviceId) {
        return new Luno("000", deviceId);
    }

    public static DescriptiveOptional<Luno> readLuno(NdcCharBuffer ndcCharBuffer) {
        final Optional<String> optionalError = ndcCharBuffer.trySkipFieldSeparator();
        if (optionalError.isPresent()) {
            return DescriptiveOptional.empty(() -> "no field separator before field: " + optionalError.get());
        }
        return ndcCharBuffer.tryReadCharSequence(3)
                .flatMap(value -> {
                    if (ndcCharBuffer.hasFieldDataRemaining()) {
                        return ndcCharBuffer.tryReadCharSequence(6)
                                .map(terminalId -> new Luno(value, terminalId));
                    }
                    return DescriptiveOptional.of(Luno.ofValue(value));
                });
    }

    public String value() {
        return value;
    }

    public String getTerminalNumber() {
        return terminalNumber;
    }

    @Override
    public String toNdcString() {
        return value.concat(terminalNumber);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Luno.class.getSimpleName() + ": {", "}")
                .add("value: '" + value + "'")
                .add("terminalNumber: '" + terminalNumber + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Luno luno = (Luno) o;
        return value.equals(luno.value) && terminalNumber.equals(luno.terminalNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, terminalNumber);
    }

}
