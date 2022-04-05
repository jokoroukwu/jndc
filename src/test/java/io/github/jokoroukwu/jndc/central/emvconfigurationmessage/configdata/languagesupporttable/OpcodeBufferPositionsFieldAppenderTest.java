package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.text.Strings;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OpcodeBufferPositionsFieldAppenderTest extends AbstractFieldAppenderTest {
    private OpcodeBufferPositionsFieldAppender appender;

    @BeforeMethod
    public void setUp() {
        appender = new OpcodeBufferPositionsFieldAppender(fieldAppenderMock);
    }

    @DataProvider
    public Object[][] validOpcodeBufferPositionsProvider() {
        return new Object[][]{
                {123},
                {67},
                {567}
        };
    }


    @Test(dataProvider = "validOpcodeBufferPositionsProvider")
    public void builder_should_have_expected_state_when_field_appended(int validOpcodeBufferPositions) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(Strings.leftPad(validOpcodeBufferPositions, "0", 3));
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();
        appender.appendComponent(buffer, builder);

        Assertions.assertThat(builder)
                .isEqualTo(new LanguageSupportTableEntryBuilder().withOpcodeBufferPositions(validOpcodeBufferPositions));
    }

    @Test
    public void should_call_next_appender() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("123");
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();
        appender.appendComponent(buffer, builder);

        verify(fieldAppenderMock, times(1))
                .appendComponent(buffer, builder);
    }

    @Test
    public void should_throw_expected_exception_when_buffer_returns_no_result() {
        final NdcCharBuffer buffer = NdcCharBuffer.EMPTY;
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, builder))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(OpcodeBufferPositionsFieldAppender.FIELD_NAME);

        verify(fieldAppenderMock, times(0))
                .appendComponent(any(), any());
    }

    @DataProvider
    public Object[][] invalidOpcodeBufferPositionsProvider() {
        return new Object[][]{
                {0},
                {1},
                {128},
                {77},
                {812},
                {287},
                {100}
        };
    }

    @Test(dataProvider = "invalidOpcodeBufferPositionsProvider", enabled = false)
    public void should_throw_expected_exception_on_invalid_opcode_buffer_positions(int invalidOpcodeBufferPositions) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(Strings.leftPad(invalidOpcodeBufferPositions, "0", 3));
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, builder))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(OpcodeBufferPositionsFieldAppender.FIELD_NAME);

        verify(fieldAppenderMock, times(0))
                .appendComponent(any(), any());
    }

}
