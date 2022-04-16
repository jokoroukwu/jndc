package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.configidload;

import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.CustomisationDataCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.MessageId;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.StringJoiner;

public class ConfigurationIdNumberLoadCommand extends CustomisationDataCommand {
    public static final String COMMAND_NAME = "Configuration ID Number Load";
    private final int configIdNumber;

    protected ConfigurationIdNumberLoadCommand(int configIdNumber) {
        super(MessageId.CONFIG_ID_NUMBER);
        this.configIdNumber = configIdNumber;
    }

    public int getConfigIdNumber() {
        return configIdNumber;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConfigurationIdNumberLoadCommand.class.getSimpleName() + ": {", "}")
                .add("messageIdentifier: " + messageIdentifier)
                .add("configIdNumber: " + configIdNumber)
                .toString();
    }

    @Override
    public String toNdcString() {
        return messageIdentifier.toNdcString() +
                NdcConstants.FIELD_SEPARATOR +
                Strings.leftPad(configIdNumber, "0", 4);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigurationIdNumberLoadCommand that = (ConfigurationIdNumberLoadCommand) o;
        return configIdNumber == that.configIdNumber &&
                messageIdentifier == that.messageIdentifier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageIdentifier, configIdNumber);
    }

}
