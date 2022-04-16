package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.Integers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum NdcNoteReader implements NdcComponentReader<List<NdcNote>> {

    INSTANCE;

    @Override
    public List<NdcNote> readComponent(NdcCharBuffer ndcCharBuffer) {
        final int numberOfNoteTypesReported = ndcCharBuffer.tryReadInt(3)
                .mapDescription("'Number of Note Types Reported': "::concat)
                .getOrThrow(NdcMessageParseException::new);

        return readNotes(ndcCharBuffer, numberOfNoteTypesReported);
    }

    private List<NdcNote> readNotes(NdcCharBuffer ndcCharBuffer, int numberOfNotes) {
        if (numberOfNotes == 0) {
            return List.of();
        }
        final List<NdcNote> notes = new ArrayList<>(numberOfNotes);
        for (int i = 0; i < numberOfNotes; i++) {
            final NdcNote note = readNote(ndcCharBuffer);
            notes.add(note);
        }
        return Collections.unmodifiableList(notes);
    }

    private NdcNote readNote(NdcCharBuffer ndcCharBuffer) {
        final int noteTypeId = ndcCharBuffer.tryReadHexInt(4)
                .filter(Integers::isPositive,
                        val -> () -> String.format("should be in range 0x0001 to 0xFFFF hex but was 0x%X", val))
                .mapDescription("'Note Type Identifier': "::concat)
                .getOrThrow(NdcMessageParseException::new);

        return ndcCharBuffer.tryReadInt(5)
                .mapToObject(numberOfNotes -> new NdcNote(noteTypeId, numberOfNotes, null))
                .wrapDescription("'Number of notes': "::concat)
                .getOrThrow(NdcMessageParseException::new);
    }
}
