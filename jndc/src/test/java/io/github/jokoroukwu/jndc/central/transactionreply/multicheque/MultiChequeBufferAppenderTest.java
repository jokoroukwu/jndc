package io.github.jokoroukwu.jndc.central.transactionreply.multicheque;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyAppenderTest;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.Strings;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MultiChequeBufferAppenderTest extends TransactionReplyAppenderTest {
    private final Cheque dummyCheque = new Cheque(0, 15, ChequeStamp.NO_STAMP, Strings.EMPTY_STRING);
    private NdcComponentReader<List<Cheque>> chequeReaderMock;
    private MultiChequeBufferAppender appender;


    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        chequeReaderMock = mock(NdcComponentReader.class);
        appender = new MultiChequeBufferAppender(chequeReaderMock, nextAppenderMock);
    }


    @Test
    public void should_append_expected_value() {
        when(chequeReaderMock.readComponent(any()))
                .thenReturn(List.of(dummyCheque));
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + MultiChequeBuffer.ID + "A");
        appender.appendComponent(buffer, builder, deviceConfigurationMock);

        verify(chequeReaderMock, times(1))
                .readComponent(buffer);

        Assertions.assertThat(builder.getMultiChequeBuffer())
                .isEqualTo(MultiChequeBuffer.of(dummyCheque));
    }

    @Test
    public void should_call_next_appender() {
        when(chequeReaderMock.readComponent(any()))
                .thenReturn(List.of(dummyCheque));
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + MultiChequeBuffer.ID + "A");
        appender.appendComponent(buffer, builder, deviceConfigurationMock);

        verify(nextAppenderMock, times(1))
                .appendComponent(buffer, builder, deviceConfigurationMock);
    }
}
