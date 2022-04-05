package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.entry;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.PrimaryAidICCAppTypeAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PrimaryAidIccAppTypeAppenderTest extends AbstractFieldAppenderTest {
    private PrimaryAidICCAppTypeAppender appTypeAppender;


    @BeforeMethod
    public void setAppender() {
        appTypeAppender = new PrimaryAidICCAppTypeAppender(nextAppenderMock);
    }

    @Test
    public void should_call_expected_methods() {
        when(bufferMock.tryReadCharSequence(anyInt()))
                .thenReturn(DescriptiveOptional.of("CAM"));
        appTypeAppender.appendComponent(bufferMock, entryBuilderMock);

        verify(bufferMock, times(1))
                .tryReadCharSequence(3);
        verify(nextAppenderMock, times(1))
                .appendComponent(bufferMock, entryBuilderMock);
    }

    @Test
    public void should_throw_exception_on_value_mismatch() {
        final String errorMessage = "my Error Message";
        when(bufferMock.tryReadCharSequence(anyInt()))
                .thenReturn(DescriptiveOptional.empty(() -> errorMessage));

        Assertions.assertThatThrownBy(() -> appTypeAppender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(errorMessage);
        verify(nextAppenderMock, times(0))
                .appendComponent(any(), any());
        verifyNoInteractions(entryBuilderMock);
    }
}
