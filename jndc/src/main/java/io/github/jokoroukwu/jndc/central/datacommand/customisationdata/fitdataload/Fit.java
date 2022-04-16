package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.*;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pckln.MaxPinDigitsChecked;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pckln.MaxPinDigitsCheckedAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pmxpn.MaxPinDigitsEntered;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pmxpn.MaxPinDigitsEnteredAppender;
import io.github.jokoroukwu.jndc.util.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class Fit implements NdcComponent {
    public static final int STRING_BUILDER_CAPACITY = 123;

    private final int fitNumber;
    private final int institutionIndex;
    private final long institutionId;
    private final int indirectNextStateIndex;
    private final int algorithmOrBankId;
    private final MaxPinDigitsEntered maxPinDigitsEntered;
    private final MaxPinDigitsChecked maxPinDigitsChecked;
    private final int pinPad;
    private final int panDataIndex;
    private final int panDataLength;
    private final int panPad;
    private final int track3PinRetryCount;
    private final int pinOffsetIndex;
    private final long decimalisationTable;
    private final byte[] encryptedPinKey;
    private final int indexReferencePoint;
    private final int languageCodeIndex;
    private final int cimSensorFlag;
    private final int reservedField;
    private final int pinBlockFormat;

    public Fit(int fitNumber,
               int institutionIndex,
               long institutionId,
               int indirectNextStateIndex,
               int algorithmOrBankId,
               MaxPinDigitsEntered maxPinDigitsEntered,
               MaxPinDigitsChecked maxPinDigitsChecked,
               int pinPad,
               int panDataIndex,
               int panDataLength,
               int panPad,
               int track3PinRetryCount,
               int pinOffsetIndex,
               long decimalisationTable,
               byte[] encryptedPinKey,
               int indexReferencePoint,
               int languageCodeIndex,
               int cimSensorFlag,
               int reservedField,
               int pinBlockFormat) {

        this.fitNumber = Integers.validateRange(fitNumber, 0, 999, FitNumberAppender.FIELD_NAME);
        this.institutionIndex = Integers.validateHexRangeOrExactValue(institutionIndex, 0, 0x7F, 0xFF,
                InstitutionIdIndexAppender.FIELD_NAME);
        this.institutionId = InstitutionIdAppender.validateInstitutionId(institutionId);
        this.indirectNextStateIndex = IndirectNextStateIndexAppender.validateNextStateIndex(indirectNextStateIndex);
        this.algorithmOrBankId = Integers.validateHexRangeOrExactValue(algorithmOrBankId, 0, 0x7F0, 0xFF,
                AlgorithmBankIdAppender.FIELD_NAME);
        this.maxPinDigitsEntered = ObjectUtils.validateNotNull(maxPinDigitsEntered, MaxPinDigitsEnteredAppender.FIELD_NAME);
        this.maxPinDigitsChecked = ObjectUtils.validateNotNull(maxPinDigitsChecked, MaxPinDigitsCheckedAppender.FIELD_NAME);
        this.pinPad = Integers.validateHexRange(pinPad, 0, 0xCF, PinPadAppender.FIELD_NAME);
        this.panDataIndex = Integers.validateHexRange(panDataIndex, 0, 0x7F, PanDataIndexAppender.FIELD_NAME);
        this.panDataLength = PanDataLengthAppender.validatePanDataLength(panDataLength, maxPinDigitsChecked);
        this.panPad = PanPadAppender.validatePanPad(panPad, pinPad, maxPinDigitsChecked);
        this.track3PinRetryCount = Track3PinAppender.validateTrack3PinRetryCount(track3PinRetryCount);
        this.pinOffsetIndex = Integers.validateHexRangeOrExactValue(pinOffsetIndex, 0, 0x7F, 0xFF,
                PinOffsetDataAppender.FIELD_NAME);
        this.decimalisationTable = DecimalisationTableAppender.validateDecimalisationTable(decimalisationTable);
        this.encryptedPinKey = ArrayUtils.validateLength(encryptedPinKey, 8, EncryptedPinKeyAppender.FIELD_NAME);
        this.indexReferencePoint = IndexReferencePointAppender.validateValue(indexReferencePoint);
        this.languageCodeIndex = Integers.validateHexRange(languageCodeIndex, 0x00, 0x7F,
                LanguageCodeIndexAppender.FIELD_NAME);
        this.cimSensorFlag = ByteUtils.validateIsWithinUnsignedRange(cimSensorFlag, "PMMSR (MM Sensor Flag)");
        this.reservedField = Integers.validateHexRange(reservedField, 0x00, 0xFF_FF_FF, "Reserved Field");
        this.pinBlockFormat = Integers.validateHexRange(pinBlockFormat, 0x00, 0x05, PinBlockFormatAppender.FIELD_NAME);
    }

    Fit(int fitNumber,
        int institutionIndex,
        long institutionId,
        int indirectNextStateIndex,
        int algorithmOrBankId,
        MaxPinDigitsEntered maxPinDigitsEntered,
        MaxPinDigitsChecked maxPinDigitsChecked,
        int pinPad,
        int panDataIndex,
        int panDataLength,
        int panPad,
        int track3PinRetryCount,
        int pinOffsetIndex,
        long decimalisationTable,
        byte[] encryptedPinKey,
        int indexReferencePoint,
        int languageCodeIndex,
        int cimSensorFlag,
        int reservedField,
        int pinBlockFormat,
        Void unused) {

        this.fitNumber = fitNumber;
        this.institutionIndex = institutionIndex;
        this.institutionId = institutionId;
        this.indirectNextStateIndex = indirectNextStateIndex;
        this.algorithmOrBankId = algorithmOrBankId;
        this.maxPinDigitsEntered = maxPinDigitsEntered;
        this.maxPinDigitsChecked = maxPinDigitsChecked;
        this.pinPad = pinPad;
        this.panDataIndex = panDataIndex;
        this.panDataLength = panDataLength;
        this.panPad = panPad;
        this.track3PinRetryCount = track3PinRetryCount;
        this.pinOffsetIndex = pinOffsetIndex;
        this.decimalisationTable = decimalisationTable;
        this.encryptedPinKey = encryptedPinKey;
        this.indexReferencePoint = indexReferencePoint;
        this.languageCodeIndex = languageCodeIndex;
        this.cimSensorFlag = cimSensorFlag;
        this.reservedField = reservedField;
        this.pinBlockFormat = pinBlockFormat;
    }

    public static FitBuilder builder() {
        return new FitBuilder();
    }

    public int getFitNumber() {
        return fitNumber;
    }

    public int getInstitutionIndex() {
        return institutionIndex;
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

    public int getPinOffsetIndex() {
        return pinOffsetIndex;
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

    @Override
    public String toNdcString() {
        final NdcStringBuilder builder = new NdcStringBuilder(STRING_BUILDER_CAPACITY + 3);
        return builder
                .appendZeroPadded(fitNumber, 3)
                .appendZeroPadded(institutionIndex, 3)
                .append(Fits.toThreeDigitDecimalString(institutionId, 5))
                .appendZeroPadded(indirectNextStateIndex, 3)
                .appendZeroPadded(algorithmOrBankId, 3)
                .appendComponent(maxPinDigitsEntered)
                .appendComponent(maxPinDigitsChecked)
                .appendZeroPadded(pinPad, 3)
                .appendZeroPadded(panDataIndex, 3)
                .appendZeroPadded(panDataLength, 3)
                .appendZeroPadded(panPad, 3)
                .appendZeroPadded(track3PinRetryCount, 3)
                .appendZeroPadded(pinOffsetIndex, 3)
                .append(Fits.toThreeDigitDecimalString(decimalisationTable, Long.BYTES))
                .append(ArrayUtils.toDecimalString(encryptedPinKey))
                .append(Fits.toThreeDigitDecimalString(indexReferencePoint, 3))
                .appendZeroPadded(languageCodeIndex, 3)
                .appendZeroPadded(cimSensorFlag, 3)
                .append(Fits.toThreeDigitDecimalString(reservedField, 3))
                .appendZeroPadded(pinBlockFormat, 3)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Fit.class.getSimpleName() + ": {", "}")
                .add("fitNumber: " + fitNumber)
                .add("institutionIndex: " + Integer.toHexString(institutionIndex).toUpperCase())
                .add("institutionId: " + Long.toHexString(institutionId).toUpperCase())
                .add("indirectNextStateIndex: " + Integer.toHexString(indirectNextStateIndex).toUpperCase())
                .add("algorithmOrBankId: " + Integer.toHexString(algorithmOrBankId).toUpperCase())
                .add("maxPinDigitsEntered: " + maxPinDigitsEntered)
                .add("maxPinDigitsChecked: " + maxPinDigitsChecked)
                .add("pinPad: " + Integer.toHexString(pinPad).toUpperCase())
                .add("panDataIndex: " + Integer.toHexString(panDataIndex).toUpperCase())
                .add("panDataLength: " + Integer.toHexString(panDataLength).toUpperCase())
                .add("panPad: " + Integer.toHexString(panPad).toUpperCase())
                .add("trackThreePinRetryCount: " + Integer.toHexString(track3PinRetryCount).toUpperCase())
                .add("pinOffsetIndex: " + Integer.toHexString(pinOffsetIndex).toUpperCase())
                .add("decimalisationTable: " + Long.toHexString(decimalisationTable).toUpperCase())
                .add("encryptedPinKey: " + Arrays.toString(encryptedPinKey))
                .add("indexReferencePoint: " + Integer.toHexString(indexReferencePoint).toUpperCase())
                .add("languageCodeIndex: " + Integer.toHexString(languageCodeIndex).toUpperCase())
                .add("cimSensorFlag: " + Integer.toHexString(cimSensorFlag).toUpperCase())
                .add("reservedTag: " + Integer.toHexString(reservedField).toUpperCase())
                .add("pinBlockFormat: " + Integer.toHexString(pinBlockFormat).toUpperCase())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fit fit = (Fit) o;
        return fitNumber == fit.fitNumber &&
                institutionIndex == fit.institutionIndex &&
                institutionId == fit.institutionId &&
                indirectNextStateIndex == fit.indirectNextStateIndex &&
                algorithmOrBankId == fit.algorithmOrBankId &&
                pinPad == fit.pinPad && panDataIndex == fit.panDataIndex &&
                panDataLength == fit.panDataLength &&
                panPad == fit.panPad &&
                track3PinRetryCount == fit.track3PinRetryCount &&
                pinOffsetIndex == fit.pinOffsetIndex &&
                indexReferencePoint == fit.indexReferencePoint &&
                languageCodeIndex == fit.languageCodeIndex &&
                cimSensorFlag == fit.cimSensorFlag &&
                reservedField == fit.reservedField &&
                pinBlockFormat == fit.pinBlockFormat &&
                decimalisationTable == fit.decimalisationTable &&
                Arrays.equals(encryptedPinKey, fit.encryptedPinKey) &&
                maxPinDigitsEntered.equals(fit.maxPinDigitsEntered) &&
                maxPinDigitsChecked.equals(fit.maxPinDigitsChecked);

    }

    @Override
    public int hashCode() {
        return Objects.hash(fitNumber, institutionIndex, institutionId, indirectNextStateIndex, algorithmOrBankId,
                maxPinDigitsEntered, maxPinDigitsChecked, pinPad, panDataIndex, panDataLength, panPad,
                track3PinRetryCount, pinOffsetIndex, decimalisationTable, Arrays.hashCode(encryptedPinKey), indexReferencePoint,
                languageCodeIndex, cimSensorFlag, reservedField, pinBlockFormat);
    }
}
