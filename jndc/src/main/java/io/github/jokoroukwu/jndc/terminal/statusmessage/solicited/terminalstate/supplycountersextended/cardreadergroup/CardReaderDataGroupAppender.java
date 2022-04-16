package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cardreadergroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ChainedCounterGroupAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtended;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cashhandlergroup.CashHandlerDataGroupAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cassettecounters.CashHandlerCassetteCountersReader;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispensergroup.CoinDispenserDataGroupAppender;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class CardReaderDataGroupAppender extends ChainedCounterGroupAppender {

    public CardReaderDataGroupAppender(ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppender) {
        super(nextAppender);
    }

    public CardReaderDataGroupAppender() {
        super(
                CashHandlerDataGroupAppender.builder()
                        .withHandler0Id()
                        .withHandler0GroupName()
                        .withDataAcceptor(SupplyCountersExtendedBuilder::withCashHandler0DataGroup)
                        .withCassettesReader(CashHandlerCassetteCountersReader.INSTANCE)
                        .withNextAppender(CashHandlerDataGroupAppender.builder()
                                .withHandler1Id()
                                .withHandler1GroupName()
                                .withDataAcceptor(SupplyCountersExtendedBuilder::withCashHandler1DataGroup)
                                .withNextAppender(new CoinDispenserDataGroupAppender())
                                .withCassettesReader(CashHandlerCassetteCountersReader.INSTANCE)
                                .build())
                        .build()
        );
    }

    @Override
    protected char getGroupId() {
        return CardReaderDataGroup.ID;
    }

    @Override
    protected String getGroupName() {
        return "Card Reader data group";
    }

    @Override
    protected void doAppendComponent(NdcCharBuffer ndcCharBuffer,
                                     SupplyCountersExtendedBuilder stateObject,
                                     DeviceConfiguration deviceConfiguration) {
        final int cardsCaptured = ndcCharBuffer.tryReadInt(5)
                .getOrThrow(errorMessage
                        -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Cards captured'", errorMessage, ndcCharBuffer));
        final CardReaderDataGroup cardReaderDataGroup = new CardReaderDataGroup(cardsCaptured, null);
        stateObject.withCardReaderDataGroup(cardReaderDataGroup);
    }
}
