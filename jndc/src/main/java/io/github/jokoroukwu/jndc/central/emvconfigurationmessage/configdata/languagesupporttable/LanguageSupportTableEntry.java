package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.Languages;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public class LanguageSupportTableEntry implements NdcComponent {
    public static final int NDC_STRING_LENGTH = 14;
    private final String languageCode;
    private final int screenBase;
    private final int audioBase;
    private final int opcodeBufferPositions;
    private final String opCodeBufferValues;

    //  no-validation constructor;
    //  is used internally by the corresponding reader
    LanguageSupportTableEntry(String languageCode,
                              int screenBase,
                              int audioBase,
                              int opcodeBufferPositions,
                              String opCodeBufferValues,
                              Void empty) {
        this.languageCode = languageCode;
        this.screenBase = screenBase;
        this.audioBase = audioBase;
        this.opcodeBufferPositions = opcodeBufferPositions;
        this.opCodeBufferValues = opCodeBufferValues;
    }

    public LanguageSupportTableEntry(String languageCode,
                                     int screenBase,
                                     int audioBase,
                                     int opcodeBufferPositions,
                                     String opCodeBufferValues) {
        this.languageCode = Languages.validateIso639LanguageCode(languageCode);
        this.screenBase = Integers.validateRange(screenBase, 0, 999, "'Screen Base'");
        this.audioBase = Integers.validateRange(audioBase, 0, 7, "'Audio Base'");
        this.opcodeBufferPositions = validateBufferPositions(opcodeBufferPositions);
        this.opCodeBufferValues = validateOpCodeBufferValues(opCodeBufferValues);
    }

    public static LanguageSupportTableEntryBuilder builder() {
        return new LanguageSupportTableEntryBuilder();
    }

    public static boolean areBufferPositionsValid(int bufferPositions) {
        if (bufferPositions > 0 && bufferPositions < 765) {
            int copy = bufferPositions;
            int modOne = copy % 10;
            copy /= 10;
            int modTwo = copy % 10;
            if (modOne != modTwo && (modOne <= 7 && modTwo <= 7)) {
                copy /= 10;
                int modThree = copy % 10;
                return modThree != modOne && modThree != modTwo;
            }
        }
        return false;
    }

    public static boolean areOpcodeBufferValuesValid(String opcodeBufferValues) {
        ObjectUtils.validateNotNull(opcodeBufferValues, "'Opcode Buffer Values'");
        return opcodeBufferValues.length() == 3 &&
                isOpCodeBufferValueValid(opcodeBufferValues.charAt(0)) &&
                isOpCodeBufferValueValid(opcodeBufferValues.charAt(1)) &&
                isOpCodeBufferValueValid(opcodeBufferValues.charAt(2));
    }

    private static boolean isOpCodeBufferValueValid(char character) {
        return ((character >= 'A' && character <= 'D') || (character >= 'F' && character <= 'I') || character == '@');
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public int getScreenBase() {
        return screenBase;
    }

    public int getAudioBase() {
        return audioBase;
    }

    public int getOpcodeBufferPositions() {
        return opcodeBufferPositions;
    }

    public String getOpCodeBufferValues() {
        return opCodeBufferValues;
    }

    @Override
    public String toNdcString() {
        return String.format("%s%03d%03d%03d%s", languageCode, screenBase, audioBase, opcodeBufferPositions, opCodeBufferValues);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LanguageSupportTableEntry.class.getSimpleName() + ": {", "}")
                .add("languageCode: '" + languageCode + "'")
                .add("screenBase: " + screenBase)
                .add("audioBase: " + audioBase)
                .add("opcodeBufferPositions: " + opcodeBufferPositions)
                .add("opCodeBufferValues: '" + opCodeBufferValues + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LanguageSupportTableEntry that = (LanguageSupportTableEntry) o;
        return screenBase == that.screenBase &&
                audioBase == that.audioBase &&
                opcodeBufferPositions == that.opcodeBufferPositions &&
                languageCode.equals(that.languageCode) &&
                opCodeBufferValues.equals(that.opCodeBufferValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(languageCode, screenBase, audioBase, opcodeBufferPositions, opCodeBufferValues);
    }

    private int validateBufferPositions(int opcodeBufferPositions) {
        if (areBufferPositionsValid(opcodeBufferPositions)) {
            return opcodeBufferPositions;
        }
        final String errorMessage = "'Opcode Buffer Positions' should three distinct decimal digits in range 0-7 but was: ";
        throw new IllegalArgumentException(errorMessage + opcodeBufferPositions);
    }

    private String validateOpCodeBufferValues(String opCodeBufferValues) {
        if (areOpcodeBufferValuesValid(opCodeBufferValues)) {
            return opCodeBufferValues;
        }
        final String message = "'Opcode Buffer Values' should be three characters, each in range A-D or F-I or @ but was: '%s'";
        throw new IllegalArgumentException(String.format(message, opCodeBufferValues));
    }

}
