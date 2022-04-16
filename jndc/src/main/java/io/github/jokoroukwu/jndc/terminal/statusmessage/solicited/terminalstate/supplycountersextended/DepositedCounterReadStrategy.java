package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;

public interface DepositedCounterReadStrategy {

    DescriptiveOptionalInt readDepositedValue(NdcCharBuffer ndcCharBuffer);
}
