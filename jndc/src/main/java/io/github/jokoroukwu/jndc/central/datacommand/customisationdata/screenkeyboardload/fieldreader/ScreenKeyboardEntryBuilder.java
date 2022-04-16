package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.fieldreader;

import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.ScreenKeyboardEntry;
import io.github.jokoroukwu.jndc.screen.Screen;
import io.github.jokoroukwu.jndc.screen.ScreenAcceptor;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;

public final class ScreenKeyboardEntryBuilder implements ScreenAcceptor<ScreenKeyboardEntryBuilder> {
    private Screen screen;
    private KeyboardData keyboardData;
    private String touchScreenData = Strings.EMPTY_STRING;
    private String nestedKeyboardData = Strings.EMPTY_STRING;
    private String miscKeyboardData = Strings.EMPTY_STRING;

    public ScreenKeyboardEntryBuilder withScreen(Screen screen) {
        this.screen = screen;
        return this;
    }

    public ScreenKeyboardEntryBuilder withKeyboardData(KeyboardData keyboardData) {
        this.keyboardData = keyboardData;
        return this;
    }

    public ScreenKeyboardEntryBuilder withTouchScreenData(String touchScreenData) {
        this.touchScreenData = ObjectUtils.validateNotNull(touchScreenData, "touchScreenData");
        return this;
    }

    public ScreenKeyboardEntryBuilder withNestedKeyboardData(String nestedKeyboardData) {
        this.nestedKeyboardData = ObjectUtils.validateNotNull(nestedKeyboardData, "nestedKeyboardData");
        return this;
    }

    public ScreenKeyboardEntryBuilder withMiscKeyboardData(String miscKeyboardData) {
        this.miscKeyboardData = ObjectUtils.validateNotNull(miscKeyboardData, "miscKeyboardData");
        return this;
    }

    public ScreenKeyboardEntry build() {
        return new ScreenKeyboardEntry(screen, keyboardData, touchScreenData, nestedKeyboardData, miscKeyboardData);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScreenKeyboardEntryBuilder that = (ScreenKeyboardEntryBuilder) o;
        return Objects.equals(screen, that.screen) &&
                Objects.equals(keyboardData, that.keyboardData) &&
                touchScreenData.equals(that.touchScreenData) &&
                nestedKeyboardData.equals(that.nestedKeyboardData) &&
                miscKeyboardData.equals(that.miscKeyboardData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(screen,
                keyboardData,
                touchScreenData,
                nestedKeyboardData,
                miscKeyboardData);
    }
}
