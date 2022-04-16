package io.github.jokoroukwu.jndc.central.transactionreply;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.notestodispense.NotesToDispenseAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.util.text.Strings;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onNoFieldSeparator;

public class NextStateIdAppender extends ChainedConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> {
    public static final String FIELD_NAME = "Next State ID Data";

    public NextStateIdAppender(ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender) {
        super(nextAppender);
    }

    public NextStateIdAppender() {
        super(new NotesToDispenseAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionReplyCommandBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage -> onNoFieldSeparator(FIELD_NAME, errorMessage, ndcCharBuffer));

        //  field may be empty
        if (ndcCharBuffer.hasFieldDataRemaining()) {
            ndcCharBuffer.tryReadCharSequence(3)
                    .filter(this::isNotReserved, value -> () -> String.format("'%s' is a reserved state number", value))
                    .filter(Strings::isAlphanumeric, value -> () -> String.format("should be alphanumeric but was '%s'", value))
                    .resolve(stateObject::withNextStateIdData,
                            errorMessage -> onFieldParseError(FIELD_NAME, errorMessage, ndcCharBuffer));
        }
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private boolean isNotReserved(String value) {
        return !"255".equals(value);
    }

}
