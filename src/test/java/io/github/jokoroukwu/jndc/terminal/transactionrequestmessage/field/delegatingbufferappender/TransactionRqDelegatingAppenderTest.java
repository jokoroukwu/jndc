package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.delegatingbufferappender;

import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMsgAppenderTest;
import org.testng.annotations.BeforeMethod;

import static org.mockito.Mockito.mock;

public class TransactionRqDelegatingAppenderTest extends TransactionRequestMsgAppenderTest {
    private IdentifiableBufferAppenderSupplier<TransactionRequestMessageBuilder> appenderSupplierMock;
    private TransactionRqDelegatingAppender appender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        appenderSupplierMock = mock(IdentifiableBufferAppenderSupplier.class);
        appender = new TransactionRqDelegatingAppender(appenderSupplierMock, nextAppenderMock);
    }
}
