package io.github.jokoroukwu.jndc;

import io.github.jokoroukwu.jndc.exception.CharSequenceMismatchException;
import io.github.jokoroukwu.jndc.exception.IncompleteNdcMessageException;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.Longs;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalLong;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.nio.CharBuffer;
import java.util.Optional;

public class NdcCharBuffer {
    public static final NdcCharBuffer EMPTY = NdcCharBuffer.wrap(Strings.EMPTY_STRING);
    private final CharBuffer charBuffer;

    private NdcCharBuffer(CharBuffer charBuffer) {
        this.charBuffer = ObjectUtils.validateNotNull(charBuffer, "charBuffer");
    }

    public static NdcCharBuffer wrap(CharBuffer charBuffer) {
        ObjectUtils.validateNotNull(charBuffer, "charBuffer");
        return new NdcCharBuffer(charBuffer);
    }

    public static NdcCharBuffer wrap(CharSequence charSequence) {
        ObjectUtils.validateNotNull(charSequence, "charSequence");
        return new NdcCharBuffer(CharBuffer.wrap(charSequence));
    }

    public static NdcCharBuffer wrap(char[] characters) {
        ObjectUtils.validateNotNull(characters, "characters");
        return new NdcCharBuffer(CharBuffer.wrap(characters));
    }

    public int position() {
        return charBuffer.position();
    }

    public String readCharSequence(int charLength) {
        if (!hasRemaining(charLength)) {
            var template = "expected %d character(s), starting at position %d but %d remaining";
            throw new IncompleteNdcMessageException(String.format(template, charLength, charBuffer.position(), charBuffer.remaining()));
        }
        var chars = new char[charLength];
        charBuffer.get(chars);
        return new String(chars);
    }

    public DescriptiveOptionalLong tryReadLong(int charLength, int radix) {
        return tryReadCharSequence(charLength)
                .flatMapToLong(value -> Longs.tryParseLong(value, radix))
                //  there are no negative numbers in NDC messages
                .filter(Longs::isNotNegative, value -> ()
                        -> String.format("expected a non-negative value but was %d at position %d",
                        value, charBuffer.position() - charLength));
    }

    public DescriptiveOptionalLong tryReadLong(int charLength) {
        return tryReadLong(charLength, 10);
    }

    public DescriptiveOptionalLong tryReadHexLong(int charLength) {
        return tryReadLong(charLength, 16);
    }

    public DescriptiveOptionalInt tryReadInt(int charLength, int radix) {
        return tryReadCharSequence(charLength)
                .flatMapToInt(value -> Integers.tryParseInt(value, radix))
                //  there are no negative numbers in NDC messages
                .filter(Integers::isNotNegative, value -> () -> String.format("expected a non-negative value but was %d at position %d",
                        value, charBuffer.position() - charLength));
    }

    public DescriptiveOptionalInt tryReadInt(int charLength) {
        return tryReadInt(charLength, 10);
    }

    public DescriptiveOptionalInt tryReadHexInt(int charLength) {
        return tryReadInt(charLength, 16);
    }

    public DescriptiveOptional<String> tryReadCharSequence(int charLength) {
        if (!hasRemaining(charLength)) {
            var template = "expected %d character(s) at position %d but %d remaining";
            return DescriptiveOptional.empty(() ->
                    String.format(template, charLength, charBuffer.position(), charBuffer.remaining()));
        }
        var chars = new char[charLength];
        charBuffer.get(chars);
        return DescriptiveOptional.of(new String(chars));
    }

    public DescriptiveOptional<String> tryReadCharSequenceMatching(String matcher) {
        ObjectUtils.validateNotNull(matcher, "matcher");
        var length = matcher.length();
        return tryReadCharSequence(length)
                .filter(matcher::equals, value -> () -> String.format("expected subsequence '%s' at position %d but found '%s'",
                        matcher, charBuffer.position() - length, value));
    }

    public DescriptiveOptionalInt tryReadNextChar() {
        if (!charBuffer.hasRemaining()) {
            var messageTemplate = "expected a character but none remaining at position %d";
            return DescriptiveOptionalInt.empty(() -> String.format(messageTemplate, charBuffer.position()));
        }
        return DescriptiveOptionalInt.of(charBuffer.get());
    }

    public DescriptiveOptionalInt tryReadNextCharMatching(char matcher) {
        return tryReadNextChar()
                .filter(value -> matcher == value, value -> () -> String.format("expected character '%c' but found '%c' at position %d",
                        matcher, value, charBuffer.position() - 1));
    }

    public char readNextChar() {
        if (!charBuffer.hasRemaining()) {
            final String messageTemplate = "expected a character but none remaining at position %d";
            throw new IncompleteNdcMessageException(String.format(messageTemplate, charBuffer.position()));
        }
        return charBuffer.get();
    }

    public char getCharAt(int offset) {
        final int remaining = charBuffer.remaining();
        if (remaining > offset) {
            return charBuffer.charAt(offset);
        }
        final String messageTemplate = "expected a character at position %d but only %d character(s) remaining";
        throw new IncompleteNdcMessageException(messageTemplate, charBuffer.position() + offset, remaining);
    }

    public DescriptiveOptionalInt tryGetCharAt(int offset) {
        final int remaining = charBuffer.remaining();
        if (remaining > offset) {
            return DescriptiveOptionalInt.of(charBuffer.charAt(offset));
        }
        final String messageTemplate = "expected a character at position %d but only %d character(s) remaining";
        return DescriptiveOptionalInt.empty(() -> String.format(messageTemplate, charBuffer.position() + offset, remaining));
    }

    public char readNextCharMatching(char matcher) {
        final char nextChar = readNextChar();
        if (matcher != nextChar) {
            var template = "expected a character '%c' but got '%c' at position %d";
            throw new CharSequenceMismatchException(template, matcher, nextChar, charBuffer.position());
        }
        return nextChar;
    }

    public boolean hasNextCharMatching(char matcher) {
        return charBuffer.hasRemaining() && charBuffer.charAt(0) == matcher;
    }

    public boolean hasFollowingGroupSeparator() {
        return hasNextCharMatching(NdcConstants.GROUP_SEPARATOR);
    }

    public boolean hasFollowingFieldSeparator() {
        return hasNextCharMatching(NdcConstants.FIELD_SEPARATOR);
    }

    public void skip(int count) {
        if (count > 0) {
            if (!hasRemaining(count)) {
                var messageTemplate = "expected at least %d character(s) but none remaining at position %d";
                throw new IncompleteNdcMessageException(messageTemplate, count, charBuffer.position());
            }
            charBuffer.position(charBuffer.position() + count);
        }
    }

    public boolean hasRemaining(int length) {
        return charBuffer.remaining() >= length;
    }

    public boolean hasRemaining() {
        return charBuffer.hasRemaining();
    }

    public int remaining() {
        return charBuffer.remaining();
    }

    public Optional<String> trySkipFieldSeparator() {
        return trySkipNextChar(NdcConstants.FIELD_SEPARATOR);
    }

    public Optional<String> trySkipGroupSeparator() {
        return trySkipNextChar(NdcConstants.GROUP_SEPARATOR);
    }

    public void skipGroupSeparator() {
        skipChar(NdcConstants.GROUP_SEPARATOR);
    }

    public Optional<String> trySkipNextChar(char character) {
        if (!charBuffer.hasRemaining()) {
            final String messageTemplate = "expected a character '%c' but no characters remaining at position %d";
            return Optional.of(String.format(messageTemplate, character, charBuffer.position()));
        }
        final char nextChar = charBuffer.get();
        if (nextChar != character) {
            final String messageTemplate = "expected a character '%c' but got '%c' at position %d";
            return Optional.of(String.format(messageTemplate, character, nextChar, charBuffer.position() - 1));
        }
        return Optional.empty();
    }

    public Optional<String> trySkip(int numberOfChars) {
        if (hasRemaining(numberOfChars)) {
            charBuffer.position(charBuffer.position() + numberOfChars);
            return Optional.empty();
        }
        final String messageTemplate = "expected at least %d character(s) but got %d remaining at position %d";
        return Optional.of(String.format(messageTemplate, numberOfChars, charBuffer.remaining(), charBuffer.position()));
    }

    public void skipFieldSeparator() {
        skipChar(NdcConstants.FIELD_SEPARATOR);
    }

    public void skipChar(char character) {
        readNextCharMatching(character);
    }

    public Optional<String> trySkipNextSubsequence(String matcher) {
        ObjectUtils.validateNotNull(matcher, "matcher");
        var length = matcher.length();
        final int remaining = charBuffer.remaining();
        if (remaining < length) {
            final String errorMessage = "expected subsequence '%s' at position %d but only %d characters remained";
            return Optional.of(String.format(errorMessage, matcher, charBuffer.position(), remaining));
        }
        final String subsequence = readCharSequence(length);
        if (matcher.equals(subsequence)) {
            return Optional.empty();
        }
        final String errorMessage = "expected subsequence '%s' at position %d but found '%s'";
        return Optional.of(String.format(errorMessage, matcher, charBuffer.position() - length, subsequence));
    }


    public NdcCharBuffer subBuffer() {
        if (!charBuffer.hasRemaining()) {
            return EMPTY;
        }
        final char[] remainingCharacters = new char[charBuffer.remaining()];
        charBuffer.get(remainingCharacters);
        return NdcCharBuffer.wrap(remainingCharacters);
    }

    public NdcCharBuffer subBuffer(char delimiter) {
        if (!charBuffer.hasRemaining()) {
            return EMPTY;
        }
        final char[] characters = new char[charBuffer.remaining()];
        int charsConsumed = 0;
        do {
            characters[charsConsumed++] = charBuffer.get();
        } while (charBuffer.hasRemaining() && charBuffer.charAt(0) != delimiter);
        return new NdcCharBuffer(CharBuffer.wrap(characters, 0, charsConsumed));
    }

    public boolean hasFieldDataRemaining() {
        if (charBuffer.hasRemaining()) {
            final char nextChar = charBuffer.charAt(0);
            return nextChar != NdcConstants.FIELD_SEPARATOR && nextChar != NdcConstants.GROUP_SEPARATOR;
        }
        return false;
    }

    @Override
    public String toString() {
        var position = charBuffer.position();
        var data = charBuffer.rewind().toString();
        charBuffer.position(position);
        return data;
    }
}
