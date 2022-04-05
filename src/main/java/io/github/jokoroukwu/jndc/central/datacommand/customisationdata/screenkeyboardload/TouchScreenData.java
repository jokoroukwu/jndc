package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Collections;
import java.util.List;

public class TouchScreenData implements NdcComponent {
    public static final TouchScreenData EMPTY = new TouchScreenData(Collections.emptyList());
    private final List<String> dataSets;

    public TouchScreenData(List<String> dataSets) {
        this.dataSets = ObjectUtils.validateNotNull(dataSets, "dataSets");
    }

    public List<String> getDataSets() {
        return Collections.unmodifiableList(dataSets);
    }

    @Override
    public String toNdcString() {
        return NdcConstants.GROUP_SEPARATOR + String.join(Strings.EMPTY_STRING, dataSets);
    }

}
