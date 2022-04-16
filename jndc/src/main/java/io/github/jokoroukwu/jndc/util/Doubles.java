package io.github.jokoroukwu.jndc.util;


import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalDouble;

public final class Doubles {

    private Doubles() {
    }

    public static DescriptiveOptionalDouble tryParseDouble(String value) {
        try {
            return DescriptiveOptionalDouble.of(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            return DescriptiveOptionalDouble.empty(e::toString);
        }
    }
}
