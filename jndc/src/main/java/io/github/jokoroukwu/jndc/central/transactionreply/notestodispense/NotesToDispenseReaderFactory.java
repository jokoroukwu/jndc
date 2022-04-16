package io.github.jokoroukwu.jndc.central.transactionreply.notestodispense;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.CashHandlers;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.ConfigurationOption;
import io.github.jokoroukwu.jndc.exception.ConfigurationException;
import io.github.jokoroukwu.jndc.terminal.ConfigurationOptions;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor.NdcComponentReaderFactory;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.ArrayList;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withFieldName;

public enum NotesToDispenseReaderFactory implements NdcComponentReaderFactory<ConfigurationOptions, NotesToDispense> {
    INSTANCE;
    private static final NdcComponentReader<NotesToDispense> fourDigitNotesReader = new NotesReaderInternal(4);
    private static final NdcComponentReader<NotesToDispense> twoDigitNotesReader = new NotesReaderInternal(2);

    @Override
    public NdcComponentReader<NotesToDispense> getNoteReader(ConfigurationOptions configurationOptions) throws IllegalArgumentException, ConfigurationException {
        return configurationOptions
                .getOption(CashHandlers.NUMBER)
                .stream()
                .mapToInt(ConfigurationOption::getCode)
                .mapToObj(CashHandlers::forCode)
                .findAny()
                .orElseGet(() -> DescriptiveOptional.of(CashHandlers.DEFAULT))
                .map(this::cashHandlersOptionToReader)
                .getOrThrow(ConfigurationException::new);
    }

    private NdcComponentReader<NotesToDispense> cashHandlersOptionToReader(CashHandlers cashHandlersOption) {
        return cashHandlersOption.hasBit2() ? fourDigitNotesReader : twoDigitNotesReader;
    }

    private static final class NotesReaderInternal implements NdcComponentReader<NotesToDispense> {
        private static final String fieldName = "'Notes to Dispense'";
        private final int numberOfDigits;

        private NotesReaderInternal(int numberOfDigits) {
            this.numberOfDigits = numberOfDigits;
        }

        @Override
        public NotesToDispense readComponent(NdcCharBuffer ndcCharBuffer) {
            final ArrayList<Integer> notesToDispense = new ArrayList<>(NotesToDispense.MAX_SIZE);
            int cassetteType = 1;
            do {
                checkRange(cassetteType, ndcCharBuffer);
                final int nextNote = readNote(ndcCharBuffer, cassetteType++);
                notesToDispense.add(nextNote);
            } while (ndcCharBuffer.hasFieldDataRemaining());

            notesToDispense.trimToSize();
            return numberOfDigits == 2 ? new TwoDigitNotes(notesToDispense) : new FourDigitNotes(notesToDispense);
        }

        private int readNote(NdcCharBuffer ndcCharBuffer, int cassetteType) {
            return ndcCharBuffer.tryReadInt(numberOfDigits)
                    .getOrThrow(errorMessage
                            -> withFieldName(fieldName + " of cassette type " + cassetteType, errorMessage, ndcCharBuffer));
        }

        private void checkRange(int cassetteType, NdcCharBuffer ndcCharBuffer) {
            if (cassetteType > NotesToDispense.MAX_SIZE) {
                final String errorMessage = "cassette type %d exceeds max number of note cassette types (%d) at position %d";
                throw withFieldName(fieldName,
                        String.format(errorMessage, cassetteType, NotesToDispense.MAX_SIZE, ndcCharBuffer.position()),
                        ndcCharBuffer);
            }
        }
    }
}
