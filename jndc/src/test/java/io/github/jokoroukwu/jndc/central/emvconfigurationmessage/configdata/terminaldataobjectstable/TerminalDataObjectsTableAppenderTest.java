package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminaldataobjectstable;

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
import io.github.jokoroukwu.jndc.tlv.TerminalCountryCode;
import io.github.jokoroukwu.jndc.tlv.TerminalType;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TerminalDataObjectsTableAppenderTest {
    private final CentralMessageMeta messageMeta
            = new CentralMessageMeta(CentralMessageClass.EMV_CONFIGURATION, '0', Luno.DEFAULT);
    private final ResponseFormat2 responseFormat2Dummy = new ResponseFormat2(List.of(
            new TerminalType("AB"), new TerminalCountryCode("ABCD")));
    private MacReader macReaderMock;
    private TerminalDataObjectsTableMessageListener messageListenerMock;
    private NdcComponentReader<DescriptiveOptional<ResponseFormat2>> responseFormat2ReaderMock;
    private TerminalDataObjectsTableAppender appender;


    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        macReaderMock = mock(MacReader.class);
        responseFormat2ReaderMock = mock(NdcComponentReader.class);
        messageListenerMock = mock(TerminalDataObjectsTableMessageListener.class);

        appender = new TerminalDataObjectsTableAppender(messageListenerMock, responseFormat2ReaderMock, macReaderMock);
    }

    @Test
    public void should_pass_expected_message_to_listener() {
        when(responseFormat2ReaderMock.readComponent(any()))
                .thenReturn(DescriptiveOptional.of(responseFormat2Dummy));

        appender.appendComponent(NdcCharBuffer.EMPTY, messageMeta);

        final IccTerminalDataObjectsTable table = new IccTerminalDataObjectsTable(responseFormat2Dummy);
        final EmvConfigurationMessage<IccTerminalDataObjectsTable> expectedMessage
                = EmvConfigurationMessage.<IccTerminalDataObjectsTable>builder()
                .withLuno(messageMeta.getLuno())
                .withResponseFlag(messageMeta.getResponseFlag())
                .withConfigurationData(table)
                .build();

        verify(responseFormat2ReaderMock, times(1))
                .readComponent(NdcCharBuffer.EMPTY);
        verify(messageListenerMock, times(1))
                .onTerminalDataObjectsTableMessage(expectedMessage);

        verifyNoInteractions(macReaderMock);
    }

    @Test
    public void should_read_mac_when_present() {
        final String mac = BmpStringGenerator.HEX.fixedLength(8);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(mac);
        when(responseFormat2ReaderMock.readComponent(any()))
                .thenReturn(DescriptiveOptional.of(responseFormat2Dummy));
        when(macReaderMock.tryReadMac(any()))
                .thenReturn(DescriptiveOptional.of(mac));

        appender.appendComponent(buffer, messageMeta);

        final IccTerminalDataObjectsTable table = new IccTerminalDataObjectsTable(responseFormat2Dummy);
        final EmvConfigurationMessage<IccTerminalDataObjectsTable> expectedMessage = EmvConfigurationMessage.<IccTerminalDataObjectsTable>builder()
                .withLuno(messageMeta.getLuno())
                .withResponseFlag(messageMeta.getResponseFlag())
                .withConfigurationData(table)
                .withMac(mac)
                .build();

        verify(responseFormat2ReaderMock, times(1))
                .readComponent(buffer);
        verify(messageListenerMock, times(1))
                .onTerminalDataObjectsTableMessage(expectedMessage);
        verify(macReaderMock, times(1))
                .tryReadMac(buffer);
    }

    @Test
    public void should_throw_expected_exception_when_response_format_2_reader_returns_no_result() {
        final String description = BmpStringGenerator.HEX.fixedLength(10);
        when(responseFormat2ReaderMock.readComponent(any()))
                .thenReturn(DescriptiveOptional.empty(() -> description));

        Assertions.assertThatThrownBy(() -> appender.appendComponent(NdcCharBuffer.EMPTY, messageMeta))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContainingAll(description, TerminalDataObjectsTableAppender.FIELD_NAME);

        verifyNoInteractions(macReaderMock, messageListenerMock);
    }


    @Test
    public void should_throw_expected_exception_when_mac_reader_returns_no_result() {
        final String description = BmpStringGenerator.HEX.fixedLength(10);
        when(responseFormat2ReaderMock.readComponent(any()))
                .thenReturn(DescriptiveOptional.of(responseFormat2Dummy));
        when(macReaderMock.tryReadMac(any()))
                .thenReturn(DescriptiveOptional.empty(() -> description));

        Assertions.assertThatThrownBy(() -> appender.appendComponent(NdcCharBuffer.wrap("MAC"), messageMeta))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContainingAll(description, MacReaderBase.FIELD_NAME);

        verifyNoInteractions(messageListenerMock);
    }
}
