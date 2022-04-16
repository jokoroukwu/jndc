package io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.transactiondata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class TransactionDataReader implements NdcComponentReader<TransactionData<? extends Cassette>> {
    private final Function<DataId, NdcComponentReader<? extends Cassette>> cassetteReaderFactory;

    public TransactionDataReader(Function<DataId, NdcComponentReader<? extends Cassette>> cassetteReaderFactory) {
        this.cassetteReaderFactory = ObjectUtils.validateNotNull(cassetteReaderFactory, "cassetteReaderFactory");
    }

    public TransactionDataReader() {
        this(CassetteReaderFactory.INSTANCE);
    }

    @Override
    public TransactionData<? extends Cassette> readComponent(NdcCharBuffer ndcCharBuffer) {
        final DataId dataId = readDataId(ndcCharBuffer);
        final int numberOfCassettes = readNumberOfCassettes(ndcCharBuffer);
        final List<? extends Cassette> cassettes = readCassettes(numberOfCassettes, dataId, ndcCharBuffer);
        return new TransactionData<>(dataId, cassettes);
    }

    private List<? extends Cassette> readCassettes(int numberOfCassettes, DataId dataId, NdcCharBuffer ndcCharBuffer) {
        final NdcComponentReader<? extends Cassette> cassetteReader = cassetteReaderFactory.apply(dataId);
        final ArrayList<Cassette> cassettes = new ArrayList<>(numberOfCassettes);
        for (int i = 0; i < numberOfCassettes; i++) {
            final Cassette nextCassette = cassetteReader.readComponent(ndcCharBuffer);
            cassettes.add(nextCassette);
        }
        return Collections.unmodifiableList(cassettes);
    }

    private int readNumberOfCassettes(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadInt(2)
                .filter(Integers::isPositive, value -> () -> "value must be in range 1-99 but was " + value)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(SolicitedStatusMessage.COMMAND_NAME, "Status Information: Number of Cassette Types",
                        errorMessage, ndcCharBuffer));
    }

    private DataId readDataId(NdcCharBuffer ndcCharBuffer) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage
                        -> NdcMessageParseException.onNoFieldSeparator(SolicitedStatusMessage.COMMAND_NAME, "Data Identifier", errorMessage, ndcCharBuffer));

        return ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(DataId::forValue)
                .getOrThrow(errorMessage
                        -> NdcMessageParseException.withMessage(SolicitedStatusMessage.COMMAND_NAME, "Status Information: Data Identifier", errorMessage, ndcCharBuffer));
    }

}
