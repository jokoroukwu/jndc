package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.CashHandlers;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.ConfigurationOption;
import io.github.jokoroukwu.jndc.exception.ConfigurationException;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;

import java.util.ArrayList;
import java.util.List;

public class NotesDispensedDataReader implements ConfigurableNdcComponentReader<List<Integer>> {

    @Override
    public List<Integer> readComponent(NdcCharBuffer ndcCharBuffer, DeviceConfiguration deviceConfiguration) {
        return deviceConfiguration
                .getConfigurationOptions()
                .getOption(CashHandlers.NUMBER)
                .stream()
                .mapToInt(ConfigurationOption::getCode)
                .mapToObj(CashHandlers::forCode)
                .findAny()
                .orElseGet(() -> DescriptiveOptional.of(CashHandlers.DEFAULT))
                .mapToInt(this::getNumberOfCassettes)
                .mapToObject(numberOfCassettes -> readCassetteData(ndcCharBuffer, numberOfCassettes))
                .getOrThrow(ConfigurationException::new);
    }

    private int getNumberOfCassettes(CashHandlers cashHandlersOption) {
        return cashHandlersOption.hasBit0() ? 7 : 4;
    }

    private List<Integer> readCassetteData(NdcCharBuffer ndcCharBuffer, int numberOfCassettes) {
        final List<Integer> cassetteData = new ArrayList<>(numberOfCassettes);
        for (int i = 0; i < numberOfCassettes; i++) {
            final DescriptiveOptionalInt optionalCassetteData = ndcCharBuffer.tryReadInt(5);
            if (optionalCassetteData.isPresent()) {
                cassetteData.add(optionalCassetteData.get());
            } else {
                final String fieldName = String.format("Last Transaction Notes Dispensed cassette %d Data", i + 1);
                NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(), fieldName,
                        optionalCassetteData.description(), ndcCharBuffer);
            }
        }
        return cassetteData;
    }
}
