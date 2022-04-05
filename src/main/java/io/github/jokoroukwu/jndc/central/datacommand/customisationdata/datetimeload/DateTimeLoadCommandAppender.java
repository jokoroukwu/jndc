package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.datetimeload;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommand;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommandBuilder;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static io.github.jokoroukwu.jndc.central.datacommand.customisationdata.datetimeload.DateTimeLoadCommand.COMMAND_NAME;
import static io.github.jokoroukwu.jndc.central.datacommand.customisationdata.datetimeload.DateTimeLoadCommand.DATE_TIME_DATA_TAG;

public class DateTimeLoadCommandAppender implements NdcComponentAppender<DataCommandBuilder<NdcComponent>> {


    private final DateTimeLoadCommandListener dateTimeLoadCommandListener;

    public DateTimeLoadCommandAppender(DateTimeLoadCommandListener dateTimeLoadCommandListener) {
        this.dateTimeLoadCommandListener = ObjectUtils.validateNotNull(dateTimeLoadCommandListener, "dateTimeLoadCommandListener");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void appendComponent(NdcCharBuffer ndcCharBuffer, DataCommandBuilder<NdcComponent> stateObject) {

        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage -> NdcMessageParseException.onNoFieldSeparator(COMMAND_NAME, DATE_TIME_DATA_TAG,
                        errorMessage, ndcCharBuffer));


        final LocalDateTime parsedDateTime = ndcCharBuffer.tryReadCharSequence(10)
                .flatMap(this::tryParseDateTime)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(COMMAND_NAME, DATE_TIME_DATA_TAG,
                        errorMessage, ndcCharBuffer));

        final DataCommand<? super DateTimeLoadCommand> dateTimeLoadCommand =
                stateObject.withCommandData(new DateTimeLoadCommand(parsedDateTime))
                        .build();
        dateTimeLoadCommandListener.onDateTimeLoadCommand((DataCommand<DateTimeLoadCommand>) dateTimeLoadCommand);
    }


    private DescriptiveOptional<LocalDateTime> tryParseDateTime(String rawDateTime) {
        try {
            return DescriptiveOptional.of(LocalDateTime.parse(rawDateTime, DateTimeLoadCommand.FORMATTER));
        } catch (DateTimeParseException e) {
            final String errorMessage = "value '%s' is not of valid date-time format (YYMMDDHHMM): " + e.toString();
            return DescriptiveOptional.empty(() -> String.format(errorMessage, rawDateTime));
        }
    }
}
