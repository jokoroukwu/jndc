package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.notesdata;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBufferAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class NotesDataBufferAppender extends IdentifiableBufferAppender<TransactionRequestMessageBuilder> {
    private final String fieldName;
    private final char id;
    private final BiConsumer<TransactionRequestMessageBuilder, NotesDataBuffer> bufferConsumer;

    NotesDataBufferAppender(String fieldName,
                            char id,
                            ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender,
                            BiConsumer<TransactionRequestMessageBuilder, NotesDataBuffer> bufferConsumer) {
        super(nextAppender);
        this.fieldName = ObjectUtils.validateNotNull(fieldName, "fieldName cannot be null");
        this.bufferConsumer = ObjectUtils.validateNotNull(bufferConsumer, "bufferConsumer cannot be null");
        this.id = id;
    }

    public static NotesDataBufferAppender suspect(ChainedConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender) {
        return new NotesDataBufferAppender("Suspect Notes Data",
                NotesDataBuffer.SUSPECT_NOTES_DATA_ID,
                nextAppender,
                TransactionRequestMessageBuilder::withSuspectNotesBuffer);
    }

    public static NotesDataBufferAppender suspect() {
        return suspect(null);
    }

    public static NotesDataBufferAppender counterfeit(ChainedConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender) {
        return new NotesDataBufferAppender("Counterfeit Notes Data",
                NotesDataBuffer.COUNTERFEIT_NOTES_DATA_ID,
                nextAppender,
                TransactionRequestMessageBuilder::withCounterfeitNotesBuffer);
    }

    public static NotesDataBufferAppender counterfeit() {
        return counterfeit(null);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionRequestMessageBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        skipBufferMeta(ndcCharBuffer);

        final NotesDataBuffer notesDataBuffer = readNotesDataBuffer(ndcCharBuffer);
        bufferConsumer.accept(stateObject, notesDataBuffer);
        callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private NotesDataBuffer readNotesDataBuffer(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return new NotesDataBuffer(id, null);
        }
        final int numberOfNoteTypes = ndcCharBuffer.tryReadInt(2)
                .ifEmpty(errorMessage -> NdcMessageParseException.onNoFieldSeparator(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        fieldName + ": number of note types", errorMessage, ndcCharBuffer))
                .get();
        if (numberOfNoteTypes == 0) {
            return new NotesDataBuffer(id, Collections.emptyList());
        }
        final List<Note> notes = new ArrayList<>(numberOfNoteTypes);
        for (int i = 0; i < numberOfNoteTypes; i++) {
            notes.add(readNote(ndcCharBuffer));
        }
        return new NotesDataBuffer(id, notes);
    }

    private Note readNote(NdcCharBuffer ndcCharBuffer) {
        final int noteType = ndcCharBuffer.tryReadHexInt(4)
                .filter(this::isNoteTypeValid, type -> ()
                        -> String.format("Note type identifier should be within range 0x01-0xFF but was: %#X", type))
                .ifEmpty(errorMessage -> NdcMessageParseException.onNoFieldSeparator(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        fieldName + ": Note type identifier", errorMessage, ndcCharBuffer))
                .get();
        final int numberOfNotes = ndcCharBuffer.tryReadInt(3)
                .filter(this::isNumberOfNotesValid, number -> ()
                        -> "Number of notes should be within range 1-999 but was: " + number)
                .ifEmpty(errorMessage -> NdcMessageParseException.onNoFieldSeparator(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        fieldName + ": Number of notes", errorMessage, ndcCharBuffer))
                .get();
        return new Note(noteType, numberOfNotes, null);
    }

    private boolean isNoteTypeValid(int noteType) {
        return noteType > 0 && noteType <= 0xFF;
    }

    private boolean isNumberOfNotesValid(int numberOfNotes) {
        return numberOfNotes > 0;
    }

    @Override
    protected char getBufferId() {
        return id;
    }

    @Override
    protected String getFieldName() {
        return fieldName;
    }

    @Override
    protected String getCommandName() {
        return TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString();
    }
}
