package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.screen.ScreenAcceptor;

public abstract class AbstractScreenAppender<T extends ScreenAcceptor<?>> extends ChainedNdcComponentAppender<T> {
    public static final String SCREEN_NUMBER_FIELD = "Screen Number";

    public AbstractScreenAppender(NdcComponentAppender<T> nextAppender) {
        super(nextAppender);
    }

}
