package net.rizov.pgnparse;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class PGNMoveContainer {

    class PGNMoveIterator implements Iterator {

        private Iterator iterator;

        PGNMoveIterator(Iterator iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Object next() {
            return iterator.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private List<PGNMove> moves;

    public PGNMoveContainer() {
        moves = new LinkedList<PGNMove>();
    }

    void addMove(PGNMove move) {
        moves.add(move);
    }

    public PGNMove getMove(int index) {
        return moves.get(index);
    }

    public Iterator<PGNMove> getMovesIterator() {
        return new PGNMoveIterator(moves.iterator());
    }

    public int getMovesCount() {
        return moves.size();
    }

    public int getMovePairsCount() {
        return moves.size() / 2;
    }

}
