package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.groupedcounters;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GroupedCounterValuesTest {

    @Test
    public void should_return_expected_ndc_string() {
        final GroupedCounterValues groupedCounterValues
                = new GroupedCounterValues(0, 1, 99999, 9999);

        Assertions.assertThat(groupedCounterValues.toNdcString())
                .isEqualTo("00000000019999909999");
    }

    @DataProvider
    public Object[][] invalidDataProvider() {
        return new Object[][]{
                {-1, 1, 1, 1, "'Group 1' value"},
                {100_000, 1, 1, 1, "'Group 1' value"},

                {1, -1, 1, 1, "'Group 2' value"},
                {1, 100_000, 1, 1, "'Group 2' value"},

                {1, 1, -1, 1, "'Group 3' value"},
                {1, 1, 100_000, 1, "'Group 3' value"},

                {1, 1, 1, -1, "'Group 4' value"},
                {1, 1, 1, 100_000, "'Group 4' value"}
        };
    }


    @Test(dataProvider = "invalidDataProvider")
    public void should_throw_exception_on_invalid_constructor_arg(int value1, int value2, int value3, int value4,
                                                                  String expectedMessagePart) {
        Assertions.assertThatThrownBy(() -> new GroupedCounterValues(value1, value2, value3, value4))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessagePart);
    }
}
