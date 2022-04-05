package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.datetimeload;

import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.CustomisationDataCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.MessageId;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Objects;
import java.util.StringJoiner;

import static java.time.temporal.ChronoField.YEAR;

public class DateTimeLoadCommand extends CustomisationDataCommand {
    public static final DateTimeFormatter FORMATTER;
    public static final String COMMAND_NAME = "Date and Time Load";
    public static final String DATE_TIME_DATA_TAG = "Date/Time Data";

    static {
        FORMATTER = new DateTimeFormatterBuilder()
                .appendValueReduced(YEAR, 2, 2, 2000)
                .appendValue(ChronoField.MONTH_OF_YEAR, 2)
                .appendValue(ChronoField.DAY_OF_MONTH, 2)
                .appendValue(ChronoField.HOUR_OF_DAY, 2)
                .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
                .toFormatter();
    }

    private final LocalDateTime dateTime;

    public DateTimeLoadCommand(LocalDateTime dateTime) {
        super(MessageId.DATE_AND_TIME);
        this.dateTime = ObjectUtils.validateNotNull(dateTime, "dateTime");
    }


    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getFormattedDateTime() {
        return FORMATTER.format(dateTime);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DateTimeLoadCommand.class.getSimpleName() + ": {", "}")
                .add("dateTime: " + dateTime)
                .toString();
    }

    @Override
    public String toNdcString() {
        return NdcConstants.FIELD_SEPARATOR + getFormattedDateTime();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final DateTimeLoadCommand that = (DateTimeLoadCommand) o;
        return getMessageIdentifier().equals(that.getMessageIdentifier()) &&
                dateTime.equals(that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessageIdentifier(), dateTime);
    }

}
