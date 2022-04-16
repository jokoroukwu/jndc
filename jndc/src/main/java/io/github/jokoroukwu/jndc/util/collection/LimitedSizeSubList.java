package io.github.jokoroukwu.jndc.util.collection;

import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.*;

class LimitedSizeSubList<E> implements List<E> {
    private final LimitedSizeList<E> parent;
    private final List<E> subListDelegate;

    LimitedSizeSubList(LimitedSizeList<E> parent, List<E> subListDelegate) {
        this.parent = parent;
        this.subListDelegate = subListDelegate;
    }

    @Override
    public void add(int index, E element) {
        parent.checkElement(element);
        parent.checkNewSize(parent.size() + 1);
        subListDelegate.add(index, element);
    }

    @Override
    public E remove(int index) {
        return subListDelegate.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return subListDelegate.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return subListDelegate.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new LimitedSizeListIterator<>(parent, subListDelegate.listIterator());
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new LimitedSizeListIterator<>(parent, subListDelegate.listIterator(index));
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return new LimitedSizeSubList<>(parent, subListDelegate.subList(fromIndex, toIndex));
    }

    @Override
    public E set(int index, E element) {
        parent.checkElement(element);
        return subListDelegate.set(index, element);
    }

    @Override
    public int size() {
        return subListDelegate.size();
    }

    @Override
    public boolean isEmpty() {
        return subListDelegate.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        return subListDelegate.contains(o);
    }

    @Override
    public void sort(Comparator<? super E> comparator) {
        ObjectUtils.validateNotNull(comparator, "comparator");
        subListDelegate.sort(comparator);
    }


    @Override
    public Iterator<E> iterator() {
        return subListDelegate.iterator();
    }

    @Override
    public Object[] toArray() {
        return subListDelegate.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return subListDelegate.toArray(a);
    }

    @Override
    public boolean add(E e) {
        parent.checkElement(e);
        parent.checkNewSize(parent.size() + 1);
        return subListDelegate.add(e);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        return subListDelegate.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        ObjectUtils.validateNotNull(collection, "collection");
        return subListDelegate.contains(collection);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        ObjectUtils.validateNotNull(collection, "collection");
        parent.checkNewSize(parent.size() + collection.size());
        parent.checkElements(collection);
        return subListDelegate.addAll(collection);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> collection) {
        ObjectUtils.validateNotNull(collection, "collection");
        parent.checkNewSize(parent.size() + collection.size());
        parent.checkElements(collection);
        return subListDelegate.addAll(index, collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        ObjectUtils.validateNotNull(collection, "collection");
        return subListDelegate.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        ObjectUtils.validateNotNull(collection, "collection");
        return subListDelegate.retainAll(collection);
    }

    @Override
    public void clear() {
        subListDelegate.clear();
    }

    @Override
    public E get(int index) {
        return subListDelegate.get(index);
    }

    @Override
    public String toString() {
        return subListDelegate.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof List)) return false;
        return subListDelegate.equals(o);
    }

    @Override
    public int hashCode() {
        return subListDelegate.hashCode();
    }
}
