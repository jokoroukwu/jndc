package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.entry;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.DefaultAppLabelAppender;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.TerminalAcceptableAidsFieldAppender;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.TerminalApplicationIdEntryBuilder;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DefaultApplicationLabelAppenderTest {
    private DefaultAppLabelAppender labelAppender;
    private TerminalAcceptableAidsFieldAppender nextAppenderMock;
    private NdcCharBuffer bufferMock;
    private TerminalApplicationIdEntryBuilder entryBuilderMock;

    @DataProvider
    public static Object[][] invalidLabelValueProvider() {
        var invalidLabel = BmpStringGenerator.ofCharacterRange(0x00, 0x1F);
        return new Object[][]{
                {invalidLabel.fixedLength(10)},
        };
    }

    @BeforeMethod
    public void setUp() {
        bufferMock = mock(NdcCharBuffer.class);

        entryBuilderMock = mock(TerminalApplicationIdEntryBuilder.class);
        when(entryBuilderMock.withPrimaryAid(any()))
                .thenReturn(entryBuilderMock);

        nextAppenderMock = mock(TerminalAcceptableAidsFieldAppender.class);
        doNothing().when(nextAppenderMock).appendComponent(any(), any());

        labelAppender = new DefaultAppLabelAppender(nextAppenderMock);
    }

    @DataProvider
    public Object[][] appendableLabelProvider() {
        var label = BmpStringGenerator.ofCharacterRange(0x20, 0x7E);
        return new Object[][]{
                {label.fixedLength(1)},
                {label.fixedLength(2)},
                {label.fixedLength(16)}
        };
    }

    @Test(dataProvider = "appendableLabelProvider")
    public void should_append_label(String label) {
        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(label.length()));
        when(bufferMock.tryReadCharSequence(anyInt()))
                .thenReturn(DescriptiveOptional.of(label));

        labelAppender.appendComponent(bufferMock, entryBuilderMock);

        verify(bufferMock, times(1))
                .tryReadHexInt(2);

        verify(bufferMock, times(1))
                .tryReadCharSequence(label.length());

        verify(entryBuilderMock, times(1))
                .withDefaultAppLabel(label);

        verify(nextAppenderMock, times(1))
                .appendComponent(bufferMock, entryBuilderMock);
    }

    @DataProvider
    public Object[][] invalidLabelLengthProvider() {
        return new Object[][]{
                {17},
                {18}
        };
    }

    @Test(dataProvider = "invalidLabelLengthProvider")
    public void should_throw_exception_on_invalid_label_length(int length) {
        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(length));
        when(bufferMock.tryReadCharSequence(anyInt()))
                .thenReturn(DescriptiveOptional.of(""));
        Assertions.assertThatThrownBy(() -> labelAppender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining("Default Application Label");

        verify(bufferMock, times(1))
                .tryReadHexInt(2);

        verify(bufferMock, times(0))
                .tryReadCharSequence(anyInt());

        verify(entryBuilderMock, times(0))
                .withDefaultAppLabel(any());

        verify(nextAppenderMock, times(0))
                .appendComponent(any(), any());
    }

    @Test(dataProvider = "invalidLabelValueProvider")
    public void should_throw_exception_on_invalid_label_value(String value) {
        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(value.length()));
        when(bufferMock.tryReadCharSequence(anyInt()))
                .thenReturn(DescriptiveOptional.of(value));
        Assertions.assertThatThrownBy(() -> labelAppender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining("Default Application Label");

        verify(bufferMock, times(1))
                .tryReadHexInt(2);

        verify(bufferMock, times(1))
                .tryReadCharSequence(anyInt());

        verify(entryBuilderMock, times(0))
                .withDefaultAppLabel(any());

        verify(nextAppenderMock, times(0))
                .appendComponent(any(), any());
    }

    @Test
    public void should_not_append_label_with_zero_length() {
        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(0));
        when(bufferMock.tryReadCharSequence(anyInt()))
                .thenReturn(DescriptiveOptional.of(""));

        labelAppender.appendComponent(bufferMock, entryBuilderMock);

        verify(bufferMock, times(1))
                .tryReadHexInt(2);

        verify(bufferMock, times(0))
                .tryReadCharSequence(anyInt());

        verify(entryBuilderMock, times(0))
                .withDefaultAppLabel(any());

        verify(nextAppenderMock, times(1))
                .appendComponent(bufferMock, entryBuilderMock);
    }

}
