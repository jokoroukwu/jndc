package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cassettecounters;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.DepositedCounterReadStrategy;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtended;

import java.util.List;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public abstract class CassetteCountersReader implements NdcComponentReader<List<CassetteCounters>> {

    protected CassetteCounters readCassette(NdcCharBuffer ndcCharBuffer,
                                            DepositedCounterReadStrategy depositedCounterReadStrategy,
                                            int minCassetteTypeValue) {

        final int cassetteType = ndcCharBuffer.tryReadInt(3)
                .filter(val -> isValidCassetteType(val, minCassetteTypeValue),
                        val -> () -> "should be in range 0-7 but was " + val)
                .getOrThrow(errorMessage -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Cassette Type'", errorMessage, ndcCharBuffer));

        final int notesInCassette = ndcCharBuffer.tryReadInt(5)
                .getOrThrow(errorMessage -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Notes in Cassette'", errorMessage, ndcCharBuffer));

        final int notesRejected = ndcCharBuffer.tryReadInt(5)
                .getOrThrow(errorMessage -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Notes Rejected'", errorMessage, ndcCharBuffer));

        final int notesDispensed = ndcCharBuffer.tryReadInt(5)
                .getOrThrow(errorMessage -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Notes Dispensed'", errorMessage, ndcCharBuffer));

        final int lastTransactionNotesDispensed = ndcCharBuffer.tryReadInt(5)
                .getOrThrow(errorMessage
                        -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Last Transaction Notes Dispensed'", errorMessage, ndcCharBuffer));

        int notesDeposited = depositedCounterReadStrategy.readDepositedValue(ndcCharBuffer)
                .getOrThrow(errorMessage
                        -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Notes Deposited'", errorMessage, ndcCharBuffer));

        return new CassetteCounters(cassetteType, notesInCassette, notesRejected, notesDispensed,
                lastTransactionNotesDispensed, notesDeposited, null);
    }

    private boolean isValidCassetteType(int cassetteType, int minCassetteType) {
        return cassetteType >= minCassetteType && cassetteType <= 7;
    }

}
