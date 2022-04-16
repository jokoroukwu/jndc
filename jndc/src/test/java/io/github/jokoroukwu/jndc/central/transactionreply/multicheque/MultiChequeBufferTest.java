package io.github.jokoroukwu.jndc.central.transactionreply.multicheque;

import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.List;

public class MultiChequeBufferTest {
    private final Cheque dummyCheque = new Cheque(0, 0, ChequeStamp.STAMP,
            BmpStringGenerator.HEX.fixedLength(10));

    @DataProvider
    public Object[][] validDataProvider() {
        final String dummyChequeString = dummyCheque.toNdcString();
        return new Object[][]{
                {MultiChequeBuffer.of(dummyCheque), MultiChequeBuffer.ID + dummyChequeString},
                {MultiChequeBuffer.of(dummyCheque, dummyCheque), MultiChequeBuffer.ID + dummyChequeString + NdcConstants.GROUP_SEPARATOR + dummyChequeString}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_ndc_string(MultiChequeBuffer multiChequeBuffer, String expectedString) {
        Assertions.assertThat(multiChequeBuffer.toNdcString())
                .isEqualTo(expectedString);
    }

    @DataProvider
    public Object[][] invalidArgProvider() {
        return new Object[][]{
                {null},
                {List.of()}
        };
    }

    @Test(dataProvider = "invalidArgProvider")
    public void should_throw_expected_exception_on_invalid_constructor_arg(Collection<Cheque> cheques) {
        Assertions.assertThatThrownBy(() -> new MultiChequeBuffer(cheques))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
