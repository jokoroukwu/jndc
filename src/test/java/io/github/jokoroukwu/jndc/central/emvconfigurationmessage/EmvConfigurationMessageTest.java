package io.github.jokoroukwu.jndc.central.emvconfigurationmessage;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.ConfigurationData;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Objects;

public class EmvConfigurationMessageTest {

    @Test
    public void copy_should_return_equal_object() {
        final EmvConfigurationMessage<?> original = EmvConfigurationMessage.builder()
                .withLuno(Luno.DEFAULT)
                .withResponseFlag(BmpStringGenerator.HEX.randomChar())
                .withConfigurationData(new FakeConfigData())
                .withMac(BmpStringGenerator.HEX.fixedLength(8))
                .build();

        final EmvConfigurationMessage<?> copy = original.copy().build();
        Assertions.assertThat(copy)
                .isEqualTo(original);
    }

    @Test
    public void should_return_expected_ndc_string() {
        final String mac = BmpStringGenerator.HEX.fixedLength(8);
        final char responseFlag = BmpStringGenerator.HEX.randomChar();
        final ConfigurationData configurationData = new FakeConfigData();
        final EmvConfigurationMessage<?> message = EmvConfigurationMessage.builder()
                .withLuno(Luno.DEFAULT)
                .withResponseFlag(responseFlag)
                .withConfigurationData(configurationData)
                .withMac(mac)
                .build();
        final String expectedString = CentralMessageClass.EMV_CONFIGURATION.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + Luno.DEFAULT.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + configurationData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + mac;
        Assertions.assertThat(message.toNdcString())
                .isEqualTo(expectedString);
    }

    private static final class FakeConfigData implements ConfigurationData {
        private final EmvConfigMessageSubClass subClass = EmvConfigMessageSubClass.CURRENCY;

        @Override
        public String toNdcString() {
            return "null";
        }

        @Override
        public EmvConfigMessageSubClass getEmvConfigMessageSubClass() {
            return subClass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FakeConfigData that = (FakeConfigData) o;
            return subClass == that.subClass;
        }

        @Override
        public int hashCode() {
            return Objects.hash(subClass);
        }
    }
}
