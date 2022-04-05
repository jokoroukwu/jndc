package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispensergroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ChainedCounterGroupAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.envelopedepositorygroup.EnvelopeDepositoryDataGroupAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.List;

public class CoinDispenserDataGroupAppender extends ChainedCounterGroupAppender {
    private final NdcComponentReader<List<CoinHopper>> coinHoppersReader;

    public CoinDispenserDataGroupAppender(NdcComponentReader<List<CoinHopper>> coinHoppersReader, ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppender) {
        super(nextAppender);
        this.coinHoppersReader = ObjectUtils.validateNotNull(coinHoppersReader, "coinHoppersReader");
    }

    public CoinDispenserDataGroupAppender() {
        this(CoinHopperDataReader.INSTANCE, new EnvelopeDepositoryDataGroupAppender());
    }

    @Override
    protected char getGroupId() {
        return CoinDispenserDataGroup.ID;
    }

    @Override
    protected String getGroupName() {
        return "Coin dispenser data group";
    }

    @Override
    protected void doAppendComponent(NdcCharBuffer ndcCharBuffer,
                                     SupplyCountersExtendedBuilder stateObject,
                                     DeviceConfiguration deviceConfiguration) {
        final List<CoinHopper> coinHoppers = coinHoppersReader.readComponent(ndcCharBuffer);
        stateObject.withCoinDispenserDataGroup(new CoinDispenserDataGroup(coinHoppers));
    }

}
