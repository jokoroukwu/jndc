package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.timeofdayclock;

import io.github.jokoroukwu.jndc.terminal.dig.Dig;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.ErrorSeverity;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.EnumSet;
import java.util.Iterator;

public class TimeOfDayClockFailureTest {

    @DataProvider
    public Iterator<Object[]> invalidErrorSeverityProvider() {
        return EnumSet.complementOf(EnumSet.of(ErrorSeverity.FATAL, ErrorSeverity.WARNING))
                .stream()
                .map(value -> new Object[]{value})
                .iterator();
    }

    @Test(dataProvider = "invalidErrorSeverityProvider")
    public void should_throw_expected_exception_on_invalid_error_severity(ErrorSeverity invalidErrorSeverity) {
        Assertions.assertThatThrownBy(() -> new TimeOfDayClockFailure(ClockDeviceStatus.RESET, invalidErrorSeverity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Error Severity");
    }

    @Test
    public void should_return_expected_ndc_string() {
        final String actualNdcString = new TimeOfDayClockFailure(ClockDeviceStatus.STOPPED, ErrorSeverity.FATAL).toNdcString();
        final String expectedNdcString = Dig.TIME_OF_DAY_CLOCK.getValue() + "2" + ErrorSeverity.FATAL.getValue();

        Assertions.assertThat(actualNdcString)
                .isEqualTo(expectedNdcString);
    }
}
