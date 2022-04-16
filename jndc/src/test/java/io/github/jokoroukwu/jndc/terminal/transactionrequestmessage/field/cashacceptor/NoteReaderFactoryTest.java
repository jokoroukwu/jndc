package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor;

import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.BnaSettings;
import io.github.jokoroukwu.jndc.terminal.ConfigurationOptions;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class NoteReaderFactoryTest {


    @DataProvider
    public static Object[][] optionsProvider() {
        return new Object[][]{
                {ConfigurationOptions.of(BnaSettings.ACCEPT_MAX_NOTES), ThreeDigitNoteReader.class},
                {ConfigurationOptions.of(BnaSettings.DEFAULT), TwoDigitNoteReader.class},
        };
    }

    @Test(dataProvider = "optionsProvider")
    public void should_return_expected_reader(ConfigurationOptions options, Class<?> expectedReaderClass) {
        Assertions.assertThat(BaseNoteReaderFactory.INSTANCE.getNoteReader(options))
                .isExactlyInstanceOf(expectedReaderClass);
    }

}
