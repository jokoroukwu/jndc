package io.github.jokoroukwu.jndc.util.optional;

import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.*;

public class DescriptiveOptionalDouble {
    private final double value;
    private final boolean isPresent;
    private final Supplier<String> descriptionSupplier;

    private DescriptiveOptionalDouble(double value, boolean isPresent, Supplier<String> descriptionSupplier) {
        this.value = value;
        this.isPresent = isPresent;
        this.descriptionSupplier = ObjectUtils.validateNotNull(descriptionSupplier, "descriptionSupplier");
    }

    public static DescriptiveOptionalDouble of(double value) {
        return new DescriptiveOptionalDouble(value, true, EmptyDescriptionSupplier.INSTANCE);
    }

    public static DescriptiveOptionalDouble empty(Supplier<String> descriptionSupplier) {
        return new DescriptiveOptionalDouble(0, false, descriptionSupplier);
    }

    public static DescriptiveOptionalDouble empty() {
        return empty(EmptyDescriptionSupplier.INSTANCE);
    }

    public DescriptiveOptionalDouble map(DoubleUnaryOperator mappingOperator) {
        ObjectUtils.validateNotNull(mappingOperator, "mappingOperator");
        if (isPresent) {
            return of(mappingOperator.applyAsDouble(value));
        }
        return this;
    }

    public <V> DescriptiveOptional<V> mapToObject(DoubleFunction<V> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isEmpty()) {
            return DescriptiveOptional.empty(EmptyDescriptionSupplier.INSTANCE);
        }
        return DescriptiveOptional.ofNullable(mappingFunction.apply(value));
    }

    public DescriptiveOptionalDouble flatMap(DoubleFunction<DescriptiveOptionalDouble> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isPresent) {
            return mappingFunction.apply(value);
        }
        return this;
    }

    public <V> DescriptiveOptional<V> flatMapToObject(DoubleFunction<DescriptiveOptional<V>> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isEmpty()) {
            return DescriptiveOptional.empty(descriptionSupplier);
        }
        return mappingFunction.apply(value);
    }


    public void resolve(DoubleConsumer onPresent, Consumer<String> onEmpty) {
        ObjectUtils.validateNotNull(onPresent, "onPresent");
        ObjectUtils.validateNotNull(onEmpty, "onEmpty");

        if (isPresent) {
            onPresent.accept(value);
        } else {
            onEmpty.accept(descriptionSupplier.get());
        }
    }

    public DescriptiveOptionalDouble filter(DoublePredicate predicate, DoubleConsumer onFalse) {
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

    public DescriptiveOptionalDouble filter(DoublePredicate predicate) {
        ObjectUtils.validateNotNull(predicate, "predicate");
        if (isPresent) {
            return predicate.test(value) ? this : empty(EmptyDescriptionSupplier.INSTANCE);
        }
        return this;
    }

    public DescriptiveOptionalDouble filter(DoublePredicate predicate, DoubleFunction<Supplier<String>> onFalse) {
        ObjectUtils.validateNotNull(predicate, "predicate");
        ObjectUtils.validateNotNull(onFalse, "onFalse");
        if (isPresent) {
            return predicate.test(value) ? this : empty(onFalse.apply(value));
        }
        return this;
    }

    public <E extends Exception> double getOrThrow(Function<String, E> exceptionSupplier) throws E {
        if (isPresent) {
            return value;
        }
        throw exceptionSupplier.apply(descriptionSupplier.get());
    }

    public double getOrDefault(ToDoubleFunction<Supplier<String>> defaultResultFunction) {
        ObjectUtils.validateNotNull(defaultResultFunction, "defaultResultFunction");
        return isPresent ? value : defaultResultFunction.applyAsDouble(descriptionSupplier);
    }

    public double getOrDefault(double defaultValue) {
        return isPresent ? value : defaultValue;
    }

    public DescriptiveOptionalDouble wrapDescription(Function<String, String> descriptionFunction) {
        ObjectUtils.validateNotNull(descriptionFunction, "descriptionSupplier");
        if (isPresent) {
            return this;
        }
        return DescriptiveOptionalDouble.empty(() -> descriptionFunction.apply(descriptionSupplier.get()));
    }

    public DescriptiveOptionalDouble ifEmpty(Consumer<String> descriptionConsumer) {
        ObjectUtils.validateNotNull(descriptionConsumer, "descriptionConsumer");
        if (isEmpty()) {
            descriptionConsumer.accept(descriptionSupplier.get());
        }
        return this;
    }

    public DescriptiveOptionalDouble ifPresent(DoubleConsumer descriptionConsumer) {
        ObjectUtils.validateNotNull(descriptionConsumer, "descriptionConsumer");
        if (isPresent) {
            descriptionConsumer.accept(value);
        }
        return this;
    }

    public double get() {
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
        return new StringJoiner(", ", DescriptiveOptionalDouble.class.getSimpleName() + ": {", "}")
                .add("value: " + value)
                .add("isPresent: " + isPresent)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DescriptiveOptionalDouble that = (DescriptiveOptionalDouble) o;
        return Double.compare(that.value, value) == 0 && isPresent == that.isPresent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, isPresent);
    }
}
