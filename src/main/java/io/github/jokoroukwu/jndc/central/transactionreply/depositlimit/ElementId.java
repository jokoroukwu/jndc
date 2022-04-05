package io.github.jokoroukwu.jndc.central.transactionreply.depositlimit;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum ElementId implements NdcComponent {
    AMOUNT_LIMIT(1, "001"),

    NOTE_LIMIT(2, "002");

    private final int id;
    private final String ndcString;

    ElementId(int id, String ndcString) {
        this.id = id;
        this.ndcString = ndcString;
    }

    public static DescriptiveOptional<ElementId> forValue(int value) {
        if (value == 1) {
            return DescriptiveOptional.of(AMOUNT_LIMIT);
        }
        if (value == 2) {
            return DescriptiveOptional.of(NOTE_LIMIT);
        }
        return DescriptiveOptional.empty(() -> " is not a valid 'Customer/Transaction Data Element Identifier'");
    }

    public static DescriptiveOptional<ElementId> forValue(String value) {
        return DescriptiveOptional.ofNullable(value, () -> "'Customer/Transaction Data Element Identifier' cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(ElementId::forValue);
    }


    public int getId() {
        return id;
    }

    @Override
    public String toNdcString() {
        return ndcString;
    }
}
