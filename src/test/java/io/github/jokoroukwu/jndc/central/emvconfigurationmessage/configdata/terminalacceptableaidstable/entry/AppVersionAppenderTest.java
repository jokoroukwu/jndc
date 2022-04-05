package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.entry;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.PrimaryAIDAppVersionNumberAppender;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.TerminalApplicationIdEntryBuilder;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;
import io.github.jokoroukwu.jndc.util.optional.EmptyDescriptionSupplier;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.function.ObjIntConsumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AppVersionAppenderTest extends AbstractFieldAppenderTest {
    private String fieldName;
    private ObjIntConsumer<TerminalApplicationIdEntryBuilder> consumerMock;
    private PrimaryAIDAppVersionNumberAppender versionNumberAppender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void set() {
        consumerMock = mock(ObjIntConsumer.class);

        fieldName = BmpStringGenerator.ALPHANUMERIC.fixedLength(10);
        versionNumberAppender = new PrimaryAIDAppVersionNumberAppender(fieldName, consumerMock, nextAppenderMock);
    }

    @Test
    public void should_call_expected_methods_when_value_received() {
        final int version = 0xFF_FF;
        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(version));

        versionNumberAppender.appendComponent(bufferMock, entryBuilderMock);

        verify(bufferMock, times(1))
                .tryReadHexInt(4);

        verify(consumerMock, times(1))
                .accept(entryBuilderMock, version);

        verify(nextAppenderMock, times(1))
                .appendComponent(bufferMock, entryBuilderMock);

        verifyNoInteractions(entryBuilderMock);
    }

    @Test
    public void should_throw_exception_on_no_version_value() {
        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.empty(EmptyDescriptionSupplier.INSTANCE));

        Assertions.assertThatThrownBy(()
                -> versionNumberAppender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(fieldName);

        verify(bufferMock, times(1))
                .tryReadHexInt(anyInt());

        verify(consumerMock, times(0))
                .accept(any(), anyInt());

        verify(nextAppenderMock, times(0))
                .appendComponent(any(), any());

        verifyNoInteractions(entryBuilderMock);
    }

}
