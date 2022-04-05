package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.text.Strings;

public class DefaultAppLabelAppender extends TerminalAcceptableAidsFieldAppender {
    public static final String FIELD_NAME = "Default Application Label";
    private static final String length = "Length of Default Application Label";

    public DefaultAppLabelAppender(TerminalAcceptableAidsFieldAppender nextReader) {
        super(nextReader);
    }

    public DefaultAppLabelAppender() {
        this(new PrimaryAidICCAppTypeAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TerminalApplicationIdEntryBuilder stateObject) {
        final int defaultAppLabelLength = ndcCharBuffer.tryReadHexInt(2)
                .filter(value -> value <= 16, length -> () -> "value should be in range 0-16 but was " + length)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(IccTerminalAcceptableAppIdsTable.COMMAND_NAME, length, errorMessage, ndcCharBuffer));

        if (defaultAppLabelLength > 0) {
            ndcCharBuffer.tryReadCharSequence(defaultAppLabelLength)
                    .filter(value -> Strings.isWithinCharRange(value, ' ', '~'),
                            value -> () -> String.format("should be withing character range 0x20-0x7E but was '%s'", value))
                    .resolve(stateObject::withDefaultAppLabel, errorMessage -> NdcMessageParseException.onFieldParseError(IccTerminalAcceptableAppIdsTable.COMMAND_NAME,
                            FIELD_NAME, errorMessage, ndcCharBuffer));
        }
        callNextAppender(ndcCharBuffer, stateObject);
    }
}
