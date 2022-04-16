package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor;

import io.github.jokoroukwu.jndc.NdcComponentReader;

public interface NdcComponentReaderFactory<T, V> {

    NdcComponentReader<V> getNoteReader(T key);
}
