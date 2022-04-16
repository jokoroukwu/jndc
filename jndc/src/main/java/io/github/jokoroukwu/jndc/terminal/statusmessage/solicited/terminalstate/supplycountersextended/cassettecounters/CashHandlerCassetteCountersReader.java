package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cassettecounters;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.DepositedCounterReadStrategy;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.EmptyDepositedCounterReadStrategy;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.NonEmptyDepositedCounterReadStrategy;
import io.github.jokoroukwu.jndc.util.NdcConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CashHandlerCassetteCountersReader extends CassetteCountersReader {
    public static final CashHandlerCassetteCountersReader INSTANCE = new CashHandlerCassetteCountersReader();

    public static final int MAX_CASSETTE_NUMBER = 7;
    public static final int MIN_CASSETTE_NUMBER = 4;

    private CashHandlerCassetteCountersReader() {
    }

    @Override
    public List<CassetteCounters> readComponent(NdcCharBuffer ndcCharBuffer) {
        final ArrayList<CassetteCounters> cassettes = new ArrayList<>(MAX_CASSETTE_NUMBER);
        final NdcCharBuffer subBuffer = ndcCharBuffer.subBuffer(NdcConstants.GROUP_SEPARATOR);
        final DepositedCounterReadStrategy depositedCounterReadStrategy = resolveStrategy(subBuffer);

        for (int i = 0; i < MIN_CASSETTE_NUMBER; i++) {
            final CassetteCounters cassette = readCassette(subBuffer, depositedCounterReadStrategy,0);
            cassettes.add(cassette);
        }

        //  might be 7 cassettes
        if (subBuffer.hasFieldDataRemaining()) {
            for (int i = 0; i < 3; i++) {
                final CassetteCounters cassette = readCassette(subBuffer, depositedCounterReadStrategy,0);
                cassettes.add(cassette);
            }
        }
        cassettes.trimToSize();
        return Collections.unmodifiableList(cassettes);
    }



    private DepositedCounterReadStrategy resolveStrategy(NdcCharBuffer buffer) {
        //  the length of all fields including 'Notes Deposited' field is 28 characters
        return buffer.remaining() % 28 == 0
                ? NonEmptyDepositedCounterReadStrategy.INSTANCE
                : EmptyDepositedCounterReadStrategy.INSTANCE;
    }

}
