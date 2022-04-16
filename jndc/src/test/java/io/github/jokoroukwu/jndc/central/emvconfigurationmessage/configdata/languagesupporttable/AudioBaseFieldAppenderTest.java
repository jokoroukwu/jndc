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

public class AudioBaseFieldAppenderTest extends AbstractFieldAppenderTest {
    private AudioBaseFieldAppender appender;

    @BeforeMethod
    public void setUp() {
        appender = new AudioBaseFieldAppender(fieldAppenderMock);
    }

    @DataProvider
    public Object[][] validAudioBaseProvider() {
        return new Object[][]{
                {0},
                {1},
                {2},
                {7}
        };
    }


    @Test(dataProvider = "validAudioBaseProvider")
    public void builder_should_have_expected_state_when_field_appended(int validAudioBase) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(Strings.leftPad(validAudioBase, "0", 3));
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();
        appender.appendComponent(buffer, builder);

        Assertions.assertThat(builder)
                .isEqualTo(new LanguageSupportTableEntryBuilder().withAudioBase(validAudioBase));
    }

    @Test
    public void should_call_next_appender_when_field_appended() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("000");
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();
        appender.appendComponent(buffer, builder);

        verify(fieldAppenderMock, times(1))
                .appendComponent(buffer, builder);
    }

    @Test
    public void should_call_expected_exception_when_buffer_returns_no_result() {
        final NdcCharBuffer buffer = NdcCharBuffer.EMPTY;
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, builder))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(AudioBaseFieldAppender.FIELD_NAME);

        verify(fieldAppenderMock, times(0))
                .appendComponent(any(), any());
    }

    @DataProvider
    public Object[][] invalidAudioBaseProvider() {
        return new Object[][]{
                {"008"},
                {"009"},
                {"100"}
        };
    }

    @Test(dataProvider = "invalidAudioBaseProvider")
    public void should_call_expected_exception_on_invalid_audio_base(String invalidAudioBase) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(invalidAudioBase);
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, builder))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(AudioBaseFieldAppender.FIELD_NAME);

        verify(fieldAppenderMock, times(0))
                .appendComponent(any(), any());
    }
}
