package io.github.jokoroukwu.jndc.mac;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public interface MacReader {
    String FIELD_NAME = "'Message Authentication Code (MAC)'";

    DescriptiveOptional<String> tryReadMac(NdcCharBuffer ndcCharBuffer);
}
