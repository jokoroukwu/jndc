package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.coincounters;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasicContext;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.bnacounters.BnaCountersAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.groupedcounters.GroupedCounterValues;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.groupedcounters.GroupedCountersReader;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onNoGroupSeparator;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;
import static io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasic.COMMAND_NAME;

public class CoinCountersAppender extends ChainedConfigurableNdcComponentAppender<SupplyCountersBasicContext> {
    private final ConfigurableNdcComponentReader<DescriptiveOptional<GroupedCounterValues>> groupedCountersReader;

    public CoinCountersAppender(ConfigurableNdcComponentReader<DescriptiveOptional<GroupedCounterValues>> groupedCountersReader,
                                ConfigurableNdcComponentAppender<SupplyCountersBasicContext> nextAppender) {
        super(nextAppender);
        this.groupedCountersReader = ObjectUtils.validateNotNull(groupedCountersReader, "groupedCountersReader");
    }

    public CoinCountersAppender() {
        this(GroupedCountersReader.INSTANCE, new BnaCountersAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, SupplyCountersBasicContext stateObject, DeviceConfiguration deviceConfiguration) {
        if (ndcCharBuffer.hasRemaining() && !ndcCharBuffer.hasFollowingFieldSeparator()) {
            ndcCharBuffer.trySkipGroupSeparator()
                    .ifPresent(errorMessage
                            -> onNoGroupSeparator(COMMAND_NAME, "Coin counters", errorMessage, ndcCharBuffer));
            final CoinCounters coinCounters = readCoinCounters(ndcCharBuffer, deviceConfiguration);
            stateObject.getCountersBasicBuilder().withCoinCounters(coinCounters);
        }

        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private CoinCounters readCoinCounters(NdcCharBuffer ndcCharBuffer, DeviceConfiguration deviceConfiguration) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return null;
        }
        final GroupedCounterValues coinsRemaining = groupedCountersReader.readComponent(ndcCharBuffer, deviceConfiguration)
                .getOrThrow(errorMessage -> withMessage(COMMAND_NAME, "Coins remaining", errorMessage, ndcCharBuffer));
        final GroupedCounterValues coinsDispensed = groupedCountersReader.readComponent(ndcCharBuffer, deviceConfiguration)
                .getOrThrow(errorMessage -> withMessage(COMMAND_NAME, "Coins dispensed", errorMessage, ndcCharBuffer));

        final GroupedCounterValues lastTransactionCoinsDispensed
                = groupedCountersReader.readComponent(ndcCharBuffer, deviceConfiguration)
                .getOrThrow(errorMessage
                        -> withMessage(COMMAND_NAME, "Last transaction coins dispensed", errorMessage, ndcCharBuffer));
        return new CoinCounters(coinsRemaining, coinsDispensed, lastTransactionCoinsDispensed, null);
    }

}
