package io.github.jokoroukwu.jndc.util.optional;

import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.*;

public final class DescriptiveOptionalInt {
    private final int value;
    private final boolean isPresent;
    private final Supplier<String> descriptionSupplier;

    private DescriptiveOptionalInt(int value, boolean isPresent, Supplier<String> descriptionSupplier) {
        this.value = value;
        this.isPresent = isPresent;
        this.descriptionSupplier = ObjectUtils.validateNotNull(descriptionSupplier, "descriptionSupplier");
    }

    public static DescriptiveOptionalInt of(int value) {
        return new DescriptiveOptionalInt(value, true, EmptyDescriptionSupplier.INSTANCE);
    }

    public static DescriptiveOptionalInt empty(Supplier<String> descriptionSupplier) {
        return new DescriptiveOptionalInt(0, false, descriptionSupplier);
    }

    public static DescriptiveOptionalInt empty() {
        return empty(EmptyDescriptionSupplier.INSTANCE);
    }

    public DescriptiveOptionalInt map(IntUnaryOperator mappingOperator) {
        ObjectUtils.validateNotNull(mappingOperator, "mappingOperator");
        if (isPresent) {
            return of(mappingOperator.applyAsInt(value));
        }

        return this;
    }

    public <V> DescriptiveOptional<V> mapToObject(IntFunction<V> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isEmpty()) {
            return DescriptiveOptional.empty(EmptyDescriptionSupplier.INSTANCE);
        }
        return DescriptiveOptional.ofNullable(mappingFunction.apply(value));
    }

    public DescriptiveOptionalInt flatMap(IntFunction<DescriptiveOptionalInt> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isPresent) {
            return mappingFunction.apply(value);
        }
        return this;
    }

    public <V> DescriptiveOptional<V> flatMapToObject(IntFunction<DescriptiveOptional<V>> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isEmpty()) {
            return DescriptiveOptional.empty(descriptionSupplier);
        }
        return mappingFunction.apply(value);
    }

    public DescriptiveOptionalInt filter(IntPredicate predicate, IntConsumer onFalse) {
        ObjectUtils.validateNotNull(predicate, "predicate");
        ObjectUtils.validateNotNull(onFalse, "onFalse");
        if (isPresent) {
            if (predicate.test(value)) {
                return this;
            }
            onFalse.accept(value);
        }
        return this;
    }

    public DescriptiveOptionalInt filter(IntPredicate predicate) {
        ObjectUtils.validateNotNull(predicate, "predicate");
        if (isPresent) {
            return predicate.test(value) ? this : empty(EmptyDescriptionSupplier.INSTANCE);
        }
        return this;
    }

    public DescriptiveOptionalInt filter(IntPredicate predicate, IntFunction<Supplier<String>> onFalse) {
        ObjectUtils.validateNotNull(predicate, "predicate");
        ObjectUtils.validateNotNull(onFalse, "onFalse");
        if (isPresent) {
            return predicate.test(value) ? this : empty(onFalse.apply(value));
        }
        return this;
    }

    public <E extends Exception> int getOrThrow(Function<String, E> exceptionSupplier) throws E {
        if (isPresent) {
            return value;
        }
        throw exceptionSupplier.apply(descriptionSupplier.get());
    }

    public int getOrDefault(ToIntFunction<Supplier<String>> defaultResultFunction) {
        ObjectUtils.validateNotNull(defaultResultFunction, "defaultResultFunction");
        return isPresent ? value : defaultResultFunction.applyAsInt(descriptionSupplier);
    }

    public int getOrDefault(int defaultValue) {
        return isPresent ? value : defaultValue;
    }

    public DescriptiveOptionalInt mapDescription(Function<String, String> descriptionFunction) {
        ObjectUtils.validateNotNull(descriptionFunction, "descriptionSupplier");
        if (isPresent) {
            return this;
        }
        return DescriptiveOptionalInt.empty(() -> descriptionFunction.apply(descriptionSupplier.get()));
    }

    public DescriptiveOptionalInt ifEmpty(Consumer<String> descriptionConsumer) {
        ObjectUtils.validateNotNull(descriptionConsumer, "descriptionConsumer");
        if (isEmpty()) {
            descriptionConsumer.accept(descriptionSupplier.get());
        }
        return this;
    }

    public DescriptiveOptionalInt ifPresent(IntConsumer descriptionConsumer) {
        ObjectUtils.validateNotNull(descriptionConsumer, "descriptionConsumer");
        if (isPresent) {
            descriptionConsumer.accept(value);
        }
        return this;
    }

    public void resolve(IntConsumer onPresent, Consumer<String> onEmpty) {
        ObjectUtils.validateNotNull(onPresent, "onPresent");
        ObjectUtils.validateNotNull(onEmpty, "onEmpty");

        if (isPresent) {
            onPresent.accept(value);
        } else {
            onEmpty.accept(descriptionSupplier.get());
        }
    }

    public int get() {
        if (isPresent) {
            return value;
        }
        final String message = descriptionSupplier.get();
        throw new NoSuchElementException(message.isEmpty() ? "Empty value" : "Empty value: ".concat(message));
    }


    public boolean isEmpty() {
        return !isPresent;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public Supplier<String> descriptionSupplier() {
        return descriptionSupplier;
    }

    public String description() {
        return descriptionSupplier.get();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DescriptiveOptionalInt.class.getSimpleName() + ": {", "}")
                .add("value: " + value)
                .add("isPresent: " + isPresent)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DescriptiveOptionalInt that = (DescriptiveOptionalInt) o;
        return value == that.value && isPresent == that.isPresent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, isPresent);
    }
}
