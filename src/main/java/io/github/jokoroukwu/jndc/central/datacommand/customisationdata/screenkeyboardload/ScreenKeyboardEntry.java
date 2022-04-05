package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.fieldreader.KeyboardData;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.fieldreader.ScreenKeyboardEntryBuilder;
import io.github.jokoroukwu.jndc.screen.Screen;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScreenKeyboardEntry implements NdcComponent {
    private final Screen screen;
    private final KeyboardData keyboardData;
    private final String touchScreenData;
    private final String nestedKeyboardData;
    private final String miscKeyboardData;

    public ScreenKeyboardEntry(Screen screen, KeyboardData keyboardData, String touchScreenData, String nestedKeyboardData, String miscKeyboardData) {
        validateFields(screen, keyboardData, touchScreenData, nestedKeyboardData, miscKeyboardData);
        this.screen = screen;
        this.keyboardData = keyboardData;
        this.touchScreenData = touchScreenData;
        this.nestedKeyboardData = nestedKeyboardData;
        this.miscKeyboardData = miscKeyboardData;
    }

    public static ScreenKeyboardEntryBuilder builder() {
        return new ScreenKeyboardEntryBuilder();
    }

    private void validateFields(Screen screen, KeyboardData keyboardData, String touchScreenData, String nestedKeyboardData, String miscKeyboardData) {
        ObjectUtils.validateNotNull(touchScreenData, "touchScreenData");
        ObjectUtils.validateNotNull(nestedKeyboardData, "nestedKeyboardData");
        ObjectUtils.validateNotNull(miscKeyboardData, "miscKeyboardData");
        final boolean notValid = screen == null &&
                keyboardData == null &&
                Stream.of(touchScreenData, nestedKeyboardData, miscKeyboardData).allMatch(String::isEmpty);
        if (notValid) {
            throw new IllegalArgumentException("Screen and/or Keyboard data must be present but all fields are empty");
        }
    }

    public Optional<Screen> getScreen() {
        return Optional.ofNullable(screen);
    }

    public Optional<KeyboardData> getKeyboardData() {
        return Optional.ofNullable(keyboardData);
    }

    public String getTouchScreenData() {
        return touchScreenData;
    }

    public String getNestedKeyboardData() {
        return nestedKeyboardData;
    }

    public String getMiscKeyboardData() {
        return miscKeyboardData;
    }

    @Override
    public String toNdcString() {
        final String screenString = screen != null ? screen.toNdcString() : Strings.EMPTY_STRING;
        final String keyboardString = keyboardData != null ? keyboardData.toNdcString() : Strings.EMPTY_STRING;
        return Stream.of(screenString, keyboardString, touchScreenData, nestedKeyboardData, miscKeyboardData)
                .filter(Predicate.not(String::isEmpty))
                .collect(Collectors.joining(NdcConstants.GROUP_SEPARATOR_STRING));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ScreenKeyboardEntry.class.getSimpleName() + ": {", "}")
                .add("screen: " + screen)
                .add("keyboardData: " + keyboardData)
                .add("touchScreenData: '" + touchScreenData + "'")
                .add("nestedKeyboardData: '" + nestedKeyboardData + "'")
                .add("miscKeyboardData: '" + miscKeyboardData + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScreenKeyboardEntry that = (ScreenKeyboardEntry) o;
        return Objects.equals(screen, that.screen) &&
                Objects.equals(keyboardData, that.keyboardData) &&
                touchScreenData.equals(that.touchScreenData) &&
                nestedKeyboardData.equals(that.nestedKeyboardData) &&
                miscKeyboardData.equals(that.miscKeyboardData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(screen, keyboardData, touchScreenData, nestedKeyboardData, miscKeyboardData);
    }
}
