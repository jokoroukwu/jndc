package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable;

import io.github.jokoroukwu.jndc.NdcComponentAppender;
import org.testng.annotations.BeforeMethod;

import static org.mockito.Mockito.mock;

public abstract class AbstractFieldAppenderTest {
    NdcComponentAppender<LanguageSupportTableEntryBuilder> fieldAppenderMock;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setMock() {
        fieldAppenderMock = mock(NdcComponentAppender.class);
    }
}
