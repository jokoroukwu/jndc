package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.timer;

import io.github.jokoroukwu.jndc.util.Integers;

import java.util.Objects;
import java.util.StringJoiner;

public class TimerBase implements Timer {
    /**
     * Default duration.
     */
    public static final int MIN_DURATION = 0;
    public static final int MAX_DURATION = 255;

    private final int number;
    private final int durationSeconds;

    TimerBase(int number, int durationSeconds, Void unused) {
        this.number = number;
        this.durationSeconds = durationSeconds;
    }

    public TimerBase(int number, int durationSeconds) {
        this.number = Integers.validateRange(number, 0, 99, "'Timer Number'");
        this.durationSeconds = Integers.validateRange(durationSeconds, MIN_DURATION, MAX_DURATION, "'Timer Duration'");
    }

    public TimerBase(int number) {
        this(number, MIN_DURATION);
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public int getDurationSeconds() {
        return durationSeconds;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TimerBase.class.getSimpleName() + ": {", "}")
                .add("number: " + number)
                .add("durationSeconds: " + durationSeconds)
                .toString();
    }

    @Override
    public String toNdcString() {
        return String.format("%02d%03d", number, durationSeconds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Timer)) return false;
        Timer other = (Timer) o;
        return number == other.getNumber() && durationSeconds == other.getDurationSeconds();
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, durationSeconds);
    }
}
