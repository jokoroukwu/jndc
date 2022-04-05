package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload;

import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.CustomisationDataCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.MessageId;
import io.github.jokoroukwu.jndc.util.CollectionUtils;
import io.github.jokoroukwu.jndc.util.NdcConstants;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class ScreenKeyboardLoadCommand extends CustomisationDataCommand {
    public static final String COMMAND_NAME = "Screen/Keyboard Data Load";

    private final List<ScreenKeyboardEntry> screenKeyboardEntries;


    public ScreenKeyboardLoadCommand(List<ScreenKeyboardEntry> screenKeyboardEntries) {
        super(MessageId.SCREEN_KEYBOARD_DATA);
        validateScreenKeyboard(screenKeyboardEntries);
        this.screenKeyboardEntries = screenKeyboardEntries;
    }

    private void validateScreenKeyboard(List<ScreenKeyboardEntry> screenKeyboardEntries) {
        if (CollectionUtils.isNullOrEmpty(screenKeyboardEntries)) {
            throw new IllegalArgumentException("Screen/Keyboard data must be present but actual: " + screenKeyboardEntries);
        }
    }


    public List<ScreenKeyboardEntry> getScreenKeyboardEntries() {
        return Collections.unmodifiableList(screenKeyboardEntries);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ScreenKeyboardLoadCommand.class.getSimpleName() + ": {", "}")
                .add("messageIdentifier: " + messageIdentifier)
                .add("screenAndKeyboardBlocks: " + screenKeyboardEntries)
                .toString();
    }

    @Override
    public String toNdcString() {
        return messageIdentifier.toNdcString() +
                NdcConstants.FIELD_SEPARATOR +
                screenKeyboardEntries.stream()
                        .map(ScreenKeyboardEntry::toNdcString)
                        .collect(Collectors.joining(NdcConstants.FIELD_SEPARATOR_STRING));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScreenKeyboardLoadCommand that = (ScreenKeyboardLoadCommand) o;
        return messageIdentifier == that.messageIdentifier && screenKeyboardEntries.equals(that.screenKeyboardEntries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageIdentifier, screenKeyboardEntries);
    }
}
