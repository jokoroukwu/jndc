package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.statetableload;


import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommand;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommandBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.MessageId;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.mac.MacReader;
import io.github.jokoroukwu.jndc.mac.MacReaderBase;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.LinkedList;
import java.util.Set;
import java.util.function.Predicate;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onNoFieldSeparator;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class StateTablesLoadCommandAppender implements NdcComponentAppender<DataCommandBuilder<NdcComponent>> {

    public static final Set<Character> VALID_STATE_TYPES;
    public static final String RESERVED_STATE_NUMBER;
    private static final String commandName;
    private static final String stateNumberField;
    private static final String stateTableDataField;
    private static final String stateTypeField;

    static {
        VALID_STATE_TYPES = Set.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z', 'b', 'd', 'e', 'f', 'g', 'k', 'm', 'w', 'z', '_', '&', '>');
        RESERVED_STATE_NUMBER = "255";
        stateTableDataField = "State Table Data";
        stateTypeField = stateTableDataField.concat(": 'State Type'");
        commandName = MessageId.STATE_TABLE.toString();
        stateNumberField = "State Number";
    }

    private final StateTablesLoadCommandListener stateTablesLoadCommandListener;
    private final MacReader macReader;

    public StateTablesLoadCommandAppender(StateTablesLoadCommandListener stateTablesLoadCommandListener) {
        this(stateTablesLoadCommandListener, MacReaderBase.INSTANCE);
    }

    public StateTablesLoadCommandAppender(StateTablesLoadCommandListener stateTablesLoadCommandListener, MacReader macReader) {
        this.stateTablesLoadCommandListener = ObjectUtils.validateNotNull(stateTablesLoadCommandListener, "stateTablesLoadCommandListener");
        this.macReader = ObjectUtils.validateNotNull(macReader, "macReader");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void appendComponent(NdcCharBuffer messageStream, DataCommandBuilder<NdcComponent> stateObject) {
        ObjectUtils.validateNotNull(messageStream, "messageStream");

        var states = new LinkedList<State>();
        //  must be at least 1 state
        do {
            messageStream.trySkipFieldSeparator()
                    .ifPresent(errorMessage -> onNoFieldSeparator(commandName, stateNumberField, errorMessage, messageStream));

            final String stateNumber = messageStream.tryReadCharSequence(3)
                    .filter(Predicate.not(RESERVED_STATE_NUMBER::equals),
                            val -> () -> String.format("%s is a reserved state number", RESERVED_STATE_NUMBER))
                    .filter(Strings::isAlphanumeric,
                            val -> () -> String.format("should be alphanumeric but was '%s'", val))
                    .getOrThrow(errorMessage -> withMessage(commandName, stateNumberField, errorMessage, messageStream));

            final char stateType = (char) messageStream.tryReadNextChar()
                    .filter(val -> VALID_STATE_TYPES.contains((char) val),
                            val -> () -> "does not match any of the valid types " + VALID_STATE_TYPES)
                    .getOrThrow(errorMessage -> withMessage(commandName, stateTypeField, errorMessage, messageStream));

            var stateData = messageStream.tryReadCharSequence(24)
                    .filter(Strings::isAlphanumeric, val -> () -> String.format("should be alphanumeric but was: '%s'", val))
                    .getOrThrow(errorMessage -> withMessage(commandName, stateTableDataField, errorMessage, messageStream));

            states.add(new State(stateNumber, Character.toString(stateType), stateData));

            //  total state fields' length + field separator symbol
        } while (messageStream.hasRemaining(29));

        final StateTablesLoadCommandBuilder stateTablesLoadCommandBuilder = StateTablesLoadCommand.builder()
                .withStates(states);

        //  check for MAC
        if (messageStream.hasRemaining()) {
            macReader.tryReadMac(messageStream)
                    .resolve(stateTablesLoadCommandBuilder::withMac, message
                            -> NdcMessageParseException.onFieldParseError(commandName, MacReaderBase.FIELD_NAME, message, messageStream));

        }
        final DataCommand<? extends NdcComponent> dataCommand = stateObject.withCommandData(stateTablesLoadCommandBuilder.build())
                .build();
        stateTablesLoadCommandListener.onStateTablesLoadCommand((DataCommand<StateTablesLoadCommand>) dataCommand);

    }

}
