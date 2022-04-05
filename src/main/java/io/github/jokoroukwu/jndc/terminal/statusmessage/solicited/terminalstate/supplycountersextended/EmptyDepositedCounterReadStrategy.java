package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;

public enum EmptyDepositedCounterReadStrategy implements DepositedCounterReadStrategy {
    INSTANCE;

    @Override
    public DescriptiveOptionalInt readDepositedValue(NdcCharBuffer ndcCharBuffer) {
        return DescriptiveOptionalInt.of(-1);
    }
}
