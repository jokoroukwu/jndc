package io.github.jokoroukwu.jndc;

public interface NdcComponentAppender<T> {

    void appendComponent(NdcCharBuffer ndcCharBuffer, T stateObject);

}
