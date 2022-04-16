package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.currencydataobjectstable;

import java.util.Optional;

public enum CurrencyEntryValidator {
    INSTANCE;

    public Optional<String> validateCurrencyType(int value) {
        if (value < 1 || value > 0xFF) {
            return Optional.of(value + " is not withing valid 'Currency Type' range (01-FF hex)");
        }
        return Optional.empty();
    }
}
