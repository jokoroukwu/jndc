package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage;

import io.github.jokoroukwu.jndc.AppenderFactoryBase;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.BnaSettings;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.CashHandlers;
import io.github.jokoroukwu.jndc.terminal.*;
import io.github.jokoroukwu.jndc.terminal.meta.TerminalMessageMeta;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class TransactionRequestIntegrationTest implements DeviceConfigurationSupplier<TerminalMessageMeta>, TerminalMessageListener {
    private DeviceConfiguration deviceConfiguration;
    private TerminalMessagePreProcessor messageProcessor;
    private TransactionRequestMessage transactionRequestMessage;

    @BeforeClass
    public void setUp() {
        deviceConfiguration = new DeviceConfigurationBase(true,
                Set.of('U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', '5', 'w', 'a', 'c', 'd', 'e', 'f', 'g', '<', '3'),
                ConfigurationOptions.of(BnaSettings.ACCEPT_MAX_NOTES, CashHandlers.DEFAULT));

        final Map<TerminalMessageSubClass, NdcComponentAppender<TerminalMessageMeta>> appenderMap =
                new EnumMap<>(TerminalMessageSubClass.class);
        appenderMap.put(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE, new TransactionRequestMessageAppender(this, this));
        messageProcessor = new TerminalMessagePreProcessor(new AppenderFactoryBase<>(Collections.emptyMap()),
                new AppenderFactoryBase<>(appenderMap));
    }

    @Override
    public DeviceConfiguration getConfiguration(TerminalMessageMeta meta) {
        return deviceConfiguration;
    }

    @Override
    public void onTransactionRequestMessage(TransactionRequestMessage message) {
        this.transactionRequestMessage = message;
    }

    @DataProvider
    public Object[][] validDataProvider() {
        final String type1 = "11" +
                "\u001C444555444" +
                "\u001C\u001C0000014B" +
                "\u001C15" +
                "\u001C;9999999900000073=25081010445000000000?" +
                "\u001C\u001CI   A DC" +
                "\u001C000000000000" +
                "\u001C<0494<076329<849" +
                "\u001C\u001C\u001C1%B9999999900000073^1/SVCRD 0099 00007" +
                "\u001C20209100000000000000000000000000000000000000000000020000000000000000000000" +
                "\u001C3012080823763" +
                "\u001CB3B9AA54";

        final String type2 = "11" +
                "\u001C444555444" +
                "\u001C\u001C0000014C" +
                "\u001C16" +
                "\u001C;9999999900000073=25081010445000000000?" +
                "\u001C\u001CDA  A DC" +
                "\u001C000000000000" +
                "\u001C4==<:=536?73:5:?" +
                "\u001C8000000" +
                "\u001C1200000" +
                "\u001C1%B9999999900000073^1/SVCRD 0099 00007          ^25081010000000000000000000000?" +
                "\u001C20001100000000000000000000000000000000000000000000000000000000000000000000" +
                "\u001C3012011413471" +
                "\u001CA00D2CF8";

        final String type3 = "11" +
                "\u001C444555444" +
                "\u001C\u001C0000014D" +
                "\u001C17" +
                "\u001C;9999999900000073=25081010445000000000?" +
                "\u001C\u001CDB  A DC" +
                "\u001C000000000000" +
                "\u001C3=>28<<;2<3?81;1" +
                "\u001C300000" +
                "\u001C900000" +
                "\u001C1%B9999999900000073^1/SVCRD 0099 00007^25081010000000000000000000000?" +
                "\u001C20002100000000000000000000000000000000000000000000000000000000000000000000" +
                "\u001C3012011413471" +
                "\u001C31FD8A30";

        final String type4 = "11" +
                //  luno
                "\u001C444555444" +
                //  time variant number
                "\u001C\u001C00000149" +
                // top of receipt flag + msg coordination number
                "\u001C13" +
                //  track2 data
                "\u001C;5484380018666453=23032010839000003690?" +
                "\u001C" +
                //  operation code data
                "\u001CBA  A CB" +
                //  amount entry field
                "\u001C000000000000" +
                //  pin buffer A
                "\u001C:821851239;=:=>:" +
                //  track1 buffer
                "\u001C\u001C\u001C1%B5484380018666453^UNLUCK/TOTAL  ^23032010839000004190?" +
                //  last transaction status data
                "\u001C20003000000000000000000000000000000000000000000000000000000000000000000000" +
                //  smart card data
                "\u001C5CAM0004" +
                "\u001C3110221349429" +
                //  mac
                "\u001CE39AB927";

        final String type5 = "11" +
                "\u001C444555444" +
                "\u001C\u001C0000014A" +
                "\u001C14" +
                "\u001C;5484380018666453=23032010839000003690?" +
                "\u001C\u001CADCAA CB" +
                "\u001C000000001200" +
                "\u001C:821851239;=:=>:" +
                "\u001C02020000" +
                "\u001CCO2.1st 0 1200 1" +
                "\u001C1%B5484380018666453^UNLUCK/TOTAL          ^23032010839000004190?" +
                "\u001C20093000000000000000000000000000000000000000000000000000000000000000000000" +
                "\u001DCAM" +
                "\u001D950580000400009B0268009F2701009F2608BB6AE243AD59F6479F10120210200003620000000000000000000000FF" +
                "\u001C5CAM00048407A00000000410109F090200029F33036040209F34034201009F3501145713548438001866645" +
                "3D23032010839000003690F5A0854843800186664535F3401019F2701809F26083CBB9927DD0B93F19F10120210A0000" +
                "3220000000000000000000000FF820239009F360200FB9A032110229C0101500A4D4153544552434152449F53015A9F1" +
                "A0206435F2A0206439F370459524D0D950580000400009F02060000001200009F03060000000000005F201A504553544" +
                "F562F414E54495020202020202020202020202020209F420206439F2103135621" +
                "\u001C3110221356431" +
                "\u001C2B062EA7";

        return new Object[][]{
                {"Type 4", type4},
                {"Type 5", type5},
                {"Type 1", type1},
                {"Type 2", type2},
                {"Type 3", type3}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_parse_message(String unusedDescription, String data) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(data);

        final SoftAssertions softly = new SoftAssertions();
        softly.assertThatCode(() -> messageProcessor.processMessage(buffer))
                .doesNotThrowAnyException();

        System.out.println(transactionRequestMessage);

        softly.assertThat(buffer.remaining())
                .as("zero characters should remain in data buffer")
                .isZero();

        softly.assertAll();
    }

    @Test(dataProvider = "validDataProvider")
    public void encoded_message_should_be_equal_to_original(String unusedDescription, String data) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(data);

        final SoftAssertions softly = new SoftAssertions();
        softly.assertThatCode(() -> messageProcessor.processMessage(buffer))
                .doesNotThrowAnyException();

        System.out.println(transactionRequestMessage);

        softly.assertThat(transactionRequestMessage.toNdcString())
                .as("marshalled message should be equal to original")
                .isEqualTo(data);

        softly.assertAll();
    }
}
