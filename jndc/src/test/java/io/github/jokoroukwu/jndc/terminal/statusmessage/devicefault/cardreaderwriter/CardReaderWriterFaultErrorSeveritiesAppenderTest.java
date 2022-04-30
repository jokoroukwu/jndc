package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreaderwriter;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.DeviceStatusInformationTest;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.ErrorSeverity;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader.CardReaderWriterErrorSeverityAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader.CardReaderWriterFaultBuilder;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CardReaderWriterFaultErrorSeveritiesAppenderTest extends DeviceStatusInformationTest {

    private CardReaderWriterErrorSeverityAppender appender;
    private CardReaderWriterFaultBuilder builder;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        final ConfigurableNdcComponentAppender<CardReaderWriterFaultBuilder> nextAppenderMock = mock(ConfigurableNdcComponentAppender.class);
        appender = new CardReaderWriterErrorSeverityAppender("commandName", nextAppenderMock);
        builder = new CardReaderWriterFaultBuilder();
    }

    @DataProvider
    public Object[][] validDataProvider() {
        final String minLength = "1";
        final String maxLength = "1".repeat(CardReaderWriterErrorSeverityAppender.MAX_FIELD_LENGTH);
        final String mac = NdcConstants.FIELD_SEPARATOR + BmpStringGenerator.HEX.fixedLength(8);
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR + minLength, List.of(ErrorSeverity.ROUTINE), false},
                {NdcConstants.FIELD_SEPARATOR + minLength + NdcConstants.FIELD_SEPARATOR, List.of(ErrorSeverity.ROUTINE), false},
                {NdcConstants.FIELD_SEPARATOR + minLength, List.of(ErrorSeverity.ROUTINE), false},
                {NdcConstants.FIELD_SEPARATOR + NdcConstants.FIELD_SEPARATOR_STRING, List.of(), false},
                {NdcConstants.FIELD_SEPARATOR + maxLength, List.of(), true},
                {NdcConstants.FIELD_SEPARATOR + maxLength + mac, Collections.nCopies(CardReaderWriterErrorSeverityAppender.MAX_FIELD_LENGTH, ErrorSeverity.ROUTINE), true},
                {NdcConstants.FIELD_SEPARATOR + maxLength, Collections.nCopies(CardReaderWriterErrorSeverityAppender.MAX_FIELD_LENGTH, ErrorSeverity.ROUTINE), false}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_result(String validData, List<ErrorSeverity> expectedResult, boolean isMacEnabled) {
        when(deviceConfigurationMock.isMacEnabled())
                .thenReturn(isMacEnabled);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);
        appender.appendComponent(buffer, builder, deviceConfigurationMock);

        Assertions.assertThat(builder.getErrorSeverities())
                .isEqualTo(expectedResult);
    }

    @DataProvider
    public Object[][] remainingDataProvider() {
        final String minLength = "1";
        final String maxLength = "1".repeat(CardReaderWriterErrorSeverityAppender.MAX_FIELD_LENGTH);
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR + minLength + NdcConstants.FIELD_SEPARATOR, 1, false},
                {NdcConstants.FIELD_SEPARATOR + NdcConstants.FIELD_SEPARATOR_STRING, 1, false},
                {NdcConstants.FIELD_SEPARATOR + NdcConstants.FIELD_SEPARATOR_STRING, 2, true},
                {NdcConstants.FIELD_SEPARATOR + maxLength + NdcConstants.FIELD_SEPARATOR, 4, true},
                {NdcConstants.FIELD_SEPARATOR + maxLength + NdcConstants.FIELD_SEPARATOR, 1, false},
                {NdcConstants.FIELD_SEPARATOR + maxLength + "AB", 2, false}
        };
    }

    @Test(dataProvider = "remainingDataProvider")
    public void should_leave_remaining_data_untouched(String validData, int expectedCharactersToRemain, boolean isMacEnabled) {
        when(deviceConfigurationMock.isMacEnabled())
                .thenReturn(isMacEnabled);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);

        appender.appendComponent(buffer, builder, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .isEqualTo(expectedCharactersToRemain);
    }
}
