package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SecondaryAIDsAppender extends TerminalAcceptableAidsFieldAppender {
    public static final String FIELD_NAME = "Full/Partial Secondary AID";
    public static final String NUMBER_FIELD = "Number of " + FIELD_NAME;
    private final AidReader aidReader;

    public SecondaryAIDsAppender(TerminalAcceptableAidsFieldAppender nextReader, AidReader aidReader) {
        super(nextReader);
        this.aidReader = ObjectUtils.validateNotNull(aidReader, "aidReader");
    }

    public SecondaryAIDsAppender() {
        this(new AppSelectionIndicatorAppender(), BaseAidReader.INSTANCE);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TerminalApplicationIdEntryBuilder stateObject) {
        final int numberOfSecondaryAIDs = ndcCharBuffer.tryReadHexInt(2)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(IccTerminalAcceptableAppIdsTable.COMMAND_NAME, NUMBER_FIELD, errorMessage, ndcCharBuffer))
                .get();

        stateObject.withSecondaryAids(readSecondaryAids(numberOfSecondaryAIDs, ndcCharBuffer));
        callNextIfFieldDataRemains(ndcCharBuffer, stateObject);
    }

    private List<Aid> readSecondaryAids(int count, NdcCharBuffer ndcCharBuffer) {
        if (count == 0) {
            return Collections.emptyList();
        }
        final List<Aid> secondaryAids = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final DescriptiveOptional<Aid> optionalAid = aidReader.readAid(ndcCharBuffer);
            if (optionalAid.isPresent()) {
                secondaryAids.add(optionalAid.get());
            } else {
                NdcMessageParseException.onFieldParseError(IccTerminalAcceptableAppIdsTable.COMMAND_NAME, FIELD_NAME + ' ' + i, optionalAid.description(), ndcCharBuffer);
            }
        }
        return secondaryAids;
    }
}
