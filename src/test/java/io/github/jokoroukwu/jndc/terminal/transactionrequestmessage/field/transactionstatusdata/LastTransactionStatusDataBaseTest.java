package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LastTransactionStatusDataBaseTest {
    private final int validSerialNumber = 9999;
    private final LastStatusIssued validStatus = LastStatusIssued.NONE_SENT;
    private final List<Integer> integers = List.of(99999, 99999, 99999, 99999);

    @DataProvider
    public Object[][] invalidArgProvider() {
        return new Object[][]{
                {-1},
                {10_000}
        };
    }

    @Test(dataProvider = "invalidArgProvider")
    public void should_throw_expected_exception_on_invalid_serial_number(int invalidSerialNumber) {
        Assertions.assertThatThrownBy(()
                -> new LastTransactionStatusDataBase(invalidSerialNumber, validStatus, integers, integers))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Last Transaction Serial Number");
    }

    @DataProvider
    public Object[][] validSerialNumberProvider() {
        return new Object[][]{
                {1},
                {2},
                {9998}
        };
    }

    @Test(dataProvider = "validSerialNumberProvider")
    public void should_not_throw_exception_on_valid_serial_number(int validSerialNumber) {
        Assertions.assertThatCode(()
                -> new LastTransactionStatusDataBase(validSerialNumber, validStatus, integers, integers))
                .doesNotThrowAnyException();
    }

    @DataProvider
    public Object[][] invalidNotesDispensedProvider() {
        return new Object[][]{
                {List.of(0)},
                {List.of(0, 0, 0)},
                {List.of(0, 0, 0, -1)},
                {List.of(0, 0, 0, 0, 0)},
                {List.of(0, 0, 0, 0, 0, 0)},
                {List.of(0, 0, 0, 0, 0, 0, 0, 0)},
                {Arrays.asList(0, 0, null, 0)},
                {null},
                {Collections.<Integer>emptyList()}
        };
    }

    @Test(dataProvider = "invalidNotesDispensedProvider")
    public void should_throw_expected_exception_on_invalid_notes_dispensed(List<Integer> invalidNotesDispensed) {
        Assertions.assertThatThrownBy(()
                -> new LastTransactionStatusDataBase(validSerialNumber, validStatus, invalidNotesDispensed, integers))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Notes Dispensed");
    }

    @DataProvider
    public Object[][] validNotesDispensedProvider() {
        return new Object[][]{
                {List.of(0, 0, 0, 0)},
                {List.of(0, 0, 0, 0, 0, 0, 0)}
        };
    }

    @Test(dataProvider = "validNotesDispensedProvider")
    public void should_not_throw_exception_on_valid_notes_dispensed(List<Integer> validNotesDispensed) {
        Assertions.assertThatCode(()
                -> new LastTransactionStatusDataBase(validSerialNumber, validStatus, validNotesDispensed, integers))
                .doesNotThrowAnyException();
    }

    @DataProvider
    public Object[][] invalidCoinsDispensedProvider() {
        return new Object[][]{
                {List.of(0)},
                {List.of(0, 0, 0)},
                {List.of(0, 0, 0, -1)},
                {List.of(0, 0, 0, 0, 0)},
                {Arrays.asList(0, 0, null, 0)},
                {null},
                {Collections.<Integer>emptyList()}
        };
    }

    @Test(dataProvider = "invalidCoinsDispensedProvider")
    public void should_throw_expected_exception_on_invalid_coins_dispensed(List<Integer> invalidCoinsDispensed) {
        Assertions.assertThatThrownBy(()
                -> new LastTransactionStatusDataBase(validSerialNumber, validStatus, integers, invalidCoinsDispensed))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Coins Dispensed");
    }

    @Test
    public void should_throw_expected_exception_on_invalid_last_status_issued() {
        Assertions.assertThatThrownBy(()
                -> new LastTransactionStatusDataBase(validSerialNumber, null, integers, integers))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Last Status Issued");
    }
}
