package io.github.jokoroukwu.jndc.central.transactionreply.smartcard;

import io.github.jokoroukwu.jndc.tlv.BerTlv;
import io.github.jokoroukwu.jndc.tlv.CompositeTlv;

import java.util.Collections;
import java.util.List;

public class TransactionReplySmartCardBufferBuilder {
    private String smartCardDataId;
    private BerTlv<String> issuerAuthDataTag;
    private BerTlv<String> authResponseTag;
    private List<CompositeTlv<String>> issuerScripts = Collections.emptyList();

    public String getSmartCardDataId() {
        return smartCardDataId;
    }

    public BerTlv<String> getIssuerAuthDataTag() {
        return issuerAuthDataTag;
    }

    public BerTlv<String> getAuthResponseTag() {
        return authResponseTag;
    }

    public List<CompositeTlv<String>> getIssuerScripts() {
        return issuerScripts;
    }

    public TransactionReplySmartCardBufferBuilder withSmartCardDataId(String smartCardDataId) {
        this.smartCardDataId = smartCardDataId;
        return this;
    }

    public TransactionReplySmartCardBufferBuilder withIssuerAuthData(BerTlv<String> issuerAuthDataTag) {
        this.issuerAuthDataTag = issuerAuthDataTag;
        return this;
    }

    public TransactionReplySmartCardBufferBuilder withAuthResponse(BerTlv<String> authResponseTag) {
        this.authResponseTag = authResponseTag;
        return this;
    }

    public TransactionReplySmartCardBufferBuilder withIssuerScripts(List<CompositeTlv<String>> issuerScripts) {
        this.issuerScripts = issuerScripts;
        return this;
    }

    public TransactionReplySmartCardBuffer build() {
        return new TransactionReplySmartCardBuffer(smartCardDataId, issuerScripts, issuerAuthDataTag, authResponseTag);
    }
}
