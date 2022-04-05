package io.github.jokoroukwu.jndc.central.transactionreply.notestodispense;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class NotesToDispenseTest {


    @DataProvider
    public static Object[][] validDataProvider() {
        return new Object[][]{
                {NotesToDispense.fourDigit(0), "0000"},
                {NotesToDispense.fourDigit(1), "0001"},
                {NotesToDispense.fourDigit(9999), "9999"},
                {NotesToDispense.fourDigit(9999, 99), "99990099"},

                {NotesToDispense.twoDigit(0), "00"},
                {NotesToDispense.twoDigit(1), "01"},
                {NotesToDispense.twoDigit(99), "99"},
                {NotesToDispense.twoDigit(99, 9), "9909"},
        };
    }

    @DataProvider
    public static Object[][] invalid2DigitElementProvider() {
        return new Object[][]{
                {-1},
                {100}
        };
    }

    @DataProvider
    public static Object[][] invalid4DigitElementProvider() {
        return new Object[][]{
                {-1},
                {10_000}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_ndc_string(NotesToDispense notesToDispense, String expectedString) {
        Assertions.assertThat(notesToDispense.toNdcString())
                .isEqualTo(expectedString);
    }

    @Test(dataProvider = "invalid2DigitElementProvider")
    public void should_throw_expected_exception_on_invalid_2_digit_note_element(int invalidValue) {
        Assertions.assertThatThrownBy(() -> NotesToDispense.twoDigit(invalidValue))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Number of notes");
    }

    @Test(dataProvider = "invalid4DigitElementProvider")
    public void should_throw_expected_exception_on_invalid_4_digit_note_element(int invalidValue) {
        Assertions.assertThatThrownBy(() -> NotesToDispense.fourDigit(invalidValue))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Number of notes");
    }
}
