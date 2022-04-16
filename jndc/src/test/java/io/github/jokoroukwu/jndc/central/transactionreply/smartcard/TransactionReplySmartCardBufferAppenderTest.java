package io.github.jokoroukwu.jndc.central.transactionreply.smartcard;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyAppenderTest;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyCommandBuilder;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.tlv.BerTlv;
import io.github.jokoroukwu.jndc.tlv.BerTlvReaderBase;
import io.github.jokoroukwu.jndc.tlv.CompositeTlv;
import io.github.jokoroukwu.jndc.tlv.HexStringBerTlv;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.mock;

public class TransactionReplySmartCardBufferAppenderTest extends TransactionReplyAppenderTest {
    final BerTlv<String> dummyScriptId = new HexStringBerTlv(0x9F18, BmpStringGenerator.HEX.fixedLength(8));
    final CompositeTlv<String> issuerScript71 = new CompositeTlv<>(0x71, List.of(dummyScriptId));

    final BerTlv<String> minIssuerAuth = new IssuerAuthData(BmpStringGenerator.HEX.fixedLength(16));
    final BerTlv<String> maxIssuerAuth = new IssuerAuthData(BmpStringGenerator.HEX.fixedLength(32));
    final BerTlv<String> dummyAuthResponse = new AuthResponseCode(BmpStringGenerator.HEX.fixedLength(4));

    private NdcComponentReader<List<CompositeTlv<String>>> issuerScriptsReaderMock;
    private TransactionReplySmartCardBufferAppender appender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        builder = new TransactionReplyCommandBuilder();
        issuerScriptsReaderMock = mock(NdcComponentReader.class);
        nextAppenderMock = mock(ConfigurableNdcComponentAppender.class);
        appender = new TransactionReplySmartCardBufferAppender(BerTlvReaderBase.INSTANCE, issuerScriptsReaderMock, nextAppenderMock);
    }

    @DataProvider
    public Object[][] validBufferProvider() {
        return new Object[][]{
                {"CAM" + minIssuerAuth.toNdcString(),
                        new TransactionReplySmartCardBuffer("CAM", List.of(), minIssuerAuth, null)},

                {"CAM" + maxIssuerAuth.toNdcString(),
                        new TransactionReplySmartCardBuffer("CAM", List.of(), maxIssuerAuth, null)},

                {"CAM" + dummyAuthResponse.toNdcString(),
                        new TransactionReplySmartCardBuffer("CAM", List.of(), null, dummyAuthResponse)},

                {"CAM" + minIssuerAuth.toNdcString() + dummyAuthResponse.toNdcString(),
                        new TransactionReplySmartCardBuffer("CAM", List.of(), minIssuerAuth, dummyAuthResponse)}
        };
    }

    @Test(dataProvider = "validBufferProvider")
    public void should_append_expected_value(String data, TransactionReplySmartCardBuffer expectedBuffer) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + TransactionReplySmartCardBuffer.ID + data);
        appender.appendComponent(buffer, builder, deviceConfigurationMock);
        Assertions.assertThat(builder.getSmartCardBuffer())
                .isEqualTo(expectedBuffer);
    }

    @DataProvider
    public Object[][] invalidTagProvider() {
        return new Object[][]{
                //  invalid tag
                {"CAM" + "3701" + BmpStringGenerator.HEX.fixedLength(2)},
                //  invalid tag
                {"CAM" + minIssuerAuth.toNdcString() + "3701" + BmpStringGenerator.HEX.fixedLength(2)},
                //  invalid tag min length
                {"CAM" + "9107" + BmpStringGenerator.HEX.fixedLength(14)},
                //  invalid tag max length
                {"CAM" + "9111" + BmpStringGenerator.HEX.fixedLength(34)},
                //  repeated tags
                {"CAM" + minIssuerAuth.toNdcString().repeat(2)},
                //  repeated tags
                {"CAM" + dummyAuthResponse.toNdcString().repeat(2)},
                //  invalid tag length
                {"CAM" + "8A03" + BmpStringGenerator.HEX.fixedLength(6)},
                //  invalid tag sequence
                {"CAM" + dummyAuthResponse.toNdcString() + minIssuerAuth.toNdcString()},
        };
    }

    @Test(dataProvider = "invalidTagProvider")
    public void should_throw_expected_exception_on_invalid_tags(String data) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + TransactionReplySmartCardBuffer.ID + data);

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, builder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class);
    }

    @Test
    public void should_throw_expected_exception_on_invalid_cam_id() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + TransactionReplySmartCardBuffer.ID + "CA");

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, builder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class);
    }


    @Test
    public void should_append_expected_issuer_scripts() {
        final List<CompositeTlv<String>> issuerScripts = List.of(issuerScript71);
        final NdcComponentReader<List<CompositeTlv<String>>> fakeReader = new FakeIssuerScriptsReader(issuerScripts);

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + TransactionReplySmartCardBuffer.ID + "CAM" + "7");
        var appender = new TransactionReplySmartCardBufferAppender(BerTlvReaderBase.INSTANCE, fakeReader, nextAppenderMock);
        appender.appendComponent(buffer, builder, deviceConfigurationMock);

        Assertions.assertThat(builder.getSmartCardBuffer())
                .isEqualTo(new TransactionReplySmartCardBuffer("CAM", issuerScripts));
    }

    private static final class FakeIssuerScriptsReader implements NdcComponentReader<List<CompositeTlv<String>>> {
        private final List<CompositeTlv<String>> dummyScripts;

        private FakeIssuerScriptsReader(List<CompositeTlv<String>> dummyScripts) {
            this.dummyScripts = dummyScripts;
        }

        @Override
        public List<CompositeTlv<String>> readComponent(NdcCharBuffer ndcCharBuffer) {
            ndcCharBuffer.skip(1);
            return dummyScripts;
        }
    }
}
