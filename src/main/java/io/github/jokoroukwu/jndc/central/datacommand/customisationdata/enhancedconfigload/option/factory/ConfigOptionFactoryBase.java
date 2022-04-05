package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.factory;


import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.*;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.nextstatenumber.NextStateNumber;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public enum ConfigOptionFactoryBase implements ConfigurationOptionFactory {
    INSTANCE;

    private static final Map<Integer, BiFunction<String, Map<Integer, ConfigurationOption>, DescriptiveOptional<? extends ConfigurationOption>>> optionFactoryMap;
    private static final String errorMessageTemplate;

    static {
        optionFactoryMap = new HashMap<>(35);

        optionFactoryMap.put(CameraControl.NUMBER, (code, opts) -> CameraControl.forCode(code));
        optionFactoryMap.put(BarcodeReader.NUMBER, (code, opts) -> BarcodeReader.forCode(code));
        optionFactoryMap.put(BnaJournalNotesCount.NUMBER, (code, opts) -> BnaJournalNotesCount.forCode(code));
        optionFactoryMap.put(SupplyModeReadyStatusAmountLength.NUMBER, (code, opts) -> SupplyModeReadyStatusAmountLength.forCode(code));
        optionFactoryMap.put(AutoVoice.NUMBER, (code, opts) -> AutoVoice.forCode(code));
        optionFactoryMap.put(DateFormat.NUMBER, (code, opts) -> DateFormat.forCode(code));
        optionFactoryMap.put(RollWidth.NUMBER, (code, opts) -> RollWidth.forCode(code));
        optionFactoryMap.put(TrackOneFormat.NUMBER, (code, opts) -> TrackOneFormat.forCode(code));
        optionFactoryMap.put(SpecificCommandReject.NUMBER, (code, opts) -> SpecificCommandReject.forCode(code));
        optionFactoryMap.put(TransactionStatusInfo.NUMBER, (code, opts) -> TransactionStatusInfo.forCode(code));
        optionFactoryMap.put(JournalBackupTime.NUMBER, (code, opts) -> JournalBackupTime.forCode(code));
        optionFactoryMap.put(JournalPrintOperations.NUMBER, (code, opts) -> JournalPrintOperations.forCode(code));
        optionFactoryMap.put(LeftPrintColumn.NUMBER, (code, opts) -> LeftPrintColumn.forCode(code));
        optionFactoryMap.put(EnveloperDispenserStatus.NUMBER, (code, opts) -> EnveloperDispenserStatus.forCode(code));
        optionFactoryMap.put(MediaEntryExitIndicators.NUMBER, (code, opts) -> MediaEntryExitIndicators.forCode(code));
        optionFactoryMap.put(RemoteRelay.NUMBER, (code, opts) -> RemoteRelay.forCode(code));
        optionFactoryMap.put(UnsolicitedReportingControl.NUMBER, (code, opts) -> UnsolicitedReportingControl.forCode(code));
        optionFactoryMap.put(SupervisorModeSimulation.NUMBER, (code, opts) -> SupervisorModeSimulation.forCode(code));
        optionFactoryMap.put(McnRange.NUMBER, (code, opts) -> McnRange.forCode(code));
        optionFactoryMap.put(ReportDualModeAndHardcopy.NUMBER, (code, opts) -> ReportDualModeAndHardcopy.forCode(code));
        optionFactoryMap.put(EjBackup.NUMBER, (code, opts) -> EjBackup.forCode(code));
        optionFactoryMap.put(TiSensorStatus.NUMBER, (code, opts) -> TiSensorStatus.forCode(code));
        optionFactoryMap.put(PrintTrackTwo.NUMBER, (code, opts) -> PrintTrackTwo.forCode(code));
        optionFactoryMap.put(BnaSettings.NUMBER, (code, opts) -> BnaSettings.forCode(code));
        optionFactoryMap.put(Ecd.NUMBER, (code, opts) -> Ecd.forCode(code));
        optionFactoryMap.put(TimeoutStateEntry.NUMBER, (code, opts) -> TimeoutStateEntry.forCode(code));
        optionFactoryMap.put(CashDepositRetractDestination.NUMBER, (code, opts) -> CashDepositRetractDestination.forCode(code));
        optionFactoryMap.put(CashHandlers.NUMBER, (code, opts) -> CashHandlers.forCode(code));
        optionFactoryMap.put(NextStateNumber.NUMBER, (code, opts) ->
                NextStateNumber.forCode(code, opts.getOrDefault(AlphanumericStateEntry.NUMBER, AlphanumericStateEntry.OFF)));
        optionFactoryMap.put(GbruReporting.NUMBER, (code, opts) -> GbruReporting.forCode(code));
        optionFactoryMap.put(CoinDispenser.NUMBER, (code, opts) -> CoinDispenser.forCode(code));
        optionFactoryMap.put(AlphanumericStateEntry.NUMBER, (code, opts) -> AlphanumericStateEntry.forCode(code));
        optionFactoryMap.put(ChequeProcessingModule.NUMBER, (code, opts) -> ChequeProcessingModule.forCode(code));
        optionFactoryMap.put(SendTrackDetails.NUMBER, (code, opts) -> SendTrackDetails.forCode(code));
        optionFactoryMap.put(DcsData.NUMBER, (code, opts) -> DcsData.forCode(code));

        errorMessageTemplate = "value '%s' does not match any of the following valid option numbers: "
                .concat(optionFactoryMap.keySet().toString());
    }

    @Override
    public DescriptiveOptional<? extends ConfigurationOption> getOption(int optionNumber,
                                                                        String optionCode,
                                                                        Map<Integer, ConfigurationOption> availableOptions) {

        return DescriptiveOptional.ofNullable(optionFactoryMap.get(optionNumber), () -> String.format(errorMessageTemplate, optionNumber))
                .flatMap(factory -> factory.apply(optionCode, availableOptions));
    }

}
