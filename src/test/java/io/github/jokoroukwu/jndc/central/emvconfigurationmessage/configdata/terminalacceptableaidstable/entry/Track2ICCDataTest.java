package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.entry;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.TerminalAcceptableAidsFieldAppender;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.TerminalApplicationIdEntryBuilder;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.Track2ICCData;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.Track2ICCDataAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class Track2ICCDataTest extends AbstractFieldAppenderTest {
    private Track2ICCDataAppender appender;

    @BeforeMethod
    @Override
    public void setMocks() {
        bufferMock = mock(NdcCharBuffer.class);
        entryBuilderMock = mock(TerminalApplicationIdEntryBuilder.class);
        nextAppenderMock = mock(TerminalAcceptableAidsFieldAppender.class);
        appender = new Track2ICCDataAppender(nextAppenderMock);
    }


    @Test
    public void should_call_expected_methods_when_icc_data_present() {
        when(bufferMock.tryReadInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(0));

        appender.appendComponent(bufferMock, entryBuilderMock);

        verify(bufferMock, times(1))
                .tryReadInt(2);

        verify(entryBuilderMock, times(1))
                .withTrack2IccData(Track2ICCData.DEFAULT);

        verify(nextAppenderMock, times(1))
                .appendComponent(bufferMock, entryBuilderMock);

        verifyNoMoreInteractions(bufferMock, entryBuilderMock, nextAppenderMock);
    }

    @Test
    public void should_call_expected_methods_when_icc_data_empty() {
        when(bufferMock.tryReadInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.empty());

        Assertions.assertThatThrownBy(() -> appender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(Track2ICCDataAppender.FIELD_NAME);

        verify(bufferMock, times(1))
                .tryReadInt(2);

        verifyNoMoreInteractions(bufferMock, entryBuilderMock, nextAppenderMock);
    }


    @Test
    public void should_call_expected_methods_on_invalid_icc_data() {
        final int invalidIccData = 5;
        when(bufferMock.tryReadInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(invalidIccData));

        Assertions.assertThatThrownBy(() -> appender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(Track2ICCDataAppender.FIELD_NAME);

        verify(bufferMock, times(1))
                .tryReadInt(2);

        verifyNoMoreInteractions(bufferMock, entryBuilderMock, nextAppenderMock);
    }
}
