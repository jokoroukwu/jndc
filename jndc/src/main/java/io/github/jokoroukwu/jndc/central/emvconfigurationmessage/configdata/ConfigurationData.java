package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigMessageSubClass;


public interface ConfigurationData extends NdcComponent {

    EmvConfigMessageSubClass getEmvConfigMessageSubClass();
}
