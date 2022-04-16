package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.reserved;

import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.StringJoiner;

public class ReservedCounterFieldsBuilder {
    private String g12 = Strings.EMPTY_STRING;
    private String g20 = Strings.EMPTY_STRING;
    private String g30 = Strings.EMPTY_STRING;
    private String g40 = Strings.EMPTY_STRING;
    private String g50 = Strings.EMPTY_STRING;
    private String g80 = Strings.EMPTY_STRING;
    private String g90 = Strings.EMPTY_STRING;
    private String g100 = Strings.EMPTY_STRING;
    private String g110 = Strings.EMPTY_STRING;
    private String g130 = Strings.EMPTY_STRING;
    private String g140 = Strings.EMPTY_STRING;
    private String g150 = Strings.EMPTY_STRING;

    public ReservedCounterFieldsBuilder withG12(String g12) {
        this.g12 = g12;
        return this;
    }

    public ReservedCounterFieldsBuilder withG20(String g20) {
        this.g20 = g20;
        return this;
    }

    public ReservedCounterFieldsBuilder withG30(String g30) {
        this.g30 = g30;
        return this;
    }

    public ReservedCounterFieldsBuilder withG40(String g40) {
        this.g40 = g40;
        return this;
    }

    public ReservedCounterFieldsBuilder withG50(String g50) {
        this.g50 = g50;
        return this;
    }

    public ReservedCounterFieldsBuilder withG80(String g80) {
        this.g80 = g80;
        return this;
    }

    public ReservedCounterFieldsBuilder withG90(String g90) {
        this.g90 = g90;
        return this;
    }

    public ReservedCounterFieldsBuilder withG100(String g100) {
        this.g100 = g100;
        return this;
    }

    public ReservedCounterFieldsBuilder withG110(String g110) {
        this.g110 = g110;
        return this;
    }

    public ReservedCounterFieldsBuilder withG130(String g130) {
        this.g130 = g130;
        return this;
    }

    public ReservedCounterFieldsBuilder withG140(String g140) {
        this.g140 = g140;
        return this;
    }

    public ReservedCounterFieldsBuilder withG150(String g150) {
        this.g150 = g150;
        return this;
    }

    public ReservedCounterFields build() {
        return new ReservedCounterFields(g12, g20, g30, g40, g50, g80, g90, g100, g110, g130, g140, g150);
    }

    public ReservedCounterFields buildWithNoValidation() {
        return new ReservedCounterFields(g12, g20, g30, g40, g50, g80, g90, g100, g110, g130, g140, g150, null);
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", ReservedCounterFieldsBuilder.class.getSimpleName() + ": {", "}")
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
        ReservedCounterFieldsBuilder that = (ReservedCounterFieldsBuilder) o;
        return Objects.equals(g12, that.g12) &&
                Objects.equals(g20, that.g20) &&
                Objects.equals(g30, that.g30) &&
                Objects.equals(g40, that.g40) &&
                Objects.equals(g50, that.g50) &&
                Objects.equals(g80, that.g80) &&
                Objects.equals(g90, that.g90) &&
                Objects.equals(g100, that.g100) &&
                Objects.equals(g110, that.g110) &&
                Objects.equals(g130, that.g130) &&
                Objects.equals(g140, that.g140) &&
                Objects.equals(g150, that.g150);
    }

    @Override
    public int hashCode() {
        return Objects.hash(g12, g20, g30, g40, g50, g80, g90, g100, g110, g130, g140, g150);
    }
}

