package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.smartcarddata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBufferAppender;
import io.github.jokoroukwu.jndc.tlv.BerTlv;
import io.github.jokoroukwu.jndc.tlv.HexStringBerTLVReader;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmartCardBufferAppender extends IdentifiableBufferAppender<TransactionRequestMessageBuilder> {
    public static final String FIELD_NAME = "Smart Card Buffer";
    private final NdcComponentReader<DescriptiveOptional<BerTlv<String>>> berTlvReader;

    public SmartCardBufferAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender,
                                   NdcComponentReader<DescriptiveOptional<BerTlv<String>>> berTlvReader) {
        super(nextAppender);
        this.berTlvReader = ObjectUtils.validateNotNull(berTlvReader, "berTlvReader");
    }

    public SmartCardBufferAppender() {
        this(null, HexStringBerTLVReader.DEFAULT);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionRequestMessageBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        skipBufferMeta(ndcCharBuffer);

        if (ndcCharBuffer.hasFieldDataRemaining()) {
            final SmartCardData smartCardData = readSmartCardData(ndcCharBuffer);
            stateObject.withSmartCardBuffer(new SmartCardBuffer(smartCardData));
        } else {
            stateObject.withSmartCardBuffer(SmartCardBuffer.EMPTY);
        }
        callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    @Override
    protected char getBufferId() {
        return SmartCardBuffer.ID;
    }

    @Override
    protected String getFieldName() {
        return FIELD_NAME;
    }

    @Override
    protected String getCommandName() {
        return TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString();
    }

    private SmartCardData readSmartCardData(NdcCharBuffer ndcCharBuffer) {
        final String smartCardDataId = ndcCharBuffer.tryReadCharSequence(3)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(getCommandName(),
                        FIELD_NAME + ": Smart card data identifier", errorMessage, ndcCharBuffer))
                .get();
        final int camFlags = ndcCharBuffer.tryReadHexInt(4)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(getCommandName(),
                        FIELD_NAME + ": CAM Flags", errorMessage, ndcCharBuffer))
                .get();

        final List<BerTlv<String>> dataObjects = readDataObjects(ndcCharBuffer);

        return new SmartCardData(camFlags, smartCardDataId, dataObjects);
    }

    private List<BerTlv<String>> readDataObjects(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return Collections.emptyList();
        }
        final ArrayList<BerTlv<String>> dataObjects = new ArrayList<>(11);
        do {
            berTlvReader.readComponent(ndcCharBuffer)
                    .resolve(dataObjects::add, errorMessage -> NdcMessageParseException.onFieldParseError(getCommandName(),
                            FIELD_NAME + ": EMV data objects for authorisation", errorMessage, ndcCharBuffer));
        } while (ndcCharBuffer.hasFieldDataRemaining());

        dataObjects.trimToSize();
        return Collections.unmodifiableList(dataObjects);
    }
}
