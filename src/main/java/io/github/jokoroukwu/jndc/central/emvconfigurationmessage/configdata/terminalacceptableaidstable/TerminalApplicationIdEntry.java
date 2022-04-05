package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.Longs;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class TerminalApplicationIdEntry implements NdcComponent {
    public static final int AVG_STRING_LENGTH = 128;

    private final int entryNumber;
    private final Aid primaryAid;
    private final String primaryAidIccAppType;
    private final String defaultAppLabel;
    private final int primaryAidLowestAppVersion;
    private final int primaryAidHighestAppVersion;
    private final long primaryAidActionCodeDenial;
    private final TagContainer transactionRqDataObjects;
    private final TagContainer completionDataObjects;
    private final List<Aid> secondaryAids;
    private final AppSelectionIndicator appSelectionIndicator;
    private final Track2CentralData track2CentralData;
    private final Track2ICCData track2ICCData;
    private final String additionalTrackTwoData;

    public TerminalApplicationIdEntry(int entryNumber,
                                      Aid primaryAid,
                                      String defaultAppLabel,
                                      int primaryAidLowestAppVersion,
                                      int primaryAidHighestAppVersion,
                                      long primaryAidActionCodeDenial,
                                      TagContainer transactionRqDataObjects,
                                      TagContainer completionDataObjects,
                                      List<Aid> secondaryAids,
                                      AppSelectionIndicator appSelectionIndicator,
                                      Track2CentralData track2CentralData,
                                      Track2ICCData track2ICCData,
                                      String additionalTrackTwoData,
                                      String primaryAidIccAppType) {

        this.primaryAidIccAppType = validatePrimaryAidIccAppType(primaryAidIccAppType);
        this.entryNumber = Integers.validateHexRange(entryNumber, 0, 0xFF, "'Entry Number'");
        this.primaryAid = ObjectUtils.validateNotNull(primaryAid, "'Full/Partial Primary AID Value'");
        this.defaultAppLabel = validateDefaultAppLabel(defaultAppLabel);
        this.primaryAidLowestAppVersion = Integers.validateHexRange(primaryAidLowestAppVersion, 0, 0xFFFF,
                "'Primary AID Lowest Application Version Number'");
        this.primaryAidHighestAppVersion = Integers.validateHexRange(primaryAidHighestAppVersion, 0, 0xFFFF,
                "'Primary AID Highest Application Version Number'");
        this.primaryAidActionCodeDenial = Longs.validateHexRange(primaryAidActionCodeDenial, 0, 0xFF_FF_FF_FF_FFL,
                "'Primary AID Terminal Action Code ‐ Denial'");
        Integers.validateHexRange(secondaryAids.size(), 0, 0xFF, "Number of Secondary AIDs");
        this.secondaryAids = List.copyOf(secondaryAids);
        this.transactionRqDataObjects = ObjectUtils.validateNotNull(transactionRqDataObjects,
                "'Data Object(s) for Transaction Request'");
        this.completionDataObjects = ObjectUtils.validateNotNull(completionDataObjects,
                "'Data Object(s) for Completion Data'");
        validateOptionalFields(appSelectionIndicator, track2CentralData, track2ICCData, additionalTrackTwoData);
        this.additionalTrackTwoData = additionalTrackTwoData;
        this.appSelectionIndicator = appSelectionIndicator;
        this.track2CentralData = track2CentralData;
        this.track2ICCData = track2ICCData;
    }

    public TerminalApplicationIdEntry(int entryNumber,
                                      Aid primaryAid,
                                      String defaultAppLabel,
                                      int primaryAidLowestAppVersion,
                                      int primaryAidHighestAppVersion,
                                      long primaryAidActionCodeDenial,
                                      TagContainer transactionRqDataObjects,
                                      TagContainer completionDataObjects,
                                      List<Aid> secondaryAids,
                                      AppSelectionIndicator appSelectionIndicator,
                                      Track2CentralData track2CentralData,
                                      Track2ICCData track2ICCData,
                                      String additionalTrackTwoData) {
        this(entryNumber,
                primaryAid,
                defaultAppLabel,
                primaryAidLowestAppVersion,
                primaryAidHighestAppVersion,
                primaryAidActionCodeDenial,
                transactionRqDataObjects,
                completionDataObjects,
                secondaryAids,
                appSelectionIndicator,
                track2CentralData,
                track2ICCData,
                additionalTrackTwoData,
                "CAM");
    }

    //  no-validation constructor;
    //  used internally by the corresponding reader
    TerminalApplicationIdEntry(int entryNumber,
                               Aid primaryAid,
                               String defaultAppLabel,
                               int primaryAidLowestAppVersion,
                               int primaryAidHighestAppVersion,
                               long primaryAidActionCodeDenial,
                               TagContainer transactionRqDataObjects,
                               TagContainer completionDataObjects,
                               List<Aid> secondaryAids,
                               AppSelectionIndicator appSelectionIndicator,
                               Track2CentralData track2CentralData,
                               Track2ICCData track2ICCData,
                               String additionalTrackTwoData,
                               String primaryAidIccAppType,
                               Void empty) {
        this.primaryAidIccAppType = primaryAidIccAppType;
        this.primaryAid = primaryAid;
        this.defaultAppLabel = defaultAppLabel;
        this.primaryAidLowestAppVersion = primaryAidLowestAppVersion;
        this.primaryAidHighestAppVersion = primaryAidHighestAppVersion;
        this.primaryAidActionCodeDenial = primaryAidActionCodeDenial;
        this.transactionRqDataObjects = transactionRqDataObjects;
        this.completionDataObjects = completionDataObjects;
        this.secondaryAids = secondaryAids;
        this.appSelectionIndicator = appSelectionIndicator;
        this.track2CentralData = track2CentralData;
        this.track2ICCData = track2ICCData;
        this.additionalTrackTwoData = additionalTrackTwoData;
        this.entryNumber = entryNumber;
    }

    public static TerminalApplicationIdEntryBuilder builder() {
        return new TerminalApplicationIdEntryBuilder();
    }

    public static String validateDefaultAppLabel(String label) {
        ObjectUtils.validateNotNull(label, "'Default Application Label'");
        Integers.validateRange(label.length(), 0, 16, "'Default Application Label' length");
        Strings.validateCharRange(label, ' ', '~', "'Default Application Label'");
        return label;
    }

    public int getEntryNumber() {
        return entryNumber;
    }

    public Aid getPrimaryAid() {
        return primaryAid;
    }

    public String getDefaultAppLabel() {
        return defaultAppLabel;
    }

    public String getPrimaryAidICCAppType() {
        return primaryAidIccAppType;
    }

    public int getPrimaryAidLowestAppVersion() {
        return primaryAidLowestAppVersion;
    }

    public int getPrimaryAidHighestAppVersion() {
        return primaryAidHighestAppVersion;
    }

    public long getPrimaryAidActionCodeDenial() {
        return primaryAidActionCodeDenial;
    }

    public List<Integer> getTransactionRqDataObjects() {
        return transactionRqDataObjects;
    }

    public List<Integer> getCompletionDataObjects() {
        return completionDataObjects;
    }

    public List<Aid> getSecondaryAids() {
        return secondaryAids;
    }

    public Optional<AppSelectionIndicator> getAppSelectionIndicator() {
        return Optional.ofNullable(appSelectionIndicator);
    }

    public Optional<Track2CentralData> getTrack2CentralData() {
        return Optional.ofNullable(track2CentralData);
    }

    public Optional<Track2ICCData> getTrack2ICCData() {
        return Optional.ofNullable(track2ICCData);
    }

    public String getAdditionalTrackTwoData() {
        return additionalTrackTwoData;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TerminalApplicationIdEntry.class.getSimpleName() + ": {", "}")
                .add("entryNumber: " + entryNumber)
                .add("primaryAidIccAppType: '" + primaryAidIccAppType + "'")
                .add("primaryAid: " + primaryAid)
                .add("defaultAppLabel: '" + defaultAppLabel + "'")
                .add("primaryAidLowestAppVersion: " + primaryAidLowestAppVersion)
                .add("primaryAidHighestAppVersion: " + primaryAidHighestAppVersion)
                .add("primaryAidActionCodeDenial: " + primaryAidActionCodeDenial)
                .add("transactionRqDataObjects: " + transactionRqDataObjects)
                .add("completionDataObjects: " + completionDataObjects)
                .add("secondaryAids: " + secondaryAids)
                .add("appSelectionIndicator: " + appSelectionIndicator)
                .add("track2CentralData: " + track2CentralData)
                .add("track2ICCData: " + track2ICCData)
                .add("additionalTrackTwoData: '" + additionalTrackTwoData + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerminalApplicationIdEntry that = (TerminalApplicationIdEntry) o;
        return entryNumber == that.entryNumber &&
                primaryAidLowestAppVersion == that.primaryAidLowestAppVersion &&
                primaryAidHighestAppVersion == that.primaryAidHighestAppVersion &&
                primaryAidActionCodeDenial == that.primaryAidActionCodeDenial &&
                appSelectionIndicator == that.appSelectionIndicator &&
                track2CentralData == that.track2CentralData &&
                track2ICCData == that.track2ICCData &&
                primaryAidIccAppType.equals(that.primaryAidIccAppType) &&
                primaryAid.equals(that.primaryAid) &&
                defaultAppLabel.equals(that.defaultAppLabel) &&
                transactionRqDataObjects.equals(that.transactionRqDataObjects) &&
                completionDataObjects.equals(that.completionDataObjects) &&
                secondaryAids.equals(that.secondaryAids) &&
                additionalTrackTwoData.equals(that.additionalTrackTwoData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entryNumber,
                primaryAid,
                primaryAidIccAppType,
                defaultAppLabel,
                primaryAidLowestAppVersion,
                primaryAidHighestAppVersion,
                primaryAidActionCodeDenial,
                transactionRqDataObjects,
                completionDataObjects,
                secondaryAids,
                appSelectionIndicator,
                track2CentralData,
                track2ICCData,
                additionalTrackTwoData);
    }

    @Override
    public String toNdcString() {
        final NdcStringBuilder builder = new NdcStringBuilder(AVG_STRING_LENGTH)
                .appendZeroPaddedHex(entryNumber, 2)
                .appendComponent(primaryAid)
                .appendZeroPaddedHex(defaultAppLabel.length(), 2)
                .append(defaultAppLabel)
                .append(primaryAidIccAppType)
                .appendZeroPaddedHex(primaryAidLowestAppVersion, 4)
                .appendZeroPaddedHex(primaryAidHighestAppVersion, 4)
                .appendZeroPaddedHex(primaryAidActionCodeDenial, 10)
                .appendComponent(transactionRqDataObjects)
                .appendComponent(completionDataObjects)
                .appendZeroPaddedHex(secondaryAids.size(), 2)
                .appendComponents(secondaryAids);
        appendOptionalFields(builder);
        return builder.toString();
    }

    private String validatePrimaryAidIccAppType(String value) {
        ObjectUtils.validateNotNull(value, "'Primary AID ICC Application Type'");
        Integers.validateIsExactValue(value.length(), 3, "'Primary AID ICC Application Type' length");
        return value;
    }

    private void appendOptionalFields(NdcStringBuilder builder) {
        if (appSelectionIndicator != null) {
            builder.appendComponent(appSelectionIndicator);
        }
        if (track2CentralData != null) {
            builder.appendComponent(track2CentralData);
        }
        if (track2ICCData != null) {
            builder.appendComponent(track2ICCData);
        }
        if (!additionalTrackTwoData.isEmpty()) {
            builder.appendZeroPaddedHex(additionalTrackTwoData.length(), 2)
                    .append(additionalTrackTwoData);
        }
    }

    private void validateOptionalFields(AppSelectionIndicator appSelectionIndicator,
                                        Track2CentralData track2CentralData,
                                        Track2ICCData track2ICCData,
                                        String additionalTrackTwoData) {
        ObjectUtils.validateNotNull(additionalTrackTwoData, "'Additional Track 2 Data'");
        if (track2ICCData != null) {
            checkTrack2CentralDataIsPresent(track2CentralData);
            checkAppSelectionIndicatorIsPresent(appSelectionIndicator);
            if (track2ICCData == Track2ICCData.USE_TAG_OR_SIMULATE || track2ICCData == Track2ICCData.SIMULATE) {
                Integers.validateRange(additionalTrackTwoData.length(), 1, 33, "'Additional Track 2 Data' length");
                Strings.validateCharRange(additionalTrackTwoData, '0', '9', "'Additional Track 2 Data'");
            } else {
                checkAdditionalTrack2DataIsOmitted(additionalTrackTwoData);
            }
        } else if (track2CentralData != null) {
            checkAppSelectionIndicatorIsPresent(appSelectionIndicator);
            checkAdditionalTrack2DataIsOmitted(additionalTrackTwoData);
        } else {
            checkAdditionalTrack2DataIsOmitted(additionalTrackTwoData);
        }
    }


    private void checkTrack2CentralDataIsPresent(Track2CentralData track2CentralData) {
        if (track2CentralData == null) {
            throw new IllegalArgumentException("'Track 2 Data for Central' must not be null when" +
                    " 'Track 2 Data To Be Used During ICC Transaction' is specified but was null");
        }
    }

    private void checkAdditionalTrack2DataIsOmitted(String value) {
        if (!value.isEmpty()) {
            throw new IllegalArgumentException("'Additional Track 2 Data' must be omitted when " +
                    "'Track 2 Data To Be Used During ICC Transaction' is omitted or is '0', '1' or '2' but was: " + value);
        }
    }

    private void checkAppSelectionIndicatorIsPresent(AppSelectionIndicator appSelectionIndicator) {
        if (appSelectionIndicator == null) {
            throw new IllegalArgumentException("'Application Selection Indicator' must not be null when" +
                    " 'Track 2 Data for Central' is specified");
        }
    }
}
