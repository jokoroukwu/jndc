package io.github.jokoroukwu.jndc.tlv;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.ByteUtils;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.*;
import java.util.stream.Collectors;

public class CompositeTlv<V> extends AbstractBerTlv<Map<Integer, BerTlv<V>>> {
    protected final int tag;
    protected final int length;
    protected final Map<Integer, BerTlv<V>> value;

    CompositeTlv(int tag, int length, LinkedHashMap<Integer, BerTlv<V>> value) {
        this.tag = tag;
        this.length = length;
        this.value = Collections.unmodifiableMap(new LinkedHashMap<>(value));
    }

    public CompositeTlv(int tag, Collection<BerTlv<V>> value) {
        this.tag = Integers.validateNotNegative(tag, "Tag");
        this.value = toMap(value);
        this.length = evaluateLength(this.value);
    }

    public CompositeTlv(int tag, LinkedHashMap<Integer, BerTlv<V>> value) {
        this.tag = Integers.validateNotNegative(tag, "Tag");
        this.value = Collections.unmodifiableMap(new LinkedHashMap<>(value));
        this.length = evaluateLength(value);
    }

    public Optional<BerTlv<V>> getNestedTlv(int tag) {
        return Optional.ofNullable(value.get(tag));
    }

    public boolean contains(int tag) {
        return value.containsKey(tag);
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public Map<Integer, BerTlv<V>> getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + ": {", "}")
                .add("tag: " + Integers.toEvenLengthHexString(tag))
                .add("length: " + length)
                .add("value: " + value.values())
                .toString();
    }

    @Override
    public String toNdcString() {
        return value.values()
                .stream()
                .map(NdcComponent::toNdcString)
                .collect(Collectors.joining(Strings.EMPTY_STRING, Integers.toEvenLengthHexString(tag) + encodeLength(length), Strings.EMPTY_STRING));
    }

    protected int evaluateLength(Map<Integer, BerTlv<V>> nestedTlv) {
        return nestedTlv.values()
                .stream()
                .mapToInt(tlv -> ByteUtils.numberOfOctets(tlv.getTag()) + countLengthOctets(tlv.getLength()) + tlv.getLength())
                .sum();
    }

    private int countLengthOctets(int length) {
        return length <= 127 ? 1 : ByteUtils.numberOfOctets(length) + 1;
    }

    protected Map<Integer, BerTlv<V>> toMap(Collection<BerTlv<V>> dataObjects) {
        ObjectUtils.validateNotNull(dataObjects, "DataObjects");
        if (dataObjects.isEmpty()) {
            return Map.of();
        }
        final Map<Integer, BerTlv<V>> map = new LinkedHashMap<>(dataObjects.size());
        for (BerTlv<V> stringBerTlv : dataObjects) {
            //  implicit element null check
            map.put(stringBerTlv.getTag(), stringBerTlv);
        }
        return Collections.unmodifiableMap(map);
    }
}
