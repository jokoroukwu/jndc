package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload;

import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommandSubClass;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.CustomisationDataCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.MessageId;
import io.github.jokoroukwu.jndc.util.CollectionUtils;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class FitDataLoadCommand extends CustomisationDataCommand {
    public static final String COMMAND_NAME = CentralMessageClass.DATA_COMMAND
            + ": " + DataCommandSubClass.CUSTOMISATION_DATA
            + ": " + MessageId.FIT_DATA;

    private final List<Fit> fits;
    private final String mac;

    public FitDataLoadCommand(List<Fit> fits, String mac) {
        super(MessageId.FIT_DATA);
        this.fits = List.copyOf(CollectionUtils.requireNonNullNonEmpty(fits, "FITs"));
        this.mac = ObjectUtils.validateNotNull(mac, "mac");
    }

    public FitDataLoadCommand(List<Fit> fits) {
        this(fits, Strings.EMPTY_STRING);
    }

    FitDataLoadCommand(List<Fit> fits, String mac, Void unused) {
        super(MessageId.FIT_DATA);
        this.fits = fits;
        this.mac = mac;
    }

    public static FitDataLoadCommandBuilder builder() {
        return new FitDataLoadCommandBuilder();
    }

    public FitDataLoadCommandBuilder copy() {
        return new FitDataLoadCommandBuilder()
                .withMac(mac)
                .withFits(fits);
    }

    public List<Fit> getFits() {
        return Collections.unmodifiableList(fits);
    }

    public String getMac() {
        return mac;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", FitDataLoadCommand.class.getSimpleName() + ": {", "}")
                .add("messageIdentifier: " + getMessageIdentifier())
                .add("fits: " + fits)
                .add("mac: '" + mac + "'")
                .toString();
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(((Fit.STRING_BUILDER_CAPACITY + 3) * fits.size()) + mac.length() + 1)
                .appendComponent(messageIdentifier)
                .appendFs()
                .appendComponents(fits, NdcConstants.FIELD_SEPARATOR_STRING)
                .appendFieldGroup(mac)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FitDataLoadCommand that = (FitDataLoadCommand) o;
        return getMessageIdentifier() == that.getMessageIdentifier() &&
                fits.equals(that.fits) &&
                mac.equals(that.mac);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageIdentifier, fits, mac);
    }


}
