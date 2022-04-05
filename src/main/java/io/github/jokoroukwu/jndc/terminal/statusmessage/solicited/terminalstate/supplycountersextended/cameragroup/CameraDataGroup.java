package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cameragroup;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.IdentifiableCounterGroup;
import io.github.jokoroukwu.jndc.util.Integers;

import java.util.StringJoiner;

public class CameraDataGroup implements IdentifiableCounterGroup {
    public static final CameraDataGroup INSTANCE = new CameraDataGroup(0);
    public static final char ID = 'G';
    private final int cameraFilmRemaining;

    private CameraDataGroup(int cameraFilmRemaining) {
        this.cameraFilmRemaining = Integers.validateIsExactValue(cameraFilmRemaining, 0, "'Camera Film Remaining'");
    }

    public int getCameraFilmRemaining() {
        return cameraFilmRemaining;
    }

    @Override
    public char getGroupId() {
        return ID;
    }

    @Override
    public String toNdcString() {
        return ID + Integers.toZeroPaddedString(cameraFilmRemaining, 5);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CameraDataGroup.class.getSimpleName() + ": {", "}")
                .add("id: '" + ID + "'")
                .add("cameraFilmRemaining: " + cameraFilmRemaining)
                .toString();
    }

}
