package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload;


import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommand;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommandBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.FitNumberAppender;
import io.github.jokoroukwu.jndc.mac.MacReader;
import io.github.jokoroukwu.jndc.mac.MacReaderBase;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.ArrayList;
import java.util.Collections;

import static io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand.COMMAND_NAME;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class FitDataLoadCommandAppender implements NdcComponentAppender<DataCommandBuilder<NdcComponent>> {
    private final NdcComponentAppender<FitBuilder> fieldAppender;
    private final FitDataLoadCommandListener messageListener;
    private final MacReader macReader;

    public FitDataLoadCommandAppender(NdcComponentAppender<FitBuilder> fieldAppender,
                                      FitDataLoadCommandListener messageListener,
                                      MacReader macReader) {
        this.fieldAppender = ObjectUtils.validateNotNull(fieldAppender, "fieldAppender");
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        this.macReader = ObjectUtils.validateNotNull(macReader, "macAppender");
    }

    public FitDataLoadCommandAppender(FitDataLoadCommandListener messageListener) {
        this(new FitNumberAppender(), messageListener, MacReaderBase.INSTANCE);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void appendComponent(NdcCharBuffer buffer, DataCommandBuilder<NdcComponent> stateObject) {
        final ArrayList<Fit> fits = new ArrayList<>();
        //  at least one FIT should be present
        do {
            final FitBuilder fitBuilder = new FitBuilder();
            fieldAppender.appendComponent(buffer, fitBuilder);
            fits.add(fitBuilder.buildWithNoValidation());
        } while (buffer.remaining() > 9);

        final String mac = readMac(buffer);
        fits.trimToSize();

        final FitDataLoadCommand fitDataLoadCommand
                = new FitDataLoadCommand(Collections.unmodifiableList(fits), mac, null);

        final DataCommand<? extends NdcComponent> message = stateObject
                .withCommandData(fitDataLoadCommand)
                .build();
        messageListener.onFitDataLoadCommand((DataCommand<FitDataLoadCommand>) message);
    }


    private String readMac(NdcCharBuffer buffer) {
        if (buffer.hasRemaining()) {
            return macReader.tryReadMac(buffer)
                    .getOrThrow(errorMessage -> withMessage(COMMAND_NAME, MacReader.FIELD_NAME, errorMessage, buffer));
        }
        return Strings.EMPTY_STRING;
    }
}
