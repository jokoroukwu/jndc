package io.github.jokoroukwu.jndc;

import io.github.jokoroukwu.jndc.util.NdcConstants;

public final class ResponseFlag {
    private ResponseFlag() {
    }

    public static char validateResponseFlag(char responseFlag){
        if (responseFlag == NdcConstants.GROUP_SEPARATOR || responseFlag == NdcConstants.FIELD_SEPARATOR) {
            throw new IllegalArgumentException("'Response Flag' must not be a Group or Field separator character");
        }
        return responseFlag;
    }
}
