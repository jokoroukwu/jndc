package io.github.jokoroukwu.jndc.util;

import java.util.Locale;
import java.util.Set;

public final class Languages {
    public static final Set<String> ISO_639_LANGUAGE_CODES = Set.of(Locale.getISOLanguages());

    private Languages() {
    }

    public static String validateIso639LanguageCode(String languageCode) {
        ObjectUtils.validateNotNull(languageCode, "'Language Code'");
        if (ISO_639_LANGUAGE_CODES.contains(languageCode)) {
            return languageCode;
        }
        throw new IllegalArgumentException(String.format("'%s' is not a valid ISO-639 language code", languageCode));
    }

}
