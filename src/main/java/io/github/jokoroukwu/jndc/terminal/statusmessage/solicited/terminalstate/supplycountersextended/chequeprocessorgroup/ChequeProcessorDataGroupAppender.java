package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.chequeprocessorgroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ChainedCounterGroupAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtended;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.bnaemulationgroup.BnaEmulationGroupAppender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class ChequeProcessorDataGroupAppender extends ChainedCounterGroupAppender {

    public ChequeProcessorDataGroupAppender(ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppender) {
        super(nextAppender);
    }

    public ChequeProcessorDataGroupAppender() {
        super(new BnaEmulationGroupAppender());
    }

    @Override
    protected char getGroupId() {
        return ChequeProcessorDataGroup.ID;
    }

    @Override
    protected String getGroupName() {
        return "Cheque processor data group J";
    }

    @Override
    protected void doAppendComponent(NdcCharBuffer ndcCharBuffer,
                                     SupplyCountersExtendedBuilder stateObject,
                                     DeviceConfiguration deviceConfiguration) {

        final List<Bin> bins = readBins(ndcCharBuffer);
        stateObject.withChequeProcessorDataGroup(new ChequeProcessorDataGroup(bins));
    }

    private List<Bin> readBins(NdcCharBuffer ndcCharBuffer) {
        final ArrayList<Bin> bins = new ArrayList<>();
        do {
            final int binNumber = ndcCharBuffer.tryReadInt(1)
                    .getOrThrow(errorMessage
                            -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Bin Number'", errorMessage, ndcCharBuffer));
            final int chequesDeposited = ndcCharBuffer.tryReadInt(5)
                    .getOrThrow(errorMessage
                            -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Cheques deposited in bin'", errorMessage, ndcCharBuffer));

            final Bin bin = new Bin(binNumber, chequesDeposited, null);
            bins.add(bin);
        } while (ndcCharBuffer.hasFieldDataRemaining());

        bins.trimToSize();
        return Collections.unmodifiableList(bins);
    }
}
