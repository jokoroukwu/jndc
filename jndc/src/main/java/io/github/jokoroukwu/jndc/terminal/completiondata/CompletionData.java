package io.github.jokoroukwu.jndc.terminal.completiondata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.tlv.BerTlv;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.*;

public class CompletionData implements NdcComponent {
    private final String smartCardId;
    private final Map<Integer, BerTlv<String>> iccDataObjects;
    private final List<ScriptResult> scriptResults;


    public CompletionData(String smartCardId, Map<Integer, BerTlv<String>> iccDataObjects, Collection<ScriptResult> scriptResults) {
        this.smartCardId = ObjectUtils.validateNotNull(smartCardId, " Smart card data identifier");
        this.iccDataObjects = Map.copyOf(iccDataObjects);
        this.scriptResults = List.copyOf(scriptResults);
    }

    public CompletionData(Map<Integer, BerTlv<String>> iccDataObjects, Collection<ScriptResult> scriptResults) {
        this("CAM", iccDataObjects, scriptResults);
    }

    CompletionData(String smartCardId, Map<Integer, BerTlv<String>> iccDataObjects, List<ScriptResult> scriptResults) {
        this.smartCardId = smartCardId;
        this.iccDataObjects = new LinkedHashMap<>(iccDataObjects);
        this.scriptResults = scriptResults;
    }

    public String getSmartCardId() {
        return smartCardId;
    }

    public Map<Integer, BerTlv<String>> getIccDataObjects() {
        return iccDataObjects;
    }

    public List<ScriptResult> getScriptResults() {
        return scriptResults;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CompletionData.class.getSimpleName() + ": {", "}")
                .add("smartCardId: '" + smartCardId + '\'')
                .add("iccDataObjects: " + iccDataObjects.values())
                .add("scriptResults: " + scriptResults)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletionData that = (CompletionData) o;
        return smartCardId.equals(that.smartCardId) && iccDataObjects.equals(that.iccDataObjects) && scriptResults.equals(that.scriptResults);
    }

    @Override
    public int hashCode() {
        return Objects.hash(smartCardId, iccDataObjects, scriptResults);
    }

    @Override
    public String toNdcString() {
        final NdcStringBuilder builder = new NdcStringBuilder(128)
                .append(smartCardId);
        if (!scriptResults.isEmpty()) {
            return builder.appendGs()
                    .appendComponents(iccDataObjects.values())
                    .appendGs()
                    .appendComponents(scriptResults, NdcConstants.GROUP_SEPARATOR_STRING)
                    .toString();
        }
        if (!iccDataObjects.isEmpty()) {
            return builder.appendGs()
                    .appendComponents(iccDataObjects.values())
                    .toString();
        }
        return builder.toString();
    }
}
