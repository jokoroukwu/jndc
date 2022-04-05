package io.github.jokoroukwu.jndc.terminal.statusmessage.readyb;

import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.StatusDescriptor;
import io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.transactiondata.Cassette;
import io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.transactiondata.TransactionData;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public final class ReadyBStatus implements SolicitedStatusInformation {
    public static final String COMMAND_NAME = TerminalMessageClass.SOLICITED + ": "
            + TerminalMessageSubClass.STATUS_MESSAGE + StatusDescriptor.READY_B;

    private final int transactionSerialNumber;
    private final TransactionData<? extends Cassette> transactionData;

    public ReadyBStatus(int transactionSerialNumber, TransactionData<? extends Cassette> transactionData) {
        this.transactionSerialNumber = Integers.validateMaxValue(transactionSerialNumber, 9999,
                "'Transaction Serial Number'");
        this.transactionData = TransactionData.unmodifiable(validateTransactionData(transactionData));
    }

    public ReadyBStatus(int transactionSerialNumber) {
        this.transactionSerialNumber = Integers.validateMaxValue(transactionSerialNumber, 9999,
                "'Transaction Serial Number'");
        this.transactionData = TransactionData.EMPTY;
    }

    ReadyBStatus(int transactionSerialNumber, TransactionData<? extends Cassette> transactionData, Void unused) {
        this.transactionSerialNumber = transactionSerialNumber;
        this.transactionData = transactionData;
    }

    public int getTransactionSerialNumber() {
        return transactionSerialNumber;
    }


    public TransactionData<? extends Cassette> getTransactionData() {
        return transactionData;
    }


    @Override
    public String toNdcString() {
        return new NdcStringBuilder(128)
                .appendZeroPadded(transactionSerialNumber, 4)
                .appendComponent(NdcConstants.FIELD_SEPARATOR_STRING, transactionData)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ReadyBStatus.class.getSimpleName() + ": {", "}")
                .add("transactionSerialNumber: " + transactionSerialNumber)
                .add("transactionData: " + transactionData)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReadyBStatus that = (ReadyBStatus) o;
        return transactionSerialNumber == that.transactionSerialNumber && transactionData.equals(that.transactionData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionSerialNumber, transactionData);
    }

    private TransactionData<? extends Cassette> validateTransactionData(TransactionData<? extends Cassette> transactionData) {
        ObjectUtils.validateNotNull(transactionData, "'Transaction Data'");
        if (transactionData.getDataId().isPresent() && transactionData.isEmpty()) {
            throw new IllegalArgumentException("'Transaction Data' should be present when 'Data Identifier' is present");
        }
        return transactionData;
    }
}
