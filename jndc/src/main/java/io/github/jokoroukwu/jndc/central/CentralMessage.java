package io.github.jokoroukwu.jndc.central;

import io.github.jokoroukwu.jndc.NdcComponent;

public interface CentralMessage extends NdcComponent {

    CentralMessageClass getMessageClass();
}
