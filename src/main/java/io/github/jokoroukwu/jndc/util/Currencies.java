package io.github.jokoroukwu.jndc.util;

import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public final class Currencies {
    public static final Set<String> ISO_4217_CODES = Currency.getAvailableCurrencies()
            .stream()
            .map(Currency::getCurrencyCode)
            .collect(Collectors.toUnmodifiableSet());

    private Currencies() {
    }

    public static String validateIso4217CurrencyCode(String currencyCode) {
        if (isValidIso4217CurrencyCode(currencyCode)) {
            return currencyCode;
        }
        throw new IllegalArgumentException(currencyCode + " is not a valid ISO 4217 currency code");
    }

    public static boolean isValidIso4217CurrencyCode(String code) {
        return ISO_4217_CODES.contains(code);
    }
}
