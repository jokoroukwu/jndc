package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;

import java.util.List;
import java.util.Optional;

public interface LastTransactionStatusData extends IdentifiableBuffer {

    int getLastTransactionSerialNumber();

    LastStatusIssued getStatusIssued();

    List<Integer> getNotesDispensed();

    int getCoinageAmountDispensed();

    List<Integer> getCoinsDispensed();

    Optional<CompletionData> getCompletionData();

    Optional<CashDepositData> getCashDepositData();
}
