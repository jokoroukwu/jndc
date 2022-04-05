package io.github.jokoroukwu.jndc;

public interface NdcComponentReader<T> {

    T readComponent(NdcCharBuffer ndcCharBuffer);
}
