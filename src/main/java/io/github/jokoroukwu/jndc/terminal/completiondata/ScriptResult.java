package io.github.jokoroukwu.jndc.terminal.completiondata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.tlv.BerTlv;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public class ScriptResult implements NdcComponent {
    private final ProcessingResult processingResult;
    private final int scriptCommandSequence;
    private final BerTlv<String> scriptId;

    public ScriptResult(ProcessingResult processingResult, int scriptCommandSequence, BerTlv<String> scriptId) {
        this.processingResult = ObjectUtils.validateNotNull(processingResult, "Result of issuer script processing");
        this.scriptCommandSequence = Integers.validateHexRange(scriptCommandSequence, 0, 0xF,
                "Sequence number of script command");
        this.scriptId = ObjectUtils.validateNotNull(scriptId, "Script Identifier (tag 0x9F18)");
    }

    ScriptResult(ProcessingResult processingResult, int scriptCommandSequence, BerTlv<String> scriptId, Void unused) {
        this.processingResult = processingResult;
        this.scriptCommandSequence = scriptCommandSequence;
        this.scriptId = scriptId;
    }


    public ProcessingResult getProcessingResult() {
        return processingResult;
    }

    public int getScriptCommandSequence() {
        return scriptCommandSequence;
    }

    public BerTlv<String> getScriptId() {
        return scriptId;
    }

    @Override
    public String toNdcString() {
        return processingResult.toNdcString()
                + Integer.toHexString(scriptCommandSequence).toUpperCase()
                + scriptId.toNdcString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ScriptResult.class.getSimpleName() + ": {", "}")
                .add("processingResult: " + processingResult)
                .add("scriptCommandSequence: " + scriptCommandSequence)
                .add("scriptId: " + scriptId)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScriptResult that = (ScriptResult) o;
        return scriptCommandSequence == that.scriptCommandSequence && processingResult == that.processingResult && scriptId.equals(that.scriptId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processingResult, scriptCommandSequence, scriptId);
    }
}
