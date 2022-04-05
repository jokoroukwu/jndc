package io.github.jokoroukwu.jndc.collection;

import java.util.ListIterator;
import java.util.function.Consumer;

class LimitedSizeListIterator<E> implements ListIterator<E> {
    private final LimitedSizeList<E> parent;
    private final ListIterator<E> listIteratorDelegate;

    LimitedSizeListIterator(LimitedSizeList<E> parent, ListIterator<E> listIteratorDelegate) {
        this.parent = parent;
        this.listIteratorDelegate = listIteratorDelegate;
    }

    @Override
    public boolean hasNext() {
        return listIteratorDelegate.hasNext();
    }

    @Override
    public E next() {
        return listIteratorDelegate.next();
    }

    @Override
    public boolean hasPrevious() {
        return listIteratorDelegate.hasPrevious();
    }

    @Override
    public E previous() {
        return listIteratorDelegate.previous();
    }

    @Override
    public int nextIndex() {
        return listIteratorDelegate.nextIndex();
    }

    @Override
    public int previousIndex() {
        return listIteratorDelegate.previousIndex();
    }

    @Override
    public void remove() {
        listIteratorDelegate.remove();
    }

    @Override
    public void set(E e) {
        parent.checkElement(e);
        listIteratorDelegate.set(e);
    }

    @Override
    public void add(E e) {
        parent.checkElement(e);
        parent.checkNewSize(parent.size() + 1);
        listIteratorDelegate.add(e);
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        listIteratorDelegate.forEachRemaining(action);
    }
}
