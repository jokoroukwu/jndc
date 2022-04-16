package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class TerminalApplicationIdEntryBuilder {
    private int entryNumber;
    private Aid primaryAid;
    private String defaultAppLabel = Strings.EMPTY_STRING;
    private String primaryAidIccAppType = "CAM";
    private int primaryAidLowestAppVersion;
    private int primaryAidHighestAppVersion;
    private long primaryAidActionCodeDenial;
    private TagContainer transactionRqDataObjects;
    private TagContainer completionDataObjects;
    private List<Aid> secondaryAids;
    private AppSelectionIndicator appSelectionIndicator;
    private Track2CentralData trackTwoCentralData;
    private Track2ICCData trackTwoIccData;
    private String additionalTrackTwoData = Strings.EMPTY_STRING;

    public TerminalApplicationIdEntryBuilder() {
    }

    public TerminalApplicationIdEntryBuilder withEntryNumber(int entryNumber) {
        this.entryNumber = entryNumber;
        return this;
    }

    public TerminalApplicationIdEntryBuilder withPrimaryAid(Aid primaryAid) {
        this.primaryAid = primaryAid;
        return this;
    }


    public TerminalApplicationIdEntryBuilder withDefaultAppLabel(String defaultAppLabel) {
        this.defaultAppLabel = defaultAppLabel;
        return this;
    }

    public TerminalApplicationIdEntryBuilder withPrimaryAidIccAppType(String primaryAidIccAppType) {
        this.primaryAidIccAppType = primaryAidIccAppType;
        return this;
    }

    public TerminalApplicationIdEntryBuilder withPrimaryAidLowestAppVersion(int primaryAidLowestAppVersion) {
        this.primaryAidLowestAppVersion = primaryAidLowestAppVersion;
        return this;
    }

    public TerminalApplicationIdEntryBuilder withPrimaryAidHighestAppVersion(int primaryAidHighestAppVersion) {
        this.primaryAidHighestAppVersion = primaryAidHighestAppVersion;
        return this;
    }

    public TerminalApplicationIdEntryBuilder withPrimaryAidActionCodeDenial(long primaryAidActionCodeDenial) {
        this.primaryAidActionCodeDenial = primaryAidActionCodeDenial;
        return this;
    }


    public TerminalApplicationIdEntryBuilder withTransactionRqDataObjects(TagContainer transactionRqDataObjects) {
        this.transactionRqDataObjects = transactionRqDataObjects;
        return this;
    }

    public TerminalApplicationIdEntryBuilder withCompletionDataObjects(TagContainer completionDataObjects) {
        this.completionDataObjects = completionDataObjects;
        return this;
    }

    public TerminalApplicationIdEntryBuilder withSecondaryAids(List<Aid> secondaryAids) {
        this.secondaryAids = secondaryAids;
        return this;
    }

    public TerminalApplicationIdEntryBuilder withAppSelectionIndicator(AppSelectionIndicator appSelectionIndicator) {
        this.appSelectionIndicator = appSelectionIndicator;
        return this;
    }

    public TerminalApplicationIdEntryBuilder withTrack2CentralData(Track2CentralData trackTwoCentralData) {
        this.trackTwoCentralData = trackTwoCentralData;
        return this;
    }

    public TerminalApplicationIdEntryBuilder withTrack2IccData(Track2ICCData trackTwoIccData) {
        this.trackTwoIccData = trackTwoIccData;
        return this;
    }

    public TerminalApplicationIdEntryBuilder withAdditionalTrackTwoData(String additionalTrackTwoData) {
        this.additionalTrackTwoData = additionalTrackTwoData;
        return this;
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

    public int getPrimaryAidLowestAppVersion() {
        return primaryAidLowestAppVersion;
    }

    public int getPrimaryAidHighestAppVersion() {
        return primaryAidHighestAppVersion;
    }

    public long getPrimaryAidActionCodeDenial() {
        return primaryAidActionCodeDenial;
    }

    public TagContainer getTransactionRqDataObjects() {
        return transactionRqDataObjects;
    }

    public TagContainer getCompletionDataObjects() {
        return completionDataObjects;
    }

    public List<Aid> getSecondaryAids() {
        return secondaryAids;
    }

    public AppSelectionIndicator getAppSelectionIndicator() {
        return appSelectionIndicator;
    }

    public Track2CentralData getTrackTwoCentralData() {
        return trackTwoCentralData;
    }

    public String getAdditionalTrackTwoData() {
        return additionalTrackTwoData;
    }

    public Track2ICCData getTrackTwoIccData() {
        return trackTwoIccData;
    }

    public TerminalApplicationIdEntry build() {
        return new TerminalApplicationIdEntry(
                entryNumber,
                primaryAid,
                defaultAppLabel,
                primaryAidLowestAppVersion,
                primaryAidHighestAppVersion,
                primaryAidActionCodeDenial,
                transactionRqDataObjects,
                completionDataObjects,
                secondaryAids,
                appSelectionIndicator,
                trackTwoCentralData,
                trackTwoIccData,
                additionalTrackTwoData
        );
    }

    TerminalApplicationIdEntry buildWithNoValidation() {
        return new TerminalApplicationIdEntry(
                entryNumber,
                primaryAid,
                defaultAppLabel,
                primaryAidLowestAppVersion,
                primaryAidHighestAppVersion,
                primaryAidActionCodeDenial,
                transactionRqDataObjects,
                completionDataObjects,
                secondaryAids,
                appSelectionIndicator,
                trackTwoCentralData,
                trackTwoIccData,
                additionalTrackTwoData,
                primaryAidIccAppType,
                null
        );
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TerminalApplicationIdEntryBuilder.class.getSimpleName() + ": {", "}")
                .add("entryNumber: " + entryNumber)
                .add("primaryAid: " + primaryAid)
                .add("defaultAppLabel: '" + defaultAppLabel + '\'')
                .add("primaryAidIccAppType: '" + primaryAidIccAppType + '\'')
                .add("primaryAidLowestAppVersion: " + primaryAidLowestAppVersion)
                .add("primaryAidHighestAppVersion: " + primaryAidHighestAppVersion)
                .add("primaryAidActionCodeDenial: " + primaryAidActionCodeDenial)
                .add("transactionRqDataObjects: " + transactionRqDataObjects)
                .add("completionDataObjects: " + completionDataObjects)
                .add("secondaryAids: " + secondaryAids)
                .add("appSelectionIndicator: " + appSelectionIndicator)
                .add("trackTwoCentralData: " + trackTwoCentralData)
                .add("trackTwoIccData: " + trackTwoIccData)
                .add("additionalTrackTwoData: '" + additionalTrackTwoData + '\'')
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerminalApplicationIdEntryBuilder that = (TerminalApplicationIdEntryBuilder) o;
        return entryNumber == that.entryNumber &&
                primaryAidLowestAppVersion == that.primaryAidLowestAppVersion &&
                primaryAidHighestAppVersion == that.primaryAidHighestAppVersion &&
                primaryAidActionCodeDenial == that.primaryAidActionCodeDenial &&
                appSelectionIndicator == that.appSelectionIndicator &&
                trackTwoCentralData == that.trackTwoCentralData &&
                trackTwoIccData == that.trackTwoIccData &&
                Objects.equals(primaryAid, that.primaryAid) &&
                Objects.equals(defaultAppLabel, that.defaultAppLabel) &&
                Objects.equals(primaryAidIccAppType, that.primaryAidIccAppType) &&
                Objects.equals(transactionRqDataObjects, that.transactionRqDataObjects) &&
                Objects.equals(completionDataObjects, that.completionDataObjects) &&
                Objects.equals(secondaryAids, that.secondaryAids) &&
                Objects.equals(additionalTrackTwoData, that.additionalTrackTwoData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entryNumber, primaryAid, defaultAppLabel, primaryAidIccAppType, primaryAidLowestAppVersion,
                primaryAidHighestAppVersion, primaryAidActionCodeDenial, transactionRqDataObjects, completionDataObjects,
                secondaryAids, appSelectionIndicator, trackTwoCentralData, trackTwoIccData, additionalTrackTwoData);
    }
}
