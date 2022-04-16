package io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.transactiondata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class CassetteReader implements NdcComponentReader<Cassette> {
    private final CassetteFactory cassetteFactory;
    private final int cassetteTypeMinValue;
    private final int cassetteTypeMaxValue;

    public CassetteReader(int cassetteTypeMinValue, int cassetteTypeMaxValue, CassetteFactory cassetteFactory) {
        this.cassetteFactory = ObjectUtils.validateNotNull(cassetteFactory, "cassetteFactory");
        validateBoundaries(cassetteTypeMinValue, cassetteTypeMaxValue);
        this.cassetteTypeMinValue = cassetteTypeMinValue;
        this.cassetteTypeMaxValue = cassetteTypeMaxValue;
    }


    @Override
    public Cassette readComponent(NdcCharBuffer ndcCharBuffer) {
        final int cassetteType = readCassetteType(ndcCharBuffer);
        final int numberOfNotes = readNumberOfNotes(ndcCharBuffer);
        final String additionalData = readAdditionalData(ndcCharBuffer);
        return cassetteFactory.getCassette(cassetteType, numberOfNotes, additionalData);
    }

    private int readCassetteType(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadInt(3)
                .filter(this::isCassetteTypeValid, type -> () -> String.format("value should be in range %d-%d but was %d",
                        cassetteTypeMinValue, cassetteTypeMaxValue, type))
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(getCommandName(),
                        "Status Information: Cassette Type", errorMessage, ndcCharBuffer));
    }

    private int readNumberOfNotes(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadInt(3)
                .filter(Integers::isPositive, number -> () -> "value should be in range 1-999 but was " + number)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(getCommandName(),
                        "Status Information: Number of notes", errorMessage, ndcCharBuffer));

    }

    private String readAdditionalData(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            throw NdcMessageParseException.withMessage(getCommandName(),
                    "'Current Fitness Status' and 'Current Supplies Status'", "data is empty", ndcCharBuffer);
        }
        final StringBuilder builder = new StringBuilder();
        do {
            builder.append(ndcCharBuffer.readNextChar());
        } while (ndcCharBuffer.hasFieldDataRemaining());
        return builder.toString();
    }


    private boolean isCassetteTypeValid(int value) {
        return value >= cassetteTypeMinValue && value <= cassetteTypeMaxValue;
    }

    private String getCommandName() {
        return TerminalMessageClass.SOLICITED.toString()
                + ": "
                + TerminalMessageSubClass.STATUS_MESSAGE.toString();
    }

    private void validateBoundaries(int min, int max) {
        if (min < 0) {
            throw new IllegalArgumentException("Minimum value cannot be negative");
        }
        if (max < min) {
            throw new IllegalArgumentException(String.format("Max value (%d) is less than min value (%d)", max, min));
        }
    }
}
