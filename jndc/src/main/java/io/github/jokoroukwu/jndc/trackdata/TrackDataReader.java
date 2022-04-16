package io.github.jokoroukwu.jndc.trackdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public class TrackDataReader implements NdcComponentReader<DescriptiveOptional<String>> {
    private final int maxDataLength;
    private final int lowerCharBound;
    private final int upperCharBound;

    public TrackDataReader(int maxDataLength, int lowerCharBound, int upperCharBound) {
        this.maxDataLength = Integers.validateMinValue(maxDataLength, 1, "Max data length");
        validateBoundaries(lowerCharBound, upperCharBound);
        this.lowerCharBound = lowerCharBound;
        this.upperCharBound = upperCharBound;
    }

    @Override
    public DescriptiveOptional<String> readComponent(NdcCharBuffer ndcCharBuffer) {
        int charsConsumed = 0;
        final char[] data = new char[maxDataLength];
        do {
            final char nextChar = ndcCharBuffer.readNextChar();
            if (nextChar < lowerCharBound || nextChar > upperCharBound) {
                final String template = "character '%s' is not within valid character range (%#X-%#X ASCII) at position %d";
                return DescriptiveOptional.empty(()
                        -> String.format(template, nextChar, lowerCharBound, upperCharBound, ndcCharBuffer.position() - 1));
            }
            data[charsConsumed++] = nextChar;
        } while (charsConsumed < maxDataLength && ndcCharBuffer.hasFieldDataRemaining());

        return DescriptiveOptional.of(String.valueOf(data, 0, charsConsumed));
    }

    private void validateBoundaries(int lower, int upper) {
        Integers.validateMinValue(lower, 0, "lower bound");
        Integers.validateMinValue(lower, 1, "upper bound");
        if (upper < lower) {
            final String errorMessage = String.format("Lower character bound (%#X) is greater than upper bound (%#X)",
                    lower, upper);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
