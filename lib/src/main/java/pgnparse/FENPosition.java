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
package pgnparse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FENPosition {

  private int halfMoves;

  private int fullMoves;

  private String playerToMove;

  private boolean whiteKingCastleAvailable = false;

  private boolean whiteQueenCastleAvailable = false;

  private boolean blackKingCastleAvailable = false;

  private boolean blackQueenCastleAvailable = false;

  private String enpassantSquare;

  private Map<String, Piece> occupiedSquares = new HashMap<String, Piece>();

  public Set<String> getOccupiedSquares() {
    return Collections.unmodifiableSet(occupiedSquares.keySet());
  }

  public Piece getPiece(String square) {
    return occupiedSquares.get(square);
  }

  public void addPiece(String square, Piece piece) {
    occupiedSquares.put(square, piece);
  }

  public int getHalfMoves() {
    return halfMoves;
  }

  public void setHalfMoves(int halfMoves) {
    this.halfMoves = halfMoves;
  }

  public int getFullMoves() {
    return fullMoves;
  }

  public void setFullMoves(int fullMoves) {
    this.fullMoves = fullMoves;
  }

  public String getPlayerToMove() {
    return playerToMove;
  }

  public void setPlayerToMove(String playerToMove) {
    this.playerToMove = playerToMove;
  }

  public boolean isWhiteKingCastleAvailable() {
    return whiteKingCastleAvailable;
  }

  public void setWhiteKingCastleAvailable(boolean whiteKingCastleAvailable) {
    this.whiteKingCastleAvailable = whiteKingCastleAvailable;
  }

  public boolean isWhiteQueenCastleAvailable() {
    return whiteQueenCastleAvailable;
  }

  public void setWhiteQueenCastleAvailable(boolean whiteQueenCastleAvailable) {
    this.whiteQueenCastleAvailable = whiteQueenCastleAvailable;
  }

  public boolean isBlackKingCastleAvailable() {
    return blackKingCastleAvailable;
  }

  public void setBlackKingCastleAvailable(boolean blackKingCastleAvailable) {
    this.blackKingCastleAvailable = blackKingCastleAvailable;
  }

  public boolean isBlackQueenCastleAvailable() {
    return blackQueenCastleAvailable;
  }

  public void setBlackQueenCastleAvailable(boolean blackQueenCastleAvailable) {
    this.blackQueenCastleAvailable = blackQueenCastleAvailable;
  }

  public String getEnpassantSquare() {
    return enpassantSquare;
  }

  public void setEnpassantSquare(String enpassantSquare) {
    this.enpassantSquare = enpassantSquare;
  }
}
