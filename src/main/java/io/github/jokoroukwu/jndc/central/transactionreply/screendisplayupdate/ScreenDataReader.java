package io.github.jokoroukwu.jndc.central.transactionreply.screendisplayupdate;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.screen.Screen;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.*;

public enum ScreenDataReader implements NdcComponentReader<List<Screen>> {
    INSTANCE;

    @Override
    public List<Screen> readComponent(NdcCharBuffer ndcCharBuffer) {
        //  screen display data field is optional
        if (ndcCharBuffer.hasFollowingFieldSeparator()) {
            return List.of();
        }
        return readScreens(ndcCharBuffer);
    }

    private List<Screen> readScreens(NdcCharBuffer ndcCharBuffer) {
        Screen nextScreen = readScreen(ndcCharBuffer);
        //  may have just a single screen
        if (ndcCharBuffer.hasFollowingFieldSeparator()) {
            return List.of(nextScreen);
        }

        final ArrayList<Screen> displayDataUpdate = new ArrayList<>();
        displayDataUpdate.add(nextScreen);
        do {
            skipDelimiterData(ndcCharBuffer);
            nextScreen = readScreen(ndcCharBuffer);
            displayDataUpdate.add(nextScreen);
        } while (!ndcCharBuffer.hasFollowingFieldSeparator());

        displayDataUpdate.trimToSize();
        return Collections.unmodifiableList(displayDataUpdate);
    }

    private void skipDelimiterData(NdcCharBuffer ndcCharBuffer) {
        ndcCharBuffer.trySkipGroupSeparator()
                .ifPresent(errorMessage
                        -> onNoGroupSeparator("'Screen Display Update': Reserved bytes", errorMessage, ndcCharBuffer));
        ndcCharBuffer.trySkip(4)
                .ifPresent(errorMessage
                        -> onFieldParseError("'Screen Display Update': Reserved bytes", errorMessage, ndcCharBuffer));
    }


    private Screen readScreen(NdcCharBuffer ndcCharBuffer) {
        final String screenNumber = Screen.readScreenNumber(ndcCharBuffer)
                .getOrThrow(errorMessage
                        -> withMessage("Screen Display Update: Screen Number", errorMessage, ndcCharBuffer));

        final String screenData = readScreenData(ndcCharBuffer);
        return new Screen(screenNumber, screenData);
    }

    private String readScreenData(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return Strings.EMPTY_STRING;
        }
        final StringBuilder builder = new StringBuilder();
        do {
            builder.append(ndcCharBuffer.readNextChar());
        } while (ndcCharBuffer.hasFieldDataRemaining());

        return builder.toString();
    }
}
