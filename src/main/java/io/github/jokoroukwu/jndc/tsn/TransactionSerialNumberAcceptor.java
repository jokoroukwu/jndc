package io.github.jokoroukwu.jndc.tsn;

public interface TransactionSerialNumberAcceptor<V> {

    V withTransactionSerialNumber(int transactionSerialNumber);
}
