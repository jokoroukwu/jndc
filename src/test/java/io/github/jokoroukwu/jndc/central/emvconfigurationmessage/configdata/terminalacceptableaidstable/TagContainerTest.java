package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.List;

public class TagContainerTest {

    @DataProvider
    public static Object[][] tagProvider() {
        return new Object[][]{
                {List.of(0xAD), "01AD"},
                {List.of(0x00, 0xFF), "0200FF"},
                {List.of(), "00"},
        };
    }

    @Test
    public void should_throw_exception_when_negative_tag_added() {
        final Collection<Integer> integers = List.of(-1);
        Assertions.assertThatThrownBy(() -> new TagContainer(integers))
                .as("constructor method")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test(dataProvider = "tagProvider")
    public void should_return_expected_ndc_string(List<Integer> tags, String expectedNdcString) {
        final TagContainer tagContainer = new TagContainer(tags);
        Assertions.assertThat(tagContainer.toNdcString())
                .isEqualTo(expectedNdcString);
    }
}
