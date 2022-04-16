package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pckln.MaxPinDigitsChecked;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pckln.PinVerificationType;

import java.util.Optional;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class PanDataLengthAppender extends ChainedNdcComponentAppender<FitBuilder> {
    public static final String FIELD_NAME = "PANLN (PAN Data Length)";

    protected PanDataLengthAppender(NdcComponentAppender<FitBuilder> nextAppender) {
        super(nextAppender);
    }

    public PanDataLengthAppender() {
        this(new PanPadAppender());
    }

    public static int validatePanDataLength(int panDataLength, MaxPinDigitsChecked maxPinDigitsChecked) {
        final Optional<String> optionalError = validateValue(maxPinDigitsChecked, panDataLength);
        if (optionalError.isEmpty()) {
            return panDataLength;
        }
        throw new IllegalArgumentException(String.format("%s %s", FIELD_NAME, optionalError.get()));
    }

    private static Optional<String> validateValue(MaxPinDigitsChecked maxPinDigitsChecked, int panDataLength) {
        final PinVerificationType pinVerificationType = maxPinDigitsChecked.getPinVerificationType();
        final int  pinCheckLength = maxPinDigitsChecked.getPinDigitsChecked();

        if (panDataLength == 0xFF || panDataLength == 0x1F) {
            return Optional.empty();
        }
        if (pinVerificationType == PinVerificationType.VISA) {
            return validateVisa(panDataLength, pinCheckLength);
        }
        if (pinVerificationType == PinVerificationType.DES) {
            return validateDes(panDataLength, pinCheckLength);
        }
        return Optional.empty();
    }

    private static Optional<String> validateVisa(int panDataLength, int pinCheckLength) {
        if (panDataLength == 0x0D || panDataLength == 0x10) {
            return Optional.empty();
        }
        if (panDataLength >= pinCheckLength) {
            return Optional.empty();
        }
        var template = "should be 0x0D or 0x10 and equal or greater than PIN check length for 'VISA PIN verification type' but was 0x%X";
        return Optional.of(String.format(template, panDataLength));
    }

    private static Optional<String> validateDes(int panDataLength, int checkLength) {
        if (panDataLength <= 0x10 && panDataLength >= checkLength) {
            return Optional.empty();
        }
        var template = "should be in range 0x00-0x10 and equal or greater than PIN check length for 'DES PIN verification type'";
        return Optional.of(String.format(template, panDataLength));
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, FitBuilder stateObject) {
        ndcCharBuffer.tryReadInt(3)
                .ifPresent(val -> validateValue(stateObject.getMaxPinDigitsChecked(), val)
                        .ifPresent(errorMessage -> onFieldParseError(FitDataLoadCommand.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer)))
                .resolve(stateObject::withPanDataLength,
                        errorMessage -> onFieldParseError(FitDataLoadCommand.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextAppender(ndcCharBuffer, stateObject);
    }
}
