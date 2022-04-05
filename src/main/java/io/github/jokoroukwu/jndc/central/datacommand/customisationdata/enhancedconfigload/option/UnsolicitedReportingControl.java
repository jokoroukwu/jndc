package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum UnsolicitedReportingControl implements ConfigurationOption {

    NONE(0),

    VOICE_ONLY(1),

    CAMERA_ONLY(2),

    VOICE_AND_CAMERA(3),

    CARDHOLDER_CHANGES_ONLY(4),

    VOICE_AND_CARDHOLDER_CHANGES(5),

    CAMERA_AND_CARDHOLDER_CHANGES(6),

    ALL(7);


    public static final int NUMBER = 32;

    private final int code;
    private final String ndcString;

    UnsolicitedReportingControl(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<UnsolicitedReportingControl> forCode(int code) {
        switch (code) {
            case 0:
                return DescriptiveOptional.of(NONE);
            case 1:
                return DescriptiveOptional.of(VOICE_ONLY);
            case 2:
                return DescriptiveOptional.of(CAMERA_ONLY);
            case 3:
                return DescriptiveOptional.of(VOICE_AND_CAMERA);
            case 4:
                return DescriptiveOptional.of(CARDHOLDER_CHANGES_ONLY);
            case 5:
                return DescriptiveOptional.of(VOICE_AND_CARDHOLDER_CHANGES);
            case 6:
                return DescriptiveOptional.of(CAMERA_AND_CARDHOLDER_CHANGES);
            case 7:
                return DescriptiveOptional.of(ALL);
            default: {
                return DescriptiveOptional.empty(()
                        -> String.format("value '%d' is not a valid 'Unsolicited Reporting Control for camera and voice guidance' option code", code));
            }
        }
    }

    public static DescriptiveOptional<UnsolicitedReportingControl> forCode(String code) {
        return DescriptiveOptional.ofNullable(code)
                .flatMapToInt(Integers::tryParseInt).flatMapToObject(UnsolicitedReportingControl::forCode);
    }

    @Override
    public int getNumber() {
        return NUMBER;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String toNdcString() {
        return ndcString;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UnsolicitedReportingControl.class.getSimpleName() + ": {", "}")
                .add("number: '" + NUMBER + "'")
                .add("code: " + code)
                .toString();
    }
}
