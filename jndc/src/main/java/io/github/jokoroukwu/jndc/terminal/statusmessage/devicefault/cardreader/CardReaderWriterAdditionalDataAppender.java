package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;
import io.github.jokoroukwu.jndc.trackdata.TrackDataReader;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class CardReaderWriterAdditionalDataAppender implements ConfigurableNdcComponentAppender<CardReaderWriterFaultBuilder> {
    private final NdcComponentReader<DescriptiveOptional<String>> track1DataReader;
    private final NdcComponentReader<DescriptiveOptional<String>> track2DataReader;
    private final NdcComponentReader<DescriptiveOptional<String>> track3DataReader;

    public CardReaderWriterAdditionalDataAppender(NdcComponentReader<DescriptiveOptional<String>> track1DataReader,
                                                  NdcComponentReader<DescriptiveOptional<String>> track2DataReader,
                                                  NdcComponentReader<DescriptiveOptional<String>> track3DataReader) {
        this.track1DataReader = ObjectUtils.validateNotNull(track1DataReader, "track1DataReader");
        this.track2DataReader = ObjectUtils.validateNotNull(track2DataReader, "track2DataReader");
        this.track3DataReader = ObjectUtils.validateNotNull(track3DataReader, "track3DataReader");
    }

    public CardReaderWriterAdditionalDataAppender() {
        this.track1DataReader = new TrackDataReader(78, ' ', '_');
        this.track2DataReader = new TrackDataReader(39, '0', '?');
        this.track3DataReader = new TrackDataReader(106, '0', '?');
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, CardReaderWriterFaultBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        if (ndcCharBuffer.hasRemaining()) {
            ndcCharBuffer.trySkipFieldSeparator()
                    .ifPresent(errorMessage
                            -> NdcMessageParseException.onNoFieldSeparator(UnsolicitedStatusMessage.COMMAND_NAME, "'Additional Data'", errorMessage, ndcCharBuffer));
            final String track1Data = track1DataReader.readComponent(ndcCharBuffer)
                    .getOrThrow(errorMessage
                            -> withMessage(UnsolicitedStatusMessage.COMMAND_NAME, "'Track 1 Data'", errorMessage, ndcCharBuffer));

            final String track2Data = track2DataReader.readComponent(ndcCharBuffer)
                    .getOrThrow(errorMessage
                            -> withMessage(UnsolicitedStatusMessage.COMMAND_NAME, "'Track 2 Data'", errorMessage, ndcCharBuffer));

            final String track3Data = track3DataReader.readComponent(ndcCharBuffer)
                    .getOrThrow(errorMessage
                            -> withMessage(UnsolicitedStatusMessage.COMMAND_NAME, "'Track 3 Data'", errorMessage, ndcCharBuffer));

            stateObject.withAdditionalData(new CardReaderWriterAdditionalData(track1Data, track2Data, track3Data));
            checkNoDataRemains(ndcCharBuffer);
        }
    }

    private void checkNoDataRemains(NdcCharBuffer ndcCharBuffer) {
        if (ndcCharBuffer.hasRemaining()) {
            final String errorMessage = String.format("unexpected data '%s' at position %d", ndcCharBuffer.subBuffer(),
                    ndcCharBuffer.position());
            throw withMessage(UnsolicitedStatusMessage.COMMAND_NAME, "'Additional Data'", errorMessage, ndcCharBuffer);
        }
    }
}
