package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreaderwriter;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.DeviceFaultTest;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.SuppliesStatus;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader.CardReaderWriterFaultBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader.CardReaderWriterSuppliesStatusAppender;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CardReaderWriterFaultSuppliesStatusAppenderTest extends DeviceFaultTest {
    private CardReaderWriterFaultBuilder builder;
    private CardReaderWriterSuppliesStatusAppender appender;


    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        nextAppenderMock = mock(ConfigurableNdcComponentAppender.class);
        appender = new CardReaderWriterSuppliesStatusAppender("commandName");
        builder = new CardReaderWriterFaultBuilder();
    }

    @DataProvider
    public Object[][] validDataProvider() {
        final String mac = NdcConstants.FIELD_SEPARATOR + BmpStringGenerator.HEX.fixedLength(8);
        return new Object[][]{
                {SuppliesStatus.OVERFILL.toNdcString() + mac, SuppliesStatus.OVERFILL, true},
                {SuppliesStatus.OVERFILL.toNdcString() + mac, SuppliesStatus.OVERFILL, false},
                {SuppliesStatus.OVERFILL.toNdcString(), null, true},
                {NdcConstants.FIELD_SEPARATOR_STRING, null, false}

        };
    }


    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_value(String validData, SuppliesStatus expectedValue, boolean isMacEnabled) {
        when(deviceConfigurationMock.isMacEnabled())
                .thenReturn(isMacEnabled);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR + validData);

        appender.appendComponent(buffer, builder, deviceConfigurationMock);
        Assertions.assertThat(builder.getSuppliesStatus())
                .isEqualTo(expectedValue);
    }
}
