package io.github.jokoroukwu.jndc.trailingdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;

import java.util.Optional;

public interface TrailingDataChecker {

    Optional<String> getErrorMessageOnTrailingData(NdcCharBuffer ndcCharBuffer);
}
