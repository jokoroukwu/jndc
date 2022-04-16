package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload;

import io.github.jokoroukwu.jndc.central.datacommand.DataCommand;

public interface FitDataLoadCommandListener {

    void onFitDataLoadCommand(DataCommand<FitDataLoadCommand> fitDataLoadMessage);
}
