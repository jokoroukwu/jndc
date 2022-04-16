package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verifyNoInteractions;

public class OpcodeBufferValuesTest extends AbstractFieldAppenderTest {
    private OpcodeBufferValuesFieldAppender appender;

    @BeforeMethod
    public void setUp() {
        appender = OpcodeBufferValuesFieldAppender.INSTANCE;
    }


    @Test
    public void builder_should_have_expected_state_when_field_appended() {
        final String opcodeValues = BmpStringGenerator.ofCharacterRanges(0x40, 0x44, 0x46, 0x49).fixedLength(3);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(opcodeValues);
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();

        appender.appendComponent(buffer, builder);

        Assertions.assertThat(builder)
                .isEqualTo(new LanguageSupportTableEntryBuilder().withOpCodeBufferValues(opcodeValues));

    }

    @Test
    public void should_not_call_next_appender() {
        final String opcodeValues = BmpStringGenerator.ofCharacterRanges(0x40, 0x44, 0x46, 0x49).fixedLength(3);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(opcodeValues);
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();

        appender.appendComponent(buffer, builder);

        verifyNoInteractions(fieldAppenderMock);
    }

    @Test
    public void should_throw_expected_exception_on_invalid_opcode_values() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("EJK");
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, builder))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(OpcodeBufferValuesFieldAppender.FIELD_NAME);

        verifyNoInteractions(fieldAppenderMock);
    }

    @Test
    public void should_throw_expected_exception_when_buffer_returns_no_result() {
        final NdcCharBuffer buffer = NdcCharBuffer.EMPTY;
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, builder))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(OpcodeBufferValuesFieldAppender.FIELD_NAME);

        verifyNoInteractions(fieldAppenderMock);
    }
}
