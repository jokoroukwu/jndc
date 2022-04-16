package io.github.jokoroukwu.jndc.util.optional;

import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.*;

public class DescriptiveOptionalLong {
    private final long value;
    private final boolean isPresent;
    private final Supplier<String> descriptionSupplier;

    private DescriptiveOptionalLong(long value, boolean isPresent, Supplier<String> descriptionSupplier) {
        this.value = value;
        this.isPresent = isPresent;
        this.descriptionSupplier = ObjectUtils.validateNotNull(descriptionSupplier, "descriptionSupplier");
    }

    public static DescriptiveOptionalLong of(long value) {
        return new DescriptiveOptionalLong(value, true, EmptyDescriptionSupplier.INSTANCE);
    }

    public static DescriptiveOptionalLong empty(Supplier<String> descriptionSupplier) {
        return new DescriptiveOptionalLong(0, false, descriptionSupplier);
    }

    public DescriptiveOptionalLong map(LongUnaryOperator mappingOperator) {
        ObjectUtils.validateNotNull(mappingOperator, "mappingOperator");
        if (isPresent) {
            return of(mappingOperator.applyAsLong(value));
        }

        return this;
    }

    public <V> DescriptiveOptional<V> mapToObject(LongFunction<V> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isEmpty()) {
            return DescriptiveOptional.empty(EmptyDescriptionSupplier.INSTANCE);
        }
        return DescriptiveOptional.of(mappingFunction.apply(value));
    }

    public DescriptiveOptionalLong flatMap(LongFunction<DescriptiveOptionalLong> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isPresent) {
            return mappingFunction.apply(value);
        }
        return this;
    }

    public <V> DescriptiveOptional<V> flatMapToObject(LongFunction<DescriptiveOptional<V>> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isEmpty()) {
            return DescriptiveOptional.<V>empty(EmptyDescriptionSupplier.INSTANCE);
        }
        return mappingFunction.apply(value);
    }

    public DescriptiveOptionalLong filter(LongPredicate predicate, LongConsumer onFalse) {
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

    public DescriptiveOptionalLong filter(LongPredicate predicate) {
        ObjectUtils.validateNotNull(predicate, "predicate");
        if (isPresent) {
            return predicate.test(value) ? this : empty(EmptyDescriptionSupplier.INSTANCE);
        }
        return this;
    }

    public DescriptiveOptionalLong filter(LongPredicate predicate, LongFunction<Supplier<String>> onFalse) {
        ObjectUtils.validateNotNull(predicate, "predicate");
        ObjectUtils.validateNotNull(onFalse, "onFalse");
        if (isPresent) {
            return predicate.test(value) ? this : empty(onFalse.apply(value));
        }
        return this;
    }


    public <E extends Exception> long getOrThrow(Function<String, E> exceptionSupplier) throws E {
        if (isPresent) {
            return value;
        }
        throw exceptionSupplier.apply(descriptionSupplier.get());
    }

    public long getOrDefault(ToIntFunction<Supplier<String>> defaultResultFunction) {
        ObjectUtils.validateNotNull(defaultResultFunction, "defaultResultFunction");
        return isPresent ? value : defaultResultFunction.applyAsInt(descriptionSupplier);
    }

    public long getOrDefault(long defaultValue) {
        return isPresent ? value : defaultValue;
    }

    public DescriptiveOptionalLong ifEmpty(Consumer<String> descriptionConsumer) {
        ObjectUtils.validateNotNull(descriptionConsumer, "descriptionConsumer");
        if (isEmpty()) {
            descriptionConsumer.accept(descriptionSupplier.get());
        }
        return this;
    }

    public DescriptiveOptionalLong ifPresent(LongConsumer descriptionConsumer) {
        ObjectUtils.validateNotNull(descriptionConsumer, "descriptionConsumer");
        if (isPresent) {
            descriptionConsumer.accept(value);
        }
        return this;
    }

    public void resolve(LongConsumer onPresent, Consumer<String> onEmpty) {
        ObjectUtils.validateNotNull(onPresent, "onPresent");
        ObjectUtils.validateNotNull(onEmpty, "onEmpty");

        if (isPresent) {
            onPresent.accept(value);
        } else {
            onEmpty.accept(descriptionSupplier.get());
        }
    }

    public long get() {
        if (isPresent) {
            return value;
        }
        var message = descriptionSupplier.get();
        throw new NoSuchElementException(message.isEmpty() ? "Empty value" : "Empty value: ".concat(message));
    }

    public DescriptiveOptionalInt toInt() {
        return isPresent ? DescriptiveOptionalInt.of((int) value) : DescriptiveOptionalInt.empty(descriptionSupplier);
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

    public Optional<String> description() {
        return Optional.ofNullable(descriptionSupplier.get());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DescriptiveOptionalLong.class.getSimpleName() + ": {", "}")
                .add("value: " + value)
                .add("isPresent: " + isPresent)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DescriptiveOptionalLong that = (DescriptiveOptionalLong) o;
        return value == that.value && isPresent == that.isPresent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, isPresent);
    }
}
