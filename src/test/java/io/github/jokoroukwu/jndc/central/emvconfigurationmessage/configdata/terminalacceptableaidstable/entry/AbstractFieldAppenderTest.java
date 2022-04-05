package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.entry;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.TerminalAcceptableAidsFieldAppender;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.TerminalApplicationIdEntryBuilder;
import org.testng.annotations.BeforeMethod;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractFieldAppenderTest {
    protected TerminalAcceptableAidsFieldAppender nextAppenderMock;
    protected NdcCharBuffer bufferMock;
    protected TerminalApplicationIdEntryBuilder entryBuilderMock;

    @BeforeMethod
    public void setMocks() {
        bufferMock = mock(NdcCharBuffer.class);
        entryBuilderMock = mock(TerminalApplicationIdEntryBuilder.class);
        nextAppenderMock = mock(TerminalAcceptableAidsFieldAppender.class);

        when(bufferMock.hasRemaining())
                .thenReturn(Boolean.TRUE);
        when(bufferMock.getCharAt(0))
                //  any char will do here since we're only verifying nextAppenderMock interactions
                //  through optionallyCallNextAppender() method call
                .thenReturn('.');
    }
}
