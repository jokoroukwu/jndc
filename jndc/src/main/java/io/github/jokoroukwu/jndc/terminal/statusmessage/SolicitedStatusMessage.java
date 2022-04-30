package io.github.jokoroukwu.jndc.terminal.statusmessage;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.mac.MacReaderBase;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.TerminalOriginatedMessage;
import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.StringJoiner;

public class SolicitedStatusMessage<V extends SolicitedStatusInformation> implements TerminalOriginatedMessage {
    public static final String COMMAND_NAME = TerminalMessageClass.SOLICITED + ": " + TerminalMessageSubClass.STATUS_MESSAGE;
    private final Luno luno;
    private final long timeVariantNumber;
    private final StatusDescriptor statusDescriptor;
    private final CompletionData completionData;
    private final V statusInformation;
    private final String mac;

    public SolicitedStatusMessage(Luno luno,
                                  long timeVariantNumber,
                                  StatusDescriptor statusDescriptor,
                                  CompletionData completionData,
                                  V statusInformation,
                                  String mac) {
        this.luno = ObjectUtils.validateNotNull(luno, "LUNO");
        this.statusDescriptor = ObjectUtils.validateNotNull(statusDescriptor, "statusDescriptor");
        this.mac = MacReaderBase.validateMac(mac);
        this.completionData = completionData;
        this.timeVariantNumber = timeVariantNumber;
        this.statusInformation = statusInformation;
    }

    public static <T extends SolicitedStatusInformation> SolicitedStatusMessageBuilder<T> builder() {
        return new SolicitedStatusMessageBuilder<>();
    }

    public SolicitedStatusMessageBuilder<V> copy() {
        return new SolicitedStatusMessageBuilder<V>()
                .withLuno(luno)
                .withTimeVariantNumber(timeVariantNumber)
                .withStatusDescriptor(statusDescriptor)
                .withStatusInformation(statusInformation)
                .withMac(mac);
    }

    @Override
    public TerminalMessageClass getMessageClass() {
        return TerminalMessageClass.SOLICITED;
    }

    @Override
    public TerminalMessageSubClass getMessageSubclass() {
        return TerminalMessageSubClass.STATUS_MESSAGE;
    }

    public Luno getLuno() {
        return luno;
    }

    public OptionalLong getTimeVariantNumber() {
        return timeVariantNumber >= 0 ? OptionalLong.of(timeVariantNumber) : OptionalLong.empty();
    }

    public StatusDescriptor getStatusDescriptor() {
        return statusDescriptor;
    }

    public Optional<CompletionData> getCompletionData() {
        return Optional.ofNullable(completionData);
    }

    public Optional<V> getStatusInformation() {
        return Optional.ofNullable(statusInformation);
    }

    public String getMac() {
        return mac;
    }

    @Override
    public String toNdcString() {
        final NdcStringBuilder builder = new NdcStringBuilder(128)
                .appendComponent(TerminalMessageClass.SOLICITED)
                .appendComponent(TerminalMessageSubClass.STATUS_MESSAGE)
                .appendFs()
                .appendComponent(luno)
                .appendFs()
                .appendFs();
        if (timeVariantNumber >= 0) {
            builder.appendZeroPaddedHex(timeVariantNumber, 8)
                    .appendFs();
        }
        builder.appendComponent(statusDescriptor);
        if (completionData != null) {
            builder.appendGs()
                    .appendComponent(completionData);
        }
        return builder
                .appendFieldGroup(statusInformation)
                .appendFieldGroup(mac)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SolicitedStatusMessage.class.getSimpleName() + ": {", "}")
                .add("messageClass: " + TerminalMessageClass.SOLICITED)
                .add("messageSubClass: " + TerminalMessageSubClass.STATUS_MESSAGE)
                .add("luno: " + luno)
                .add("timeVariantNumber: '" + timeVariantNumber + "'")
                .add("statusDescriptor: " + statusDescriptor)
                .add("completionData: " + completionData)
                .add("statusInformation: " + statusInformation)
                .add("MAC: '" + mac + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolicitedStatusMessage<?> that = (SolicitedStatusMessage<?>) o;
        return ((timeVariantNumber < 0 && that.timeVariantNumber < 0) || timeVariantNumber == that.timeVariantNumber) &&
                statusDescriptor == that.statusDescriptor &&
                mac.equals(that.mac) &&
                luno.equals(that.luno) &&
                Objects.equals(completionData, that.completionData) &&
                Objects.equals(statusInformation, that.statusInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                luno,
                Math.max(timeVariantNumber, -1),
                statusDescriptor,
                completionData,
                statusInformation,
                mac);
    }
}
