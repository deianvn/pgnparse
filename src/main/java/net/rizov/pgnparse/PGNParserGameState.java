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

class PGNParserGameState {

    int[][] board;

    int currentPlayer;

    boolean whiteKingCastleAvailable = true;

    boolean whiteQueenCastleAvailable = true;

    boolean blackKingCastleAvailable = true;

    boolean blackQueenCastleAvailable = true;

    int[] whiteEnpassantSquare;

    int[] blackEnpassantSquare;

    int halfMovesCount;

    int fullMovesCount;

    @Override
    protected PGNParserGameState clone() {
        int[][] newBoard = new int[board.length][];

        for(int i = 0; i < board.length; i++) {
            newBoard[i] = board[i].clone();
        }

        PGNParserGameState gameState = new PGNParserGameState();
        gameState.board = newBoard;
        gameState.currentPlayer = currentPlayer;
        gameState.whiteKingCastleAvailable = whiteKingCastleAvailable;
        gameState.whiteQueenCastleAvailable = whiteQueenCastleAvailable;
        gameState.blackKingCastleAvailable = blackKingCastleAvailable;
        gameState.blackQueenCastleAvailable = blackQueenCastleAvailable;
        gameState.whiteEnpassantSquare = whiteEnpassantSquare;
        gameState.blackEnpassantSquare = blackEnpassantSquare;

        return gameState;
    }

    void switchPlayer() {
        if (currentPlayer == PGNParser.WHITE && blackEnpassantSquare != null) {
            blackEnpassantSquare = null;
        } else if (currentPlayer == PGNParser.BLACK && whiteEnpassantSquare != null) {
            whiteEnpassantSquare = null;
        }

        currentPlayer *= -1;
    }
}
