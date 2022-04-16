package io.github.jokoroukwu.jndc.central.transactionreply.smartcard;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.tlv.BerTlv;
import io.github.jokoroukwu.jndc.tlv.BerTlvReader;
import io.github.jokoroukwu.jndc.tlv.CompositeTlv;
import io.github.jokoroukwu.jndc.tlv.HexStringBerTlv;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class IssuerScriptsReader implements NdcComponentReader<List<CompositeTlv<String>>> {
    private final BerTlvReader berTlvReader;

    public IssuerScriptsReader(BerTlvReader berTlvReader) {
        this.berTlvReader = ObjectUtils.validateNotNull(berTlvReader, "BER-TLV reader");
    }

    @Override
    public List<CompositeTlv<String>> readComponent(NdcCharBuffer ndcCharBuffer) {
        final ArrayList<CompositeTlv<String>> scripts = new ArrayList<>();
        while (ndcCharBuffer.hasFieldDataRemaining()) {
            final CompositeTlv<String> issuerScript = readIssuerScript(ndcCharBuffer);
            scripts.add(issuerScript);
        }
        scripts.trimToSize();
        return scripts;
    }

    private CompositeTlv<String> readIssuerScript(NdcCharBuffer messageBuffer) {
        final int tag = berTlvReader.tryReadTag(messageBuffer)
                .filter(this::isIssuerScript, actual -> () -> actual + " is not an 'Issuer Script' tag")
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(),
                        "'Issuer Script' tag", errorMessage, messageBuffer));

        final int length = berTlvReader.tryReadLength(messageBuffer)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(),
                        "'Issuer Script' length", errorMessage, messageBuffer));

        final NdcCharBuffer subBuffer = messageBuffer.tryReadCharSequence(length * 2)
                .map(NdcCharBuffer::wrap)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(),
                        "'Issuer Script' value", errorMessage, messageBuffer));

        final LinkedHashMap<Integer, BerTlv<String>> nestedTlvs = readNestedTlvs(subBuffer);
        return new CompositeTlv<>(tag, nestedTlvs);
    }

    private LinkedHashMap<Integer, BerTlv<String>> readNestedTlvs(NdcCharBuffer ndcCharBuffer) {
        final LinkedHashMap<Integer, BerTlv<String>> nestedTlvs = new LinkedHashMap<>();
        while (ndcCharBuffer.hasFieldDataRemaining()) {
            final int tag = berTlvReader.tryReadTag(ndcCharBuffer)
                    .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(),
                            "'Issuer Script' nested TLV", errorMessage, ndcCharBuffer));
            final int length = berTlvReader.tryReadLength(ndcCharBuffer)
                    .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(),
                            "'Issuer Script' nested TLV", errorMessage, ndcCharBuffer));
            ndcCharBuffer.tryReadCharSequence(length * 2)
                    .resolve(value -> nestedTlvs.put(tag, new HexStringBerTlv(tag, value)),
                            errorMessage -> NdcMessageParseException.onFieldParseError(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(),
                                    "'Issuer Script' nested TLV", errorMessage, ndcCharBuffer));
        }
        return nestedTlvs;
    }

    private boolean isIssuerScript(int tag) {
        return tag == 0x71 || tag == 0x72;
    }
}
