package io.github.jokoroukwu.jndc.util.optional;

import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.function.Supplier;

public final class EmptyDescriptionSupplier implements Supplier<String> {
    public static final EmptyDescriptionSupplier INSTANCE = new EmptyDescriptionSupplier();

    private EmptyDescriptionSupplier() {
    }

    @Override
    public String get() {
        return Strings.EMPTY_STRING;
    }
}
