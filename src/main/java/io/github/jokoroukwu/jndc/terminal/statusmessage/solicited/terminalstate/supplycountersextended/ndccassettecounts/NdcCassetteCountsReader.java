package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NdcCassetteCountsReader implements NdcComponentReader<List<NdcCassetteCounts>> {
    private final NdcComponentReader<List<NdcNote>> notesReader;

    public NdcCassetteCountsReader(NdcComponentReader<List<NdcNote>> notesReader) {
        this.notesReader = ObjectUtils.validateNotNull(notesReader, "notesReader");
    }

    public NdcCassetteCountsReader() {
        this(NdcNoteReader.INSTANCE);
    }

    @Override
    public List<NdcCassetteCounts> readComponent(NdcCharBuffer ndcCharBuffer) {
        final ArrayList<NdcCassetteCounts> cassettes = new ArrayList<>();
        do {
            final NdcCassetteCounts cassette = readCassette(ndcCharBuffer);
            cassettes.add(cassette);
        } while (ndcCharBuffer.hasFieldDataRemaining());

        cassettes.trimToSize();
        return Collections.unmodifiableList(cassettes);
    }


    private NdcCassetteCounts readCassette(NdcCharBuffer buffer) {
        final int cassetteType = buffer.tryReadInt(3)
                .filter(Integers::isPositive, val -> () -> "should be in range 1-999 but was " + val)
                .mapDescription("'NDC Cassette Type': "::concat)
                .getOrThrow(NdcMessageParseException::new);

        final int totalNotesInCassette = buffer.tryReadInt(5)
                .mapDescription("'Total Notes In Cassette': "::concat)
                .getOrThrow(NdcMessageParseException::new);

        final List<NdcNote> cassetteNotes = notesReader.readComponent(buffer);
        return new NdcCassetteCounts(cassetteType, totalNotesInCassette, cassetteNotes.size(), cassetteNotes);
    }
}
