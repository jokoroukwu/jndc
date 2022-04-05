package io.github.jokoroukwu.jndc.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Result<T> {
    private static final Result<?> empty = success(null);
    private final T output;
    private final RuntimeException runtimeException;

    private Result(T output, RuntimeException runtimeException) {
        this.output = output;
        this.runtimeException = runtimeException;
    }

    public static <T> Result<T> of(Supplier<T> supplier) {
        ObjectUtils.validateNotNull(supplier, "supplier");
        try {
            return success(supplier.get());
        } catch (RuntimeException e) {
            return failure(e);
        }
    }

    public static Result<Void> ofVoid(Runnable runnable) {
        ObjectUtils.validateNotNull(runnable, "runnable");
        try {
            runnable.run();
            @SuppressWarnings("unchecked") final Result<Void> emptyResult = (Result<Void>) empty;
            return emptyResult;
        } catch (RuntimeException e) {
            return failure(e);
        }
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> failure(RuntimeException exception) {
        return new Result<>(null, exception);
    }

    public Result<T> onFailure(Consumer<RuntimeException> exceptionConsumer) {
        ObjectUtils.validateNotNull(exceptionConsumer, "exceptionConsumer");
        if (isFailure()) {
            exceptionConsumer.accept(runtimeException);
        }
        return this;
    }

    public Result<T> onSuccess(Consumer<T> outputConsumer) {
        ObjectUtils.validateNotNull(outputConsumer, "outputConsumer");
        if (isSuccess()) {
            outputConsumer.accept(output);
        }
        return this;
    }

    public void resolve(Consumer<T> onSuccess, Consumer<RuntimeException> onFailure) {
        ObjectUtils.validateNotNull(onSuccess, "onSuccess");
        ObjectUtils.validateNotNull(onFailure, "onFailure");

        if (isSuccess()) {
            onSuccess.accept(output);
        } else {
            onFailure.accept(runtimeException);
        }
    }

    public <V> Result<V> mapCatching(Function<T, V> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isFailure()) {
            @SuppressWarnings("unchecked") final Result<V> value = (Result<V>) this;
            return value;
        }
        try {
            return success(mappingFunction.apply(output));
        } catch (RuntimeException e) {
            return failure(e);
        }
    }

    public <V> Result<V> map(Function<T, V> mappingFunction) {
        ObjectUtils.validateNotNull(mappingFunction, "mappingFunction");
        if (isSuccess()) {
            return success(mappingFunction.apply(output));
        }
        @SuppressWarnings("unchecked") final Result<V> value = (Result<V>) this;
        return value;
    }

    public boolean isFailure() {
        return runtimeException != null;
    }

    public boolean isSuccess() {
        return runtimeException == null;
    }

    public T get() {
        if (isSuccess()) {
            return output;
        }
        throw runtimeException;
    }

    public T getOrThrow(Function<RuntimeException, RuntimeException> exceptionFunction) {
        ObjectUtils.validateNotNull(exceptionFunction, "exceptionFunction");
        if (isSuccess()) {
            return output;
        }
        throw exceptionFunction.apply(runtimeException);
    }

    public RuntimeException getException() {
        return runtimeException;
    }
}
