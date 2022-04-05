package io.github.jokoroukwu.jndc.central.datacommand.extendedencyrptionkeychange;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommand;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommandBuilder;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class ExtendedEncryptionKeyChangeCommandAppender implements NdcComponentAppender<DataCommandBuilder<NdcComponent>> {
    public static final int SINGLE_LENGTH_DES_SIZE = 0x18;
    public static final int DOUBLE_LENGTH_DES_SIZE = 0X30;
    public static final int VISA_KEY_TABLE_SIZE = 0X120;

    private final ExtendedEncryptionKeyChangeCommandListener commandListener;

    public ExtendedEncryptionKeyChangeCommandAppender(ExtendedEncryptionKeyChangeCommandListener commandListener) {
        this.commandListener = ObjectUtils.validateNotNull(commandListener, "commandListener");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void appendComponent(NdcCharBuffer ndcCharBuffer, DataCommandBuilder<NdcComponent> stateObject) {
        final Modifier modifier = readModifier(ndcCharBuffer);
        final String keyData = readKeyData(ndcCharBuffer);
        final ExtendedEncryptionKeyChangeCommand command = new ExtendedEncryptionKeyChangeCommand(modifier, keyData);

        final DataCommand<? extends NdcComponent> message = stateObject
                .withCommandData(command)
                .build();
        commandListener.onExtendedEncryptionKeyChangeCommand((DataCommand<ExtendedEncryptionKeyChangeCommand>) message);
    }


    private Modifier readModifier(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(Modifier::forValue)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(ExtendedEncryptionKeyChangeCommand.COMMAND_NAME, "'Modifier'", errorMessage, ndcCharBuffer));
    }

    private String readKeyData(NdcCharBuffer ndcCharBuffer) {
        if (ndcCharBuffer.hasRemaining()) {
            ndcCharBuffer.trySkipFieldSeparator()
                    .ifPresent(errorMessage
                            -> onFieldParseError(ExtendedEncryptionKeyChangeCommand.COMMAND_NAME, "'Key Data Size'", errorMessage, ndcCharBuffer));

            return ndcCharBuffer.tryReadHexInt(3)
                    .filter(this::isKeyDataSizeValid, size -> () -> String.format("value should be %03X or %03X or %03X but was %03X",
                            SINGLE_LENGTH_DES_SIZE, DOUBLE_LENGTH_DES_SIZE, VISA_KEY_TABLE_SIZE, size))
                    .ifEmpty(errorMessage -> onFieldParseError(ExtendedEncryptionKeyChangeCommand.COMMAND_NAME, "'Key Data Size'", errorMessage, ndcCharBuffer))
                    .flatMapToObject(ndcCharBuffer::tryReadCharSequence)
                    .filter(Strings::isDecimal, data -> () -> String.format("value should be decimal but was '%s'", data))
                    .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(ExtendedEncryptionKeyChangeCommand.COMMAND_NAME, "'New Key Data'", errorMessage, ndcCharBuffer));
        }
        return Strings.EMPTY_STRING;
    }

    private boolean isKeyDataSizeValid(int intValue) {
        switch (intValue) {
            case SINGLE_LENGTH_DES_SIZE:
            case DOUBLE_LENGTH_DES_SIZE:
            case VISA_KEY_TABLE_SIZE: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

}
