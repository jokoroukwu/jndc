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

public class ScreenBaseFieldAppenderTest extends AbstractFieldAppenderTest {
    private ScreenBaseFieldAppender appender;

    @BeforeMethod
    public void setUp() {
        appender = new ScreenBaseFieldAppender(fieldAppenderMock);
    }

    @DataProvider
    public Object[][] validScreenBaseProvider() {
        return new Object[][]{
                {0},
                {1},
                {999}
        };
    }

    @Test(dataProvider = "validScreenBaseProvider")
    private void builder_should_have_expected_state_when_field_appended(int screenBase) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(Strings.leftPad(screenBase, "0", 3));
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();
        appender.appendComponent(buffer, builder);
        Assertions.assertThat(builder)
                .isEqualTo(new LanguageSupportTableEntryBuilder().withScreenBase(screenBase));
    }

    @Test
    private void should_call_next_appender_when_done() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("000");
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();
        appender.appendComponent(buffer, builder);

        verify(fieldAppenderMock, times(1))
                .appendComponent(buffer, builder);
    }


    @Test
    private void should_throw_expected_exception_when_buffer_returns_no_result() {
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();

        Assertions.assertThatThrownBy(() -> appender.appendComponent(NdcCharBuffer.EMPTY, builder))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(ScreenBaseFieldAppender.FIELD_NAME);

        verify(fieldAppenderMock, times(0))
                .appendComponent(any(), any());
    }

}
