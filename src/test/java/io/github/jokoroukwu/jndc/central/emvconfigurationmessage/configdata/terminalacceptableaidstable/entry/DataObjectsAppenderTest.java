package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.entry;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.DataObjectsAppender;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.TagContainer;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.TerminalApplicationIdEntryBuilder;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.tlv.BerTlvReader;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;
import io.github.jokoroukwu.jndc.util.optional.EmptyDescriptionSupplier;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.function.BiConsumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class DataObjectsAppenderTest extends AbstractFieldAppenderTest {
    private String fieldName;
    private DataObjectsAppender appender;
    private BiConsumer<TerminalApplicationIdEntryBuilder, TagContainer> consumerMock;
    private BerTlvReader berTlvReaderMock;

    @DataProvider
    public static Object[][] numberOfTagsProvider() {
        return new Object[][]{
                {0},
                {1},
                {2}
        };
    }

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        fieldName = BmpStringGenerator.ALPHANUMERIC.fixedLength(10);
        consumerMock = mock(BiConsumer.class);
        berTlvReaderMock = mock(BerTlvReader.class);
        appender = new DataObjectsAppender(fieldName, berTlvReaderMock, consumerMock, nextAppenderMock);
    }

    @Test(dataProvider = "numberOfTagsProvider")
    public void should_append_expected_tags_of_times(int numberOfTags) {
        final int dummyTag = 0xFF;
        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(numberOfTags));

        when(berTlvReaderMock.tryReadTag(any()))
                .thenReturn(DescriptiveOptionalInt.of(dummyTag));

        appender.appendComponent(bufferMock, entryBuilderMock);

        verify(bufferMock, times(1))
                .tryReadHexInt(2);

        verify(berTlvReaderMock, times(numberOfTags))
                .tryReadTag(bufferMock);

        verify(nextAppenderMock, times(1))
                .appendComponent(bufferMock, entryBuilderMock);

        verifyNoMoreInteractions(berTlvReaderMock, bufferMock, nextAppenderMock);
    }

    @Test(dataProvider = "numberOfTagsProvider")
    public void should_append_expected_tag_container(int numberOfTags) {
        final int dummyTag = 0xFF;
        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(numberOfTags));

        when(berTlvReaderMock.tryReadTag(any()))
                .thenReturn(DescriptiveOptionalInt.of(dummyTag));

        appender.appendComponent(bufferMock, entryBuilderMock);

        verify(consumerMock, times(1))
                .accept(entryBuilderMock, new TagContainer(Collections.nCopies(numberOfTags, dummyTag)));

        verifyNoMoreInteractions(consumerMock);
    }

    @Test
    public void should_throw_exception_on_empty_tag() {
        when(bufferMock.tryReadHexInt(anyInt()))
                .thenReturn(DescriptiveOptionalInt.of(1));

        when(berTlvReaderMock.tryReadTag(any()))
                .thenReturn(DescriptiveOptionalInt.empty(EmptyDescriptionSupplier.INSTANCE));

        Assertions.assertThatThrownBy(() -> appender.appendComponent(bufferMock, entryBuilderMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(fieldName);

        verify(bufferMock, times(1))
                .tryReadHexInt(2);

        verify(berTlvReaderMock, times(1))
                .tryReadTag(bufferMock);

        verifyNoMoreInteractions(bufferMock, berTlvReaderMock);
        verifyNoInteractions(nextAppenderMock, entryBuilderMock);
    }


}
