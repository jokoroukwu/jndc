package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.coinhoppers;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBufferAppender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CoinHopperBufferAppender extends IdentifiableBufferAppender<TransactionRequestMessageBuilder> {
    public static final String FIELD_NAME = "Coin Hoppers Buffer";
    private static final int maxNumberOfHopperTypes = 8;

    public CoinHopperBufferAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender) {
        super(nextAppender);
    }

    public CoinHopperBufferAppender() {
        super(null);
    }

    @Override
    protected char getBufferId() {
        return CoinHoppersBuffer.ID;
    }

    @Override
    protected String getFieldName() {
        return FIELD_NAME;
    }

    @Override
    protected String getCommandName() {
        return TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString();
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionRequestMessageBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        skipBufferMeta(ndcCharBuffer);

        if (ndcCharBuffer.hasFieldDataRemaining()) {
            final List<Integer> coinHoppersData = readCoinHoppersData(ndcCharBuffer);
            stateObject.withCoinHoppersBuffer(new CoinHoppersBuffer(coinHoppersData));
        } else {
            stateObject.withCoinHoppersBuffer(CoinHoppersBuffer.EMPTY);
        }
        callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private List<Integer> readCoinHoppersData(NdcCharBuffer ndcCharBuffer) {
        final List<Integer> coinsDispensed = new ArrayList<>(maxNumberOfHopperTypes);
        int hopperType = 0;
        do {
            checkMax(++hopperType, ndcCharBuffer);
            final int coins = readCoinsDispensed(ndcCharBuffer, hopperType);
            coinsDispensed.add(coins);
        } while (ndcCharBuffer.hasFieldDataRemaining());

        checkMin(coinsDispensed.size(), ndcCharBuffer);
        return Collections.unmodifiableList(coinsDispensed);
    }

    private int readCoinsDispensed(NdcCharBuffer ndcCharBuffer, int hopperType) {
        return ndcCharBuffer.tryReadInt(2)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(getCommandName(),
                        FIELD_NAME + ": hopper type " + hopperType, errorMessage, ndcCharBuffer))
                .get();
    }

    private void checkMax(int hopperType, NdcCharBuffer ndcCharBuffer) {
        if (hopperType > maxNumberOfHopperTypes) {
            throw NdcMessageParseException.withMessage(getCommandName(), FIELD_NAME,
                    "data exceeds max number of hopper types (8)", ndcCharBuffer);
        }
    }

    private void checkMin(int numberOfHopperTypes, NdcCharBuffer ndcCharBuffer) {
        if (numberOfHopperTypes < 5) {
            throw NdcMessageParseException.withMessage(getCommandName(), FIELD_NAME,
                    "at least 5 hopper types must be present but actual number was: " + numberOfHopperTypes,
                    ndcCharBuffer);
        }
    }
}
