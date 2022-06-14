package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreaderwriter;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.mac.MacAcceptor;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.terminal.statusmessage.*;
import io.github.jokoroukwu.jndc.terminal.statusmessage.cardreader.*;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.ErrorSeverity;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.SuppliesStatus;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus.DiagnosticStatus;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class SolicitedCardReaderWriterStatusInfoAppenderTest extends DeviceStatusInformationTest {
    private final SolicitedStatusMessageBuilder<SolicitedStatusInformation> solicitedStatusMessageBuilder
            = new SolicitedStatusMessageBuilder<>()
            .withLuno(Luno.DEFAULT)
            .withTimeVariantNumber(-1)
            .withStatusDescriptor(StatusDescriptor.DEVICE_FAULT);

    private ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<?>> macAppender;
    private ConfigurableNdcComponentAppender<CardReaderStatusInfoBuilder> fieldAppender;
    private SolicitedCardReaderWriterStatusInfoMessageListener messageListenerMock;


    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        macAppender = mock(ConfigurableNdcComponentAppender.class);
        fieldAppender = mock(ConfigurableNdcComponentAppender.class);
        messageListenerMock = mock(SolicitedCardReaderWriterStatusInfoMessageListener.class);
    }


    @Test
    public void should_append_expected_value() {
        final CardReaderWriterStatus cardReaderWriterStatus = CardReaderWriterStatus.CARD_EJECT_FAILURE;
        final List<ErrorSeverity> errorSeverities = List.of(ErrorSeverity.NO_ERROR, ErrorSeverity.ROUTINE);
        final DiagnosticStatus diagnosticStatus = new DiagnosticStatus(1, BmpStringGenerator.HEX.fixedLength(2));
        final SuppliesStatus suppliesStatus = SuppliesStatus.OVERFILL;
        final String mac = BmpStringGenerator.HEX.fixedLength(8);
        final FakeMacAppender<SolicitedStatusMessageBuilder<?>> fakeMacAppender = new FakeMacAppender<>(mac);
        final FakeFieldAppender fakeFieldAppender = new FakeFieldAppender(
                cardReaderWriterStatus,
                errorSeverities,
                suppliesStatus,
                dummyCompletionData,
                diagnosticStatus
        );
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING);
        new SolicitedCardReaderWriterStatusInfoAppender(messageListenerMock, fakeFieldAppender, fakeMacAppender)
                .appendComponent(buffer, solicitedStatusMessageBuilder, deviceConfigurationMock);

        final SolicitedStatusMessage<CardReaderWriterStatusInfo> expectedMessage = SolicitedStatusMessage.<CardReaderWriterStatusInfo>builder()
                .withTimeVariantNumber(solicitedStatusMessageBuilder.getTimeVariantNumber())
                .withLuno(solicitedStatusMessageBuilder.getLuno())
                .withStatusDescriptor(StatusDescriptor.DEVICE_FAULT)
                .withCompletionData(null)
                .withStatusInformation(new CardReaderStatusInfoBuilder()
                        .withTransactionDeviceStatus(cardReaderWriterStatus)
                        .withErrorSeverities(errorSeverities)
                        .withDiagnosticStatus(diagnosticStatus)
                        .withCompletionData(dummyCompletionData)
                        .withTransactionDeviceStatus(cardReaderWriterStatus)
                        .withSuppliesStatus(suppliesStatus)
                        .build())
                .withMac(mac)
                .build();

        verify(messageListenerMock, times(1))
                .onSolicitedCardReaderWriterStatusInfoMessage(expectedMessage);

        verifyNoMoreInteractions(messageListenerMock);
    }

    private static final class FakeMacAppender<T extends MacAcceptor<?>> implements ConfigurableNdcComponentAppender<T> {
        private final String mac;

        private FakeMacAppender(String mac) {
            this.mac = mac;
        }

        @Override
        public void appendComponent(NdcCharBuffer ndcCharBuffer, T stateObject, DeviceConfiguration deviceConfiguration) {
            stateObject.withMac(mac);
        }
    }

    private static final class FakeFieldAppender implements ConfigurableNdcComponentAppender<CardReaderStatusInfoBuilder> {
        private final CardReaderWriterStatus cardReaderWriterStatus;
        private final List<ErrorSeverity> errorSeverities;
        private final SuppliesStatus suppliesStatus;
        private final CompletionData completionData;
        private final DiagnosticStatus diagnosticStatus;

        private FakeFieldAppender(CardReaderWriterStatus cardReaderWriterStatus,
                                  List<ErrorSeverity> errorSeverities,
                                  SuppliesStatus suppliesStatus,
                                  CompletionData completionData,
                                  DiagnosticStatus diagnosticStatus) {
            this.cardReaderWriterStatus = cardReaderWriterStatus;
            this.errorSeverities = errorSeverities;
            this.suppliesStatus = suppliesStatus;
            this.completionData = completionData;
            this.diagnosticStatus = diagnosticStatus;
        }

        @Override
        public void appendComponent(NdcCharBuffer ndcCharBuffer, CardReaderStatusInfoBuilder stateObject, DeviceConfiguration deviceConfiguration) {
            stateObject.withTransactionDeviceStatus(cardReaderWriterStatus)
                    .withErrorSeverities(errorSeverities)
                    .withSuppliesStatus(suppliesStatus)
                    .withCompletionData(completionData)
                    .withDiagnosticStatus(diagnosticStatus);
        }
    }
}
