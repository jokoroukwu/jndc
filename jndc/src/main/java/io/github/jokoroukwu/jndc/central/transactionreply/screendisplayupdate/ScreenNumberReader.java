package io.github.jokoroukwu.jndc.central.transactionreply.screendisplayupdate;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.screen.Screen;
import io.github.jokoroukwu.jndc.util.Chars;

import java.util.Optional;

public enum ScreenNumberReader implements NdcComponentReader<Optional<String>> {
    INSTANCE;

    public static final String FIELD_NAME = "'Screen Number'";

    @Override
    public Optional<String> readComponent(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFollowingFieldSeparator()) {
            final String screenNumber = readScreenNumber(ndcCharBuffer);
            return Optional.of(screenNumber);
        }
        return Optional.empty();
    }

    private String readScreenNumber(NdcCharBuffer ndcCharBuffer) {
        final char firstChar = (char) ndcCharBuffer.tryGetCharAt(0)
                .getOrThrow(errorMessage -> NdcMessageParseException.withFieldName(FIELD_NAME, errorMessage, ndcCharBuffer));

        if (Chars.isInRange(firstChar, '0', '9')) {
            return readDecimalScreenNumber(firstChar, ndcCharBuffer);
        }
        //  only groups 'u' and 'l' are allowed
        if (firstChar == 'u' || firstChar == 'l') {
            return readGroupPrefixedScreenNumber(firstChar, ndcCharBuffer);
        }
        final String errorMessage = "should either begin with a decimal character "
                + "or group characters 'u' or 'l' but found character '%c' at position %d";
        throw NdcMessageParseException.withFieldName(FIELD_NAME, String.format(errorMessage, firstChar,
                ndcCharBuffer.position() - 1), ndcCharBuffer);
    }

    private String readDecimalScreenNumber(char firstChar, NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadCharSequence(3)
                .filter(Screen::isDecimalString, actual -> ()
                        -> String.format("expected a decimal number at position %d but was '%s'", ndcCharBuffer.position() - 3, actual))
                .filter(value -> value.charAt(1) != '0',
                        actual -> () -> String.format("should be a 3-digit decimal value in range 010-999 but was: '%s'", actual))
                .map(remaining -> firstChar + remaining)
                .getOrThrow(errorMessage -> NdcMessageParseException.withFieldName(FIELD_NAME, errorMessage, ndcCharBuffer));
    }

    private String readGroupPrefixedScreenNumber(char firstChar, NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadCharSequence(4)
                .filter(Screen::isDecimalString, actual -> ()
                        -> String.format("expected a decimal number at position %d but was '%s'", ndcCharBuffer.position() - 4, actual))
                .map(number -> firstChar + number)
                .getOrThrow(errorMessage -> NdcMessageParseException.withFieldName(FIELD_NAME, errorMessage, ndcCharBuffer));
    }
}
