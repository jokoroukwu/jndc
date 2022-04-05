package io.github.jokoroukwu.jndc.central.transactionreply.depositlimit;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

public class DepositLimitsBufferTest {
    private final int dummyNoteLimit = 10;
    private final AmountLimit dummyAmountLimit = new AmountLimit("RUR", 10);

    @DataProvider
    public Object[][] validDataProvider() {
        final String noteLimitString = ElementId.NOTE_LIMIT.toNdcString() + Integers.toZeroPaddedString(dummyNoteLimit, 3);
        final String amountLimitString = dummyAmountLimit.toNdcString();
        final String reservedBytes = "0".repeat(10);
        return new Object[][]{
                {new DepositLimitsBuffer(List.of(dummyAmountLimit), List.of()), DepositLimitsBuffer.ID + reservedBytes + amountLimitString},

                {new DepositLimitsBuffer(List.of(), List.of(dummyNoteLimit)), DepositLimitsBuffer.ID + reservedBytes + noteLimitString},

                {new DepositLimitsBuffer(List.of(dummyAmountLimit), List.of(dummyNoteLimit)),
                        DepositLimitsBuffer.ID + reservedBytes + amountLimitString + NdcConstants.GROUP_SEPARATOR + noteLimitString},

                {new DepositLimitsBuffer(List.of(dummyAmountLimit), List.of(dummyNoteLimit)),
                        DepositLimitsBuffer.ID + reservedBytes + amountLimitString + NdcConstants.GROUP_SEPARATOR + noteLimitString},

                {new DepositLimitsBuffer(List.of(dummyAmountLimit, dummyAmountLimit), List.of(dummyNoteLimit, dummyNoteLimit)),
                        DepositLimitsBuffer.ID + reservedBytes + amountLimitString + NdcConstants.GROUP_SEPARATOR
                                + amountLimitString + NdcConstants.GROUP_SEPARATOR + noteLimitString + NdcConstants.GROUP_SEPARATOR + noteLimitString},
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_ndc_string(DepositLimitsBuffer depositLimitsBuffer, String expectedString) {
        Assertions.assertThat(depositLimitsBuffer.toNdcString())
                .isEqualTo(expectedString);
    }

    @Test
    public void should_throw_expected_exception_on_empty_limits() {
        Assertions.assertThatThrownBy(() -> new DepositLimitsBuffer(Set.of(), Set.of()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("limit must be provided");
    }


    @DataProvider
    public Object[][] invalidNoteCountProvider() {
        return new Object[][]{
                {-1},
                {1000}
        };
    }

    @Test(dataProvider = "invalidNoteCountProvider")
    public void should_throw_expected_exception_on_invalid_note_count(int invalidNoteCount) {
        final SoftAssertions softly = new SoftAssertions();
        softly.assertThatThrownBy(() -> new DepositLimitsBuffer(invalidNoteCount))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Note Limit value");

        softly.assertThatThrownBy(() -> new DepositLimitsBuffer(List.of(), Set.of(invalidNoteCount)))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Note Limit value");

        softly.assertAll();
    }
}
