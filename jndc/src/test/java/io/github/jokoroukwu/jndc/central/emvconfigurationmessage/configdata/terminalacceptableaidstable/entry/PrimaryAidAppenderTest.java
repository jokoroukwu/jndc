package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.entry;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.Aid;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.BaseAidReader;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.PrimaryAIDAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.Result;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator.HEX;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PrimaryAidAppenderTest extends AbstractFieldAppenderTest {

    @DataProvider
    public static Object[][] invalidAidLengthProvider() {
        return new Object[][]{
                {0},
                {17},
                {18}
        };
    }


    @Test(dataProvider = "invalidAidLengthProvider")
    public void should_throw_exception_on_invalid_aid_length(int length) {
        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(length));
        when(bufferMock.tryReadCharSequence(anyInt()))
                .thenReturn(DescriptiveOptional.of(HEX.fixedLength(4)));

        final PrimaryAIDAppender aidAppender = new PrimaryAIDAppender(nextAppenderMock, BaseAidReader.INSTANCE);
        Assertions.assertThatThrownBy(() -> aidAppender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining("should be in range 0x01-0x10");

        verify(bufferMock, times(0))
                .tryReadCharSequence(anyInt());
        verify(nextAppenderMock, times(0))
                .appendComponent(any(), any());
    }


    @Test
    public void should_throw_exception_on_invalid_aid_value() {
        final String value = BmpStringGenerator.ofCharacterRange('а', 'я').fixedLength(10);
        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(value.length() / 2));
        when(bufferMock.tryReadCharSequence(anyInt()))
                .thenReturn(DescriptiveOptional.of(value));

        final PrimaryAIDAppender aidAppender = new PrimaryAIDAppender(nextAppenderMock, BaseAidReader.INSTANCE);
        Assertions.assertThatThrownBy(() -> aidAppender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class);
    }

    @DataProvider
    public Object[][] aidValueProvider() {
        return new Object[][]{
                {HEX.fixedLength(2)},
                {HEX.fixedLength(4)},
                {HEX.fixedLength(32)},
        };
    }

    @Test(dataProvider = "aidValueProvider")
    public void should_append_expected_aid(String value) {
        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(value.length() / 2));
        when(bufferMock.tryReadCharSequence(anyInt()))
                .thenReturn(DescriptiveOptional.of(value));

        new PrimaryAIDAppender(nextAppenderMock, BaseAidReader.INSTANCE).appendComponent(bufferMock, entryBuilderMock);

        verify(entryBuilderMock, times(1))
                .withPrimaryAid(new Aid(value));
        verify(nextAppenderMock, times(1))
                .appendComponent(bufferMock, entryBuilderMock);
    }

    @Test
    public void should_make_expected_calls_to_buffer() {
        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(1));
        when(bufferMock.tryReadCharSequence(anyInt()))
                .thenReturn(DescriptiveOptional.of("02"));
        Result.ofVoid(() -> new PrimaryAIDAppender(null, BaseAidReader.INSTANCE).appendComponent(bufferMock, entryBuilderMock));

        verify(bufferMock, times(1))
                .tryReadHexInt(2);
        verify(bufferMock, times(1))
                .tryReadCharSequence(2);
    }
}
