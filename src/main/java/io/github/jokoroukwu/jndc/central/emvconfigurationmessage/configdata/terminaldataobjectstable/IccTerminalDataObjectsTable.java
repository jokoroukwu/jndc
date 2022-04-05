package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminaldataobjectstable;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigMessageSubClass;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.ConfigurationData;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.DataObjectsContainer;
import io.github.jokoroukwu.jndc.tlv.ResponseFormat2;
import io.github.jokoroukwu.jndc.tlv.TerminalCountryCode;
import io.github.jokoroukwu.jndc.tlv.TerminalType;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Objects;
import java.util.StringJoiner;

public final class IccTerminalDataObjectsTable extends DataObjectsContainer implements ConfigurationData {
    public static final String COMMAND_NAME = EmvConfigurationMessage.COMMAND_NAME + ": " + EmvConfigMessageSubClass.TERMINAL_DATA_OBJECTS;
    private final ResponseFormat2 dataObjects;

    public IccTerminalDataObjectsTable(ResponseFormat2 dataObjects) {
        this.dataObjects = validateDataObjects(dataObjects, TerminalCountryCode.TAG, TerminalType.TAG);

    }

    public ResponseFormat2 getDataObjects() {
        return dataObjects;
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder()
                .appendComponent(EmvConfigMessageSubClass.TERMINAL_DATA_OBJECTS)
                .appendFs()
                .appendComponent(dataObjects)
                .toString();
    }

    @Override
    public EmvConfigMessageSubClass getEmvConfigMessageSubClass() {
        return EmvConfigMessageSubClass.TERMINAL_DATA_OBJECTS;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", IccTerminalDataObjectsTable.class.getSimpleName() + ": {", "}")
                .add("dataObjects: " + dataObjects)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IccTerminalDataObjectsTable that = (IccTerminalDataObjectsTable) o;
        return dataObjects.equals(that.dataObjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(EmvConfigMessageSubClass.TERMINAL_DATA_OBJECTS, dataObjects);
    }
}
