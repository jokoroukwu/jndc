package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.voiceguidance;

import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents Data ID '&lt;' of 'ci1' field of Transaction Request Message.
 * This field shows that voice guidance data is being reported.
 * The field is optional.
 */
public class VoiceGuidanceBuffer implements IdentifiableBuffer {
    public static final char ID = '<';
    public static final VoiceGuidanceBuffer EMPTY = new VoiceGuidanceBuffer(Strings.EMPTY_STRING);

    private final String languageId;

    public VoiceGuidanceBuffer(String languageId) {
        this.languageId = validateLanguageId(languageId);
    }

    public String getLanguageId() {
        return languageId;
    }

    @Override
    public char getId() {
        return ID;
    }

    @Override
    public String toNdcString() {
        return ID + languageId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VoiceGuidanceBuffer.class.getSimpleName() + ": {", "}")
                .add("id: '" + ID + "'")
                .add("languageId: '" + languageId + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoiceGuidanceBuffer that = (VoiceGuidanceBuffer) o;
        return languageId.equals(that.languageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, languageId);
    }

    private String validateLanguageId(String languageId) {
        if (languageId == null) {
            throw new IllegalArgumentException("'Voice guidance language identifier' may not be null");
        }
        final int length = languageId.length();
        if (length > 0 && length != 2) {
            final String message = "'Voice guidance language identifier' should be 2 characters long when present but was: ";
            throw new IllegalArgumentException(message + languageId);
        }
        return languageId;
    }
}
