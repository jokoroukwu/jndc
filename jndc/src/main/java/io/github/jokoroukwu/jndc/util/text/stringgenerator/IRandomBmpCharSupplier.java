package io.github.jokoroukwu.jndc.util.text.stringgenerator;

import java.util.Random;

@FunctionalInterface
public interface IRandomBmpCharSupplier {

    char getChar(Random random);

}
