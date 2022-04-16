package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pckln.MaxPinDigitsChecked;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pckln.PinVerificationType;

import java.util.Optional;

import static io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand.COMMAND_NAME;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class PanPadAppender extends ChainedNdcComponentAppender<FitBuilder> {
    public static final String FIELD_NAME = "PANPD (PAN Pad)";

    protected PanPadAppender(NdcComponentAppender<FitBuilder> nextAppender) {
        super(nextAppender);
    }

    public PanPadAppender() {
        this(new Track3PinAppender());
    }

    public static int validatePanPad(int panPad, int pinPad, MaxPinDigitsChecked maxPinDigitsChecked) {
        final Optional<String> optionalError = validateValue(panPad, pinPad, maxPinDigitsChecked);
        if (optionalError.isEmpty()) {
            return panPad;
        }
        throw new IllegalArgumentException(String.format("%s %s", FIELD_NAME, optionalError.get()));
    }

    private static Optional<String> validateValue(int panPad, int pinPad, MaxPinDigitsChecked maxPinDigitsChecked) {
        final int firstPanPadDigit = panPad >> 4;
        final int secondPanPadDigit = panPad & 0b00001111;

        if (maxPinDigitsChecked.getPinVerificationType() == PinVerificationType.DES) {
            if (firstPanPadDigit != 0x00 && firstPanPadDigit != 0x08) {
                var template = "first digit should be 0x00 or 0x08 for 'DES' verification type but was 0x%X";
                return Optional.of(String.format(template, firstPanPadDigit));
            }
            final int pinPadFirstDigit = pinPad >> 4;
            if (secondPanPadDigit != pinPadFirstDigit) {
                var template = "second digit should have the same value as 'PIN Pad' but was 0x%X";
                return Optional.of(String.format(template, secondPanPadDigit));
            }
        }
        return Optional.empty();
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, FitBuilder stateObject) {
        ndcCharBuffer.tryReadInt(3)
                .ifPresent(panPad -> validateValue(panPad, stateObject.getPinPad(), stateObject.getMaxPinDigitsChecked())
                        .ifPresent(errorMessage -> onFieldParseError(COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer)))
                .resolve(stateObject::withPanPad, errorMessage -> onFieldParseError(COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextAppender(ndcCharBuffer, stateObject);
    }
}
