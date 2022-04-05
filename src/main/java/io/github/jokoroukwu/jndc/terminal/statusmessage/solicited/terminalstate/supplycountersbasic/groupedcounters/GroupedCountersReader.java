package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.groupedcounters;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum GroupedCountersReader implements ConfigurableNdcComponentReader<DescriptiveOptional<GroupedCounterValues>> {
    INSTANCE;

    @Override
    public DescriptiveOptional<GroupedCounterValues> readComponent(NdcCharBuffer ndcCharBuffer, DeviceConfiguration deviceConfiguration) {
        final List<Integer> values = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            final DescriptiveOptionalInt optionalValue = ndcCharBuffer.tryReadInt(5);
            if (optionalValue.isEmpty()) {
                final int group = i + 1;
                return DescriptiveOptional.empty(() -> String.format("group %d value: %s", group, optionalValue.description()));
            }
            values.add(optionalValue.get());
        }
        return DescriptiveOptional.of(new GroupedCounterValues(Collections.unmodifiableList(values)));
    }

}
