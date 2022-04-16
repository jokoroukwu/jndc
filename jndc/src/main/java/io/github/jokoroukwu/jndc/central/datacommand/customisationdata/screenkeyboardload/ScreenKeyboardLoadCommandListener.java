package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload;

import io.github.jokoroukwu.jndc.central.datacommand.DataCommand;

public interface ScreenKeyboardLoadCommandListener {

    void onScreenKeyboardLoadCommand(DataCommand<ScreenKeyboardLoadCommand> screenKeyboardLoadMessage);
}
