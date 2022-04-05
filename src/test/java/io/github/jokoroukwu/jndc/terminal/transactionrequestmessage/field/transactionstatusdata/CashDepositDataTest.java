package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

public class CashDepositDataTest {
    private final CashDepositNotes dummyNotes = new CashDepositNotes(0, 0, 0, 0);
    private final RecycleCassette dummyCassette = new RecycleCassette(1, 1);
    private final DepositTransactionDirection validDirection = DepositTransactionDirection.REFUND_DIRECTION;


    @Test
    public void should_throw_expected_exception_on_invalid_deposit_direction() {
        Assertions.assertThatThrownBy(() -> new CashDepositData(null, null, null))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Last Cash Deposit Transaction Direction");
    }

    @DataProvider
    public Object[][] invalidCassettesProvider() {
        return new Object[][]{
                {Collections.nCopies(100, dummyCassette)},
                {Collections.nCopies(101, dummyCassette)}
        };
    }

    @Test(dataProvider = "invalidCassettesProvider")
    public void should_throw_expected_exception_on_invalid_cassette_count(List<RecycleCassette> invalidCassetteCount) {
        Assertions.assertThatThrownBy(() -> new CashDepositData(validDirection, null, invalidCassetteCount))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Number of recycle cassettes");
    }

    @DataProvider
    public Object[][] validCassettesProvider() {
        return new Object[][]{
                {Collections.nCopies(99, dummyCassette)},
                {Collections.emptyList()},
                {null}
        };
    }

    @Test(dataProvider = "validCassettesProvider")
    public void should_not_throw_exception_on_valid_cassettes(List<RecycleCassette> validCassettes) {
        Assertions.assertThatCode(() -> new CashDepositData(validDirection, null, validCassettes))
                .doesNotThrowAnyException();
    }

    @DataProvider
    public Object[][] argProvider() {
        final String directionString = Character.toString(validDirection.value());
        final String cassetteString = dummyCassette.toNdcString();
        return new Object[][]{
                {null, null, directionString},
                {null, List.of(dummyCassette), directionString + "01" + cassetteString},
                {null, List.of(dummyCassette, dummyCassette), directionString + "02" + cassetteString.repeat(2)},
                {null, List.of(), "200"},
                {dummyNotes, null, "2" + dummyNotes.toNdcString()},
                {dummyNotes, List.of(dummyCassette), "2" + dummyNotes.toNdcString() + "01" + cassetteString},
        };
    }

    @Test(dataProvider = "argProvider")
    public void should_return_expected_ndc_string(CashDepositNotes notes, List<RecycleCassette> cassettes, String expectedString) {
        Assertions.assertThat(new CashDepositData(validDirection, notes, cassettes).toNdcString())
                .isEqualTo(expectedString);
    }
}
