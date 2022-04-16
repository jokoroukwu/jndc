package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cassettecounters;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.NonEmptyDepositedCounterReadStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DualDispenserCassetteCountersReader extends CassetteCountersReader {
    public static final DualDispenserCassetteCountersReader INSTANCE = new DualDispenserCassetteCountersReader();

    private DualDispenserCassetteCountersReader() {
    }

    @Override
    public List<CassetteCounters> readComponent(NdcCharBuffer ndcCharBuffer) {
        final ArrayList<CassetteCounters> cassetteCounters = new ArrayList<>();
        do {
            final CassetteCounters nextCassetteCounters
                    = readCassette(ndcCharBuffer, NonEmptyDepositedCounterReadStrategy.INSTANCE, 1);
            cassetteCounters.add(nextCassetteCounters);
        } while (ndcCharBuffer.hasFieldDataRemaining());

        cassetteCounters.trimToSize();
        return Collections.unmodifiableList(cassetteCounters);
    }
}
