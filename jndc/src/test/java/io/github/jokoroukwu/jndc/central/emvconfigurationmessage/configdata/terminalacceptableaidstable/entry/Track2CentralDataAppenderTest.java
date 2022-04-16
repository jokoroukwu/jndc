package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.entry;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.Track2CentralDataAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class Track2CentralDataAppenderTest extends AbstractFieldAppenderTest {
    private Track2CentralDataAppender appender;

    @BeforeMethod
    public void setUp() {
        appender = new Track2CentralDataAppender(nextAppenderMock);
    }

    @Test
    public void should_call_expected_methods_when_central_data_not_present() {
        when(bufferMock.tryReadInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.empty());

        Assertions.assertThatThrownBy(() -> appender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(Track2CentralDataAppender.FIELD_NAME);

        verify(bufferMock, times(1))
                .tryReadInt(2);

        verifyNoMoreInteractions(bufferMock, entryBuilderMock, nextAppenderMock);
    }


    @Test
    public void should_call_expected_methods_on_invalid_central_data() {
        final int invalidIndicator = 3;
        when(bufferMock.tryReadInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(invalidIndicator));

        Assertions.assertThatThrownBy(() -> appender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(Track2CentralDataAppender.FIELD_NAME);

        verify(bufferMock, times(1))
                .tryReadInt(2);

        verifyNoMoreInteractions(bufferMock, entryBuilderMock, nextAppenderMock);
    }
}
