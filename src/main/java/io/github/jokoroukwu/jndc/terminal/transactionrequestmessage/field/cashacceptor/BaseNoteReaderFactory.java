package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor;

import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.BnaSettings;
import io.github.jokoroukwu.jndc.terminal.ConfigurationOptions;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public enum BaseNoteReaderFactory implements NdcComponentReaderFactory<ConfigurationOptions, CashAcceptorNote> {
    INSTANCE;

    @Override
    public NdcComponentReader<CashAcceptorNote> getNoteReader(ConfigurationOptions configurationOptions) {
        ObjectUtils.validateNotNull(configurationOptions, "ConfigurationOptions cannot be null");

        return evaluateNumberOfNoteDigits(configurationOptions) == 3
                ? ThreeDigitNoteReader.INSTANCE
                : TwoDigitNoteReader.INSTANCE;
    }

    private int evaluateNumberOfNoteDigits(ConfigurationOptions configurationOptions) {
        final int optionCode = configurationOptions
                .getOption(BnaSettings.NUMBER)
                .orElse(BnaSettings.DEFAULT)
                .getCode();

        return BnaSettings.isAcceptMaxNotes(optionCode) ? 3 : 2;
    }
}
