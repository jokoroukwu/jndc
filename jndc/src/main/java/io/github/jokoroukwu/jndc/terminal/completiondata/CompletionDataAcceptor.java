package io.github.jokoroukwu.jndc.terminal.completiondata;

public interface CompletionDataAcceptor<V> {

    V withCompletionData(CompletionData completionData);
}
