package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.entry;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.Aid;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.AidReader;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.SecondaryAIDsAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;
import io.github.jokoroukwu.jndc.util.optional.EmptyDescriptionSupplier;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class SecondaryAidsAppenderTest extends AbstractFieldAppenderTest {
    private final Aid dummyAid = new Aid("12");
    private AidReader aidReaderMock;
    private SecondaryAIDsAppender appender;

    @BeforeMethod
    public void setUp() {
        aidReaderMock = mock(AidReader.class);
        appender = new SecondaryAIDsAppender(nextAppenderMock, aidReaderMock);
    }


    @Test
    public void should_throw_exception_when_number_of_aids_not_present() {
        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.empty(EmptyDescriptionSupplier.INSTANCE));


        Assertions.assertThatThrownBy(() -> appender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class);

        verify(bufferMock, times(1))
                .tryReadHexInt(2);

        verifyNoMoreInteractions(bufferMock);
        verifyNoInteractions(nextAppenderMock, entryBuilderMock, aidReaderMock);
    }

    @Test
    public void should_throw_exception_when_aid_not_present() {
        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(1));

        when(aidReaderMock.readAid(any()))
                .thenReturn(DescriptiveOptional.empty());


        Assertions.assertThatThrownBy(() -> appender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(SecondaryAIDsAppender.FIELD_NAME);

        verify(bufferMock, times(1))
                .tryReadHexInt(2);

        verify(aidReaderMock, times(1))
                .readAid(bufferMock);

        verifyNoMoreInteractions(bufferMock, aidReaderMock);
        verifyNoInteractions(nextAppenderMock, entryBuilderMock);
    }

}
