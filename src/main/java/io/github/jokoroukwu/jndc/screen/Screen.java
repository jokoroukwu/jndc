package io.github.jokoroukwu.jndc.screen;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Supplier;

public class Screen implements NdcComponent {
    public static final Set<Character> TWO_DIGIT_NUMBER_GROUPS = Set.of('A', 'C', 'd', 'e', 'G', 'I', 'i', 'j', 'K', 'L',
            'M', 'm', 'P', 'p', 'R', 'S', 's', 'T', 't', 'U');
    public static final Set<Character> FOUR_DIGIT_NUMBER_GROUPS = Set.of('E', 'X', 'Y', 'Z', 'u', 'l');

    private final String screenNumber;
    private final String screenData;

    public Screen(String screenNumber, String screenData) {
        this.screenNumber = validateScreenNumber(screenNumber);
        this.screenData = ObjectUtils.validateNotNull(screenData, "Screen Data");
    }

    public static DescriptiveOptional<String> readScreenNumber(NdcCharBuffer charBuffer) {
        final DescriptiveOptionalInt optionalFirstChar = charBuffer.tryReadNextChar();
        if (optionalFirstChar.isEmpty()) {
            return DescriptiveOptional.empty(optionalFirstChar::description);
        }
        final char firstChar = (char) optionalFirstChar.get();

        if (isDecimalChar(firstChar)) {
            return charBuffer.tryReadCharSequence(2)
                    .filter(Screen::isDecimalString, value -> onNonDecimal(firstChar, value))
                    .map(value -> firstChar + value);
        }
        if (isDoubleQuote(firstChar)) {
            return charBuffer.tryReadCharSequence(5)
                    .filter(value -> isDecimalString(value, 0, 4), value -> ()
                            -> "expected decimal value at position " + (charBuffer.position() - 5))
                    .filter(value -> isDoubleQuote(value.charAt(value.length() - 1)), value -> ()
                            -> "expected a double quote character at position " + (charBuffer.position() - 1))
                    .map(value -> firstChar + value);
        }

        if (FOUR_DIGIT_NUMBER_GROUPS.contains(firstChar)) {
            return charBuffer.tryReadCharSequence(4)
                    .filter(Screen::isDecimalString, value -> onNonDecimal(firstChar, value))
                    .map(value -> firstChar + value);
        }
        if (TWO_DIGIT_NUMBER_GROUPS.contains(firstChar)) {
            return charBuffer.tryReadCharSequence(2)
                    .filter(Screen::isDecimalString, value -> onNonDecimal(firstChar, value))
                    .map(value -> firstChar + value);
        }
        return DescriptiveOptional.empty(() -> String.format("invalid Screen Number character '%c' at position %d", firstChar,
                charBuffer.position() - 1));
    }

    public static boolean isScreenNumberValid(String screenNumber) {
        final char firstChar = screenNumber.charAt(0);
        if (isDecimalChar(firstChar)) {
            return Strings.isWithinCharRange(screenNumber, 1, '0', '9');
        }
        if (isDoubleQuote(firstChar)) {
            return screenNumber.length() == 5
                    && Strings.isWithinCharRange(screenNumber, 1, 4, '0', '9')
                    && isDoubleQuote(screenNumber.charAt(screenNumber.length() - 1));
        }

        if (FOUR_DIGIT_NUMBER_GROUPS.contains(firstChar)) {
            return screenNumber.length() == 6 && Strings.isWithinCharRange(screenNumber, 1, '0', '9');
        }
        if (TWO_DIGIT_NUMBER_GROUPS.contains(firstChar)) {
            return screenNumber.length() == 3 && Strings.isWithinCharRange(screenNumber, 1, '0', '9');
        }

        return false;
    }

    public static boolean isDecimalString(String value) {
        return Strings.isWithinCharRange(value, '0', '9');
    }

    public static boolean isDecimalString(String value, int from, int to) {
        return Strings.isWithinCharRange(value, from, to, '0', '9');
    }

    private static Supplier<String> onNonDecimal(char firstChar, String postfix) {
        return () -> String.format("'%c%s' is not within valid character range ('0'-'9')", firstChar, postfix);
    }

    public static boolean isDecimalChar(int character) {
        return character >= '0' && character <= '9';
    }

    public static boolean isDoubleQuote(int character) {
        return character == '\"';
    }

    private String validateScreenNumber(String value) {
        Strings.validateNotNullNotEmpty(value, "Screen Number");
        if (isScreenNumberValid(value)) {
            return value;
        }
        throw new IllegalArgumentException(value + " is not a valid Screen Number");
    }

    public String getScreenNumber() {
        return screenNumber;
    }

    public String getScreenData() {
        return screenData;
    }

    @Override
    public String toNdcString() {
        return screenNumber + screenData;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Screen.class.getSimpleName() + ": {", "}")
                .add("screenNumber: '" + screenNumber + '\'')
                .add("screenData: '" + screenData + '\'')
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Screen screen = (Screen) o;
        return screenNumber.equals(screen.screenNumber) && screenData.equals(screen.screenData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(screenNumber, screenData);
    }

}
