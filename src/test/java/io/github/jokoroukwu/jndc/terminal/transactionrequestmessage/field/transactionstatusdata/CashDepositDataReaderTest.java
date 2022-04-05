package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.Strings;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

public class CashDepositDataReaderTest {
    private final CashDepositNotes dummyNotes = new CashDepositNotes(0, 0, 0, 0);
    private final RecycleCassette dummyCassette = new RecycleCassette(1, 1);

    private FakeNotesReader fakeNotesReader;
    private FakeRecycleCassetteReader fakeRecycleCassetteReader;
    private CashDepositDataReader reader;

    @BeforeMethod
    public void setUp() {
        fakeNotesReader = new FakeNotesReader(dummyNotes);
        fakeRecycleCassetteReader = new FakeRecycleCassetteReader(dummyCassette);
        reader = new CashDepositDataReader(fakeRecycleCassetteReader, fakeNotesReader);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {Strings.EMPTY_STRING, null},
                {"0", new CashDepositData(DepositTransactionDirection.NOT_CASH_DEPOSIT)},
                {"0A", new CashDepositData(DepositTransactionDirection.NOT_CASH_DEPOSIT, dummyNotes, null)},
                {"0AA", new CashDepositData(DepositTransactionDirection.NOT_CASH_DEPOSIT, dummyNotes, List.of(dummyCassette))}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_cash_deposit_data(String validData, CashDepositData expectedResult) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);
        final Optional<CashDepositData> actualResult = reader.readComponent(buffer);

        Assertions.assertThat(actualResult)
                .isEqualTo(Optional.ofNullable(expectedResult));

    }

    @Test(dataProvider = "validDataProvider")
    public void should_leave_remaining_data_untouched(String validData, CashDepositData expectedResult) {
        final String remainingData = BmpStringGenerator.HEX.randomLength(20);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData + NdcConstants.FIELD_SEPARATOR + remainingData);

        reader.readComponent(buffer);

        Assertions.assertThat(buffer.remaining())
                .as("expected number of characters should remain")
                .isEqualTo(remainingData.length() + 1);
    }

    @Test
    public void should_throw_expected_exception_on_invalid_deposit_transaction_direction() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("A");

        final SoftAssertions softly = new SoftAssertions();
        softly.assertThatThrownBy(() -> reader.readComponent(buffer))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContainingAll("Last Cash Deposit Transaction Direction",
                        TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString());

        softly.assertThat(fakeNotesReader.interactions)
                .as("should not call note reader")
                .isZero();

        softly.assertThat(fakeRecycleCassetteReader.interactions)
                .as("should not call recycle cassette reader")
                .isZero();
        softly.assertAll();
    }


    private static final class FakeNotesReader implements NdcComponentReader<CashDepositNotes> {
        private final CashDepositNotes dummyNotes;
        private int interactions;


        private FakeNotesReader(CashDepositNotes dummyNotes) {
            this.dummyNotes = dummyNotes;
        }

        @Override
        public CashDepositNotes readComponent(NdcCharBuffer ndcCharBuffer) {
            ndcCharBuffer.skip(1);
            ++interactions;
            return dummyNotes;
        }
    }

    private static final class FakeRecycleCassetteReader implements NdcComponentReader<List<RecycleCassette>> {
        private final RecycleCassette dummyCassette;
        private int interactions;

        private FakeRecycleCassetteReader(RecycleCassette dummyCassette) {
            this.dummyCassette = dummyCassette;
        }

        @Override
        public List<RecycleCassette> readComponent(NdcCharBuffer ndcCharBuffer) {
            ndcCharBuffer.skip(1);
            ++interactions;
            return List.of(dummyCassette);
        }
    }
}
