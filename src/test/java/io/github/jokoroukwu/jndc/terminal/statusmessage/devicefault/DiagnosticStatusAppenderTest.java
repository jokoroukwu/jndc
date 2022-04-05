package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.DeviceFaultTest;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader.CardReaderWriterFaultBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus.DiagnosticStatus;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus.DiagnosticStatusAppender;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.github.jokoroukwu.jndc.util.text.Strings.EMPTY_STRING;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiagnosticStatusAppenderTest extends DeviceFaultTest {
    private final String minData = BmpStringGenerator.HEX.fixedLength(2);
    private final String arbitraryData = BmpStringGenerator.HEX.fixedLength(10);
    private ConfigurableNdcComponentAppender<CardReaderWriterFaultBuilder> nextAppenderMock;
    private CardReaderWriterFaultBuilder builder;
    private DiagnosticStatusAppender<CardReaderWriterFaultBuilder> appender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        nextAppenderMock = mock(ConfigurableNdcComponentAppender.class);
        appender = new DiagnosticStatusAppender<>("commandName", nextAppenderMock);
        builder = new CardReaderWriterFaultBuilder();
    }

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR + "99" + minData, new DiagnosticStatus(99, minData), false},
                {NdcConstants.FIELD_SEPARATOR + "99" + minData, null, true},
                {NdcConstants.FIELD_SEPARATOR + "99" + arbitraryData, new DiagnosticStatus(99, arbitraryData), true},
                {NdcConstants.FIELD_SEPARATOR + "99" + arbitraryData, new DiagnosticStatus(99, arbitraryData), false},
                {NdcConstants.FIELD_SEPARATOR + "00", new DiagnosticStatus(0, EMPTY_STRING), false},
                {NdcConstants.FIELD_SEPARATOR + "00" + NdcConstants.FIELD_SEPARATOR, new DiagnosticStatus(0, EMPTY_STRING), false},
                {EMPTY_STRING, null, false},
                {NdcConstants.FIELD_SEPARATOR_STRING + NdcConstants.FIELD_SEPARATOR, null, false}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_result(String data, DiagnosticStatus expectedResult, boolean isMacEnabled) {
        when(deviceConfigurationMock.isMacEnabled())
                .thenReturn(isMacEnabled);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(data);
        appender.appendComponent(buffer, builder, deviceConfigurationMock);

        Assertions.assertThat(builder.getDiagnosticStatus())
                .isEqualTo(expectedResult);
    }


    @DataProvider
    public Object[][] remainingDataProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR + BmpStringGenerator.HEX.fixedLength(8), 9, true},
                {NdcConstants.FIELD_SEPARATOR + "99" + minData + NdcConstants.FIELD_SEPARATOR, 1, false},
                {NdcConstants.FIELD_SEPARATOR_STRING + NdcConstants.FIELD_SEPARATOR, 1, false}
        };
    }

    @Test(dataProvider = "remainingDataProvider")
    public void should_leave_remaining_data_untouched(String data, int expectedNumberOfCharactersRemaining, boolean isMacEnabled) {
        when(deviceConfigurationMock.isMacEnabled())
                .thenReturn(isMacEnabled);

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(data);
        appender.appendComponent(buffer, builder, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .isEqualTo(expectedNumberOfCharactersRemaining);
    }
}
