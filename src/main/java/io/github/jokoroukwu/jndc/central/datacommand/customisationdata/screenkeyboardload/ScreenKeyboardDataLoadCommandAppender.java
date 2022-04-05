package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommand;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommandBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.fieldreader.KeyboardDataAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.fieldreader.ScreenKeyboardEntryBuilder;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.screen.ScreenAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.ArrayList;

public class ScreenKeyboardDataLoadCommandAppender implements NdcComponentAppender<DataCommandBuilder<NdcComponent>> {
    private final NdcComponentAppender<ScreenKeyboardEntryBuilder> screenKeyboardEntryReader;
    private final ScreenKeyboardLoadCommandListener messageListener;

    public ScreenKeyboardDataLoadCommandAppender(NdcComponentAppender<ScreenKeyboardEntryBuilder> screenKeyboardEntryReader,
                                                 ScreenKeyboardLoadCommandListener messageListener) {
        this.screenKeyboardEntryReader = ObjectUtils.validateNotNull(screenKeyboardEntryReader, "screenKeyboardEntryReader");
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
    }

    public ScreenKeyboardDataLoadCommandAppender(ScreenKeyboardLoadCommandListener messageListener) {
        this(new ScreenAppender<>(ScreenKeyboardLoadCommand.COMMAND_NAME, new KeyboardDataAppender()), messageListener);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void appendComponent(NdcCharBuffer charBuffer, DataCommandBuilder<NdcComponent> stateObject) {
        ObjectUtils.validateNotNull(charBuffer, "charBuffer");
        ObjectUtils.validateNotNull(stateObject, "messageBuilder");

        final ArrayList<ScreenKeyboardEntry> screenKeyboardEntries = new ArrayList<>();
        int entryIndex = 0;
        do {
            final ScreenKeyboardEntryBuilder screenKeyboardEntryBuilder = new ScreenKeyboardEntryBuilder();
            screenKeyboardEntryReader.appendComponent(charBuffer, screenKeyboardEntryBuilder);
            /*
            ScreenKeyboardEntry object construction will fail
            when Screen and Keyboard fields are properly delimited
            with group separators but neither contain actual data (empty).
            Hence, the exception should be handled properly.
            */
            try {
                screenKeyboardEntries.add(screenKeyboardEntryBuilder.build());
            } catch (IllegalArgumentException e) {
                NdcMessageParseException.onMessageParseError(ScreenKeyboardLoadCommand.COMMAND_NAME,
                        "Screen/Keyboard entry " + entryIndex + ": " + e.getMessage(), charBuffer);
            }
            ++entryIndex;
        } while (charBuffer.hasRemaining());

        screenKeyboardEntries.trimToSize();
        //  ScreenKeyboardEntry list must never be empty.
        //  Otherwise, object construction will fail.
        final ScreenKeyboardLoadCommand screenKeyboardLoadCommand = new ScreenKeyboardLoadCommand(screenKeyboardEntries);
        final DataCommand<? super ScreenKeyboardLoadCommand> screenKeyboardLoadMessage = stateObject
                .withCommandData(screenKeyboardLoadCommand)
                .build();
        messageListener.onScreenKeyboardLoadCommand((DataCommand<ScreenKeyboardLoadCommand>) screenKeyboardLoadMessage);
    }
}
