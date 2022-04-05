package io.github.jokoroukwu.jndc;

public abstract class ChainedNdcComponentAppender<T> implements NdcComponentAppender<T> {
    private final NdcComponentAppender<T> nextAppender;

    public ChainedNdcComponentAppender(NdcComponentAppender<T> nextAppender) {
        this.nextAppender = nextAppender;
    }

    protected final void callNextAppender(NdcCharBuffer ndcCharBuffer, T collector) {
        if (nextAppender != null) {
            nextAppender.appendComponent(ndcCharBuffer, collector);
        }
    }

    protected void callNextAppenderIfDataRemains(NdcCharBuffer ndcCharBuffer, T collector) {
        if (ndcCharBuffer.hasRemaining()) {
            callNextAppender(ndcCharBuffer, collector);
        }
    }
}
