package io.github.jokoroukwu.jndc.central.transactionreply.functionid;

import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public interface CustomFunctionIdFactory {
    CustomFunctionIdFactory EMPTY = (value) -> DescriptiveOptional.empty(() -> value + " does not match any Function ID");

    DescriptiveOptional<FunctionId> getCustomFunctionId(char value);

}
