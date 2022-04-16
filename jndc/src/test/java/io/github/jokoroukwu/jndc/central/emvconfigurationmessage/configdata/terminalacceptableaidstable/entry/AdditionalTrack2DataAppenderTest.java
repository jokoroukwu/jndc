package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.entry;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.AdditionalTrack2DataAppender;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.TerminalApplicationIdEntryBuilder;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.Track2ICCData;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.IStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class AdditionalTrack2DataAppenderTest extends AbstractFieldAppenderTest {
    private AdditionalTrack2DataAppender appender;

    @BeforeMethod
    @Override
    public void setMocks() {
        bufferMock = mock(NdcCharBuffer.class);
        entryBuilderMock = mock(TerminalApplicationIdEntryBuilder.class);
        appender = new AdditionalTrack2DataAppender();
    }

    @DataProvider
    public Object[][] ignoredIccDataProvider() {
        return new Object[][]{
                {Track2ICCData.DEFAULT},
                {Track2ICCData.USE_TAG_OR_CARD},
                {Track2ICCData.USE_CARD},
        };
    }


    @Test(dataProvider = "ignoredIccDataProvider")
    public void should_not_call_any_methods_when_icc_data_is_ignored(Track2ICCData ignoredIccData) {
        when(entryBuilderMock.getTrackTwoIccData())
                .thenReturn(ignoredIccData);

        appender.appendComponent(bufferMock, entryBuilderMock);

        verify(entryBuilderMock, times(1))
                .getTrackTwoIccData();

        verifyNoMoreInteractions(entryBuilderMock);
        verifyNoInteractions(bufferMock);
    }

    @DataProvider
    public Object[][] validIccDataParamsProvider() {
        final IStringGenerator iccData = BmpStringGenerator.ofCharacterRanges(0x30, 0x39);
        return new Object[][]{
                {Track2ICCData.USE_TAG_OR_SIMULATE, iccData.fixedLength(1)},
                {Track2ICCData.SIMULATE, iccData.fixedLength(1)},
                {Track2ICCData.USE_TAG_OR_SIMULATE, iccData.fixedLength(33)},
                {Track2ICCData.SIMULATE, iccData.fixedLength(33)}
        };
    }

    @Test(dataProvider = "validIccDataParamsProvider")
    public void should_call_expected_methods_on_valid_icc_data(Track2ICCData processedData, String validIccData) {
        when(entryBuilderMock.getTrackTwoIccData())
                .thenReturn(processedData);

        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(validIccData.length()));

        when(bufferMock.tryReadCharSequence(anyInt()))
                .thenReturn(DescriptiveOptional.of(validIccData));

        appender.appendComponent(bufferMock, entryBuilderMock);

        verify(bufferMock, times(1))
                .tryReadHexInt(2);
        verify(bufferMock, times(1))
                .tryReadCharSequence(validIccData.length());
        verifyNoMoreInteractions(bufferMock);

        verify(entryBuilderMock, times(1))
                .getTrackTwoIccData();
        verify(entryBuilderMock, times(1))
                .withAdditionalTrackTwoData(validIccData);
        verifyNoMoreInteractions(entryBuilderMock);


    }


    @DataProvider
    public Object[][] invalidIccDataLengthProvider() {
        return new Object[][]{
                {0},
                {34}
        };
    }

    @Test(dataProvider = "invalidIccDataLengthProvider")
    public void should_throw_expected_exception_on_invalid_icc_data_length(int invalidIccDataLength) {
        when(entryBuilderMock.getTrackTwoIccData())
                .thenReturn(Track2ICCData.SIMULATE);

        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(invalidIccDataLength));

        Assertions.assertThatThrownBy(() -> appender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(AdditionalTrack2DataAppender.FIELD_NAME);

        verify(bufferMock, times(1))
                .tryReadHexInt(2);
        verifyNoMoreInteractions(bufferMock);

        verify(entryBuilderMock, times(1))
                .getTrackTwoIccData();
        verifyNoMoreInteractions(entryBuilderMock);

    }

    @Test
    public void should_throw_expected_exception_on_invalid_icc_data_value() {
        final String inValidIccData = BmpStringGenerator.ofCharacterRanges(0, 0x2F, 0x3A, 0x7F).fixedLength(33);

        when(entryBuilderMock.getTrackTwoIccData())
                .thenReturn(Track2ICCData.SIMULATE);

        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(inValidIccData.length()));

        when(bufferMock.tryReadCharSequence(anyInt()))
                .thenReturn(DescriptiveOptional.of(inValidIccData));

        Assertions.assertThatThrownBy(() -> appender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(AdditionalTrack2DataAppender.FIELD_NAME);

        verify(bufferMock, times(1))
                .tryReadHexInt(2);
        verify(bufferMock, times(1))
                .tryReadCharSequence(inValidIccData.length());
        verifyNoMoreInteractions(bufferMock);

        verify(entryBuilderMock, times(1))
                .getTrackTwoIccData();
        verifyNoMoreInteractions(entryBuilderMock);

    }
}
