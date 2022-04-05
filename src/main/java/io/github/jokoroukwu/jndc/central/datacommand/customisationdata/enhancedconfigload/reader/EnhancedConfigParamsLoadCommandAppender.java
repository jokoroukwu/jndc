package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.reader;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommand;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommandBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.EnhancedConfigParamsLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.EnhancedConfigParamsLoadCommandBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.EnhancedConfigParamsLoadCommandListener;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.timer.TimerAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onNoFieldSeparator;

public class EnhancedConfigParamsLoadCommandAppender implements NdcComponentAppender<DataCommandBuilder<NdcComponent>> {
    private final NdcComponentAppender<EnhancedConfigParamsLoadCommandBuilder> optionAppender;
    private final NdcComponentAppender<EnhancedConfigParamsLoadCommandBuilder> timerReader;
    private final EnhancedConfigParamsLoadCommandListener messageListener;

    public EnhancedConfigParamsLoadCommandAppender(NdcComponentAppender<EnhancedConfigParamsLoadCommandBuilder> optionAppender,
                                                   NdcComponentAppender<EnhancedConfigParamsLoadCommandBuilder> timerReader,
                                                   EnhancedConfigParamsLoadCommandListener messageListener) {
        this.optionAppender = ObjectUtils.validateNotNull(optionAppender, "optionReader");
        this.timerReader = ObjectUtils.validateNotNull(timerReader, "timerReader");
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
    }

    public EnhancedConfigParamsLoadCommandAppender(EnhancedConfigParamsLoadCommandListener messageListener) {
        this(new ConfigOptionAppender(), TimerAppender.INSTANCE, messageListener);
    }


    @Override
    @SuppressWarnings("unchecked")
    public void appendComponent(NdcCharBuffer ndcCharBuffer, DataCommandBuilder<NdcComponent> stateObject) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage -> onNoFieldSeparator(EnhancedConfigParamsLoadCommand.COMMAND_NAME, Luno.FIELD_NAME, errorMessage, ndcCharBuffer));

        final EnhancedConfigParamsLoadCommandBuilder enhancedConfigParamsLoadCommandBuilder = new EnhancedConfigParamsLoadCommandBuilder();
        ndcCharBuffer.tryReadCharSequence(3)
                .map(Luno::ofValue)
                .resolve(enhancedConfigParamsLoadCommandBuilder::withAdditionalLuno,
                        errorMessage -> onFieldParseError(EnhancedConfigParamsLoadCommand.COMMAND_NAME, Luno.FIELD_NAME, errorMessage, ndcCharBuffer));


        readOptions(ndcCharBuffer, enhancedConfigParamsLoadCommandBuilder);
        readTimers(ndcCharBuffer, enhancedConfigParamsLoadCommandBuilder);

        final DataCommand<? extends NdcComponent> dataCommand = stateObject
                .withCommandData(enhancedConfigParamsLoadCommandBuilder.build())
                .build();

        messageListener.onEnhancedConfigParamsLoadCommand((DataCommand<EnhancedConfigParamsLoadCommand>) dataCommand);
    }

    private void readOptions(NdcCharBuffer ndcCharBuffer, EnhancedConfigParamsLoadCommandBuilder builder) {
        //  options are optional
        if (ndcCharBuffer.hasRemaining()) {
            ndcCharBuffer.trySkipFieldSeparator()
                    .ifPresent(errorMessage
                            -> onNoFieldSeparator(EnhancedConfigParamsLoadCommand.COMMAND_NAME, "'Option Number'", errorMessage, ndcCharBuffer));
            //  field separator might immediately follow
            //  if only timers are present
            while (ndcCharBuffer.hasFieldDataRemaining()) {
                optionAppender.appendComponent(ndcCharBuffer, builder);
            }
        }
    }

    private void readTimers(NdcCharBuffer ndcCharBuffer, EnhancedConfigParamsLoadCommandBuilder builder) {
        if (ndcCharBuffer.hasRemaining()) {
            ndcCharBuffer.trySkipFieldSeparator()
                    .ifPresent(errorMessage
                            -> onNoFieldSeparator(EnhancedConfigParamsLoadCommand.COMMAND_NAME, "'Timer Number'", errorMessage, ndcCharBuffer));
            do {
                //  at least one timer should be present
                timerReader.appendComponent(ndcCharBuffer, builder);
            } while (ndcCharBuffer.hasRemaining());
        }
    }
}
