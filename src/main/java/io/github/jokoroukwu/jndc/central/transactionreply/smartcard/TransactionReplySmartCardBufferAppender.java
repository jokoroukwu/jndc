package io.github.jokoroukwu.jndc.central.transactionreply.smartcard;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyCommandBuilder;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBufferAppender;
import io.github.jokoroukwu.jndc.tlv.BerTlv;
import io.github.jokoroukwu.jndc.tlv.BerTlvReader;
import io.github.jokoroukwu.jndc.tlv.BerTlvReaderBase;
import io.github.jokoroukwu.jndc.tlv.CompositeTlv;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.List;

public class TransactionReplySmartCardBufferAppender extends IdentifiableBufferAppender<TransactionReplyCommandBuilder> {
    public static final String FIELD_NAME = "Smart Card Buffer data";
    private final BerTlvReader berTlvReader;
    private final NdcComponentReader<List<CompositeTlv<String>>> issuerScriptsReader;

    public TransactionReplySmartCardBufferAppender(BerTlvReader berTlvReader,
                                                   NdcComponentReader<List<CompositeTlv<String>>> issuerScriptsReader,
                                                   ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender) {
        super(nextAppender);
        this.berTlvReader = ObjectUtils.validateNotNull(berTlvReader, "BER-TLV reader");
        this.issuerScriptsReader = issuerScriptsReader;
    }

    public TransactionReplySmartCardBufferAppender(ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender) {
        this(BerTlvReaderBase.INSTANCE, new IssuerScriptsReader(BerTlvReaderBase.INSTANCE), nextAppender);
    }

    public TransactionReplySmartCardBufferAppender() {
        this(null);
    }

    @Override
    protected char getBufferId() {
        return TransactionReplySmartCardBuffer.ID;
    }

    @Override
    protected String getFieldName() {
        return FIELD_NAME;
    }

    @Override
    protected String getCommandName() {
        return CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString();
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionReplyCommandBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        skipBufferMeta(ndcCharBuffer);

        final String smartCardId = readSmartCardId(ndcCharBuffer);
        final TransactionReplySmartCardBufferBuilder builder = new TransactionReplySmartCardBufferBuilder()
                .withSmartCardDataId(smartCardId);
        do {
            readTlvs(ndcCharBuffer, builder);
        } while (ndcCharBuffer.hasFieldDataRemaining());

        stateObject.withSmartCardBuffer(builder.build());
        callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private void readTlvs(NdcCharBuffer buffer, TransactionReplySmartCardBufferBuilder builder) {
        char firstChar = readFirstChar(buffer);
        //  if this sub-field is present then it should be the first one
        //  but may be completely omitted
        if (isIssuerAuthData(firstChar)) {
            if (builder.getIssuerAuthDataTag() == null && builder.getAuthResponseTag() == null) {
                builder.withIssuerAuthData(readIssuerAuthData(buffer));
                return;
            }
            throw onUnexpectedTag(buffer);
        }
        //  this sub-field should only appear once or never
        if (isAuthResponseCode(firstChar)) {
            if (builder.getAuthResponseTag() == null) {
                builder.withAuthResponse(readAuthResponseData(buffer));
                return;
            }
            throw onUnexpectedTag(buffer);
        }
        //  this sub-field is terminal
        //  and may be completely omitted
        if (isIssuerScript(firstChar)) {
            final List<CompositeTlv<String>> issuerScripts = issuerScriptsReader.readComponent(buffer);
            builder.withIssuerScripts(issuerScripts);
            return;
        }
        throw onUnexpectedTag(buffer);
    }


    private boolean isIssuerAuthData(char character) {
        return character == '9';
    }

    private boolean isAuthResponseCode(char character) {
        return character == '8';
    }

    private boolean isIssuerScript(char character) {
        return character == '7';
    }

    private char readFirstChar(NdcCharBuffer ndcCharBuffer) {
        return (char) ndcCharBuffer.tryGetCharAt(0)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(getCommandName(),
                        "Tag", errorMessage, ndcCharBuffer));
    }

    private NdcMessageParseException onUnexpectedTag(NdcCharBuffer ndcCharBuffer) {
        final String errorMessage = "unexpected tag at position " + (ndcCharBuffer.position() - 1);
        return NdcMessageParseException.withMessage(getCommandName(), FIELD_NAME, errorMessage, ndcCharBuffer);
    }


    private BerTlv<String> readIssuerAuthData(NdcCharBuffer ndcCharBuffer) {
        berTlvReader.tryReadTag(ndcCharBuffer)
                .filter(this::isIssuerAuthDataTag, actual -> () -> actual + " is not an 'Issuer Authentication Data' tag")
                .ifEmpty(errorMessage -> NdcMessageParseException.onNoFieldSeparator(getCommandName(),
                        "Issuer Authentication Data", errorMessage, ndcCharBuffer));

        final int length = berTlvReader.tryReadLength(ndcCharBuffer)
                .filter(this::isIssuerAuthDataLengthValid, actual -> () -> "tag length should be in range 8-16 but was " + actual)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(getCommandName(),
                        "Issuer Authentication Data", errorMessage, ndcCharBuffer));

        return ndcCharBuffer.tryReadCharSequence(length * 2)
                .map(value -> new IssuerAuthData(value, null))
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(getCommandName(),
                        "Issuer Authentication Data", errorMessage, ndcCharBuffer));
    }

    private BerTlv<String> readAuthResponseData(NdcCharBuffer ndcCharBuffer) {
        berTlvReader.tryReadTag(ndcCharBuffer)
                .filter(this::isAuthResponseTag, actual -> () -> actual + " is not an 'Authorisation Response Code' tag")
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(getCommandName(), FIELD_NAME, errorMessage, ndcCharBuffer));

        berTlvReader.tryReadLength(ndcCharBuffer)
                .filter(this::isAuthResponseLengthValid, actual -> ()
                        -> "'Authorisation Response Code' tag length should be 2 but was " + actual)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(getCommandName(), FIELD_NAME, errorMessage, ndcCharBuffer));

        return ndcCharBuffer.tryReadCharSequence(AuthResponseCode.OCTET_LENGTH * 2)
                .map(value -> new AuthResponseCode(value, null))
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(getCommandName(),
                        FIELD_NAME + ": 'Authorisation Response Code' value", errorMessage, ndcCharBuffer));
    }


    private boolean isIssuerAuthDataLengthValid(int length) {
        return length >= 0x08 && length <= 0x10;
    }

    private boolean isAuthResponseLengthValid(int length) {
        return AuthResponseCode.OCTET_LENGTH == length;
    }

    private boolean isAuthResponseTag(int tag) {
        return AuthResponseCode.TAG == tag;
    }

    private boolean isIssuerAuthDataTag(int tag) {
        return IssuerAuthData.TAG == tag;
    }


    private String readSmartCardId(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadCharSequence(3)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(getCommandName(),
                        "Smart Card Data Identifier", errorMessage, ndcCharBuffer));
    }
}
