package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pckln;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.PinPadAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pmxpn.MaxPinDigitsEnteredAppender;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class MaxPinDigitsCheckedAppender extends ChainedNdcComponentAppender<FitBuilder> {
    public static final String FIELD_NAME = "PCKLN (Maximum PIN Digits Checked)";

    public MaxPinDigitsCheckedAppender(NdcComponentAppender<FitBuilder> nextAppender) {
        super(nextAppender);
    }

    public MaxPinDigitsCheckedAppender() {
        this(new PinPadAppender());
    }


    private static DescriptiveOptional<MaxPinDigitsChecked> toMaxPinDigitsChecked(int value,
                                                                                  PinVerificationType pinVerificationType,
                                                                                  int maxPinDigitsEntered) {
        final int pinDigits = value & 0b00011111;
        if (pinVerificationType == PinVerificationType.DES) {
            return toDes(pinDigits, maxPinDigitsEntered);
        }
        if (pinDigits <= maxPinDigitsEntered) {
            return DescriptiveOptional.of(new MaxPinDigitsChecked(pinVerificationType, maxPinDigitsEntered));
        }

        return DescriptiveOptional.empty(() -> pmxpnLengthExceededError(pinDigits, maxPinDigitsEntered));
    }


    private static DescriptiveOptional<MaxPinDigitsChecked> toDes(int digitsToCheck, int maxPinDigitsEntered) {
        if (digitsToCheck == 0 || digitsToCheck <= maxPinDigitsEntered) {
            return DescriptiveOptional.of(new MaxPinDigitsChecked(PinVerificationType.DES, digitsToCheck, null));
        }
        return DescriptiveOptional.empty(() -> pmxpnLengthExceededError(digitsToCheck, maxPinDigitsEntered));
    }

    private static String pmxpnLengthExceededError(int digitsToCheck, int maxPinDigitsEntered) {
        return String.format("'PIN digits to check' should be less or equal to '%s' (%d) but was %d",
                MaxPinDigitsEnteredAppender.FIELD_NAME, maxPinDigitsEntered, digitsToCheck);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, FitBuilder stateObject) {
        final int value = ndcCharBuffer.tryReadInt(3)
                .getOrThrow(errorMessage -> withMessage(FIELD_NAME, FitDataLoadCommand.COMMAND_NAME, errorMessage, ndcCharBuffer));

        PinVerificationType.forValue(value >> 5)
                .flatMap(type -> toMaxPinDigitsChecked(value, type, stateObject.getMaxPinDigitsEntered().getNumberOfPinDigits()))
                .resolve(stateObject::withMaxPinDigitsChecked,
                        errorMessage -> onFieldParseError(FIELD_NAME, FitDataLoadCommand.COMMAND_NAME, errorMessage, ndcCharBuffer));

        callNextAppender(ndcCharBuffer, stateObject);
    }
}
