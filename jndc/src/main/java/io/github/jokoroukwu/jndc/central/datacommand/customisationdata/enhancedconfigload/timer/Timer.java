package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.timer;

import io.github.jokoroukwu.jndc.NdcComponent;

public interface Timer extends NdcComponent {

    int getNumber();

    int getDurationSeconds();
}
