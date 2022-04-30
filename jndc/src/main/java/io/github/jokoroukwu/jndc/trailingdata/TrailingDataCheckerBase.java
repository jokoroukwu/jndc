package io.github.jokoroukwu.jndc.trailingdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;

import java.util.Optional;

public enum TrailingDataCheckerBase implements TrailingDataChecker {

    INSTANCE;

    @Override
    public Optional<String> getErrorMessageOnTrailingData(NdcCharBuffer ndcCharBuffer) {
        if (ndcCharBuffer.hasRemaining()) {
            final int position = ndcCharBuffer.position();
            final String remainingData = ndcCharBuffer.subBuffer().toString();
            return Optional.of(String.format("unexpected trailing data at position %d: %s", position, remainingData));
        }
        return Optional.empty();
    }
}
