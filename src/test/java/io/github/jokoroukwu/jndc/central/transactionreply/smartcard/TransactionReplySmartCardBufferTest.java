package io.github.jokoroukwu.jndc.central.transactionreply.smartcard;

import io.github.jokoroukwu.jndc.tlv.BerTlv;
import io.github.jokoroukwu.jndc.tlv.CompositeTlv;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class TransactionReplySmartCardBufferTest {
    private final BerTlv<String> dummyIssuerAuth = new IssuerAuthData(BmpStringGenerator.HEX.fixedLength(16));
    private final BerTlv<String> dummyAuthResponse = new AuthResponseCode(BmpStringGenerator.HEX.fixedLength(4));
    private final CompositeTlv<String> dummyCompositeTlv = new CompositeTlv<>(0x77, List.of(dummyAuthResponse, dummyAuthResponse));


    @DataProvider
    public Object[][] validDataProvider() {
        final String issuerString = dummyIssuerAuth.toNdcString();
        final String authRespString = dummyAuthResponse.toNdcString();
        final String compositeTlvString = dummyCompositeTlv.toNdcString();
        return new Object[][]{
                {new TransactionReplySmartCardBuffer("VIS", List.of(), null, null),
                        TransactionReplySmartCardBuffer.ID + "VIS"},

                {new TransactionReplySmartCardBuffer(List.of(), null, null), TransactionReplySmartCardBuffer.ID + "CAM"},

                {new TransactionReplySmartCardBuffer(List.of(dummyCompositeTlv), null, null),
                        TransactionReplySmartCardBuffer.ID + "CAM" + compositeTlvString},

                {new TransactionReplySmartCardBuffer(List.of(), null, dummyAuthResponse),
                        TransactionReplySmartCardBuffer.ID + "CAM" + authRespString},

                {new TransactionReplySmartCardBuffer(List.of(), dummyIssuerAuth, null),
                        TransactionReplySmartCardBuffer.ID + "CAM" + issuerString},

                {new TransactionReplySmartCardBuffer(List.of(), dummyIssuerAuth, dummyAuthResponse),
                        TransactionReplySmartCardBuffer.ID + "CAM" + issuerString + authRespString},

                {new TransactionReplySmartCardBuffer(List.of(dummyCompositeTlv), dummyIssuerAuth, dummyAuthResponse),
                        TransactionReplySmartCardBuffer.ID + "CAM" + issuerString + authRespString + compositeTlvString},

                {new TransactionReplySmartCardBuffer(List.of(dummyCompositeTlv, dummyCompositeTlv), dummyIssuerAuth, dummyAuthResponse),
                        TransactionReplySmartCardBuffer.ID + "CAM" + issuerString + authRespString + compositeTlvString.repeat(2)},
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_ndc_string(TransactionReplySmartCardBuffer buffer, String expectedString) {
        Assertions.assertThat(buffer.toNdcString())
                .isEqualTo(expectedString);
    }
}
