package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.fieldreader;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;

public abstract class AbstractScreenKeyboardFieldAppender implements NdcComponentAppender<ScreenKeyboardEntryBuilder> {
    private final NdcComponentAppender<ScreenKeyboardEntryBuilder> nextReader;

    public AbstractScreenKeyboardFieldAppender(NdcComponentAppender<ScreenKeyboardEntryBuilder> nextReader) {
        super();
        this.nextReader = nextReader;
    }

    @Override
    public final void appendComponent(NdcCharBuffer ndcCharBuffer, ScreenKeyboardEntryBuilder stateObject) {
        readField(ndcCharBuffer, stateObject);
    }

    protected abstract void readField(NdcCharBuffer ndcCharBuffer, ScreenKeyboardEntryBuilder builder);

    protected void callNextReader(NdcCharBuffer ndcCharBuffer, ScreenKeyboardEntryBuilder builder) {
        if (nextReader != null) {
            nextReader.appendComponent(ndcCharBuffer, builder);
        }
    }
}
