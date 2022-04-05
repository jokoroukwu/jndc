package io.github.jokoroukwu.jndc.field.fieldmeta;

import io.github.jokoroukwu.jndc.NdcCharBuffer;

import java.util.Optional;

public interface FieldMetaSkipStrategy {

    Optional<String> skipFieldMeta(NdcCharBuffer ndcCharBuffer);
}
