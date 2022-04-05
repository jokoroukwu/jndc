package io.github.jokoroukwu.jndc;

public interface MessageCoordinationNumberAcceptor<T> {

    T withMessageCoordinationNumber(char messageCoordinationNumber);
}
