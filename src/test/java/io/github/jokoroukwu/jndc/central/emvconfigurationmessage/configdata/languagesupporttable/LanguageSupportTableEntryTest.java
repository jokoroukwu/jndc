package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable;

import io.github.jokoroukwu.jndc.util.random.ThreadLocalSecureRandom;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Random;

import static io.github.jokoroukwu.jndc.util.text.Strings.EMPTY_STRING;

public class LanguageSupportTableEntryTest {


    @DataProvider
    public Object[][] invalidFieldProvider() {
        return new Object[][]{
                {"12", 999, 7, 123, "AB@", "ISO-639 language code"},
                {null, 999, 7, 123, "AB@", "Language Code"},
                {EMPTY_STRING, 999, 7, 123, "AB@", "language code"},


                {"ru", 1000, 7, 123, "AB@", "Screen Base"},
                {"ru", 999, 8, 123, "AB@", "Audio Base"},
                {"ru", 999, 7, 220, "AB@", "Opcode Buffer Positions"},

                {"ru", 999, 7, 123, "EB9", "Opcode Buffer Values"},
                {"ru", 999, 7, 123, null, "Opcode Buffer Values"},
                {"ru", 999, 7, 123, EMPTY_STRING, "Opcode Buffer Values"}
        };
    }

    @Test(dataProvider = "invalidFieldProvider")
    private void should_throw_expected_exception_on_invalid_field(String languageCode,
                                                                  int screenBase,
                                                                  int audioBase,
                                                                  int opcodeBufferPositions,
                                                                  String opCodeBufferValues,
                                                                  String expectedMessage) {
        Assertions.assertThatThrownBy(() -> new LanguageSupportTableEntry(languageCode,
                screenBase,
                audioBase,
                opcodeBufferPositions,
                opCodeBufferValues)
        ).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    private void should_return_expected_ndc_string() {
        final Random random = ThreadLocalSecureRandom.get();
        final int screenBase = random.nextInt(1000);
        final int audioBase = random.nextInt(8);
        final String bufferValues = BmpStringGenerator.ofCharacterRanges(0x40, 0x44, 0x46, 0x49).fixedLength(3);

        final LanguageSupportTableEntry entry = LanguageSupportTableEntry.builder()
                .withLanguageCode("ru")
                .withScreenBase(screenBase)
                .withAudioBase(audioBase)
                .withOpcodeBufferPositions(123)
                .withOpCodeBufferValues(bufferValues)
                .build();

        final String expectedString = String.format("ru%03d%03d123%s", screenBase, audioBase, bufferValues);
        Assertions.assertThat(entry.toNdcString())
                .isEqualTo(expectedString);
    }
}
