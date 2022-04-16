package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.entry;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.*;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.IStringGenerator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.github.jokoroukwu.jndc.util.text.Strings.EMPTY_STRING;
import static org.assertj.core.api.Assertions.*;

public class TerminalApplicationIdEntryTest {
    private final int entryNumber = 1;
    private final Aid validAid = new Aid("CC");
    private final String validAppLabel = "123";
    private final int validLowestAppVersion = 0xFF_FF;
    private final int validHighestAppVersion = Short.MAX_VALUE;
    private final long validActionCodeDenial = 0xFFL;
    private final TagContainer validDataObjects = new TagContainer(List.of(0xFFBD, 0xA9));
    private final List<Aid> validSecondaryAids = List.of(validAid);

    @DataProvider
    public static Object[][] invalidAppLabelProvider() {
        final IStringGenerator validLabelGenerator = BmpStringGenerator.ofCharacterRange(0x20, 0x7E);
        final IStringGenerator invalidLabelGenerator = BmpStringGenerator.ofCharacterRange('а', 'я');
        return new Object[][]{
                {null},
                {invalidLabelGenerator.fixedLength(10)},
                {validLabelGenerator.fixedLength(17)},
                {validLabelGenerator.fixedLength(18)}
        };
    }

    @DataProvider
    public static Object[][] validAppLabelProvider() {
        final IStringGenerator validLabelGenerator = BmpStringGenerator.ofCharacterRange(0x20, 0x7E);
        return new Object[][]{
                {EMPTY_STRING},
                {validLabelGenerator.fixedLength(1)},
                {validLabelGenerator.fixedLength(2)},
                {validLabelGenerator.fixedLength(15)},
                {validLabelGenerator.fixedLength(16)}
        };
    }

    @DataProvider
    public static Object[][] invalidActionCodeProvider() {
        return new Object[][]{
                {0xFF_FF_FF_FF_FFL + 1L},
                {0xFF_FF_FF_FF_FFL + 2L},
                {-1},
                {-2}
        };
    }

    @DataProvider
    public static Object[][] validActionCodeProvider() {
        return new Object[][]{
                {0L},
                {1L},
                {0xFF_FF_FF_FFL},
                {0xFF_FF_FFL + 1}
        };
    }

    @DataProvider
    public static Object[][] invalidVersionProvider() {
        return new Object[][]{
                {0xFF_FF + 1, 1, "Lowest Application Version Number"},
                {0xFF_FF + 2, 1, "Lowest Application Version Number"},
                {-1, 1, "Lowest Application Version Number"},
                {1, 0xFF_FF + 1, "Highest Application Version Number"},
                {1, 0xFF_FF + 2, "Highest Application Version Number"},
                {1, -1, "Highest Application Version Number"},
        };
    }

    @DataProvider
    public static Object[][] invalidOptionalFieldsProvider() {
        final IStringGenerator cyrillic = BmpStringGenerator.ofCharacterRange('а', 'я');
        return new Object[][]{
                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.SIMULATE, cyrillic.fixedLength(3)},
                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.SIMULATE, cyrillic.fixedLength(3)},
                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.SIMULATE, "\u0000"},

                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.USE_TAG_OR_SIMULATE, cyrillic.fixedLength(3)},
                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.USE_TAG_OR_SIMULATE, cyrillic.fixedLength(3)},
                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.USE_TAG_OR_SIMULATE, "\u0000"},

                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.DEFAULT, "1"},
                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.USE_TAG_OR_CARD, "1"},
                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.USE_CARD, "1"},
                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, null, "1"},

                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.USE_TAG_OR_SIMULATE, null},
                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.USE_TAG_OR_SIMULATE, ""},
                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.SIMULATE, null},
                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.SIMULATE, ""},

                {AppSelectionIndicator.PARTIAL_MATCH, null, Track2ICCData.DEFAULT, ""},
                {null, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.DEFAULT, ""},
                {null, null, Track2ICCData.DEFAULT, ""},
                {null, null, null, "1"}
        };
    }

    @DataProvider
    public static Object[][] validOptionalFieldsProvider() {
        final IStringGenerator validRange = BmpStringGenerator.ofCharacterRange(0x30, 0x39);
        return new Object[][]{
                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.SIMULATE, validRange.fixedLength(1)},
                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.SIMULATE, validRange.fixedLength(33)},
                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.USE_TAG_OR_SIMULATE, validRange.fixedLength(1)},
                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.USE_TAG_OR_SIMULATE, validRange.fixedLength(33)},

                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.DEFAULT, ""},
                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.USE_CARD, ""},
                {AppSelectionIndicator.PARTIAL_MATCH, Track2CentralData.CARD_OR_DEFINED, Track2ICCData.USE_TAG_OR_CARD, ""},


                {AppSelectionIndicator.PARTIAL_MATCH, null, null, ""},
                {null, null, null, ""},
        };
    }

    @DataProvider
    public Object[][] invalidDataObjectsProvider() {
        return new Object[][]{
                {List.of(-1, 0xFF), validDataObjects},
                {Arrays.asList(null, 1), validDataObjects},
                {null, validDataObjects},
                {validDataObjects, List.of(-1, 0xFF)},
                {validDataObjects, Arrays.asList(null, 1)},
                {validDataObjects, null}
        };
    }

    @Test(dataProvider = "invalidAppLabelProvider")
    public void should_throw_exception_when_default_app_label_not_valid(String invalidLabel) {
        assertThatThrownBy(() -> new TerminalApplicationIdEntry(entryNumber, validAid,
                invalidLabel,
                validLowestAppVersion,
                validHighestAppVersion,
                validActionCodeDenial,
                validDataObjects,
                validDataObjects,
                validSecondaryAids,
                null,
                null,
                null,
                EMPTY_STRING)
        ).hasMessageContaining("Default Application Label");
    }

    @Test(dataProvider = "validAppLabelProvider")
    public void should_not_throw_exception_when_default_app_label_valid(String validLabel) {
        assertThatCode(() -> new TerminalApplicationIdEntry(entryNumber, validAid,
                validLabel,
                validLowestAppVersion,
                validHighestAppVersion,
                validActionCodeDenial,
                validDataObjects,
                validDataObjects,
                validSecondaryAids,
                null,
                null,
                null,
                EMPTY_STRING)
        ).doesNotThrowAnyException();
    }

    @Test(dataProvider = "invalidVersionProvider")
    public void should_throw_exception_on_invalid_app_versions(int lowestVersion, int highestVersion, String expectedMessage) {
        assertThatThrownBy(() -> new TerminalApplicationIdEntry(entryNumber, validAid,
                validAppLabel,
                lowestVersion,
                highestVersion,
                validActionCodeDenial,
                validDataObjects,
                validDataObjects,
                validSecondaryAids,
                null,
                null,
                null,
                EMPTY_STRING)
        ).hasMessageContaining(expectedMessage);
    }

    @Test(dataProvider = "invalidActionCodeProvider")
    public void should_throw_exception_on_invalid_action_code_denial(long invalidActionCode) {
        assertThatThrownBy(() -> new TerminalApplicationIdEntry(entryNumber, validAid,
                validAppLabel,
                validLowestAppVersion,
                validHighestAppVersion,
                invalidActionCode,
                validDataObjects,
                validDataObjects,
                validSecondaryAids,
                null,
                null,
                null,
                EMPTY_STRING)
        ).hasMessageContaining("Primary AID Terminal Action Code");
    }

    @Test(dataProvider = "validActionCodeProvider")
    public void should_not_throw_exception_on_valid_action_code_denial(long validActionCodeDenial) {
        assertThatCode(() -> new TerminalApplicationIdEntry(entryNumber, validAid,
                validAppLabel,
                validLowestAppVersion,
                validHighestAppVersion,
                validActionCodeDenial,
                validDataObjects,
                validDataObjects,
                validSecondaryAids,
                null,
                null,
                null,
                EMPTY_STRING)
        ).doesNotThrowAnyException();
    }

    @Test(dataProvider = "invalidOptionalFieldsProvider")
    public void should_throw_exception_on_invalid_optional_fields(AppSelectionIndicator appSelectionIndicator,
                                                                  Track2CentralData track2CentralData,
                                                                  Track2ICCData track2ICCData,
                                                                  String additionalTrack2Data) {
        assertThatThrownBy(() -> new TerminalApplicationIdEntry(entryNumber, validAid,
                validAppLabel,
                validLowestAppVersion,
                validHighestAppVersion,
                validActionCodeDenial,
                validDataObjects,
                validDataObjects,
                validSecondaryAids,
                appSelectionIndicator,
                track2CentralData,
                track2ICCData,
                additionalTrack2Data)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test(dataProvider = "validOptionalFieldsProvider")
    public void should_not_throw_exception_on_valid_optional_fields(AppSelectionIndicator appSelectionIndicator,
                                                                    Track2CentralData track2CentralData,
                                                                    Track2ICCData track2ICCData,
                                                                    String additionalTrack2Data) {
        assertThatCode(() -> new TerminalApplicationIdEntry(entryNumber, validAid,
                validAppLabel,
                validLowestAppVersion,
                validHighestAppVersion,
                validActionCodeDenial,
                validDataObjects,
                validDataObjects,
                validSecondaryAids,
                appSelectionIndicator,
                track2CentralData,
                track2ICCData,
                additionalTrack2Data)
        ).doesNotThrowAnyException();
    }

    @DataProvider
    public Object[][] entryProvider() {
        return new Object[][]{
                {new TerminalApplicationIdEntry(entryNumber, validAid,
                        validAppLabel,
                        validLowestAppVersion,
                        validHighestAppVersion,
                        validActionCodeDenial,
                        validDataObjects,
                        validDataObjects,
                        validSecondaryAids,
                        null,
                        null,
                        null,
                        EMPTY_STRING), "01"
                        + "01" + "CC"
                        + "03" + "123"
                        + "CAM"
                        + "FFFF"
                        + "7FFF"
                        + "00000000FF"
                        + "02" + "FFBD" + "A9"
                        + "02" + "FFBD" + "A9"
                        + "01" + "01" + "CC"
                },
                {new TerminalApplicationIdEntry(entryNumber, validAid,
                        validAppLabel,
                        validLowestAppVersion,
                        validHighestAppVersion,
                        validActionCodeDenial,
                        validDataObjects,
                        validDataObjects,
                        validSecondaryAids,
                        AppSelectionIndicator.PARTIAL_MATCH,
                        null,
                        null,
                        EMPTY_STRING), "01"
                        + "01" + "CC"
                        + "03" + "123"
                        + "CAM"
                        + "FFFF"
                        + "7FFF"
                        + "00000000FF"
                        + "02" + "FFBD" + "A9"
                        + "02" + "FFBD" + "A9"
                        + "01" + "01" + "CC"
                        + "00"
                },
                {new TerminalApplicationIdEntry(entryNumber,
                        validAid,
                        validAppLabel,
                        validLowestAppVersion,
                        validHighestAppVersion,
                        validActionCodeDenial,
                        validDataObjects,
                        validDataObjects,
                        validSecondaryAids,
                        AppSelectionIndicator.PARTIAL_MATCH,
                        Track2CentralData.CARD_OR_DEFINED,
                        null,
                        EMPTY_STRING), "01"
                        + "01" + "CC"
                        + "03" + "123"
                        + "CAM"
                        + "FFFF"
                        + "7FFF"
                        + "00000000FF"
                        + "02" + "FFBD" + "A9"
                        + "02" + "FFBD" + "A9"
                        + "01" + "01" + "CC"
                        + "00"
                        + "01"
                },
                {new TerminalApplicationIdEntry(entryNumber,
                        validAid,
                        validAppLabel,
                        validLowestAppVersion,
                        validHighestAppVersion,
                        validActionCodeDenial,
                        validDataObjects,
                        validDataObjects,
                        validSecondaryAids,
                        AppSelectionIndicator.PARTIAL_MATCH,
                        Track2CentralData.CARD_OR_DEFINED,
                        Track2ICCData.DEFAULT,
                        EMPTY_STRING), "01"
                        + "01" + "CC"
                        + "03" + "123"
                        + "CAM"
                        + "FFFF"
                        + "7FFF"
                        + "00000000FF"
                        + "02" + "FFBD" + "A9"
                        + "02" + "FFBD" + "A9"
                        + "01" + "01" + "CC"
                        + "00"
                        + "01"
                        + "00"
                },
                {new TerminalApplicationIdEntry(entryNumber,
                        validAid,
                        validAppLabel,
                        validLowestAppVersion,
                        validHighestAppVersion,
                        validActionCodeDenial,
                        validDataObjects,
                        validDataObjects,
                        validSecondaryAids,
                        AppSelectionIndicator.PARTIAL_MATCH,
                        Track2CentralData.CARD_OR_DEFINED,
                        Track2ICCData.SIMULATE,
                        "123"), "01"
                        + "01" + "CC"
                        + "03" + "123"
                        + "CAM"
                        + "FFFF"
                        + "7FFF"
                        + "00000000FF"
                        + "02" + "FFBD" + "A9"
                        + "02" + "FFBD" + "A9"
                        + "01" + "01" + "CC"
                        + "00"
                        + "01"
                        + "04"
                        + "03" + "123"
                },
        };
    }

    @Test(dataProvider = "entryProvider")
    public void should_return_expected_ndc_string(TerminalApplicationIdEntry entry, String expectedNdcString) {
        System.out.println(entry);
        assertThat(entry.toNdcString())
                .isEqualTo(expectedNdcString);
    }

}
