package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.configidload;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommand;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommandBuilder;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class ConfigurationIdNumberLoadAppender implements NdcComponentAppender<DataCommandBuilder<NdcComponent>> {
    public static final String CONFIG_ID_NUMBER_FIELD = "Configuration ID Number";

    private final ConfigurationIdNumberLoadCommandListener messageListener;

    public ConfigurationIdNumberLoadAppender(ConfigurationIdNumberLoadCommandListener messageListener) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void appendComponent(NdcCharBuffer messageStream, DataCommandBuilder<NdcComponent> stateObject) {
        ObjectUtils.validateNotNull(messageStream, "messageStream");
        ObjectUtils.validateNotNull(stateObject, "messageBuilder");

        messageStream.trySkipFieldSeparator()
                .ifPresent(errorMessage ->
                        NdcMessageParseException.onNoFieldSeparator(ConfigurationIdNumberLoadCommand.COMMAND_NAME, CONFIG_ID_NUMBER_FIELD, errorMessage, messageStream));

        messageStream.tryReadInt(4)
                .filter(value -> value >= 1, value ->
                        () -> String.format("value '%d' is not within valid range (0001-9999)", value))
                .mapToObject(ConfigurationIdNumberLoadCommand::new)
                .resolve(stateObject::withCommandData, errorMessage ->
                        NdcMessageParseException.onFieldParseError(ConfigurationIdNumberLoadCommand.COMMAND_NAME, CONFIG_ID_NUMBER_FIELD, errorMessage, messageStream));

        final DataCommand<? extends NdcComponent> configIdNumberLoadCommand = stateObject.build();
        messageListener.onConfigurationIdNumberLoadCommand((DataCommand<ConfigurationIdNumberLoadCommand>) configIdNumberLoadCommand);
    }
}
