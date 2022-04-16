package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.reserved;

import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ReservedCounterFieldsTest {
    private final ReservedCounterFields empty = ReservedCounterFields.builder().build();

    @DataProvider
    public Object[][] invalidDataProvider() {
        return new Object[][]{
                {empty.copy().withG12("12"), "g12"},
                {empty.copy().withG20(BmpStringGenerator.HEX.fixedLength(16)), "g20"},
                {empty.copy().withG30(BmpStringGenerator.HEX.fixedLength(16)), "g30"},
                {empty.copy().withG40(null), "g40"},
                {empty.copy().withG50(BmpStringGenerator.HEX.fixedLength(21)), "g50"},

                {empty.copy().withG80(BmpStringGenerator.HEX.fixedLength(6)), "g80"},
                {empty.copy().withG80(BmpStringGenerator.HEX.fixedLength(1)), "g80"},

                {empty.copy().withG90(BmpStringGenerator.HEX.fixedLength(6)), "g90"},
                {empty.copy().withG90(BmpStringGenerator.HEX.fixedLength(1)), "g90"},

                {empty.copy().withG100(BmpStringGenerator.HEX.fixedLength(6)), "g100"},
                {empty.copy().withG100(BmpStringGenerator.HEX.fixedLength(1)), "g100"},

                {empty.copy().withG110(BmpStringGenerator.HEX.fixedLength(6)), "g110"},
                {empty.copy().withG110(BmpStringGenerator.HEX.fixedLength(1)), "g110"},

                {empty.copy().withG130(BmpStringGenerator.HEX.fixedLength(6)), "g130"},
                {empty.copy().withG130(BmpStringGenerator.HEX.fixedLength(1)), "g130"},


                {empty.copy().withG140(BmpStringGenerator.HEX.fixedLength(6)), "g140"},
                {empty.copy().withG140(BmpStringGenerator.HEX.fixedLength(1)), "g140"},


                {empty.copy().withG150(BmpStringGenerator.HEX.fixedLength(6)), "g150"},
                {empty.copy().withG150(BmpStringGenerator.HEX.fixedLength(1)), "g150"},
        };
    }

    @Test(dataProvider = "invalidDataProvider")
    public void should_throw_expected_exception_on_invalid_constructor_arg(ReservedCounterFieldsBuilder builder,
                                                                           String expectedMessagePart) {
        Assertions.assertThatThrownBy(builder::build)
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessagePart);
    }

    @Test
    public void should_not_throw_exception_on_valid_data() {
        final ReservedCounterFieldsBuilder builder = ReservedCounterFields.builder()
                .withG12("1")
                .withG20(BmpStringGenerator.HEX.fixedLength(15))
                .withG30(BmpStringGenerator.HEX.fixedLength(15))
                .withG40(BmpStringGenerator.HEX.fixedLength(15))
                .withG50(BmpStringGenerator.HEX.fixedLength(20))
                .withG80(BmpStringGenerator.HEX.fixedLength(5))
                .withG90(BmpStringGenerator.HEX.fixedLength(5))
                .withG100(BmpStringGenerator.HEX.fixedLength(5))
                .withG110(BmpStringGenerator.HEX.fixedLength(5))
                .withG130(BmpStringGenerator.HEX.fixedLength(5))
                .withG140(BmpStringGenerator.HEX.fixedLength(5))
                .withG150(BmpStringGenerator.HEX.fixedLength(5));

        Assertions.assertThatCode(builder::build)
                .doesNotThrowAnyException();

    }
}
