package io.github.jokoroukwu.jndc.tlv;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;

public interface BerTlvReader {

    DescriptiveOptionalInt tryReadTag(NdcCharBuffer ndcCharBuffer);

    DescriptiveOptionalInt tryReadLength(NdcCharBuffer buffer);
}
