package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload;

import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pckln.MaxPinDigitsChecked;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pmxpn.MaxPinDigitsEntered;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public final class FitBuilder {
    private int fitNumber;
    private int institutionIdIndex;
    private long institutionId;
    private int indirectNextStateIndex;
    private int algorithmOrBankId;
    private MaxPinDigitsEntered maxPinDigitsEntered;
    private MaxPinDigitsChecked maxPinDigitsChecked;
    private int pinPad;
    private int panDataIndex;
    private int panDataLength;
    private int panPad;
    private int track3PinRetryCount;
    private int pinOffsetData;
    private long decimalisationTable;
    private byte[] encryptedPinKey;
    private int indexReferencePoint;
    private int languageCodeIndex;
    private int cimSensorFlag;
    private int reservedField;
    private int pinBlockFormat;

    public FitBuilder withFitNumber(int fitNumber) {
        this.fitNumber = fitNumber;
        return this;
    }

    public FitBuilder withInstitutionIdIndex(int institutionIdIndex) {
        this.institutionIdIndex = institutionIdIndex;
        return this;
    }

    public FitBuilder withInstitutionId(long institutionId) {
        this.institutionId = institutionId;
        return this;
    }

    public FitBuilder withIndirectNextStateIndex(int indirectNextStateIndex) {
        this.indirectNextStateIndex = indirectNextStateIndex;
        return this;
    }

    public FitBuilder withAlgorithmOrBankId(int algorithmOrBankId) {
        this.algorithmOrBankId = algorithmOrBankId;
        return this;
    }

    public FitBuilder withMaxPinDigitsEntered(MaxPinDigitsEntered maxPinDigitsEntered) {
        this.maxPinDigitsEntered = maxPinDigitsEntered;
        return this;
    }

    public FitBuilder withMaxPinDigitsChecked(MaxPinDigitsChecked maxPinDigitsChecked) {
        this.maxPinDigitsChecked = maxPinDigitsChecked;
        return this;
    }

    public FitBuilder withPinPad(int pinPad) {
        this.pinPad = pinPad;
        return this;
    }

    public FitBuilder withPanDataIndex(int panDataIndex) {
        this.panDataIndex = panDataIndex;
        return this;
    }

    public FitBuilder withPanDataLength(int panDataLength) {
        this.panDataLength = panDataLength;
        return this;
    }

    public FitBuilder withPanPad(int panPad) {
        this.panPad = panPad;
        return this;
    }

    public FitBuilder withTrack3PinRetryCount(int trackThreePinRetryCount) {
        this.track3PinRetryCount = trackThreePinRetryCount;
        return this;
    }

    public FitBuilder withPinOffsetData(int pinOffsetData) {
        this.pinOffsetData = pinOffsetData;
        return this;
    }

    public FitBuilder withDecimalisationTable(long decimalisationTable) {
        this.decimalisationTable = decimalisationTable;
        return this;
    }

    public FitBuilder withEncryptedPinKey(byte[] encryptedPinKey) {
        this.encryptedPinKey = encryptedPinKey;
        return this;
    }

    public FitBuilder withIndexReferencePoint(int indexReferencePoint) {
        this.indexReferencePoint = indexReferencePoint;
        return this;
    }

    public FitBuilder withLanguageCodeIndex(int languageCodeIndex) {
        this.languageCodeIndex = languageCodeIndex;
        return this;
    }

    public FitBuilder withMmSensorFlag(int mmSensorFlag) {
        this.cimSensorFlag = mmSensorFlag;
        return this;
    }

    public FitBuilder withReservedField(int reservedField) {
        this.reservedField = reservedField;
        return this;
    }

    public FitBuilder withPinBlockFormat(int pinBlockFormat) {
        this.pinBlockFormat = pinBlockFormat;
        return this;
    }

    public int getFitNumber() {
        return fitNumber;
    }

    public int getInstitutionIdIndex() {
        return institutionIdIndex;
    }

    public long getInstitutionId() {
        return institutionId;
    }

    public int getIndirectNextStateIndex() {
        return indirectNextStateIndex;
    }

    public int getAlgorithmOrBankId() {
        return algorithmOrBankId;
    }

    public MaxPinDigitsEntered getMaxPinDigitsEntered() {
        return maxPinDigitsEntered;
    }

    public MaxPinDigitsChecked getMaxPinDigitsChecked() {
        return maxPinDigitsChecked;
    }

    public int getPinPad() {
        return pinPad;
    }

    public int getPanDataIndex() {
        return panDataIndex;
    }

    public int getPanDataLength() {
        return panDataLength;
    }

    public int getPanPad() {
        return panPad;
    }

    public int getTrack3PinRetryCount() {
        return track3PinRetryCount;
    }

    public int getPinOffsetData() {
        return pinOffsetData;
    }

    public long getDecimalisationTable() {
        return decimalisationTable;
    }

    public byte[] getEncryptedPinKey() {
        return encryptedPinKey.clone();
    }

    public int getIndexReferencePoint() {
        return indexReferencePoint;
    }

    public int getLanguageCodeIndex() {
        return languageCodeIndex;
    }

    public int getCimSensorFlag() {
        return cimSensorFlag;
    }

    public int getReservedField() {
        return reservedField;
    }

    public int getPinBlockFormat() {
        return pinBlockFormat;
    }

    public Fit build() {
        return new Fit(fitNumber,
                institutionIdIndex,
                institutionId,
                indirectNextStateIndex,
                algorithmOrBankId,
                maxPinDigitsEntered,
                maxPinDigitsChecked,
                pinPad,
                panDataIndex,
                panDataLength,
                panPad,
                track3PinRetryCount,
                pinOffsetData,
                decimalisationTable,
                encryptedPinKey,
                indexReferencePoint,
                languageCodeIndex,
                cimSensorFlag,
                reservedField,
                pinBlockFormat);
    }

    Fit buildWithNoValidation() {
        return new Fit(fitNumber,
                institutionIdIndex,
                institutionId,
                indirectNextStateIndex,
                algorithmOrBankId,
                maxPinDigitsEntered,
                maxPinDigitsChecked,
                pinPad,
                panDataIndex,
                panDataLength,
                panPad,
                track3PinRetryCount,
                pinOffsetData,
                decimalisationTable,
                encryptedPinKey,
                indexReferencePoint,
                languageCodeIndex,
                cimSensorFlag,
                reservedField,
                pinBlockFormat,
                null);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FitBuilder.class.getSimpleName() + ": {", "}")
                .add("fitNumber: " + fitNumber)
                .add("institutionIdIndex: " + institutionIdIndex)
                .add("institutionId: " + institutionId)
                .add("indirectNextStateIndex: " + indirectNextStateIndex)
                .add("algorithmOrBankId: " + algorithmOrBankId)
                .add("maxPinDigitsEntered: " + maxPinDigitsEntered)
                .add("maxPinDigitsChecked: " + maxPinDigitsChecked)
                .add("pinPad: " + pinPad)
                .add("panDataIndex: " + panDataIndex)
                .add("panDataLength: " + panDataLength)
                .add("panPad: " + panPad)
                .add("track3PinRetryCount: " + track3PinRetryCount)
                .add("pinOffsetData: " + pinOffsetData)
                .add("decimalisationTable: " + decimalisationTable)
                .add("encryptedPinKey: " + Arrays.toString(encryptedPinKey))
                .add("indexReferencePoint: " + indexReferencePoint)
                .add("languageCodeIndex: " + languageCodeIndex)
                .add("cimSensorFlag: " + cimSensorFlag)
                .add("reservedField: " + reservedField)
                .add("pinBlockFormat: " + pinBlockFormat)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FitBuilder that = (FitBuilder) o;
        return fitNumber == that.fitNumber &&
                institutionIdIndex == that.institutionIdIndex &&
                institutionId == that.institutionId &&
                indirectNextStateIndex == that.indirectNextStateIndex &&
                algorithmOrBankId == that.algorithmOrBankId &&
                pinPad == that.pinPad &&
                panDataIndex == that.panDataIndex &&
                panDataLength == that.panDataLength &&
                panPad == that.panPad &&
                track3PinRetryCount == that.track3PinRetryCount &&
                pinOffsetData == that.pinOffsetData &&
                indexReferencePoint == that.indexReferencePoint &&
                languageCodeIndex == that.languageCodeIndex &&
                cimSensorFlag == that.cimSensorFlag &&
                reservedField == that.reservedField &&
                pinBlockFormat == that.pinBlockFormat &&
                decimalisationTable == that.decimalisationTable &&
                Arrays.equals(encryptedPinKey, that.encryptedPinKey) &&
                Objects.equals(maxPinDigitsEntered, that.maxPinDigitsEntered) &&
                Objects.equals(maxPinDigitsChecked, that.maxPinDigitsChecked);

    }

    @Override
    public int hashCode() {
        return Objects.hash(fitNumber, institutionIdIndex, institutionId, indirectNextStateIndex, algorithmOrBankId,
                maxPinDigitsEntered, maxPinDigitsChecked, pinPad, panDataIndex, panDataLength, panPad, track3PinRetryCount,
                pinOffsetData, decimalisationTable, Arrays.hashCode(encryptedPinKey), indexReferencePoint, languageCodeIndex, cimSensorFlag,
                reservedField, pinBlockFormat);
    }
}
