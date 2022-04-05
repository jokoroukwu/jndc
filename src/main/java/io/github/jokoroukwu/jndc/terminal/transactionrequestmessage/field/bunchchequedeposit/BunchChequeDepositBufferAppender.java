package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBufferAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class BunchChequeDepositBufferAppender extends IdentifiableBufferAppender<TransactionRequestMessageBuilder> {
    public static final String FIELD_NAME = "Bunch Cheque Deposit buffer";
    private final NdcComponentReader<BunchChequeDepositData> depositDataReader;

    public BunchChequeDepositBufferAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender,
                                            NdcComponentReader<BunchChequeDepositData> depositDataReader) {
        super(nextAppender);
        this.depositDataReader = ObjectUtils.validateNotNull(depositDataReader, "depositDataReader cannot be null");
    }

    public BunchChequeDepositBufferAppender() {
        this(null, new BunchChequeDepositDataReader());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionRequestMessageBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        skipBufferMeta(ndcCharBuffer);

        if (ndcCharBuffer.hasFieldDataRemaining()) {
            final BunchChequeDepositData bunchChequeDepositData = depositDataReader.readComponent(ndcCharBuffer);
            stateObject.withBunchChequeDepositBuffer(new BunchChequeDepositBuffer(bunchChequeDepositData));
        } else {
            stateObject.withBunchChequeDepositBuffer(BunchChequeDepositBuffer.EMPTY);
        }
        callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    @Override
    protected char getBufferId() {
        return BunchChequeDepositBuffer.ID;
    }

    @Override
    protected String getFieldName() {
        return FIELD_NAME;
    }

    @Override
    protected String getCommandName() {
        return TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString();
    }
}
