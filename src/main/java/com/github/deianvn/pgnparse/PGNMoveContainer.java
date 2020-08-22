package com.github.deianvn.pgnparse;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class PGNMoveContainer {

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

  public List<PGNMove> getMoves() {
    return Collections.unmodifiableList(moves);
  }

  public Iterator<PGNMove> getMovesIterator() {
    return Collections.unmodifiableList(moves).iterator();
  }

  public int getMovesCount() {
    return moves.size();
  }

  public int getMovePairsCount() {
    return moves.size() / 2;
  }

}
