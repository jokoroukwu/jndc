package io.github.jokoroukwu.jndc.terminal.statusmessage.generic;

import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public final class GenericSolicitedStatusInformation implements SolicitedStatusInformation {
    private final String value;

    public GenericSolicitedStatusInformation(String value) {
        this.value = ObjectUtils.validateNotNull(value, "value");
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toNdcString() {
        return value;
    }
}
