package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.delegatingbufferappender;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.ConfigurationException;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.genericbuffer.GenericBufferAppender;
import io.github.jokoroukwu.jndc.mac.MacAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.barcodedata.BarCodeBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.barcodedata.BarcodeBufferAppender;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit.BunchChequeDepositBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit.BunchChequeDepositBufferAppender;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor.CashAcceptorBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor.CashAcceptorBufferAppender;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.coinhoppers.CoinHopperBufferAppender;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.coinhoppers.CoinHoppersBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cspdata.CspBufferAppender;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cspdata.CspData;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.documentdata.DocumentBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.documentdata.DocumentBufferAppender;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.notesdata.NotesDataBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.notesdata.NotesDataBufferAppender;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.smartcarddata.SmartCardBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.smartcarddata.SmartCardBufferAppender;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.voiceguidance.VoiceGuidanceBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.voiceguidance.VoiceGuidanceBufferAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TransactionRqDelegatingAppender extends ChainedConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> {
    private final IdentifiableBufferAppenderSupplier<TransactionRequestMessageBuilder> appenderSupplier;

    public TransactionRqDelegatingAppender(IdentifiableBufferAppenderSupplier<TransactionRequestMessageBuilder> appenderSupplier,
                                           ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender) {
        super(nextAppender);
        this.appenderSupplier = ObjectUtils.validateNotNull(appenderSupplier, "appenderSupplier cannot be null");
    }

    public TransactionRqDelegatingAppender() {
        super(new MacAppender<>(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString()));
        final Map<Character, ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder>> appenderMap = new HashMap<>();
        appenderMap.put(CspData.CSP_DATA_ID, CspBufferAppender.csp());
        appenderMap.put(CspData.CONFIRMATION_CSP_DATA_ID, CspBufferAppender.confirmationCsp());
        appenderMap.put(SmartCardBuffer.ID, new SmartCardBufferAppender());
        appenderMap.put(CashAcceptorBuffer.ID, new CashAcceptorBufferAppender());
        appenderMap.put(DocumentBuffer.ID, new DocumentBufferAppender());
        appenderMap.put(NotesDataBuffer.SUSPECT_NOTES_DATA_ID, NotesDataBufferAppender.suspect());
        appenderMap.put(NotesDataBuffer.COUNTERFEIT_NOTES_DATA_ID, NotesDataBufferAppender.counterfeit());
        appenderMap.put(BarCodeBuffer.ID, new BarcodeBufferAppender());
        appenderMap.put(CoinHoppersBuffer.ID, new CoinHopperBufferAppender());
        appenderMap.put(BunchChequeDepositBuffer.ID, new BunchChequeDepositBufferAppender());
        appenderMap.put(VoiceGuidanceBuffer.ID, new VoiceGuidanceBufferAppender());

        final GenericBufferAppender<TransactionRequestMessageBuilder> exitsAppender
                = new GenericBufferAppender<>(TransactionRequestMessageBuilder::putExitsBuffer);
        appenderMap.put('W', exitsAppender);
        appenderMap.put('X', exitsAppender);
        appenderMap.put('Y', exitsAppender);
        appenderMap.put('Z', exitsAppender);
        appenderMap.put('[', exitsAppender);
        appenderMap.put('\\', exitsAppender);

        final GenericBufferAppender<TransactionRequestMessageBuilder> optionalDataFieldAppender
                = new GenericBufferAppender<>(TransactionRequestMessageBuilder::putOptionalDataField);
        appenderSupplier = new BufferAppenderSupplierBase<>(appenderMap, optionalDataFieldAppender);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionRequestMessageBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        final Set<Character> expectedFieldIds = new HashSet<>(deviceConfiguration.getTransactionRequestOptionalDataFieldsIds());
        do {
            final char bufferId = getNextBufferId(ndcCharBuffer);
            if (!expectedFieldIds.remove(bufferId)) {
                //  unexpected field id
                callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
                return;
            }
            final ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> appender = getAppenderForId(bufferId);
            appender.appendComponent(ndcCharBuffer, stateObject, deviceConfiguration);

        } while (ndcCharBuffer.hasRemaining());
    }


    private char getNextBufferId(NdcCharBuffer ndcCharBuffer) {
        return (char) ndcCharBuffer.tryGetCharAt(1)
                .ifEmpty(message -> NdcMessageParseException.onMessageParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "no buffer ID to read: " + message, ndcCharBuffer))
                .get();
    }

    private ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> getAppenderForId(char bufferId) {
        return appenderSupplier.getBufferAppender(bufferId)
                .orElseThrow(() -> ConfigurationException.withMessage(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        String.format("no buffer appender for buffer ID '%c'", bufferId)));
    }
}
