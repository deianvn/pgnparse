package com.github.deianvn.pgnparse;

class PGNParserGameState {

  int[][] board;

  int currentPlayer;

  boolean whiteKingCastleAvailable = true;

  boolean whiteQueenCastleAvailable = true;

  boolean blackKingCastleAvailable = true;

  boolean blackQueenCastleAvailable = true;

  int[] enpassantSquare;

  int halfMovesCount;

  int fullMovesCount;

  @Override
  protected PGNParserGameState clone() {
    int[][] newBoard = new int[board.length][];

    for (int i = 0; i < board.length; i++) {
      newBoard[i] = board[i].clone();
    }

    PGNParserGameState gameState = new PGNParserGameState();
    gameState.board = newBoard;
    gameState.currentPlayer = currentPlayer;
    gameState.whiteKingCastleAvailable = whiteKingCastleAvailable;
    gameState.whiteQueenCastleAvailable = whiteQueenCastleAvailable;
    gameState.blackKingCastleAvailable = blackKingCastleAvailable;
    gameState.blackQueenCastleAvailable = blackQueenCastleAvailable;
    gameState.enpassantSquare = enpassantSquare;

    return gameState;
  }

  void switchPlayer() {
    enpassantSquare = null;
    currentPlayer *= -1;
  }
}
