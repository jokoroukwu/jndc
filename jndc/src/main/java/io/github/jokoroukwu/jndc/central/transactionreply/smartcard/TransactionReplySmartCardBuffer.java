package io.github.jokoroukwu.jndc.central.transactionreply.smartcard;

import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;
import io.github.jokoroukwu.jndc.tlv.BerTlv;
import io.github.jokoroukwu.jndc.tlv.CompositeTlv;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.*;

public class TransactionReplySmartCardBuffer implements IdentifiableBuffer {
    public static final char ID = '5';
    private final String smartCardDataId;
    private final BerTlv<String> issuerAuthDataTag;
    private final BerTlv<String> authResponseTag;
    private final List<CompositeTlv<String>> issuerScripts;

    public TransactionReplySmartCardBuffer(String smartCardDataId,
                                           Collection<CompositeTlv<String>> issuerScripts,
                                           BerTlv<String> issuerAuthDataTag,
                                           BerTlv<String> authResponseTag) {
        ObjectUtils.validateNotNull(smartCardDataId, "Smart Card Data Identifier");
        Integers.validateIsExactValue(smartCardDataId.length(), 3, "Smart Card Data Identifier");
        this.smartCardDataId = smartCardDataId;
        this.issuerScripts = List.copyOf(issuerScripts);
        this.issuerAuthDataTag = issuerAuthDataTag;
        this.authResponseTag = authResponseTag;
    }

    public TransactionReplySmartCardBuffer(Collection<CompositeTlv<String>> issuerScripts,
                                           BerTlv<String> issuerAuthDataTag,
                                           BerTlv<String> authResponseTag) {
        this("CAM", issuerScripts, issuerAuthDataTag, authResponseTag);
    }

    public TransactionReplySmartCardBuffer(String smartCardDataId, Collection<CompositeTlv<String>> issuerScripts) {
        this(smartCardDataId, issuerScripts, null, null);
    }

    public String getSmartCardDataId() {
        return smartCardDataId;
    }

    public Optional<BerTlv<String>> getIssuerAuthDataTag() {
        return Optional.ofNullable(issuerAuthDataTag);
    }

    public Optional<BerTlv<String>> getAuthResponseTag() {
        return Optional.ofNullable(authResponseTag);
    }

    public List<CompositeTlv<String>> getIssuerScripts() {
        return issuerScripts;
    }

    @Override
    public String toNdcString() {
        final StringBuilder builder = new StringBuilder(128)
                .append(ID)
                .append(smartCardDataId);
        if (issuerAuthDataTag != null) {
            builder.append(issuerAuthDataTag.toNdcString());
        }
        if (authResponseTag != null) {
            builder.append(authResponseTag.toNdcString());
        }
        if (!issuerScripts.isEmpty()) {
            for (CompositeTlv<String> issuerScript : issuerScripts) {
                builder.append(issuerScript.toNdcString());
            }
        }
        return builder.toString();
    }

    @Override
    public char getId() {
        return ID;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransactionReplySmartCardBuffer.class.getSimpleName() + ": {", "}")
                .add("smartCardDataId: '" + smartCardDataId + "'")
                .add("issuerAuthDataTag: " + issuerAuthDataTag)
                .add("authResponseTag: " + authResponseTag)
                .add("issuerScripts: " + issuerScripts)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionReplySmartCardBuffer that = (TransactionReplySmartCardBuffer) o;
        return smartCardDataId.equals(that.smartCardDataId) &&
                Objects.equals(issuerAuthDataTag, that.issuerAuthDataTag) &&
                Objects.equals(authResponseTag, that.authResponseTag) &&
                Objects.equals(issuerScripts, that.issuerScripts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(smartCardDataId, issuerAuthDataTag, authResponseTag, issuerScripts);
    }
}
