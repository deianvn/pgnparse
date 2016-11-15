/*
 * Copyright 2016 Deyan Rizov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.rizov.pgnparse;

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

    public List<PGNMove> getMove() {
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
