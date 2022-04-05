package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.statetableload;

import io.github.jokoroukwu.jndc.central.datacommand.DataCommand;

public interface StateTablesLoadCommandListener {

    void onStateTablesLoadCommand(DataCommand<StateTablesLoadCommand> stateTablesLoadMessage);
}
