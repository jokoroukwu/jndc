package io.github.jokoroukwu.jndc.util.text.stringgenerator;

public interface IStringGenerator {


    String fixedLength(int stringLength);

    char randomChar();

    String randomLength(int min, int max);

    String strings(ILength numberOfSubstrings, ILength substringsLength, CharSequence delimiter);

    default String randomLength(int max) {
        return randomLength(1, max);
    }

    default String strings(ILength wordQuantity, ILength wordLength) {
        return strings(wordQuantity, wordLength, " ");
    }
}
