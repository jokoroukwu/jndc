package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.nextstatenumber;

import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.AlphanumericStateEntry;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.ConfigurationOption;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.Objects;
import java.util.StringJoiner;

public abstract class NextStateNumber implements ConfigurationOption {
    public static final int NUMBER = 77;

    private final int code;

    NextStateNumber(int code) {
        this.code = code;
    }

    public static DescriptiveOptional<NextStateNumber> forCode(int code, ConfigurationOption alphaNumericStateEntryOption) {
        ObjectUtils.validateNotNull(alphaNumericStateEntryOption, "alphaNumericStateEntryOption");

        if (AlphanumericStateEntry.ON.getCode() == alphaNumericStateEntryOption.getCode()) {
            return Base36NextStateNumber.forCode(code);
        }
        return DecimalNextStateNumber.forCode(code);
    }

    public static DescriptiveOptional<NextStateNumber> forCode(String code, ConfigurationOption alphaNumericStateEntryOption) {
        ObjectUtils.validateNotNull(alphaNumericStateEntryOption, "alphaNumericStateEntryOption");

        if (AlphanumericStateEntry.ON.getCode() == alphaNumericStateEntryOption.getCode()) {
            return Base36NextStateNumber.forCode(code);
        }
        return DecimalNextStateNumber.forCode(code);
    }

    @Override
    public int getNumber() {
        return NUMBER;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NextStateNumber)) return false;
        NextStateNumber that = (NextStateNumber) o;
        return code == that.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(NUMBER, code);
    }
}
