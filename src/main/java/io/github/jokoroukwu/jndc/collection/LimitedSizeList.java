package io.github.jokoroukwu.jndc.collection;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.*;
import java.util.function.Consumer;

/**
 * This list implementation is constrained to preserve the following state:
 * <ul>
 *     <li>size may not exceed maxSize specified in constructors</li>
 *     <li>may never contain null elements</li>
 * </ul>
 * Sub-classes may add additional implementation-specific constraints by overriding {@link #performElementChecks(Object)}.
 *
 * @param <E> element type
 */
public abstract class LimitedSizeList<E> implements List<E>, RandomAccess {
    protected final List<E> listDelegate;
    private final int maxSize;

    /**
     * Merely wraps an arbitrary List to construct a new instance.
     * The list should implement {@link RandomAccess}.
     * <br>
     * <br>
     * NOTE: List elements are neither copied nor validated,
     * hence it is the responsibility of the caller to ensure
     * the provided listDelegate does not contain null references.
     *
     * @param maxSize      maximum list size
     * @param listDelegate ArrayList to delegate operations to
     */
    protected LimitedSizeList(int maxSize, List<E> listDelegate) {
        ObjectUtils.validateNotNull(listDelegate, "listDelegate");
        this.maxSize = Integers.validateNotNegative(maxSize, "Max size");
        checkNewSize(listDelegate.size());
        this.listDelegate = listDelegate;
    }

    protected LimitedSizeList(int maxSize, Collection<? extends E> collection) {
        this.maxSize = Integers.validateNotNegative(maxSize, "Max size");
        listDelegate = new ArrayList<>();
        addAll(collection);
    }

    protected LimitedSizeList(int maxSize) {
        this(maxSize, new ArrayList<>());
    }

    protected LimitedSizeList(int maxSize, int initCapacity) {
        this(maxSize, new ArrayList<>(initCapacity));
    }

    @Override
    public boolean add(E element) {
        checkElement(element);
        checkNewSize(size() + 1);
        return listDelegate.add(element);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        ObjectUtils.validateNotNull(collection, "collection");
        checkNewSize(size() + collection.size());
        checkElements(collection);
        return listDelegate.addAll(collection);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> collection) {
        ObjectUtils.validateNotNull(collection, "collection");
        final int newSize = size() + collection.size();
        checkNewSize(newSize);
        checkElements(collection);
        return listDelegate.addAll(index, collection);
    }


    @Override
    public boolean containsAll(Collection<?> collection) {
        ObjectUtils.validateNotNull(collection, "collection");
        return listDelegate.containsAll(collection);
    }

    @Override
    public void sort(Comparator<? super E> comparator) {
        ObjectUtils.validateNotNull(comparator, "comparator");
        listDelegate.sort(comparator);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        return listDelegate.remove(o);
    }


    @Override
    public void add(int index, E element) {
        checkElement(element);
        checkNewSize(size() + 1);
        listDelegate.add(index, element);
    }

    @Override
    public E set(int index, E element) {
        checkElement(element);
        return listDelegate.set(index, element);
    }

    @Override
    public E remove(int index) {
        return listDelegate.remove(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return new LimitedSizeSubList<>(this, listDelegate.subList(fromIndex, toIndex));
    }

    @Override
    public int size() {
        return listDelegate.size();
    }

    @Override
    public boolean isEmpty() {
        return listDelegate.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        return listDelegate.contains(o);
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            return -1;
        }
        return listDelegate.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            return -1;
        }
        return listDelegate.lastIndexOf(o);
    }

    @Override
    public Object[] toArray() {
        return listDelegate.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return listDelegate.toArray(a);
    }

    @Override
    public E get(int index) {
        return listDelegate.get(index);
    }

    @Override
    public Iterator<E> iterator() {
        return listDelegate.iterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new LimitedSizeListIterator<>(this, listDelegate.listIterator());
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new LimitedSizeListIterator<>(this, listDelegate.listIterator(index));
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        listDelegate.forEach(action);
    }

    @Override
    public Spliterator<E> spliterator() {
        return listDelegate.spliterator();
    }

    @Override
    public void clear() {
        listDelegate.clear();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        ObjectUtils.validateNotNull(collection, "collection");
        return listDelegate.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        ObjectUtils.validateNotNull(collection, "collection");
        return listDelegate.retainAll(collection);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + listDelegate.toString();
    }

    @Override
    public int hashCode() {
        return listDelegate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        return listDelegate.equals(obj);
    }

    void checkNewSize(int newSize) {
        if (newSize > maxSize) {
            final String messageTemplate = "New list size (%d) exceeds maximum size (%d)";
            throw new IllegalArgumentException(String.format(messageTemplate, newSize, maxSize));
        }
    }


    void checkElements(Collection<? extends E> elements) {
        for (E element : elements) {
            checkElement(element);
        }
    }

    void checkElement(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Cannot add a null element");
        }
        performElementChecks(element);
    }


    protected abstract void performElementChecks(E element);
}
