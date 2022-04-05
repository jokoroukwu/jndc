package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.tlv.ResponseFormat2;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class DataObjectsTableEntryReaderTest {
    private final ResponseFormat2 dummyDataObjects = new ResponseFormat2(List.of());
    private DataObjectsTableEntryFactory<NdcComponent> factoryMock;
    private NdcComponent ndcComponentMock;
    private NdcComponentReader<DescriptiveOptional<ResponseFormat2>> readerMock;
    private DataObjectsTableEntryReader<NdcComponent> reader;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        ndcComponentMock = mock(NdcComponent.class);
        factoryMock = mock(DataObjectsTableEntryFactory.class);
        when(factoryMock.getEntry(anyInt(), any()))
                .thenReturn(ndcComponentMock);

        readerMock = mock(NdcComponentReader.class);
        reader = new DataObjectsTableEntryReader<>(readerMock, factoryMock);
    }

    @Test
    public void should_return_component() {
        when(readerMock.readComponent(any()))
                .thenReturn(DescriptiveOptional.of(dummyDataObjects));

        final NdcCharBuffer buffer = NdcCharBuffer.wrap("01");
        final DescriptiveOptional<NdcComponent> result = reader.readComponent(buffer);

        Assertions.assertThat(result.isPresent())
                .withFailMessage("should have returned result but was: %s", result.description())
                .isTrue();
        Assertions.assertThat(result.get())
                .isEqualTo(ndcComponentMock);

        verify(readerMock, times(1))
                .readComponent(buffer);
        verify(factoryMock, times(1))
                .getEntry(1, dummyDataObjects);
    }


    @Test
    public void should_return_empty_result_on_invalid_entry_type() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("00");
        final DescriptiveOptional<NdcComponent> result = reader.readComponent(buffer);

        final SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result.isEmpty())
                .withFailMessage("should have returned empty result but was: %s", result)
                .isTrue();

        softly.assertThat(result.description())
                .contains("is not within valid 'Entry Type' range");

        softly.assertAll();

        verifyNoInteractions(readerMock, factoryMock);
    }

    @Test
    public void should_propagate_empty_result() {
        final String description = BmpStringGenerator.HEX.fixedLength(10);
        when(readerMock.readComponent(any()))
                .thenReturn(DescriptiveOptional.empty(() -> description));

        final NdcCharBuffer buffer = NdcCharBuffer.wrap("01");
        final DescriptiveOptional<NdcComponent> result = reader.readComponent(buffer);

        final SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result.isEmpty())
                .withFailMessage("should have returned empty result but was: %s", result)
                .isTrue();

        softly.assertThat(result.description())
                .isEqualTo(description);

        softly.assertAll();

        verify(readerMock, times(1))
                .readComponent(buffer);
        verifyNoInteractions(factoryMock);
    }

}
