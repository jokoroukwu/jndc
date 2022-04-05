package io.github.jokoroukwu.jndc.central.transactionreply;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.transactionreply.chequedestination.ChequeDestinationBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.chequedestination.ChequeDestinationBufferAppender;
import io.github.jokoroukwu.jndc.central.transactionreply.depositlimit.DepositLimitsBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.depositlimit.DepositLimitsBufferAppender;
import io.github.jokoroukwu.jndc.central.transactionreply.multicheque.MultiChequeBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.multicheque.MultiChequeBufferAppender;
import io.github.jokoroukwu.jndc.central.transactionreply.smartcard.TransactionReplySmartCardBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.smartcard.TransactionReplySmartCardBufferAppender;
import io.github.jokoroukwu.jndc.central.transactionreply.transactionupdate.TransactionUpdateBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.transactionupdate.TransactionUpdateBufferAppender;
import io.github.jokoroukwu.jndc.exception.ConfigurationException;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicators;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategies;
import io.github.jokoroukwu.jndc.genericbuffer.GenericBufferAppender;
import io.github.jokoroukwu.jndc.mac.MacAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.delegatingbufferappender.BufferAppenderSupplierBase;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.delegatingbufferappender.IdentifiableBufferAppenderSupplier;
import io.github.jokoroukwu.jndc.trackdata.*;

import java.util.HashMap;
import java.util.Map;

public class TransactionReplyDelegatingAppender extends ChainedConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> {
    private final IdentifiableBufferAppenderSupplier<TransactionReplyCommandBuilder> bufferAppenderSupplier;

    public TransactionReplyDelegatingAppender(ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> macAppender,
                                              IdentifiableBufferAppenderSupplier<TransactionReplyCommandBuilder> bufferAppenderSupplier) {
        super(macAppender);
        this.bufferAppenderSupplier = bufferAppenderSupplier;
    }

    public TransactionReplyDelegatingAppender() {
        super(new MacAppender<>(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString()));
        final Map<Character, ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder>> factoryMap = new HashMap<>();

        factoryMap.put(Track3DataBuffer.ID, TrackBufferAppender.<TransactionReplyCommandBuilder>builder()
                .withCommandName(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString())
                .withFieldName("Track 3 data Buffer")
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.ALWAYS_SKIP_FS)
                .withFieldPresenceIndicator(FieldPresenceIndicators.MANDATORY)
                .withTrackDataReader(new TrackDataReader(106,0x30,0x3F))
                .withDataConsumer((builder, value) -> builder.withTrack3DataBuffer(new Track3DataBuffer(value)))
                .build());

        factoryMap.put(Track1DataBuffer.TRANSACTION_REPLY_ID, TrackBufferAppender.<TransactionReplyCommandBuilder>builder()
                .withCommandName(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString())
                .withFieldName("Track 1 data Buffer")
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.ALWAYS_SKIP_FS)
                .withFieldPresenceIndicator(FieldPresenceIndicators.MANDATORY)
                .withTrackDataReader(new TrackDataReader(78,0x20,0x5F))
                .withDataConsumer((builder, value) -> builder.withTrack1DataBuffer(Track1DataBuffer.replyBuffer(value)))
                .build());

        factoryMap.put(Track2DataBuffer.ID, TrackBufferAppender.<TransactionReplyCommandBuilder>builder()
                .withCommandName(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString())
                .withFieldName("Track 2 data Buffer")
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.ALWAYS_SKIP_FS)
                .withFieldPresenceIndicator(FieldPresenceIndicators.MANDATORY)
                .withTrackDataReader(new TrackDataReader(39,0x30,0x3F))
                .withDataConsumer((builder, value) -> builder.withTrack2DataBuffer(new Track2DataBuffer(value)))
                .build());

        factoryMap.put('S', new GenericBufferAppender<>(TransactionReplyCommandBuilder::withBufferS));
        factoryMap.put(TransactionReplySmartCardBuffer.ID, new TransactionReplySmartCardBufferAppender());
        factoryMap.put(ChequeDestinationBuffer.ID, new ChequeDestinationBufferAppender());
        factoryMap.put(MultiChequeBuffer.ID, new MultiChequeBufferAppender());
        factoryMap.put(TransactionUpdateBuffer.ID, new TransactionUpdateBufferAppender());
        factoryMap.put(DepositLimitsBuffer.ID, new DepositLimitsBufferAppender());

        final GenericBufferAppender<TransactionReplyCommandBuilder> exitsBufferAppender
                = new GenericBufferAppender<>(TransactionReplyCommandBuilder::putExitsBuffer);
        bufferAppenderSupplier = new BufferAppenderSupplierBase<>(factoryMap, exitsBufferAppender);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionReplyCommandBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        if (deviceConfiguration.isMacEnabled()) {
            readWithMac(ndcCharBuffer, stateObject, deviceConfiguration);
        } else {
            readWithNoMac(ndcCharBuffer, stateObject, deviceConfiguration);
        }
    }

    private void readWithMac(NdcCharBuffer ndcCharBuffer, TransactionReplyCommandBuilder dataCollector, DeviceConfiguration deviceConfiguration) {
        while (!isMac(ndcCharBuffer.remaining())) {
            final char nextBufferId = getBufferId(ndcCharBuffer);
            getAppender(nextBufferId).appendComponent(ndcCharBuffer, dataCollector, deviceConfiguration);
        }

        callNextAppender(ndcCharBuffer, dataCollector, deviceConfiguration);
    }

    private void readWithNoMac(NdcCharBuffer ndcCharBuffer, TransactionReplyCommandBuilder dataCollector, DeviceConfiguration deviceConfiguration) {
        while (ndcCharBuffer.hasRemaining()) {
            final char nextBufferId = getBufferId(ndcCharBuffer);
            getAppender(nextBufferId).appendComponent(ndcCharBuffer, dataCollector, deviceConfiguration);
        }
    }

    private char getBufferId(NdcCharBuffer ndcCharBuffer) {
        return (char) ndcCharBuffer.tryGetCharAt(1)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(),
                        "Buffer ID", errorMessage, ndcCharBuffer));
    }

    private ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> getAppender(char bufferId) {
        return bufferAppenderSupplier.getBufferAppender(bufferId)
                .orElseThrow(() -> ConfigurationException.withMessage(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(),
                        String.format("no buffer appender for buffer id '%c'", bufferId)));
    }

    private boolean isMac(int remaining) {
        //  FS + MAC length
        return remaining <= 9;
    }

}
