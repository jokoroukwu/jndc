package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicator;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategy;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMsgAppenderTest;
import io.github.jokoroukwu.jndc.trackdata.TrackDataAppender;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.IStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

import static org.mockito.Mockito.*;

public class TrackDataAppenderTest extends TransactionRequestMsgAppenderTest {
    private final IStringGenerator validDataGenerator = BmpStringGenerator.ofCharacterRange('0', '?');
    private final String fieldName = BmpStringGenerator.ALPHANUMERIC.fixedLength(10);
    private final int maxDataLength = ThreadLocalRandom.current().nextInt(2, 30);
    private FieldMetaSkipStrategy fieldMetaSkipStrategyMock;
    private FieldPresenceIndicator fieldPresenceIndicatorMock;
    private NdcComponentReader<DescriptiveOptional<String>> trackDataReaderMock;
    private BiConsumer<TransactionRequestMessageBuilder, String> dataConsumerMock;
    private TrackDataAppender<TransactionRequestMessageBuilder> appender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        fieldMetaSkipStrategyMock = mock(FieldMetaSkipStrategy.class);
        fieldPresenceIndicatorMock = mock(FieldPresenceIndicator.class);
        dataConsumerMock = mock(BiConsumer.class);
        trackDataReaderMock = mock(NdcComponentReader.class);
        appender = TrackDataAppender.<TransactionRequestMessageBuilder>builder()
                .withCommandName("command name")
                .withFieldMetaSkipStrategy(fieldMetaSkipStrategyMock)
                .withFieldPresenceIndicator(fieldPresenceIndicatorMock)
                .withTrackDataReader(trackDataReaderMock)
                .withFieldName(fieldName)
                .withTrackDataConsumer(dataConsumerMock)
                .withNextAppender(nextAppenderMock)
                .build();
        when(fieldMetaSkipStrategyMock.skipFieldMeta(any()))
                .thenReturn(Optional.empty());
        when(fieldPresenceIndicatorMock.isFieldPresent(any(), any()))
                .thenReturn(Boolean.TRUE);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        final IStringGenerator dataGenerator = BmpStringGenerator.ofCharacterRange('0', '?');
        return new Object[][]{
                {dataGenerator.fixedLength(1)},
                {dataGenerator.fixedLength(maxDataLength)}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_data(String validData) {
        when(trackDataReaderMock.readComponent(any()))
                .thenReturn(DescriptiveOptional.of(validData));

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verify(dataConsumerMock, times(1))
                .accept(messageBuilder, validData);
        verifyNoMoreInteractions(dataConsumerMock);
    }


    @Test
    public void should_call_next_appender() {
        final String trackData = "A";
        when(trackDataReaderMock.readComponent(any()))
                .thenReturn(DescriptiveOptional.of(trackData));

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(trackData);
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verify(nextAppenderMock, times(1))
                .appendComponent(buffer, messageBuilder, deviceConfigurationMock);
    }


    @DataProvider
    public Object[][] remainingDataProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR + validDataGenerator.fixedLength(1), 2},
                {NdcConstants.FIELD_SEPARATOR_STRING, 1}
        };
    }

    @Test(dataProvider = "remainingDataProvider")
    public void should_leave_remaining_data_untouched(String trackData, int expectedRemainingDataLength) {
        when(trackDataReaderMock.readComponent(any()))
                .thenReturn(DescriptiveOptional.of(trackData));

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(trackData);
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .as("should have expected number of characters left")
                .isEqualTo(expectedRemainingDataLength);
    }
}
