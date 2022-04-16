package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable;

import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public final class LanguageSupportTableEntryBuilder {
    private String languageCode;
    private int screenBase;
    private int audioBase;
    private int opcodeBufferPositions;
    private String opCodeBufferValues;

    public LanguageSupportTableEntryBuilder withLanguageCode(String languageCode) {
        this.languageCode = ObjectUtils.validateNotNull(languageCode, "languageCode");
        return this;
    }

    public LanguageSupportTableEntryBuilder withScreenBase(int screenBase) {
        this.screenBase = screenBase;
        return this;
    }

    public LanguageSupportTableEntryBuilder withAudioBase(int audioBase) {
        this.audioBase = audioBase;
        return this;
    }

    public LanguageSupportTableEntryBuilder withOpcodeBufferPositions(int opcodeBufferPositions) {
        this.opcodeBufferPositions = opcodeBufferPositions;
        return this;
    }

    public LanguageSupportTableEntryBuilder withOpCodeBufferValues(String opCodeBufferValues) {
        this.opCodeBufferValues = ObjectUtils.validateNotNull(opCodeBufferValues, "opCodeBufferValues");
        return this;
    }

    public LanguageSupportTableEntry build() {
        return new LanguageSupportTableEntry(languageCode, screenBase, audioBase, opcodeBufferPositions, opCodeBufferValues);
    }

    LanguageSupportTableEntry buildWithNoValidation() {
        return new LanguageSupportTableEntry(languageCode, screenBase, audioBase, opcodeBufferPositions, opCodeBufferValues, null);
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", LanguageSupportTableEntryBuilder.class.getSimpleName() + ": {", "}")
                .add("languageCode: '" + languageCode + "'")
                .add("screenBase: " + screenBase)
                .add("audioBase: " + audioBase)
                .add("opcodeBufferPositions: " + opcodeBufferPositions)
                .add("opCodeBufferValues: '" + opCodeBufferValues + "'")
                .toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LanguageSupportTableEntryBuilder)) return false;
        LanguageSupportTableEntryBuilder that = (LanguageSupportTableEntryBuilder) o;
        return screenBase == that.screenBase &&
                audioBase == that.audioBase &&
                opcodeBufferPositions == that.opcodeBufferPositions &&
                Objects.equals(languageCode, that.languageCode) &&
                Objects.equals(opCodeBufferValues, that.opCodeBufferValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(languageCode, screenBase, audioBase, opcodeBufferPositions, opCodeBufferValues);
    }
}
