package io.github.jokoroukwu.jndc.tlv;

import io.github.jokoroukwu.jndc.NdcComponent;

public interface BerTlv<V> extends NdcComponent {

    int getTag();

    int getLength();

    V getValue();
}
