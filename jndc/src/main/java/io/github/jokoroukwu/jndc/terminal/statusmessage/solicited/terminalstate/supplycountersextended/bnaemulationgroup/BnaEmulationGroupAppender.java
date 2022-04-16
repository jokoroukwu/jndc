package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.bnaemulationgroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ChainedCounterGroupAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtended;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.dualdispensergroup.DualDispenserDataGroupAppender;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata.CashDepositNotes;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata.CashDepositNotesReader;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.Result;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class BnaEmulationGroupAppender extends ChainedCounterGroupAppender {
    private final NdcComponentReader<CashDepositNotes> cashDepositNotesReader;

    public BnaEmulationGroupAppender(NdcComponentReader<CashDepositNotes> cashDepositNotesReader,
                                     ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppender) {
        super(nextAppender);
        this.cashDepositNotesReader = ObjectUtils.validateNotNull(cashDepositNotesReader, "cashDepositNotesReader");
    }

    public BnaEmulationGroupAppender() {
        this(CashDepositNotesReader.INSTANCE, new DualDispenserDataGroupAppender());
    }

    @Override
    protected char getGroupId() {
        return BnaEmulationDepositDataGroup.ID;
    }

    @Override
    protected String getGroupName() {
        return "BNA Emulation deposit data group K";
    }

    @Override
    protected void doAppendComponent(NdcCharBuffer ndcCharBuffer, SupplyCountersExtendedBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        final CashDepositNotes cashDepositNotes = Result.of(() -> cashDepositNotesReader.readComponent(ndcCharBuffer))
                .getOrThrow(exception -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'BNA Emulation deposit data group K'",
                        exception.getMessage(), ndcCharBuffer));

        stateObject.withBnaEmulationDepositDataGroup(new BnaEmulationDepositDataGroup(cashDepositNotes, null));
    }
}
