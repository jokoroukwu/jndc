package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.reader;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.EnhancedConfigParamsLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.EnhancedConfigParamsLoadCommandBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.ConfigurationOption;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.factory.ConfigOptionFactoryBase;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.factory.ConfigurationOptionFactory;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Map;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class ConfigOptionAppender implements NdcComponentAppender<EnhancedConfigParamsLoadCommandBuilder> {
    public static final String OPTION_NUMBER_FIELD = "Option Number";
    public static final String OPTION_CODE_FIELD = "Option Code";

    private final ConfigurationOptionFactory optionFactory;

    public ConfigOptionAppender(ConfigurationOptionFactory optionFactory) {
        this.optionFactory = ObjectUtils.validateNotNull(optionFactory, "optionFactory");
    }

    public ConfigOptionAppender() {
        this(ConfigOptionFactoryBase.INSTANCE);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, EnhancedConfigParamsLoadCommandBuilder stateObject) {
        final Map<Integer, ConfigurationOption> availableOptions = stateObject.getOptionMap();
        final int optionNumber = ndcCharBuffer.tryReadInt(2)
                .ifEmpty(errorMessage -> onFieldParseError(EnhancedConfigParamsLoadCommand.COMMAND_NAME, OPTION_NUMBER_FIELD, errorMessage, ndcCharBuffer))
                .get();

        ndcCharBuffer.tryReadCharSequence(3)
                .ifEmpty(errorMessage -> onFieldParseError(EnhancedConfigParamsLoadCommand.COMMAND_NAME, OPTION_CODE_FIELD, errorMessage, ndcCharBuffer))
                .flatMap(optionCode -> optionFactory.getOption(optionNumber, optionCode, availableOptions))
                .resolve(stateObject::addOption, errorMessage
                        -> onFieldParseError(EnhancedConfigParamsLoadCommand.COMMAND_NAME, "'Configuration Option'" + optionNumber, errorMessage, ndcCharBuffer));

    }

}
