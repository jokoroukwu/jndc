package io.github.jokoroukwu.jndc.terminal.statusmessage;

import io.github.jokoroukwu.jndc.central.transactionreply.smartcard.ScriptId;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.terminal.completiondata.ProcessingResult;
import io.github.jokoroukwu.jndc.terminal.completiondata.ScriptResult;
import io.github.jokoroukwu.jndc.tlv.TransactionCategoryCode;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.testng.annotations.BeforeMethod;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;

public abstract class DeviceStatusInformationTest {
    protected final TransactionCategoryCode dummyCurrencyCodeTlv = new TransactionCategoryCode("AB");
    protected final CompletionData dummyCompletionData = new CompletionData(Map.of(dummyCurrencyCodeTlv.getTag(), dummyCurrencyCodeTlv),
            List.of(new ScriptResult(ProcessingResult.SUCCESS, 1,
                    new ScriptId(BmpStringGenerator.HEX.fixedLength(8)))));

    protected DeviceConfiguration deviceConfigurationMock;
    protected ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<?>> nextAppenderMock;


    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void baseSetUp() {
        deviceConfigurationMock = mock(DeviceConfiguration.class);
        nextAppenderMock = mock(ConfigurableNdcComponentAppender.class);
    }
}
