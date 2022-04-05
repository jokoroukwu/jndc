package io.github.jokoroukwu.jndc.central.transactionreply.screendisplayupdate;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyCommandBuilder;
import io.github.jokoroukwu.jndc.central.transactionreply.cardflag.CardFlagAppender;
import io.github.jokoroukwu.jndc.screen.Screen;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.MessageCoordinationNumberAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

public class ScreenDisplayUpdateAppender extends ChainedConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> {
    public static String FIELD_NAME = "Screen Display Update";
    private final NdcComponentReader<Optional<String>> screenNumberReader;
    private final NdcComponentReader<List<Screen>> screenDataReader;

    public ScreenDisplayUpdateAppender(NdcComponentReader<Optional<String>> screenNumberReader,
                                       NdcComponentReader<List<Screen>> screenDataReader,
                                       ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender) {
        super(nextAppender);
        this.screenNumberReader = ObjectUtils.validateNotNull(screenNumberReader, "screenNumberReader");
        this.screenDataReader = ObjectUtils.validateNotNull(screenDataReader, "screenDataReader");
    }

    public ScreenDisplayUpdateAppender(ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender) {
        this(ScreenNumberReader.INSTANCE, ScreenDataReader.INSTANCE, nextAppender);
    }

    public ScreenDisplayUpdateAppender() {
        this(new MessageCoordinationNumberAppender<>(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(), true, new CardFlagAppender()));
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionReplyCommandBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        screenNumberReader.readComponent(ndcCharBuffer)
                .map(screenNumber -> new ScreenDisplayUpdate(screenNumber, screenDataReader.readComponent(ndcCharBuffer), null))
                .ifPresent(stateObject::withScreenDisplayUpdate);

        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }

}
