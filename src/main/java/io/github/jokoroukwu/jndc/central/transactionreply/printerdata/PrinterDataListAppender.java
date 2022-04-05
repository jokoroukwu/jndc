package io.github.jokoroukwu.jndc.central.transactionreply.printerdata;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyCommandBuilder;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyDelegatingAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.ArrayList;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withFieldName;

public class PrinterDataListAppender extends ChainedConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> {

    public PrinterDataListAppender(ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender) {
        super(nextAppender);
    }

    public PrinterDataListAppender() {
        super(new TransactionReplyDelegatingAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionReplyCommandBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        final PrinterDataList printerDataList = readPrinterDataList(ndcCharBuffer);
        stateObject.withPrinterDataList(printerDataList);

        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private PrinterDataList readPrinterDataList(NdcCharBuffer ndcCharBuffer) {
        final ArrayList<PrinterData> printerDataList = new ArrayList<>();
        PrinterFlag printerFlag = readPrinterFlag(ndcCharBuffer);
        String printerData = readPrinterData(ndcCharBuffer);
        printerDataList.add(new PrinterData(printerFlag, printerData, null));

        int numberOfElements = 1;
        while (!ndcCharBuffer.hasFollowingFieldSeparator()) {
            checkOverflow(numberOfElements, ndcCharBuffer);
            ndcCharBuffer.trySkipGroupSeparator()
                    .ifPresent(errorMessage -> onFieldParseError("Printer Flag", errorMessage, ndcCharBuffer));

            printerFlag = readPrinterFlag(ndcCharBuffer);
            printerData = readPrinterData(ndcCharBuffer);
            printerDataList.add(new PrinterData(printerFlag, printerData, null));
        }
        printerDataList.trimToSize();
        return new PrinterDataList(printerDataList);
    }

    private PrinterFlag readPrinterFlag(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(PrinterFlag::forValue)
                .getOrThrow(errorMessage -> withFieldName("Printer Flag", errorMessage, ndcCharBuffer));
    }

    private String readPrinterData(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return Strings.EMPTY_STRING;
        }
        final StringBuilder dataBuilder = new StringBuilder(128);
        do {
            dataBuilder.append(ndcCharBuffer.readNextChar());
        } while (ndcCharBuffer.hasFieldDataRemaining());
        return dataBuilder.toString();
    }

    private void checkOverflow(int numberOfElements, NdcCharBuffer ndcCharBuffer) {
        if (numberOfElements > PrinterDataList.MAX_SIZE) {
            final String errorMessage = String.format("number of Printer elements (%d) exceeds max number (%d) at position %d",
                    numberOfElements, PrinterDataList.MAX_SIZE, ndcCharBuffer.position());
            throw withFieldName("Printer Flag", errorMessage, ndcCharBuffer);
        }
    }
}
