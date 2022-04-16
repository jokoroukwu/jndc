package io.github.jokoroukwu.jndc.central.transactionreply.smartcard;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.tlv.BerTlv;
import io.github.jokoroukwu.jndc.tlv.BerTlvReaderBase;
import io.github.jokoroukwu.jndc.tlv.CompositeTlv;
import io.github.jokoroukwu.jndc.tlv.HexStringBerTlv;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class IssuerScriptsReaderTest {
    private final BerTlv<String> dummyScriptId = new HexStringBerTlv(0x9F18, BmpStringGenerator.HEX.fixedLength(8));
    private final BerTlv<String> dummyCommandTemplate = new HexStringBerTlv(0x86, BmpStringGenerator.HEX.fixedLength(28));

    private final CompositeTlv<String> issuerScript71 = new CompositeTlv<>(0x71, List.of(dummyScriptId));
    private final CompositeTlv<String> issuerScript72 = new CompositeTlv<>(0x72, List.of(dummyScriptId));
    private final CompositeTlv<String> issuerMultiScript = new CompositeTlv<>(0x71, List.of(dummyScriptId, dummyCommandTemplate));

    private final IssuerScriptsReader reader = new IssuerScriptsReader(BerTlvReaderBase.INSTANCE);

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {"7107" + dummyScriptId.toNdcString(), List.of(issuerScript71)},
                {"7207" + dummyScriptId.toNdcString(), List.of(issuerScript72)},
                {"7117" + dummyScriptId.toNdcString() + dummyCommandTemplate.toNdcString(), List.of(issuerMultiScript)}
        };
    }


    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_scripts(String data, List<CompositeTlv<String>> expectedScript) {
        final List<CompositeTlv<String>> result = reader.readComponent(NdcCharBuffer.wrap(data));
        Assertions.assertThat(result)
                .isEqualTo(expectedScript);
    }

    @Test
    public void should_throw_expected_exception_on_invalid_nested_tag_data() {
        final String unexpectedData = "AB";
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("7108" + dummyScriptId.toNdcString() + unexpectedData);
        Assertions.assertThatThrownBy(() -> reader.readComponent(buffer))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(unexpectedData);
    }

    @Test
    public void should_throw_expected_exception_on_invalid_issuer_script_tag() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("7308" + dummyScriptId.toNdcString());
        Assertions.assertThatThrownBy(() -> reader.readComponent(buffer))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining("not an 'Issuer Script'");
    }
}
