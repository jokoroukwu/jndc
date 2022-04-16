package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.currencydataobjectstable;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.CentralMessageMeta;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.mac.MacReader;
import io.github.jokoroukwu.jndc.mac.MacReaderBase;
import io.github.jokoroukwu.jndc.tlv.ResponseFormat2;
import io.github.jokoroukwu.jndc.tlv.TransactionCurrencyCode;
import io.github.jokoroukwu.jndc.tlv.TransactionCurrencyExponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.IStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

import static org.mockito.Mockito.*;

public class IccCurrencyDataObjectsTableAppenderTest {
    private final CentralMessageMeta messageMeta
            = new CentralMessageMeta(CentralMessageClass.EMV_CONFIGURATION, BmpStringGenerator.HEX.randomChar(), Luno.DEFAULT);
    private MacReader macReaderMock;
    private CurrencyDataObjectsTableMessageListener messageListenerMock;
    private FakeEntryReader fakeEntryReader;
    private IccCurrencyDataObjectsTableAppender appender;


    @BeforeMethod
    public void setUp() {
        macReaderMock = mock(MacReader.class);
        messageListenerMock = mock(CurrencyDataObjectsTableMessageListener.class);
        fakeEntryReader = new FakeEntryReader();
        appender = new IccCurrencyDataObjectsTableAppender(messageListenerMock, fakeEntryReader, macReaderMock);
    }

    @DataProvider
    public Object[][] dataProvider() {
        final int maxEntryNumber = 0xFF;
        final String prefix = Integers.toEvenLengthHexString(maxEntryNumber);
        final StringJoiner joiner = new StringJoiner("", prefix, "");
        for (int i = 1; i <= maxEntryNumber; i++) {
            joiner.add(Integers.toEvenLengthHexString(i));
        }
        final String mac = BmpStringGenerator.HEX.fixedLength(8);
        return new Object[][]{
                {"0101", ""},
                {"0101" + mac, mac},
                {"020102", ""},
                {"020102" + mac, mac},
                {joiner.toString(), ""},
                {joiner.toString() + mac, mac}
        };
    }


    @Test(dataProvider = "dataProvider")
    public void should_append_call_listener_with_expected_message(String bufferValue, String mac) {
        when(macReaderMock.tryReadMac(any()))
                .thenReturn(DescriptiveOptional.of(mac));
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferValue);
        appender.appendComponent(buffer, messageMeta);

        final EmvConfigurationMessage<IccCurrencyDataObjectsTable> expectedMessage
                = EmvConfigurationMessage.<IccCurrencyDataObjectsTable>builder()
                .withLuno(messageMeta.getLuno())
                .withResponseFlag(messageMeta.getResponseFlag())
                .withConfigurationData(new IccCurrencyDataObjectsTable(fakeEntryReader.getEntries()))
                .withMac(mac)
                .build();

        verify(messageListenerMock, times(1))
                .onCurrencyDataObjectsTableMessage(expectedMessage);
    }

    @Test
    public void should_throw_exception_on_mac_read_attempt_failure() {
        final NdcCharBuffer charBuffer = NdcCharBuffer.wrap("0101" + BmpStringGenerator.HEX.fixedLength(8));
        when(macReaderMock.tryReadMac(any()))
                .thenReturn(DescriptiveOptional.empty());

        Assertions.assertThatThrownBy(() -> appender.appendComponent(charBuffer, messageMeta))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(MacReaderBase.FIELD_NAME);

        verifyNoInteractions(messageListenerMock);
    }

    private static final class FakeEntryReader implements NdcComponentReader<DescriptiveOptional<CurrencyTableEntry>> {
        private final List<CurrencyTableEntry> entries = new LinkedList<>();
        private int entryType = 1;

        @Override
        public DescriptiveOptional<CurrencyTableEntry> readComponent(NdcCharBuffer ndcCharBuffer) {
            final IStringGenerator hex = BmpStringGenerator.HEX;
            final TransactionCurrencyCode transactionCurrencyCode = new TransactionCurrencyCode(hex.fixedLength(4));
            final TransactionCurrencyExponent transactionCurrencyExponent = new TransactionCurrencyExponent(hex.fixedLength(2));
            final ResponseFormat2 responseFormat2 = new ResponseFormat2(List.of(transactionCurrencyCode, transactionCurrencyExponent));
            final CurrencyTableEntry entry = new CurrencyTableEntry(entryType++, responseFormat2);

            entries.add(entry);
            return DescriptiveOptional.of(entry);
        }

        public Collection<CurrencyTableEntry> getEntries() {
            return entries;
        }
    }
}
