package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;

import java.util.ArrayList;
import java.util.List;

public class CoinsDispensedDataReader implements NdcComponentReader<List<Integer>> {

    @Override
    public List<Integer> readComponent(NdcCharBuffer ndcCharBuffer) {
        //  coinage amount dispensed should always be zero
        ndcCharBuffer.trySkipNextSubsequence("00000")
                .ifPresent(message -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Last Transaction Coinage Amount Dispensed", message, ndcCharBuffer));

        final int numberOfHoppers = 4;
        final List<Integer> coinsDispensedData = new ArrayList<>(numberOfHoppers);
        for (int i = 0; i < numberOfHoppers; ) {
            coinsDispensedData.add(readHopperData(ndcCharBuffer, ++i));
        }
        return coinsDispensedData;
    }

    private int readHopperData(NdcCharBuffer ndcCharBuffer, int hopperType) {
        final DescriptiveOptionalInt optionalHopperData = ndcCharBuffer.tryReadInt(5);
        if (optionalHopperData.isPresent()) {
            return optionalHopperData.get();
        } else {
            final String fieldName = String.format("Last Transaction Coins Dispensed hopper %d Data", hopperType);
            throw NdcMessageParseException.withMessage(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(), fieldName,
                    optionalHopperData.description(), ndcCharBuffer);
        }
    }
}
