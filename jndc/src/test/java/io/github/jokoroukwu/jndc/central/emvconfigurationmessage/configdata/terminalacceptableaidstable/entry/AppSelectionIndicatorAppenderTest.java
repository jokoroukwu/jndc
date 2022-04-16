package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.entry;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.AppSelectionIndicator;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.AppSelectionIndicatorAppender;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.TerminalApplicationIdEntryBuilder;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class AppSelectionIndicatorAppenderTest extends AbstractFieldAppenderTest {
    private AppSelectionIndicatorAppender appender;

    @BeforeMethod
    public void setUp() {
        appender = new AppSelectionIndicatorAppender(nextAppenderMock);
    }

    @Test
    public void shouldAppendExpectedValue() {
        final TerminalApplicationIdEntryBuilder builder = new TerminalApplicationIdEntryBuilder();
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("00");
        appender.appendComponent(buffer, builder);
        Assertions.assertThat(builder.getAppSelectionIndicator())
                .isEqualTo(AppSelectionIndicator.PARTIAL_MATCH);
    }

    @Test
    public void should_throw_exception_when_app_selection_indicator_not_present() {
        when(bufferMock.tryReadInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.empty());

        Assertions.assertThatThrownBy(() -> appender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(AppSelectionIndicatorAppender.FIELD_NAME);

        verify(bufferMock, times(1))
                .tryReadInt(2);

        verifyNoMoreInteractions(bufferMock, entryBuilderMock, nextAppenderMock);
    }


    @Test
    public void should_throw_exception_when_app_selection_indicator_not_valid() {
        final int invalidIndicator = 3;
        when(bufferMock.tryReadInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(invalidIndicator));

        Assertions.assertThatThrownBy(() -> appender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(AppSelectionIndicatorAppender.FIELD_NAME);

        verify(bufferMock, times(1))
                .tryReadInt(2);

        verifyNoMoreInteractions(bufferMock, entryBuilderMock, nextAppenderMock);
    }
}
