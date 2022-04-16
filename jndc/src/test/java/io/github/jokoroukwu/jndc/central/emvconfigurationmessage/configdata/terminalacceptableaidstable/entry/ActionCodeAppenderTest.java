package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.entry;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.PrimaryAidTerminalActionCodeDenialAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalLong;
import io.github.jokoroukwu.jndc.util.optional.EmptyDescriptionSupplier;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ActionCodeAppenderTest extends AbstractFieldAppenderTest {
    private PrimaryAidTerminalActionCodeDenialAppender actionCodeDenialAppender;


    @BeforeMethod
    public void setAppender() {
        actionCodeDenialAppender = new PrimaryAidTerminalActionCodeDenialAppender(nextAppenderMock);
    }

    @Test
    public void should_call_expected_methods_when_value_present() {
        final long actionCodeDenial = 0xFF;
        when(bufferMock.tryReadHexLong(anyInt()))
                .thenReturn(DescriptiveOptionalLong.of(actionCodeDenial));
        actionCodeDenialAppender.appendComponent(bufferMock, entryBuilderMock);

        verify(bufferMock, times(1))
                .tryReadHexLong(10);

        verify(entryBuilderMock, times(1))
                .withPrimaryAidActionCodeDenial(actionCodeDenial);

        verify(nextAppenderMock, times(1))
                .appendComponent(bufferMock, entryBuilderMock);

        verifyNoMoreInteractions(bufferMock, entryBuilderMock, nextAppenderMock);
    }

    @Test
    public void should_throw_exception_on_empty_value() {
        when(bufferMock.tryReadHexLong(anyInt()))
                .thenReturn(DescriptiveOptionalLong.empty(EmptyDescriptionSupplier.INSTANCE));

        Assertions.assertThatThrownBy(() -> actionCodeDenialAppender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(PrimaryAidTerminalActionCodeDenialAppender.FIELD_NAME);

        verifyNoInteractions(entryBuilderMock, nextAppenderMock);
    }
}
