package io.github.jokoroukwu.jndc.central.emvconfigurationmessage;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;

public class CentralMessageMeta {
    private final CentralMessageClass centralMessageClass;
    private final char responseFlag;
    private final Luno luno;


    public CentralMessageMeta(CentralMessageClass centralMessageClass, char responseFlag, Luno luno) {
        this.centralMessageClass = ObjectUtils.validateNotNull(centralMessageClass, "centralMessageClass");
        this.luno = ObjectUtils.validateNotNull(luno, "LUNO");
        this.responseFlag = responseFlag;
    }

    public CentralMessageClass getCentralMessageClass() {
        return centralMessageClass;
    }

    public char getResponseFlag() {
        return responseFlag;
    }

    public Luno getLuno() {
        return luno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CentralMessageMeta that = (CentralMessageMeta) o;
        return responseFlag == that.responseFlag && luno.equals(that.luno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseFlag, luno);
    }
}
