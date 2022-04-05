package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.smartcarddata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.tlv.BerTlv;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents Smart Card data field.
 * The field is optional.
 */
public final class SmartCardData implements NdcComponent {
    private final int camFlags;
    private final String smartCardDataId;
    private final List<BerTlv<String>> emvDataObjects;

    public SmartCardData(int camFlags, String smartCardDataId, Collection<BerTlv<String>> emvDataObjects) {
        this.camFlags = Integers.validateMaxValue(camFlags, 0xFF_FF, "'CAM Flags'");
        ObjectUtils.validateNotNull(smartCardDataId, "'Smart card data identifier'");
        Integers.validateIsExactValue(smartCardDataId.length(), 3, "'Smart card data identifier' length");
        this.smartCardDataId = Strings.validateLength(smartCardDataId, 3, "'Smart Card Data Identifier'");
        this.emvDataObjects = List.copyOf(emvDataObjects);
    }

    public SmartCardData(int camFlags, Collection<BerTlv<String>> emvDataObjects) {
        this(camFlags, "CAM", emvDataObjects);
    }

    SmartCardData(int camFlags, String smartCardDataId, List<BerTlv<String>> emvDataObjects) {
        this.camFlags = camFlags;
        this.smartCardDataId = smartCardDataId;
        this.emvDataObjects = emvDataObjects;
    }

    public String getSmartCardDataId() {
        return smartCardDataId;
    }

    public int getCamFlags() {
        return camFlags;
    }

    /**
     * Checks whether bit 6 of the first byte of CAM flags is NOT set.
     *
     * @return true if not set i.e. Full CAM processing / false otherwise i.e. Partial CAM processing
     */
    public boolean hasFullCamProcessing() {
        return (camFlags & 0b00100000) == 0;
    }

    /**
     * Checks whether bit 5 of the first byte of CAM flags is set.
     *
     * @return true if set / false otherwise
     */
    public boolean isTransactionDeclinedOffline() {
        return (camFlags & 0b00010000) != 0;
    }

    /**
     * Checks whether bit 4 of the first byte of CAM flags is NOT set.
     *
     * @return true if not set / false otherwise
     */
    public boolean isAppDataRetrievalSuccessful() {
        return (camFlags & 0b00001000) == 0;
    }

    /**
     * Checks whether bit 3 of the first byte of CAM flags is NOT set.
     *
     * @return true if not set / false otherwise
     */
    public boolean isGetProcessingOptionsSuccessful() {
        return (camFlags & 0b00000100) == 0;
    }

    /**
     * Checks whether bit 2 of the first byte of CAM flags is NOT set.
     *
     * @return true if NOT set / false otherwise
     */
    public boolean isAppSelectionSuccessful() {
        return (camFlags & 0b00000010) == 0;
    }

    /**
     * Checks whether bit 8 of the second byte of CAM flags is NOT set.
     *
     * @return true if not set / false otherwise
     */
    public boolean isPdolDataValid() {
        return (camFlags >> Byte.SIZE & 0b10000000) == 0;
    }

    /**
     * Checks whether bit 7 of the second byte of CAM flags is NOT set.
     *
     * @return true if not set / false otherwise
     */
    public boolean isCdolDataValid() {
        return (camFlags >> Byte.SIZE & 0b01000000) == 0;
    }

    /**
     * Checks whether bit 6 of the second byte of CAM flags is NOT set.
     *
     * @return true if not set / false otherwise
     */
    public boolean isAcGenerationSuccessful() {
        return (camFlags >> Byte.SIZE & 0b00100000) == 0;
    }

    /**
     * Checks whether bit 4 of the second byte of CAM flags is set.
     *
     * @return true if set / false otherwise
     */
    public boolean isPreviousCamProcessingSuccessful() {
        return (camFlags >> Byte.SIZE & 0b00001000) != 0;
    }

    /**
     * Checks whether bit 3 of the second byte of CAM flags is set.
     *
     * @return true if set / false otherwise
     */
    public boolean isProcessingInitiated() {
        return (camFlags >> Byte.SIZE & 0b00000100) != 0;
    }

    /**
     * Checks bit 2 of the second byte of CAM flags
     * to determine the mode of contactless transaction.
     *
     * @return true if the bit is set, i.e. the mode id EMV-like or
     * false when mode of contactless transaction is Magnetic Stripe‐Like.
     */
    public boolean isEmvTransactionModeUsed() {
        return (camFlags >> Byte.SIZE & 0b00000010) != 0;
    }

    /**
     * Checks bit 1 of the second byte of CAM flags to determine
     * the type of the card reader used to perform transaction.
     *
     * @return true if the bit is set, i.e. transaction is performed using a contactless card reader
     * or false when contact card reader is used.
     */
    public boolean isContactlessReaderUsed() {
        return (camFlags >> Byte.SIZE & 0b00000001) != 0;
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(emvDataObjects.size() * 10 + 8)
                .append(smartCardDataId)
                .appendZeroPaddedHex(camFlags, 4)
                .appendComponents(emvDataObjects, Strings.EMPTY_STRING)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SmartCardData.class.getSimpleName() + ": {", "}")
                .add("camFlags: " + Strings.leftPad(Integer.toBinaryString(camFlags), "0", 16))
                .add("smartCardDataId: '" + smartCardDataId + '\'')
                .add("emvDataObjects: " + emvDataObjects)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmartCardData that = (SmartCardData) o;
        return camFlags == that.camFlags &&
                smartCardDataId.equals(that.smartCardDataId) &&
                emvDataObjects.equals(that.emvDataObjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(camFlags, smartCardDataId, emvDataObjects);
    }
}
