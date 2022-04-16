package io.github.jokoroukwu.jndc.central.transactionreply.screendisplayupdate;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.screen.Screen;
import io.github.jokoroukwu.jndc.util.Chars;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class ScreenDisplayUpdate implements NdcComponent {
    private final String screenNumber;
    private final List<Screen> screens;

    ScreenDisplayUpdate(String screenNumber, List<Screen> screens, Void unused) {
        this.screenNumber = screenNumber;
        this.screens = screens;
    }

    public ScreenDisplayUpdate(String screenNumber, List<Screen> screens) {
        this.screenNumber = validateScreenNumber(screenNumber);
        this.screens = List.copyOf(screens);
    }

    public ScreenDisplayUpdate(String screenNumber) {
        this(screenNumber, List.of());
    }

    public String getScreenNumber() {
        return screenNumber;
    }

    public List<Screen> getScreens() {
        return screens;
    }


    @Override
    public String toNdcString() {
        return new NdcStringBuilder(screenNumber.length() + (64 * screens.size()) + (5 * screens.size()))
                .append(screenNumber)
                //  0000 stands for reserved bytes
                .appendComponents(screens, NdcConstants.GROUP_SEPARATOR + "0000")
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ScreenDisplayUpdate.class.getSimpleName() + ": {", "}")
                .add("screenNumber: '" + screenNumber + '\'')
                .add("screens: " + screens)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScreenDisplayUpdate that = (ScreenDisplayUpdate) o;
        return screenNumber.equals(that.screenNumber) && screens.equals(that.screens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(screenNumber, screens);
    }

    private String validateScreenNumber(String screenNumber) {
        ObjectUtils.validateNotNull(screenNumber, "'Screen Number'");

        if (isValidDecimalScreenNumber(screenNumber)) {
            return screenNumber;
        }
        if (isValidGroupPrefixedScreenNumber(screenNumber)) {
            return screenNumber;
        }
        final String errorMessage = "'Screen Number' should either be a 3-digit decimal value in range 010-999 "
                + "or a 4-digit decimal value prefixed by 'u' or 'l' but was: " + screenNumber;
        throw new IllegalArgumentException(errorMessage);
    }

    private boolean isValidGroupPrefixedScreenNumber(String screenNumber) {
        final char firstChar = screenNumber.charAt(0);
        return (firstChar == 'u' || firstChar == 'l')
                && screenNumber.length() == 5
                && Strings.isWithinCharRange(screenNumber, 1, '0', '9');
    }

    private boolean isValidDecimalScreenNumber(String screenNumber) {
        return screenNumber.length() == 3
                && Chars.isInRange(screenNumber.charAt(0), '0', '9')
                && screenNumber.charAt(1) != '0'
                && Chars.isInRange(screenNumber.charAt(2), '0', '9');
    }
}
