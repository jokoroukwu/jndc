package io.github.jokoroukwu.jndc.util.random;

import java.security.SecureRandom;

public final class ThreadLocalSecureRandom {
    private static final ThreadLocal<SecureRandom> threadLocalSecureRandom = ThreadLocal.withInitial(SecureRandom::new);

    private ThreadLocalSecureRandom() {
        throw new InstantiationError(getClass() + " is for static use only");
    }

    public static SecureRandom get() {
        return threadLocalSecureRandom.get();
    }
}
