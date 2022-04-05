package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.collection.LimitedSizeList;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class LimitedSizeListTest {


    @Test
    public void should_throw_exception_when_null_element_added() {
        Assertions.assertThatThrownBy(() -> new FakeLimitedSizeList(10, Arrays.asList(1, null)))
                .as("add() method")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void should_throw_exception_when_max_size_value_is_negative() {
        Assertions.assertThatThrownBy(() -> new FakeLimitedSizeList(-1, List.of(1)))
                .as("FakeLimitedSizeList(maxSize, elements) constructor")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void should_throw_exception_when_elements_exceed_size() {
        Assertions.assertThatThrownBy(() -> new FakeLimitedSizeList(1, List.of(1, 2)))
                .as("new FakeLimitedSizeList(index, collection) constructor")
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    public void should_not_throw_exception_when_elements_do_not_exceed_size() {
        Assertions.assertThatCode(() -> new FakeLimitedSizeList(1, List.of(1)))
                .as("new FakeLimitedSizeList(index, collection) constructor")
                .doesNotThrowAnyException();
    }


    private static final class FakeLimitedSizeList extends LimitedSizeList<Integer> {

        protected FakeLimitedSizeList(int maxSize, Collection<? extends Integer> collection) {
            super(maxSize, collection);
        }

        @Override
        protected void performElementChecks(Integer element) {

        }

    }
}
