package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.powerfailure;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.trailingdata.TrailingDataChecker;
import io.github.jokoroukwu.jndc.trailingdata.TrailingDataCheckerBase;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onMessageParseError;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;
import static io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.powerfailure.PowerFailure.COMMAND_NAME;

public class PowerFailureMessageAppender implements ConfigurableNdcComponentAppender<UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation>> {
    private final PowerFailureMessageListener messageListener;
    private final TrailingDataChecker trailingDataChecker;

    public PowerFailureMessageAppender(PowerFailureMessageListener messageListener, TrailingDataChecker trailingDataChecker) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        this.trailingDataChecker = ObjectUtils.validateNotNull(trailingDataChecker, "trailingDataChecker");
    }

    public PowerFailureMessageAppender(PowerFailureMessageListener messageListener) {
        this(messageListener, TrailingDataCheckerBase.INSTANCE);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {

        final int configurationId = ndcCharBuffer.tryReadInt(4)
                .filter(val -> val <= 9999, val -> () -> "should be in range 0-9999 but was: " + val)
                .getOrThrow(errorMessage -> withMessage(COMMAND_NAME, "Device Status", errorMessage, ndcCharBuffer));

        trailingDataChecker.getErrorMessageOnTrailingData(ndcCharBuffer)
                .ifPresent(errorMessage -> onMessageParseError(COMMAND_NAME, errorMessage, ndcCharBuffer));

        final PowerFailure powerFailure = new PowerFailure(configurationId, null);
        final UnsolicitedStatusMessageBuilder<? extends UnsolicitedStatusInformation> messageBuilder = stateObject
                .withStatusInformation(powerFailure);

        @SuppressWarnings("unchecked") final UnsolicitedStatusMessage<PowerFailure> powerFailureMessage
                = (UnsolicitedStatusMessage<PowerFailure>) messageBuilder.build();
        messageListener.onPowerFailureMessage(powerFailureMessage);
    }
}
