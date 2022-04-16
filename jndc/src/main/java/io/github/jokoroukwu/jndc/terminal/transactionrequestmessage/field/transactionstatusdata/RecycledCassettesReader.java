package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecycledCassettesReader implements NdcComponentReader<List<RecycleCassette>> {

    @Override
    public List<RecycleCassette> readComponent(NdcCharBuffer ndcCharBuffer) {
        final int numberOfCassettes = ndcCharBuffer.tryReadInt(2)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Last Transaction Status Data: Number of recycle cassettes reported", errorMessage, ndcCharBuffer))
                .get();

        return readRecycleCassettes(numberOfCassettes, ndcCharBuffer);
    }

    private List<RecycleCassette> readRecycleCassettes(int numberOfCassettes, NdcCharBuffer ndcCharBuffer) {
        if (numberOfCassettes == 0) {
            return Collections.emptyList();
        }
        final List<RecycleCassette> recycleCassettes = new ArrayList<>(numberOfCassettes);
        for (int i = 0; i < numberOfCassettes; i++) {
            final RecycleCassette cassette = readCassette(ndcCharBuffer);
            recycleCassettes.add(cassette);
        }
        return Collections.unmodifiableList(recycleCassettes);
    }


    private RecycleCassette readCassette(NdcCharBuffer ndcCharBuffer) {
        final int cassetteType = ndcCharBuffer.tryReadInt(3)
                .filter(this::isTypeValid, type -> () -> "value should be within valid range (1-7 dec) but was: " + type)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Last Transaction Status Data: NDC Cassette Type", errorMessage, ndcCharBuffer))
                .get();
        final int numberOfNotes = ndcCharBuffer.tryReadInt(3)
                .filter(this::isNumberOfNotesValid, number -> () -> "value should be within valid range (1-999 dec) but was: " + number)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Last Transaction Status Data: Number of Notes", errorMessage, ndcCharBuffer))
                .get();
        return new RecycleCassette(cassetteType, numberOfNotes, null);
    }

    private boolean isTypeValid(int type) {
        return type >= 1 && type <= 7;
    }

    private boolean isNumberOfNotesValid(int numberOfNotes) {
        return numberOfNotes > 0;
    }
}
