package io.github.jokoroukwu.jndc.tlv;

import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AbstractBerTlvTest {
    private FakerBerTlv fakerBerTlv;

    @DataProvider
    public static Object[][] lengthProvider() {
        return new Object[][]{
                {0, "00"},
                {1, "01"},
                {127, "7F"},
                {126, "7E"},
                {128, "8180"},
                {0x1FFFFFFF, "841FFFFFFF"},
                {Integer.MAX_VALUE, "847FFFFFFF"}
        };
    }

    @BeforeMethod
    public void setUp() {
        fakerBerTlv = new FakerBerTlv();
    }

    @Test(dataProvider = "lengthProvider")
    public void should_return_expected_encoded_length(int length, String expectedLengthString) {
        Assertions.assertThat(fakerBerTlv.encodeLength(length))
                .isEqualTo(expectedLengthString);
    }

    private static final class FakerBerTlv extends AbstractBerTlv<Void> {

        @Override
        public String toNdcString() {
            return null;
        }

        @Override
        public int getLength() {
            return 0;
        }

        @Override
        public Void getValue() {
            return null;
        }

        @Override
        public int getTag() {
            return 0;
        }

    }
}
