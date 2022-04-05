package io.github.jokoroukwu.jndc.terminal.statusmessage;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.mac.MacAcceptor;
import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.StringJoiner;

public class SolicitedStatusMessageBuilder<V extends SolicitedStatusInformation> implements MacAcceptor<SolicitedStatusMessageBuilder<V>> {
    private Luno luno;
    private long timeVariantNumber = -1;
    private StatusDescriptor statusDescriptor;
    private CompletionData completionData;
    private V statusInformation;
    private String mac = Strings.EMPTY_STRING;

    public SolicitedStatusMessageBuilder<V> withLuno(Luno luno) {
        this.luno = luno;
        return this;
    }

    public SolicitedStatusMessageBuilder<V> withTimeVariantNumber(long timeVariantNumber) {
        this.timeVariantNumber = timeVariantNumber;
        return this;
    }

    public SolicitedStatusMessageBuilder<V> withStatusDescriptor(StatusDescriptor statusDescriptor) {
        this.statusDescriptor = statusDescriptor;
        return this;
    }

    public SolicitedStatusMessageBuilder<V> withCompletionData(CompletionData completionData) {
        this.completionData = completionData;
        return this;
    }

    public SolicitedStatusMessageBuilder<V> withStatusInformation(V statusInformation) {
        this.statusInformation = statusInformation;
        return this;
    }

    public SolicitedStatusMessageBuilder<V> withMac(String mac) {
        this.mac = mac;
        return this;
    }

    public Luno getLuno() {
        return luno;
    }

    public long getTimeVariantNumber() {
        return timeVariantNumber;
    }

    public StatusDescriptor getStatusDescriptor() {
        return statusDescriptor;
    }

    public CompletionData getCompletionData() {
        return completionData;
    }

    public V getStatusInformation() {
        return statusInformation;
    }

    public String getMac() {
        return mac;
    }

    public SolicitedStatusMessage<V> build() {
        return new SolicitedStatusMessage<>(luno, timeVariantNumber, statusDescriptor, completionData, statusInformation, mac);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SolicitedStatusMessageBuilder.class.getSimpleName() + ": {", "}")
                .add("luno: " + luno)
                .add("timeVariantNumber: " + timeVariantNumber)
                .add("statusDescriptor: " + statusDescriptor)
                .add("statusInformation: " + statusInformation)
                .add("mac: '" + mac + '\'')
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolicitedStatusMessageBuilder<?> that = (SolicitedStatusMessageBuilder<?>) o;
        return ((timeVariantNumber < 0 && that.timeVariantNumber < 0) || timeVariantNumber == that.timeVariantNumber) &&
                statusDescriptor == that.statusDescriptor &&
                Objects.equals(luno, that.luno) &&
                Objects.equals(statusInformation, that.statusInformation) &&
                Objects.equals(mac, that.mac);
    }

    @Override
    public int hashCode() {
        return Objects.hash(luno, timeVariantNumber, statusDescriptor, statusInformation, mac);
    }
}
