package net.rizov.pgnparse;

import java.util.Iterator;

public class PGNUnmodifiableIterator<T> implements Iterator<T> {

    private Iterator<T> iterator;

    PGNUnmodifiableIterator(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}