package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.datetimeload;

import io.github.jokoroukwu.jndc.central.datacommand.DataCommand;

public interface DateTimeLoadCommandListener {
    void onDateTimeLoadCommand(DataCommand<DateTimeLoadCommand> dateTimeLoadMessage);
}
