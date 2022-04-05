package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.random.ThreadLocalSecureRandom;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Locale;

import static org.mockito.Mockito.*;

public class LanguageCodeFieldAppenderTest {
    private final String[] isoLanguageCodes = Locale.getISOLanguages();
    private NdcComponentAppender<LanguageSupportTableEntryBuilder> nextAppenderMock;
    private LanguageCodeFieldAppender appender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        nextAppenderMock = mock(NdcComponentAppender.class);
        appender = new LanguageCodeFieldAppender(nextAppenderMock);
    }

    @DataProvider
    private Object[][] validBufferProvider() {
        return new Object[][]{
                {NdcCharBuffer.wrap(isoLanguageCodes[ThreadLocalSecureRandom.get().nextInt(isoLanguageCodes.length)])}
        };
    }

    @Test(dataProvider = "validBufferProvider")
    public void builder_should_have_expected_state_when_language_code_appended(NdcCharBuffer validBuffer) {
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();
        appender.appendComponent(validBuffer, builder);

        Assertions.assertThat(builder)
                .isEqualTo(new LanguageSupportTableEntryBuilder().withLanguageCode(validBuffer.toString()));

    }

    @Test(dataProvider = "validBufferProvider")
    public void should_call_next_appender(NdcCharBuffer validBuffer) {
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();
        appender.appendComponent(validBuffer, builder);
        verify(nextAppenderMock, times(1))
                .appendComponent(validBuffer, builder);
    }

    @DataProvider
    private Object[][] invalidBufferDataProvider() {
        return new Object[][]{
                {NdcCharBuffer.wrap("12")},
                {NdcCharBuffer.EMPTY}
        };
    }

    @Test(dataProvider = "invalidBufferDataProvider")
    public void should_throw_expected_exception_on_invalid_buffer_data(NdcCharBuffer invalidDataBuffer) {
        final LanguageSupportTableEntryBuilder builder = new LanguageSupportTableEntryBuilder();

        Assertions.assertThatThrownBy(() -> appender.appendComponent(invalidDataBuffer, builder))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining("Language Code");

    }
}
