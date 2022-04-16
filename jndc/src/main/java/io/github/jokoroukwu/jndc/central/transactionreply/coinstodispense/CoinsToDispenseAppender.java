package io.github.jokoroukwu.jndc.central.transactionreply.coinstodispense;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyCommandBuilder;
import io.github.jokoroukwu.jndc.central.transactionreply.functionid.FunctionIdAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.tsn.TransactionSerialNumberAppender;
import io.github.jokoroukwu.jndc.util.NdcConstants;

import java.util.ArrayList;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withFieldName;

public class CoinsToDispenseAppender extends ChainedConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> {
    public static final String FIELD_NAME = "'Coins to Dispense'";

    public CoinsToDispenseAppender(ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender) {
        super(nextAppender);
    }

    public CoinsToDispenseAppender() {
        super(new TransactionSerialNumberAppender<>(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(), new FunctionIdAppender()));
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionReplyCommandBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        //  field may be empty
        if (ndcCharBuffer.hasNextCharMatching(NdcConstants.GROUP_SEPARATOR)) {
            final CoinsToDispense coinsToDispense = readCoinsToDispense(ndcCharBuffer);
            stateObject.withCoinsToDispense(coinsToDispense);
        }
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }


    private CoinsToDispense readCoinsToDispense(NdcCharBuffer ndcCharBuffer) {
        final ArrayList<Integer> coinsToDispense = new ArrayList<>();
        int hopperType = 1;
        do {
            checkHopperTypeRange(hopperType, ndcCharBuffer);
            final int nextHopperCoins = readNextHopperCoins(ndcCharBuffer, hopperType);
            coinsToDispense.add(nextHopperCoins);
        } while (ndcCharBuffer.hasFieldDataRemaining());

        coinsToDispense.trimToSize();
        return new CoinsToDispense(CoinsToDispense.MAX_SIZE, coinsToDispense);
    }

    private int readNextHopperCoins(NdcCharBuffer ndcCharBuffer, int hopperType) {
        return ndcCharBuffer.tryReadInt(2)
                .getOrThrow(errorMessage
                        -> withFieldName(FIELD_NAME + " of hopper type " + hopperType, errorMessage, ndcCharBuffer));
    }

    private void checkHopperTypeRange(int hopperType, NdcCharBuffer ndcCharBuffer) {
        if (hopperType > CoinsToDispense.MAX_SIZE) {

            final String errorMessage = String.format("hopper type %d exceeds max number of hopper types (%d) at position %d",
                    hopperType, CoinsToDispense.MAX_SIZE, ndcCharBuffer.position());
            throw NdcMessageParseException.withFieldName(FIELD_NAME, errorMessage, ndcCharBuffer);
        }
    }
}
