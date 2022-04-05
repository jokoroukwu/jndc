package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.reserved;

import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public final class ReservedCounterFields {
    public static final ReservedCounterFields EMPTY = new ReservedCounterFields(Map.of());
    private final String g12;
    private final String g20;
    private final String g30;
    private final String g40;
    private final String g50;
    private final String g80;
    private final String g90;
    private final String g100;
    private final String g110;
    private final String g130;
    private final String g140;
    private final String g150;

    public ReservedCounterFields(String g12,
                                 String g20,
                                 String g30,
                                 String g40,
                                 String g50,
                                 String g80,
                                 String g90,
                                 String g100,
                                 String g110,
                                 String g130,
                                 String g140,
                                 String g150) {
        this.g12 = Strings.validateLengthRange(g12, 0, 1, "Field 'g12'");
        this.g20 = Strings.validateLengthRange(g20, 0, 15, "Field 'g20'");
        this.g30 = Strings.validateLengthRange(g30, 0, 15, "Field 'g30'");
        this.g40 = ObjectUtils.validateNotNull(g40, "Field 'g40'");
        this.g50 = Strings.validateLengthRange(g50, 0, 20, "Field 'g50'");
        this.g80 = Strings.validateLength(g80, 0, 5, "Field 'g80'");
        this.g90 = Strings.validateLength(g90, 0, 5, "Field 'g90'");
        this.g100 = Strings.validateLength(g100, 0, 5, "Field 'g100'");
        this.g110 = Strings.validateLength(g110, 0, 5, "Field 'g110'");
        this.g130 = Strings.validateLength(g130, 0, 5, "Field 'g130'");
        this.g140 = Strings.validateLength(g140, 0, 5, "Field 'g140'");
        this.g150 = Strings.validateLength(g150, 0, 5, "Field 'g150'");
    }

    public ReservedCounterFields(Map<Integer, String> fieldIdToFieldValueMap) {
        this(
                fieldIdToFieldValueMap.getOrDefault(12, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(20, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(30, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(40, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(50, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(80, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(90, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(100, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(110, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(130, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(140, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(150, Strings.EMPTY_STRING)
        );
    }

    public ReservedCounterFields(Map<Integer, String> fieldIdToFieldValueMap, Void unused) {
        this(
                fieldIdToFieldValueMap.getOrDefault(12, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(20, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(30, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(40, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(50, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(80, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(90, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(100, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(110, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(130, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(140, Strings.EMPTY_STRING),
                fieldIdToFieldValueMap.getOrDefault(150, Strings.EMPTY_STRING),
                unused
        );
    }

    ReservedCounterFields(String g12,
                          String g20,
                          String g30,
                          String g40,
                          String g50,
                          String g80,
                          String g90,
                          String g100,
                          String g110,
                          String g130,
                          String g140,
                          String g150,
                          Void unused) {
        this.g12 = g12;
        this.g20 = g20;
        this.g30 = g30;
        this.g40 = g40;
        this.g50 = g50;
        this.g80 = g80;
        this.g90 = g90;
        this.g100 = g100;
        this.g110 = g110;
        this.g130 = g130;
        this.g140 = g140;
        this.g150 = g150;
    }

    public static ReservedCounterFieldsBuilder builder() {
        return new ReservedCounterFieldsBuilder();
    }

    public ReservedCounterFieldsBuilder copy() {
        return new ReservedCounterFieldsBuilder()
                .withG12(g12)
                .withG20(g20)
                .withG30(g30)
                .withG40(g40)
                .withG50(g50)
                .withG80(g80)
                .withG90(g90)
                .withG100(g100)
                .withG110(g110)
                .withG130(g130)
                .withG140(g140)
                .withG150(g150);
    }

    public String getG12() {
        return g12;
    }

    public String getG20() {
        return g20;
    }

    public String getG30() {
        return g30;
    }

    public String getG40() {
        return g40;
    }

    public String getG50() {
        return g50;
    }

    public String getG80() {
        return g80;
    }

    public String getG90() {
        return g90;
    }

    public String getG100() {
        return g100;
    }

    public String getG110() {
        return g110;
    }

    public String getG130() {
        return g130;
    }

    public String getG140() {
        return g140;
    }

    public String getG150() {
        return g150;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ReservedCounterFields.class.getSimpleName() + ": {", "}")
                .add("g12: '" + g12 + '\'')
                .add("g20: '" + g20 + '\'')
                .add("g30: '" + g30 + '\'')
                .add("g40: '" + g40 + '\'')
                .add("g50: '" + g50 + '\'')
                .add("g80: '" + g80 + '\'')
                .add("g90: '" + g90 + '\'')
                .add("g100: '" + g100 + '\'')
                .add("g110: '" + g110 + '\'')
                .add("g130: '" + g130 + '\'')
                .add("g140: '" + g140 + '\'')
                .add("g150: '" + g150 + '\'')
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservedCounterFields that = (ReservedCounterFields) o;
        return g12.equals(that.g12) &&
                g20.equals(that.g20) &&
                g30.equals(that.g30) &&
                g40.equals(that.g40) &&
                g50.equals(that.g50) &&
                g80.equals(that.g80) &&
                g90.equals(that.g90) &&
                g100.equals(that.g100) &&
                g110.equals(that.g110) &&
                g130.equals(that.g130) &&
                g140.equals(that.g140) &&
                g150.equals(that.g150);
    }

    @Override
    public int hashCode() {
        return Objects.hash(g12, g20, g30, g40, g50, g80, g90, g100, g110, g130, g140, g150);
    }
}
