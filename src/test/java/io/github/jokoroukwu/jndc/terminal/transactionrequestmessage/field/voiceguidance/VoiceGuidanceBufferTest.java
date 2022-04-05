package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.voiceguidance;

import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class VoiceGuidanceBufferTest {

    @DataProvider
    public Object[][] languageIdProvider() {
        final String languageId = "ru";
        return new Object[][]{
                {VoiceGuidanceBuffer.EMPTY, Character.toString(VoiceGuidanceBuffer.ID)},
                {new VoiceGuidanceBuffer(languageId), VoiceGuidanceBuffer.ID + languageId},
        };
    }

    @Test(dataProvider = "languageIdProvider")
    public void should_return_expected_ndc_string(VoiceGuidanceBuffer buffer, String expectedString) {
        Assertions.assertThat(buffer.toNdcString())
                .isEqualTo(expectedString);
    }


    @DataProvider
    public Object[][] invalidLanguageIdProvider() {
        return new Object[][]{
                {BmpStringGenerator.HEX.fixedLength(1)},
                {BmpStringGenerator.HEX.fixedLength(3)}
        };
    }

    @Test(dataProvider = "invalidLanguageIdProvider")
    public void should_throw_expected_exception_on_invalid_language_id(String invalidLanguageId) {
        Assertions.assertThatThrownBy(() -> new VoiceGuidanceBuffer(invalidLanguageId))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Voice guidance language identifier");
    }
}
