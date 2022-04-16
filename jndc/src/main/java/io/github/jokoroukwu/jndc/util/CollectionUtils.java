package io.github.jokoroukwu.jndc.util;

import java.util.Collection;


public final class CollectionUtils {

    private CollectionUtils() {
        throw new InstantiationError(getClass() + " is for static use only");
    }


    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }


    public static <V, T extends Collection<V>> T requireNonNullNonEmpty(T collection, String fieldName) {
        if (isNullOrEmpty(collection)) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
        return collection;
    }
}
