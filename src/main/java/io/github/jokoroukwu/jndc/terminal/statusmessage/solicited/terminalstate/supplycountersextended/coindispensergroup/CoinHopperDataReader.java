package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispensergroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.DepositedCounterReadStrategy;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.EmptyDepositedCounterReadStrategy;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.NonEmptyDepositedCounterReadStrategy;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtended;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public enum CoinHopperDataReader implements NdcComponentReader<List<CoinHopper>> {
    INSTANCE;

    public static final int MAX_NUMBER_OF_HOPPERS = 8;

    @Override
    public List<CoinHopper> readComponent(NdcCharBuffer ndcCharBuffer) {
        final ArrayList<CoinHopper> coinHoppers = new ArrayList<>(MAX_NUMBER_OF_HOPPERS);
        final NdcCharBuffer subBuffer = ndcCharBuffer.subBuffer(NdcConstants.GROUP_SEPARATOR);
        checkMaxDataLength(subBuffer, ndcCharBuffer);
        final DepositedCounterReadStrategy depositedCounterReadStrategy = evaluateDepositedCounterStrategy(subBuffer);

        do {
            final CoinHopper nextHopper = readCoinHopper(subBuffer, depositedCounterReadStrategy);
            coinHoppers.add(nextHopper);
        } while (subBuffer.hasRemaining());

        coinHoppers.trimToSize();
        return Collections.unmodifiableList(coinHoppers);
    }


    private CoinHopper readCoinHopper(NdcCharBuffer buffer, DepositedCounterReadStrategy depositedCounterReadStrategy) {
        final int hopperType = buffer.tryReadInt(2)
                .filter(Integers::isPositive, val -> () -> "should be within range 01-08 dec but was " + val)
                .getOrThrow(errorMessage -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Hopper Type Number'", errorMessage, buffer));

        final int coinsRemaining = buffer.tryReadInt(5)
                .getOrThrow(errorMessage -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Coins Remaining'", errorMessage, buffer));

        final int coinsDispensed = buffer.tryReadInt(5)
                .getOrThrow(errorMessage -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Coins Dispensed'", errorMessage, buffer));

        final int lastTransactionCoinsDispensed = buffer.tryReadInt(5)
                .getOrThrow(errorMessage
                        -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Last Transaction Coins Dispensed'", errorMessage, buffer));

        final int coinsDeposited = depositedCounterReadStrategy.readDepositedValue(buffer)
                .getOrThrow(errorMessage -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Coins Deposited'", errorMessage, buffer));

        return new CoinHopper(hopperType, coinsRemaining, coinsDispensed, lastTransactionCoinsDispensed, coinsDeposited, null);
    }

    private void checkMaxDataLength(NdcCharBuffer subBuffer, NdcCharBuffer buffer) {
        if (subBuffer.remaining() > 22 * MAX_NUMBER_OF_HOPPERS) {
            final String errorMessage = String.format("Coin Hopper data '%s' length exceeds max field data length",
                    subBuffer);
            throw NdcMessageParseException.withMessage(SupplyCountersExtended.COMMAND_NAME, "'Coin dispenser data group E'", errorMessage, buffer);
        }
    }

    private DepositedCounterReadStrategy evaluateDepositedCounterStrategy(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.remaining() % 22 == 0
                ? NonEmptyDepositedCounterReadStrategy.INSTANCE
                : EmptyDepositedCounterReadStrategy.INSTANCE;
    }
}
