package io.github.jokoroukwu.jndc.util.optional;

import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.*;

public final class DescriptiveOptional<T> {
    private static final DescriptiveOptional<?> empty = new DescriptiveOptional<>(null, EmptyDescriptionSupplier.INSTANCE);
    private final T value;
    private final Supplier<String> descriptionSupplier;

    private DescriptiveOptional(T value, Supplier<String> descriptionSupplier) {
        this.value = value;
        this.descriptionSupplier = ObjectUtils.validateNotNull(descriptionSupplier, "descriptionSupplier");
    }

    public static <T> DescriptiveOptional<T> ofNullable(T value, Supplier<String> descriptionSupplier) {
        ObjectUtils.validateNotNull(descriptionSupplier, "descriptionSupplier");

        return new DescriptiveOptional<>(value, descriptionSupplier);
    }

    public static <T> DescriptiveOptional<T> ofNullable(T value) {
        return ofNullable(value, EmptyDescriptionSupplier.INSTANCE);
    }

    public static <T> DescriptiveOptional<T> empty() {
        @SuppressWarnings("unchecked") final DescriptiveOptional<T> value = (DescriptiveOptional<T>) empty;
        return value;
    }

    public static <T> DescriptiveOptional<T> empty(Supplier<String> descriptionSupplier) {
        return new DescriptiveOptional<T>(null, descriptionSupplier);
    }

    public static <T> DescriptiveOptional<T> of(T value, Supplier<String> descriptionSupplier) {
        ObjectUtils.validateNotNull(value, "value");
        ObjectUtils.validateNotNull(descriptionSupplier, "descriptionSupplier");
        return new DescriptiveOptional<>(value, descriptionSupplier);
    }

    public static <T> DescriptiveOptional<T> of(T value) {
        return of(value, EmptyDescriptionSupplier.INSTANCE);
    }


    public <V> DescriptiveOptional<V> map(Function<T, V> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isEmpty()) {
            @SuppressWarnings("unchecked") final DescriptiveOptional<V> result = (DescriptiveOptional<V>) this;
            return result;
        }
        return ofNullable(mappingFunction.apply(value));
    }

    public <V> DescriptiveOptional<V> flatMap(Function<T, DescriptiveOptional<V>> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isEmpty()) {
            @SuppressWarnings("unchecked") final DescriptiveOptional<V> result = (DescriptiveOptional<V>) this;
            return result;
        }
        return mappingFunction.apply(value);
    }

    public <V> Optional<V> flatMapToOptional(Function<T, Optional<V>> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isEmpty()) {
            return Optional.empty();
        }
        return mappingFunction.apply(value);
    }

    public DescriptiveOptionalInt mapToInt(ToIntFunction<T> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isEmpty()) {
            return DescriptiveOptionalInt.empty(descriptionSupplier);
        }
        return DescriptiveOptionalInt.of(mappingFunction.applyAsInt(value));
    }

    public DescriptiveOptionalInt flatMapToInt(Function<T, DescriptiveOptionalInt> toIntOptionalFunction) {
        ObjectUtils.validateNotNull(toIntOptionalFunction, "toIntOptionalFunction");
        if (isEmpty()) {
            return DescriptiveOptionalInt.empty(descriptionSupplier);
        }
        return toIntOptionalFunction.apply(value);
    }

    public DescriptiveOptionalLong mapToLong(ToLongFunction<T> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isEmpty()) {
            return DescriptiveOptionalLong.empty(descriptionSupplier);
        }
        return DescriptiveOptionalLong.of(mappingFunction.applyAsLong(value));
    }

    public DescriptiveOptionalLong flatMapToLong(Function<T, DescriptiveOptionalLong> toOptionalLongFunction) {
        ObjectUtils.validateNotNull(toOptionalLongFunction, "toOptionalLongFunction");
        if (isEmpty()) {
            return DescriptiveOptionalLong.empty(descriptionSupplier);
        }
        return toOptionalLongFunction.apply(value);
    }

    public DescriptiveOptionalDouble mapToDouble(ToDoubleFunction<T> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isEmpty()) {
            return DescriptiveOptionalDouble.empty(descriptionSupplier);
        }
        return DescriptiveOptionalDouble.of(mappingFunction.applyAsDouble(value));
    }

    public DescriptiveOptionalDouble flatMapToDouble(Function<T, DescriptiveOptionalDouble> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isEmpty()) {
            return DescriptiveOptionalDouble.empty(descriptionSupplier);
        }
        return mappingFunction.apply(value);
    }

    public DescriptiveOptional<T> filter(Predicate<T> predicate) {
        ObjectUtils.validateNotNull(predicate, "predicate");
        if (!isEmpty()) {
            return predicate.test(value) ? this : DescriptiveOptional.empty();
        }
        return this;
    }

    public DescriptiveOptional<T> filter(Predicate<T> predicate, Function<T, Supplier<String>> onFalse) {
        ObjectUtils.validateNotNull(predicate, "predicate");
        ObjectUtils.validateNotNull(onFalse, "onFalse");
        if (!isEmpty()) {
            return predicate.test(value) ? this : DescriptiveOptional.empty(onFalse.apply(value));
        }
        return this;
    }

    public DescriptiveOptional<T> or(Supplier<DescriptiveOptional<T>> otherSupplier) {
        ObjectUtils.validateNotNull(otherSupplier, "otherSupplier");
        return isPresent() ? this : otherSupplier.get();
    }

    public <E extends Throwable> T getOrThrow(Function<String, E> exceptionSupplier) throws E {
        if (isEmpty()) {
            throw exceptionSupplier.apply(descriptionSupplier.get());
        }
        return value;
    }

    public T getOrDefault(Function<Supplier<String>, T> defaultResultFunction) {
        ObjectUtils.validateNotNull(defaultResultFunction, "defaultResultFunction");
        if (isPresent()) {
            return value;
        }
        return defaultResultFunction.apply(descriptionSupplier);
    }

    public T getOrDefault(T defaultValue) {
        ObjectUtils.validateNotNull(defaultValue, "defaultResultFunction");
        if (isPresent()) {
            return value;
        }
        return defaultValue;
    }


    public DescriptiveOptional<T> wrapDescription(Function<String, String> newMessageFunction) {
        ObjectUtils.validateNotNull(newMessageFunction, "newMessageFunction");
        if (isPresent()) {
            return this;
        }
        return DescriptiveOptional.empty(() -> newMessageFunction.apply(descriptionSupplier.get()));
    }

    public DescriptiveOptional<T> ifEmpty(Consumer<String> descriptionConsumer) {
        ObjectUtils.validateNotNull(descriptionConsumer, "descriptionConsumer");
        if (isEmpty()) {
            descriptionConsumer.accept(descriptionSupplier.get());
        }
        return this;
    }

    public DescriptiveOptional<T> ifPresent(Consumer<T> descriptionConsumer) {
        ObjectUtils.validateNotNull(descriptionConsumer, "descriptionConsumer");

        if (isPresent()) {
            descriptionConsumer.accept(value);
        }
        return this;
    }

    public void resolve(Consumer<T> onPresent, Consumer<String> onEmpty) {
        ObjectUtils.validateNotNull(onPresent, "onPresent");
        ObjectUtils.validateNotNull(onEmpty, "onEmpty");

        if (isPresent()) {
            onPresent.accept(value);
        } else {
            onEmpty.accept(descriptionSupplier.get());
        }
    }

    public T get() {
        if (isPresent()) {
            return value;
        }
        var message = descriptionSupplier.get();
        throw new NoSuchElementException(message.isEmpty() ? "Empty value" : "Empty value: ".concat(message));
    }

    public boolean isPresent() {
        return value != null;
    }

    public boolean isEmpty() {
        return value == null;
    }

    public Supplier<String> descriptionSupplier() {
        return descriptionSupplier;
    }

    public String description() {
        return descriptionSupplier.get();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DescriptiveOptional.class.getSimpleName() + ": {", "}")
                .add("value: " + value)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DescriptiveOptional<?> that = (DescriptiveOptional<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
