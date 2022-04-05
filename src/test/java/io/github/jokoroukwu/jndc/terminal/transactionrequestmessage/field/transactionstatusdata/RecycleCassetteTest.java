package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RecycleCassetteTest {

    @DataProvider
    public Object[][] invalidTypeProvider() {
        return new Object[][]{
                {-1},
                {0},
                {8}
        };
    }

    @Test(dataProvider = "invalidTypeProvider")
    public void should_throw_expected_exception_on_invalid_type(int invalidType) {
        Assertions.assertThatThrownBy(() -> new RecycleCassette(invalidType, 1))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cassette Type");
    }

    @DataProvider
    public Object[][] validTypeProvider() {
        return new Object[][]{
                {1},
                {2},
                {6},
                {7}
        };
    }

    @Test(dataProvider = "validTypeProvider")
    public void should_not_throw_exception_on_valid_type(int validType) {
        Assertions.assertThatCode(() -> new RecycleCassette(validType, 1))
                .doesNotThrowAnyException();
    }

    @DataProvider
    public Object[][] invalidNoteNumberProvider() {
        return new Object[][]{
                {0},
                {-1},
                {1000}
        };
    }

    @Test(dataProvider = "invalidNoteNumberProvider")
    public void should_throw_expected_exception_on_invalid_note_number(int invalidNoteNumber) {
        Assertions.assertThatThrownBy(() -> new RecycleCassette(1, invalidNoteNumber))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Number of Notes");
    }

    @DataProvider
    public Object[][] validNoteNumberProvider() {
        return new Object[][]{
                {999},
                {998}
        };
    }

    @Test(dataProvider = "validNoteNumberProvider")
    public void should_not_throw_exception_on_valid_note_number(int validNoteNumber) {
        Assertions.assertThatCode(() -> new RecycleCassette(1, validNoteNumber))
                .doesNotThrowAnyException();
    }

    @Test
    public void should_return_expected_ndc_string() {
        final String actualString = new RecycleCassette(7, 1).toNdcString();
        Assertions.assertThat(actualString)
                .isEqualTo("007001");
    }
}
