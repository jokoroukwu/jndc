package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.transactiondataobjectstable;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.CentralMessageMeta;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.mac.MacReader;
import io.github.jokoroukwu.jndc.tlv.ResponseFormat2;
import io.github.jokoroukwu.jndc.tlv.TransactionCategoryCode;
import io.github.jokoroukwu.jndc.tlv.TransactionType;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class IccTransactionDataObjectsTableAppenderTest {
    private final CentralMessageMeta messageMeta
            = new CentralMessageMeta(CentralMessageClass.EMV_CONFIGURATION, '0', Luno.DEFAULT);

    private final String mac = BmpStringGenerator.HEX.fixedLength(8);
    private final TransactionTableEntry dummyEntry = new TransactionTableEntry(1,
            new ResponseFormat2(List.of(new TransactionType("AB"), new TransactionCategoryCode("AB"))));
    private TransactionDataObjectsTableMessageListener messageListenerMock;
    private NdcComponentReader<DescriptiveOptional<TransactionTableEntry>> entryReaderMock;
    private MacReader macReaderMock;
    private IccTransactionDataObjectsTableAppender appender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        messageListenerMock = mock(TransactionDataObjectsTableMessageListener.class);
        entryReaderMock = mock(NdcComponentReader.class);
        macReaderMock = mock(MacReader.class);
        appender = new IccTransactionDataObjectsTableAppender(messageListenerMock, entryReaderMock, macReaderMock);
    }

    @DataProvider
    public Object[][] numberOfEntriesProvider() {
        return new Object[][]{
                {1},
                {2},
                {IccTransactionDataObjectsTable.MAX_SIZE}
        };
    }

    @Test(dataProvider = "numberOfEntriesProvider")
    public void should_pass_expected_message_to_listener(int numberOfEntries) {
        when(entryReaderMock.readComponent(any()))
                .thenReturn(DescriptiveOptional.of(dummyEntry));
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(Integers.toEvenLengthHexString(numberOfEntries));

        appender.appendComponent(buffer, messageMeta);

        final Collection<TransactionTableEntry> entries = Collections.nCopies(numberOfEntries, dummyEntry);
        final IccTransactionDataObjectsTable table = new IccTransactionDataObjectsTable(entries);
        final EmvConfigurationMessage<IccTransactionDataObjectsTable> expectedMessage = EmvConfigurationMessage.<IccTransactionDataObjectsTable>builder()
                .withResponseFlag(messageMeta.getResponseFlag())
                .withLuno(messageMeta.getLuno())
                .withConfigurationData(table)
                .build();

        verify(entryReaderMock, times(numberOfEntries))
                .readComponent(buffer);
        verify(messageListenerMock, times(1))
                .onTransactionDataObjectsTableMessage(expectedMessage);
        verifyNoInteractions(macReaderMock);
    }

    @Test
    public void should_read_mac_when_present() {
        when(entryReaderMock.readComponent(any()))
                .thenReturn(DescriptiveOptional.of(dummyEntry));
        when(macReaderMock.tryReadMac(any()))
                .thenReturn(DescriptiveOptional.of(mac));

        final NdcCharBuffer buffer = NdcCharBuffer.wrap("01MAC");

        appender.appendComponent(buffer, messageMeta);
        final IccTransactionDataObjectsTable table = IccTransactionDataObjectsTable.of(dummyEntry);
        final EmvConfigurationMessage<IccTransactionDataObjectsTable> expectedMessage = EmvConfigurationMessage.<IccTransactionDataObjectsTable>builder()
                .withResponseFlag(messageMeta.getResponseFlag())
                .withLuno(messageMeta.getLuno())
                .withConfigurationData(table)
                .withMac(mac)
                .build();
        verify(macReaderMock, times(1))
                .tryReadMac(buffer);
        verify(messageListenerMock, times(1))
                .onTransactionDataObjectsTableMessage(expectedMessage);
    }

    @Test
    public void should_throw_exception_on_invalid_number_of_entries() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("00");

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageMeta))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining("number of entries");


        verifyNoInteractions(macReaderMock, messageListenerMock, entryReaderMock);
    }

    @Test
    public void should_throw_expected_exception_when_entry_reader_returns_no_result() {
        final String description = BmpStringGenerator.HEX.fixedLength(10);
        when(entryReaderMock.readComponent(any()))
                .thenReturn(DescriptiveOptional.empty(() -> description));

        final NdcCharBuffer buffer = NdcCharBuffer.wrap("01");
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageMeta))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(description);

        verifyNoInteractions(macReaderMock, messageListenerMock);
    }

    @Test
    public void should_throw_expected_exception_when_buffer_returns_no_result() {
        final String description = BmpStringGenerator.HEX.fixedLength(10);
        final NdcCharBuffer bufferMock = mock(NdcCharBuffer.class);
        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.empty(() -> description));

        Assertions.assertThatThrownBy(() -> appender.appendComponent(bufferMock, messageMeta))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(description);

        verifyNoInteractions(macReaderMock, entryReaderMock, messageListenerMock);
    }

    @Test
    public void should_throw_expected_exception_when_mac_reader_returns_no_result() {
        when(entryReaderMock.readComponent(any()))
                .thenReturn(DescriptiveOptional.of(dummyEntry));
        final String description = BmpStringGenerator.HEX.fixedLength(10);
        when(macReaderMock.tryReadMac(any()))
                .thenReturn(DescriptiveOptional.empty(() -> description));

        final NdcCharBuffer buffer = NdcCharBuffer.wrap("01MAC");
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageMeta))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(description);

        verifyNoInteractions(messageListenerMock);
    }
}
