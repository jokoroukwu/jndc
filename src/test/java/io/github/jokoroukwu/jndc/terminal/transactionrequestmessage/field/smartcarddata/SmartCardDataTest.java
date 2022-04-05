package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.smartcarddata;

import io.github.jokoroukwu.jndc.tlv.BerTlv;
import io.github.jokoroukwu.jndc.tlv.HexStringBerTlv;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.random.ThreadLocalSecureRandom;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

public class SmartCardDataTest {

    @DataProvider
    public Object[][] camProcessingFlagProvider() {
        return new Object[][]{
                {0b00000000_00100000, false},
                {0b11111111_11111111, false},
                {0b00000000_00000000, true},
                {0b11111111_11011111, true}
        };
    }

    @Test(dataProvider = "camProcessingFlagProvider")
    public void should_return_expected_boolean_for_full_cam_processing_flag(int camFlags, boolean expectedBoolean) {
        Assertions.assertThat(new SmartCardData(camFlags, Collections.emptyList()).hasFullCamProcessing())
                .isEqualTo(expectedBoolean);
    }

    @DataProvider
    public Object[][] transactionDeclinedFlagProvider() {
        return new Object[][]{
                {0b00000000_00000000, false},
                {0b11111111_11101111, false},
                {0b00000000_00010000, true},
                {0b11111111_11111111, true}
        };
    }

    @Test(dataProvider = "transactionDeclinedFlagProvider")
    public void should_return_expected_boolean_for_transaction_declined_flag(int camFlags, boolean expectedBoolean) {
        Assertions.assertThat(new SmartCardData(camFlags, Collections.emptyList()).isTransactionDeclinedOffline())
                .isEqualTo(expectedBoolean);
    }

    @DataProvider
    public Object[][] appDataRetrievalFlagProvider() {
        return new Object[][]{
                {0b00000000_00001000, false},
                {0b11111111_11111111, false},
                {0b00000000_00000000, true},
                {0b11111111_11110111, true}
        };
    }

    @Test(dataProvider = "appDataRetrievalFlagProvider")
    public void should_return_expected_boolean_for_app_data_retrieval_flag(int camFlags, boolean expectedBoolean) {
        Assertions.assertThat(new SmartCardData(camFlags, Collections.emptyList()).isAppDataRetrievalSuccessful())
                .isEqualTo(expectedBoolean);
    }

    @DataProvider
    public Object[][] processingOptionsFlagProvider() {
        return new Object[][]{
                {0b00000000_00000100, false},
                {0b11111111_11111111, false},
                {0b00000000_00000000, true},
                {0b11111111_11111011, true}
        };
    }

    @Test(dataProvider = "processingOptionsFlagProvider")
    public void should_return_expected_boolean_for_processing_options_flag(int camFlags, boolean expectedBoolean) {
        Assertions.assertThat(new SmartCardData(camFlags, Collections.emptyList()).isGetProcessingOptionsSuccessful())
                .isEqualTo(expectedBoolean);
    }

    @DataProvider
    public Object[][] appSelectionFlagProvider() {
        return new Object[][]{
                {0b00000000_00000010, false},
                {0b11111111_11111111, false},
                {0b00000000_00000000, true},
                {0b11111111_11111101, true}
        };
    }

    @Test(dataProvider = "appSelectionFlagProvider")
    public void should_return_expected_boolean_for_app_selection_flag(int camFlags, boolean expectedBoolean) {
        Assertions.assertThat(new SmartCardData(camFlags, Collections.emptyList()).isAppSelectionSuccessful())
                .isEqualTo(expectedBoolean);
    }

    @DataProvider
    public Object[][] pdolValidityFlagProvider() {
        return new Object[][]{
                {0b10000000_00000000, false},
                {0b11111111_11111111, false},
                {0b00000000_00000000, true},
                {0b01111111_11111101, true}
        };
    }

    @Test(dataProvider = "pdolValidityFlagProvider")
    public void should_return_expected_boolean_for_pdol_validity_flag(int camFlags, boolean expectedBoolean) {
        Assertions.assertThat(new SmartCardData(camFlags, Collections.emptyList()).isPdolDataValid())
                .isEqualTo(expectedBoolean);
    }

    @DataProvider
    public Object[][] cdolValidityFlagProvider() {
        return new Object[][]{
                {0b01000000_00000000, false},
                {0b11111111_11111111, false},
                {0b00000000_00000000, true},
                {0b10111111_11111101, true}
        };
    }

    @Test(dataProvider = "cdolValidityFlagProvider")
    public void should_return_expected_boolean_for_cdol_validity_flag(int camFlags, boolean expectedBoolean) {
        Assertions.assertThat(new SmartCardData(camFlags, Collections.emptyList()).isCdolDataValid())
                .isEqualTo(expectedBoolean);
    }

    @DataProvider
    public Object[][] acGenerationFlagProvider() {
        return new Object[][]{
                {0b00100000_00000000, false},
                {0b11111111_11111111, false},
                {0b00000000_00000000, true},
                {0b11011111_11111101, true}
        };
    }

    @Test(dataProvider = "acGenerationFlagProvider")
    public void should_return_expected_boolean_for_ac_generation_flag(int camFlags, boolean expectedBoolean) {
        Assertions.assertThat(new SmartCardData(camFlags, Collections.emptyList()).isAcGenerationSuccessful())
                .isEqualTo(expectedBoolean);
    }

    @DataProvider
    public Object[][] previousCamProcessingFlagProvider() {
        return new Object[][]{
                {0b00000000_00000000, false},
                {0b11110111_11111111, false},
                {0b00001000_00000000, true},
                {0b11111111_11111111, true}
        };
    }

    @Test(dataProvider = "previousCamProcessingFlagProvider")
    public void should_return_expected_boolean_for_previous_cam_processing_flag(int camFlags, boolean expectedBoolean) {
        Assertions.assertThat(new SmartCardData(camFlags, Collections.emptyList()).isPreviousCamProcessingSuccessful())
                .isEqualTo(expectedBoolean);
    }

    @DataProvider
    public Object[][] processingInitiatedFlagProvider() {
        return new Object[][]{
                {0b00000000_00000000, false},
                {0b11111011_11111111, false},
                {0b00000100_00000000, true},
                {0b11111111_11111111, true}
        };
    }

    @Test(dataProvider = "processingInitiatedFlagProvider")
    public void should_return_expected_boolean_for_processing_initiated_flag(int camFlags, boolean expectedBoolean) {
        Assertions.assertThat(new SmartCardData(camFlags, Collections.emptyList()).isProcessingInitiated())
                .isEqualTo(expectedBoolean);
    }

    @DataProvider
    public Object[][] transactionModeFlagProvider() {
        return new Object[][]{
                {0b00000000_00000000, false},
                {0b11111101_11111111, false},
                {0b11111111_11111111, true},
                {0b00000010_00000000, true}
        };
    }

    @Test(dataProvider = "transactionModeFlagProvider")
    public void should_return_expected_transaction_mode_for_corresponding_flag(int camFlags, boolean expectedValue) {
        Assertions.assertThat(new SmartCardData(camFlags, Collections.emptyList()).isEmvTransactionModeUsed())
                .isEqualTo(expectedValue);
    }


    @DataProvider
    public Object[][] cardReaderTypeFlagProvider() {
        return new Object[][]{
                {0b00000000_00000000, false},
                {0b11111110_11111111, false},
                {0b11111111_11111111, true},
                {0b00000001_00000000, true}
        };
    }

    @Test(dataProvider = "cardReaderTypeFlagProvider")
    public void should_return_expected_card_reader_type_for_corresponding_flag(int camFlags, boolean expectedValue) {
        Assertions.assertThat(new SmartCardData(camFlags, Collections.emptyList()).isContactlessReaderUsed())
                .isEqualTo(expectedValue);
    }


    @DataProvider
    public Object[][] expectedNdcStringProvider() {
        final int randomCamFlags = ThreadLocalSecureRandom.get().nextInt(0x010000);
        final String camFlagsString = Integers.toZeroPaddedHexString(randomCamFlags, 4);

        final BerTlv<String> dummyDataObject = new HexStringBerTlv(0xFF, BmpStringGenerator.HEX.fixedLength(4));
        final String dataObjectString = dummyDataObject.toNdcString();

        return new Object[][]{
                {randomCamFlags, Collections.emptyList(), "CAM" + camFlagsString},
                {randomCamFlags, List.of(dummyDataObject), "CAM" + camFlagsString + dataObjectString},
                {randomCamFlags, List.of(dummyDataObject, dummyDataObject), "CAM" + camFlagsString + dataObjectString.repeat(2)}
        };
    }


    @Test(dataProvider = "expectedNdcStringProvider")
    public void should_return_expected_ndc_string(int camFlags, List<BerTlv<String>> dataObjects, String expectedString) {
        Assertions.assertThat(new SmartCardData(camFlags, dataObjects).toNdcString())
                .isEqualTo(expectedString);
    }

    @Test
    public void should_throw_expected_exception_on_invalid_cam_flags() {
        Assertions.assertThatThrownBy(() -> new SmartCardData(0xFFFF + 1, Collections.emptyList()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CAM Flags");
    }
}
