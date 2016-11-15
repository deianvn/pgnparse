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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

public class PGNParser {

    private static final String PAWN = "P";

    private static final String KNIGHT = "N";

    private static final String BISHOP = "B";

    private static final String ROOK = "R";

    private static final String QUEEN = "Q";

    private static final String KING = "K";

    private static final String MOVE_TYPE_1_RE = "[a-h][1-8]";

    private static final int MOVE_TYPE_1_LENGTH = 2;

    private static final String MOVE_TYPE_2_RE = "[" + PAWN + KNIGHT + BISHOP + ROOK + QUEEN + KING + "][a-h][1-8]";

    private static final int MOVE_TYPE_2_LENGTH = 3;

    private static final String MOVE_TYPE_3_RE = "[" + PAWN + KNIGHT + BISHOP + ROOK + QUEEN + KING + "][a-h][a-h][1-8]";

    private static final int MOVE_TYPE_3_LENGTH = 4;

    private static final String MOVE_TYPE_4_RE = "[" + PAWN + KNIGHT + BISHOP + ROOK + QUEEN + KING + "][a-h][1-8][a-h][1-8]";

    private static final int MOVE_TYPE_4_LENGTH = 5;

    private static final String MOVE_TYPE_5_RE = "[a-h][a-h][1-8]";

    private static final String MOVE_TYPE_6_RE = "[" + PAWN + KNIGHT + BISHOP + ROOK + QUEEN + KING + "][1-8][a-h][1-8]";

    private static final byte WHITE = -1;

    private static final byte BLACK = 1;

    private static final byte WHITE_PAWN = -1;

    private static final byte WHITE_KNIGHT = -2;

    private static final byte WHITE_BISHOP = -3;

    private static final byte WHITE_ROOK = -4;

    private static final byte WHITE_QUEEN = -5;

    private static final byte WHITE_KING = -6;

    private static final byte EMPTY = 0;

    private static final byte BLACK_PAWN = 1;

    private static final byte BLACK_KNIGHT = 2;

    private static final byte BLACK_BISHOP = 3;

    private static final byte BLACK_ROOK = 4;

    private static final byte BLACK_QUEEN = 5;

    private static final byte BLACK_KING = 6;

    private static final byte[][] KNIGHT_SEARCH_PATH = { { -1, 2 }, { 1, 2 }, { -1, -2 }, { 1, -2 }, { -2, 1 }, { -2, -1 }, { 2, -1 }, { 2, 1 } };

    private static final byte[][] BISHOP_SEARCH_PATH = { {1, 1}, {1, -1}, {-1, -1}, {-1, 1} };

    private static final byte[][] ROOK_SEARCH_PATH = { {0, 1}, {1, 0}, {0, -1}, {-1, 0} };

    private static final byte[][] QUEEN_KING_SEARCH_PATH = { {1, 1}, {1, -1}, {-1, -1}, {-1, 1}, {0, 1}, {1, 0}, {0, -1}, {-1, 0} };

    public static PGNGame parsePGNGame(String pgnGame) throws PGNParseException {
        final PGNGame game = new PGNGame(pgnGame);
        final byte[][] board = createDefaultBoard();
        final int[] color = { WHITE };

        BufferedReader br = new BufferedReader(new StringReader(pgnGame));
        String line;
        StringBuilder pgn = new StringBuilder();
        int lineNumber = 1;

        try {
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("@")) {
                    lineNumber++;
                    continue;
                }

                if (line.startsWith("[")) {
                    try {
                        String tagName = line.substring(1, line.indexOf("\"")).trim();
                        String tagValue = line.substring(line.indexOf("\"") + 1,
                                line.lastIndexOf("\""));
                        game.addTag(tagName, tagValue);
                    } catch (IndexOutOfBoundsException e) {
                        throw new PGNParseException("Error in line " + lineNumber);
                    }
                } else {
                    if (!line.isEmpty()) {
                        pgn.append(line + "\n");
                    }
                }

                lineNumber++;
            }
        } catch (IOException e) {
            throw new PGNParseException(e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {}
        }

        parse(game, pgn.toString(), color, board);
        return game;
    }

    public static List<String> splitPGNString(String pgn) throws PGNParseException {
        List<String> pgnGames = new LinkedList<String>();
        BufferedReader br = new BufferedReader(new StringReader(pgn));
        String line;
        StringBuilder buffer = new StringBuilder();

        try {
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (!line.isEmpty()) {
                    buffer.append(line + "\n");

                    if (line.endsWith("1-0") || line.endsWith("0-1") || line.endsWith("1/2-1/2") || line.endsWith("*")) {
                        pgnGames.add(buffer.toString());
                        buffer.delete(0, buffer.length());
                    }
                }

            }
        } catch (IOException e) {
            throw new PGNParseException(e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {}
        }

        return pgnGames;
    }



    private static int getLineEndIndex(String line, int start) {
        int index = line.indexOf('\n', start);

        if (index == -1) {
            index = line.length();
        }

        return index;
    }

    private static void parse(PGNMoveContainer container, String pgn, int[] color, byte[][] board) throws PGNParseException {

        StringBuilder token = new StringBuilder();

        for (int i = 0; i < pgn.length(); i++) {

            char ch = pgn.charAt(i);

            if (ch != ' ' && ch != '\n' && ch != '{' && ch != ';' && ch != '(') {
                token.append(ch);
            } else {
                processMoveToken(token.toString().trim(), container, board, color);
                token.delete(0, token.length());

                if (ch == ';') {
                    int lineEndIndex = getLineEndIndex(pgn, i + 1);
                    String commentToken = pgn.substring(i + 1, lineEndIndex).trim();
                    processCommentToken(commentToken, container);
                    i = lineEndIndex;
                } else if (ch == '{') {
                    int commentEndIndex = pgn.indexOf('}', i);

                    if (commentEndIndex != -1) {
                        String commentToken = pgn.substring(i + 1, commentEndIndex);
                        processCommentToken(commentToken, container);
                        i = commentEndIndex + 1;
                    } else {
                        throw new PGNParseException("Error near character {");
                    }
                } else if (ch == '(') {
                    int end = 1;

                    for (int k = i + 1; k < pgn.length(); k++) {
                        char nextCh = pgn.charAt(k);

                        if (nextCh == '(') {
                            end++;
                        } else if (nextCh == ')' && --end == 0) {
                            String variationPgn = pgn.substring(i+1, k);
                            PGNMove lastMove = container.getMove(container.getMovesCount() - 1);
                            byte[][] variationBoard = cloneBoard(board);
                            int[] varaitionColor = color.clone();
                            handleBoardBackMove(lastMove, variationBoard, varaitionColor);
                            switchColor(varaitionColor);
                            PGNVariation variation = new PGNVariation();
                            lastMove.addVariation(variation);
                            parse(variation, variationPgn, varaitionColor, variationBoard);
                            i = k;
                            break;
                        }
                    }
                }

            }
        }

        processMoveToken(token.toString().trim(), container, board, color);
    }

    private static void handleBoardBackMove(PGNMove move, byte[][] board, int[] color) {

        if (move.isCastle()) {
            if (move.isKingSideCastle()) {

                if (move.getColor() == Color.white) {
                    board[4][0] = board[6][0];
                    board[7][0] = board[5][0];
                    board[6][0] = EMPTY;
                    board[5][0] = EMPTY;
                } else {
                    board[4][7] = board[6][7];
                    board[7][7] = board[5][7];
                    board[6][7] = EMPTY;
                    board[5][7] = EMPTY;
                }
            } else {

                if (move.getColor() == Color.white) {
                    board[4][0] = board[2][0];
                    board[0][0] = board[3][0];
                    board[2][0] = EMPTY;
                    board[3][0] = EMPTY;
                } else {
                    board[4][7] = board[2][7];
                    board[0][7] = board[3][7];
                    board[2][7] = EMPTY;
                    board[3][7] = EMPTY;
                }

            }

        } else {
            String from = move.getFromSquare();
            String to = move.getToSquare();

            if (move.isPromoted()) {
                board[getChessATOI(from.charAt(0))][from.charAt(1) - '1'] = (byte)(BLACK_PAWN * color[0]);
            } else {
                board[getChessATOI(from.charAt(0))][from.charAt(1) - '1'] = board[getChessATOI(to.charAt(0))][to.charAt(1) - '1'];
            }

            if (move.isCaptured()) {
                byte piece = pieceToByte(move.getCapturedPiece(), color[0] * -1);

                if (move.isEnpassant()) {
                    String ep = move.getEnpassantPieceSquare();
                    board[getChessATOI(ep.charAt(0))][ep.charAt(1) - '1'] = piece;
                } else {
                    board[getChessATOI(to.charAt(0))][to.charAt(1) - '1'] = piece;
                }
            } else {
                board[getChessATOI(to.charAt(0))][to.charAt(1) - '1'] = EMPTY;
            }
        }
    }

    private static void processMoveToken(String token, PGNMoveContainer container, byte[][] board, int[] color) throws PGNParseException {
        token = token.replaceAll("\\s*\\d+\\.+\\s*", "");

        if (token.length() > 0) {
            try {
                handleToken(token, container, board, color);
            } catch (RuntimeException e) {
                throw new PGNParseException("Eror near token: " + token);
            }
        }
    }

    private static void processCommentToken(String token, PGNMoveContainer container) throws PGNParseException {
        if (container.getMovesCount() > 0) {
            container.getMove(container.getMovesCount() - 1).setComment(token);
        } else {
            throw new PGNParseException("Error near token: " + token);
        }
    }

    static PGNMove parseMove(String move) throws PGNParseException, IndexOutOfBoundsException {
        PGNMove pgnMove = new PGNMove();
        pgnMove.setFullMove(move);
        String piece;

        if (move.startsWith(PAWN)) {
            piece = PAWN;
        } else if (move.startsWith(KNIGHT)) {
            piece = KNIGHT;
        } else if (move.startsWith(BISHOP)) {
            piece = BISHOP;
        } else if (move.startsWith(ROOK)) {
            piece = ROOK;
        } else if (move.startsWith(QUEEN)) {
            piece = QUEEN;
        } else if (move.startsWith(KING)) {
            piece = KING;
        } else {
            piece = PAWN;
        }

        pgnMove.setPiece(piece);

        if (move.contains("x")) {
            pgnMove.setCaptured(true);
            move = move.replace("x", "");
        }

        if (move.contains("+")) {
            pgnMove.setChecked(true);
            move = move.replace("+", "");
        }

        if (move.contains("#")) {
            pgnMove.setCheckMated(true);
            move = move.replace("#", "");
        }

        if (move.contains("=")) {
            String promotedPiece = move.substring(move.indexOf('=') + 1);

            if (promotedPiece.equals(PAWN)
                    || promotedPiece.equals(KNIGHT)
                    || promotedPiece.equals(BISHOP)
                    || promotedPiece.equals(ROOK)
                    || promotedPiece.equals(QUEEN)
                    || promotedPiece.equals(KING))
            {
                move = move.substring(0, move.indexOf('='));
                pgnMove.setPromoted(true);
                pgnMove.setPromotion(promotedPiece);
            }
            else
            {
                throw new PGNParseException("Wrong piece abr [" + promotedPiece + "]");
            }
        }

        if (move.equals("0-0") || move.equals("O-O")) {
            pgnMove.setKingSideCastle(true);
        } else if (move.equals("0-0-0") || move.equals("O-O-O")) {
            pgnMove.setQueenSideCastle(true);
        } else if (move.equals("1-0") || move.equals("0-1") || move.equals("1/2-1/2") || move.equals("*")) {
            pgnMove.setEndGameMarked(true);
            pgnMove.setEndGameMark(move);
        }

        pgnMove.setMove(move);

        return pgnMove;
    }

    private static void handleToken(String token, PGNMoveContainer container, byte[][] board, int[] color) throws PGNParseException {

        PGNMove move = null;

        if (container.getMovesCount() > 0) {
            move = container.getMove(container.getMovesCount() - 1);
        }

        if (token.equals("e.p.")) {
            return;
        } else if (token.startsWith("{") && token.endsWith("}")) {
            move.setComment(token.substring(1, token.length() - 1));
        } else {
            move = parseMove(token);

            if (validateMove(move)) {

                if (color[0] == WHITE) {
                    move.setColor(Color.white);
                } else {
                    move.setColor(Color.black);
                }

                container.addMove(move);
                updateNextMove(move, board);
                switchColor(color);
            } else {
                throw new PGNParseException("Error near token: " + token);
            }
        }
    }

    private static void updateNextMove(PGNMove move, byte[][] board) throws PGNParseException {

        String strippedMove = move.getMove();
        byte color;

        if (move.getColor() == Color.white) {
            color = WHITE;
        } else {
            color = BLACK;
        }

        if (move.isCastle()) {
            if (move.isKingSideCastle()) {

                if (move.getColor() == Color.white) {
                    board[6][0] = board[4][0];
                    board[5][0] = board[7][0];
                    board[4][0] = EMPTY;
                    board[7][0] = EMPTY;
                } else {
                    board[6][7] = board[4][7];
                    board[5][7] = board[7][7];
                    board[4][7] = EMPTY;
                    board[7][7] = EMPTY;
                }
            } else {

                if (move.getColor() == Color.white) {
                    board[2][0] = board[4][0];
                    board[3][0] = board[0][0];
                    board[4][0] = EMPTY;
                    board[0][0] = EMPTY;
                } else {
                    board[2][7] = board[4][7];
                    board[3][7] = board[0][7];
                    board[4][7] = EMPTY;
                    board[0][7] = EMPTY;
                }

            }

        } else if (!move.isEndGameMarked()) {
            switch (strippedMove.length()) {
                case MOVE_TYPE_1_LENGTH :
                    handleMoveType1(move, strippedMove, color, board);

                    break;
                case MOVE_TYPE_2_LENGTH :
                    if (strippedMove.matches(MOVE_TYPE_2_RE)) {
                        handleMoveType2(move, strippedMove, color, board);
                    } else if (strippedMove.matches(MOVE_TYPE_5_RE)) {
                        handleMoveType5(move, strippedMove, color, board);
                    }

                    break;
                case MOVE_TYPE_3_LENGTH :
                    if (strippedMove.matches(MOVE_TYPE_3_RE)) {
                        handleMoveType3(move, strippedMove, color, board);
                    } else if (strippedMove.matches(MOVE_TYPE_6_RE)) {
                        handleMoveType6(move, strippedMove, color, board);
                    }

                    break;
                case MOVE_TYPE_4_LENGTH :
                    handleMoveType4(move, strippedMove, color, board);

                    break;
            }

            byte capturedPiece = board[getChessATOI(move.getToSquare().charAt(0))][move.getToSquare().charAt(1) - '1'];
            board[getChessATOI(move.getToSquare().charAt(0))][move.getToSquare().charAt(1) - '1'] = board[getChessATOI(move.getFromSquare().charAt(0))][move.getFromSquare().charAt(1) - '1'];
            board[getChessATOI(move.getFromSquare().charAt(0))][move.getFromSquare().charAt(1) - '1'] = EMPTY;

            if (move.isEnpassantCapture()) {
                capturedPiece = board[getChessATOI(move.getEnpassantPieceSquare().charAt(0))][move.getEnpassantPieceSquare().charAt(1) - '1'];
                board[getChessATOI(move.getEnpassantPieceSquare().charAt(0))][move.getEnpassantPieceSquare().charAt(1) - '1'] = EMPTY;
            }

            if (capturedPiece != EMPTY) {
                switch (Math.abs(capturedPiece)) {
                    case BLACK_PAWN :
                        move.setCapturedPiece(PAWN);
                        break;
                    case BLACK_ROOK :
                        move.setCapturedPiece(ROOK);
                        break;
                    case BLACK_KNIGHT :
                        move.setCapturedPiece(KNIGHT);
                        break;
                    case BLACK_BISHOP :
                        move.setCapturedPiece(BISHOP);
                        break;
                    case BLACK_QUEEN :
                        move.setCapturedPiece(QUEEN);
                        break;
                    case BLACK_KING :
                        move.setCapturedPiece(KING);
                        break;
                }
            }

            if (move.isPromoted()) {
                if (move.getPromotion().equals(QUEEN)) {
                    board[getChessATOI(move.getToSquare().charAt(0))][move.getToSquare().charAt(1) - '1'] = (byte)(BLACK_QUEEN * color);
                } else if (move.getPromotion().equals(ROOK)) {
                    board[getChessATOI(move.getToSquare().charAt(0))][move.getToSquare().charAt(1) - '1'] = (byte)(BLACK_ROOK * color);
                } else if (move.getPromotion().equals(BISHOP)) {
                    board[getChessATOI(move.getToSquare().charAt(0))][move.getToSquare().charAt(1) - '1'] = (byte)(BLACK_BISHOP * color);
                } else if (move.getPromotion().equals(KNIGHT)) {
                    board[getChessATOI(move.getToSquare().charAt(0))][move.getToSquare().charAt(1) - '1'] = (byte)(BLACK_KNIGHT * color);
                }
            }

        }

    }

    private static void handleMoveType1(PGNMove move, String strippedMove, byte color, byte[][] board) throws PGNParseException {
        int tohPos = getChessATOI(strippedMove.charAt(0));
        int tovPos = strippedMove.charAt(1) - '1';
        byte piece = (byte)(BLACK_PAWN * color);
        int fromvPos = getPawnvPos(tohPos, tovPos, piece, board);
        int fromhPos = tohPos;

        if (fromvPos == - 1) {
            throw new PGNParseException("Invalid move: " + move.getFullMove());
        }

        move.setFromSquare(getChessCoords(fromhPos, fromvPos));
        move.setToSquare(getChessCoords(tohPos, tovPos));
    }

    private static void handleMoveType2(PGNMove move, String strippedMove, byte color, byte[][] board) throws PGNParseException {
        byte piece;
        int tohPos = getChessATOI(strippedMove.charAt(1));
        int tovPos = strippedMove.charAt(2) - '1';
        int fromvPos = -1;
        int fromhPos = -1;

        if (strippedMove.charAt(0) == PAWN.charAt(0)) {
            piece = (byte)(BLACK_PAWN * color);
            fromvPos = getPawnvPos(tohPos, tovPos, piece, board);
            fromhPos = tohPos;
        } else if (strippedMove.charAt(0) == KNIGHT.charAt(0)) {
            piece = (byte)(BLACK_KNIGHT * color);
            int[]  fromPos = getSingleMovePiecePos(tohPos, tovPos, piece, board, KNIGHT_SEARCH_PATH);

            if (fromPos == null) {
                throw new PGNParseException("Invalid move: " + move.getFullMove());
            }

            fromhPos = fromPos[0];
            fromvPos = fromPos[1];
        } else if (strippedMove.charAt(0) == BISHOP.charAt(0)) {
            piece = (byte)(BLACK_BISHOP * color);
            int[] fromPos = getMultiMovePiecePos(tohPos, tovPos, piece, board, BISHOP_SEARCH_PATH);

            if (fromPos == null) {
                throw new PGNParseException("Invalid move: " + move.getFullMove());
            }

            fromhPos = fromPos[0];
            fromvPos = fromPos[1];
        } else if (strippedMove.charAt(0) == ROOK.charAt(0)) {
            piece = (byte)(BLACK_ROOK * color);
            int[] fromPos = getMultiMovePiecePos(tohPos, tovPos, piece, board, ROOK_SEARCH_PATH);

            if (fromPos == null) {
                throw new PGNParseException(move.getFullMove());
            }

            fromhPos = fromPos[0];
            fromvPos = fromPos[1];
        } else if (strippedMove.charAt(0) == QUEEN.charAt(0)) {
            piece = (byte)(BLACK_QUEEN * color);
            int[] fromPos = getMultiMovePiecePos(tohPos, tovPos, piece, board, QUEEN_KING_SEARCH_PATH);

            if (fromPos == null) {
                throw new PGNParseException("Invalid move: " + move.getFullMove());
            }

            fromhPos = fromPos[0];
            fromvPos = fromPos[1];
        } else if (strippedMove.charAt(0) == KING.charAt(0)) {
            piece = (byte)(BLACK_KING * color);
            int[]  fromPos = getSingleMovePiecePos(tohPos, tovPos, piece, board, QUEEN_KING_SEARCH_PATH);

            if (fromPos == null) {
                throw new PGNParseException("Invalid move: " + move.getFullMove());
            }

            fromhPos = fromPos[0];
            fromvPos = fromPos[1];
        }

        if (fromvPos == - 1 || fromhPos == -1) {
            throw new PGNParseException("Invalid move: " + move.getFullMove());
        }

        move.setFromSquare(getChessCoords(fromhPos, fromvPos));
        move.setToSquare(getChessCoords(tohPos, tovPos));
    }

    private static void handleMoveType3(PGNMove move, String strippedMove, byte color, byte[][] board) throws PGNParseException {
        byte piece = WHITE_PAWN;
        int fromhPos = getChessATOI(strippedMove.charAt(1));
        int tohPos = getChessATOI(strippedMove.charAt(2));
        int tovPos = strippedMove.charAt(3) - '1';
        int fromvPos = -1;

        if (strippedMove.charAt(0) == PAWN.charAt(0)) {
            piece = (byte)(BLACK_PAWN * color);
            fromvPos = getPawnvPos(fromhPos, tovPos, piece, board);
        } else if (strippedMove.charAt(0) == KNIGHT.charAt(0)) {
            piece = (byte)(BLACK_KNIGHT * color);
            fromvPos = getSingleMovePiecevPos(tohPos, tovPos, fromhPos, piece, board, KNIGHT_SEARCH_PATH);
        } else if (strippedMove.charAt(0) == BISHOP.charAt(0)) {
            piece = (byte)(BLACK_BISHOP * color);
            fromvPos = getMultiMovePiecevPos(tohPos, tovPos, fromhPos, piece, board, BISHOP_SEARCH_PATH);
        } else if (strippedMove.charAt(0) == ROOK.charAt(0)) {
            piece = (byte)(BLACK_ROOK * color);
            fromvPos = getMultiMovePiecevPos(tohPos, tovPos, fromhPos, piece, board, ROOK_SEARCH_PATH);
        } else if (strippedMove.charAt(0) == QUEEN.charAt(0)) {
            piece = (byte)(BLACK_QUEEN * color);
            fromvPos = getMultiMovePiecevPos(tohPos, tovPos, fromhPos, piece, board, QUEEN_KING_SEARCH_PATH);
        } else if (strippedMove.charAt(0) == KING.charAt(0)) {
            piece = (byte)(BLACK_KING * color);
            fromvPos = getSingleMovePiecevPos(tohPos, tovPos, fromhPos, piece, board, QUEEN_KING_SEARCH_PATH);
        }

        if (fromvPos == - 1 || fromhPos == -1) {
            throw new PGNParseException("Invalid move: " + move.getFullMove());
        }

        move.setFromSquare(getChessCoords(fromhPos, fromvPos));
        move.setToSquare(getChessCoords(tohPos, tovPos));
    }

    private static void handleMoveType4(PGNMove move, String strippedMove, byte color, byte[][] board) throws PGNParseException {
        byte piece = WHITE_PAWN;
        int fromhPos = getChessATOI(strippedMove.charAt(1));
        int fromvPos = strippedMove.charAt(2) - '1';
        int tohPos = getChessATOI(strippedMove.charAt(3));
        int tovPos = strippedMove.charAt(4) - '1';

        if (strippedMove.charAt(0) == PAWN.charAt(0)) {
            piece = (byte)(BLACK_PAWN * color);
        } else if (strippedMove.charAt(0) == KNIGHT.charAt(0)) {
            piece = (byte)(BLACK_KNIGHT * color);
        } else if (strippedMove.charAt(0) == BISHOP.charAt(0)) {
            piece = (byte)(BLACK_BISHOP * color);
        } else if (strippedMove.charAt(0) == ROOK.charAt(0)) {
            piece = (byte)(BLACK_ROOK * color);
        } else if (strippedMove.charAt(0) == QUEEN.charAt(0)) {
            piece = (byte)(BLACK_QUEEN * color);
        } else if (strippedMove.charAt(0) == KING.charAt(0)) {
            piece = (byte)(BLACK_KING * color);
        }

        if (fromvPos == - 1 || fromhPos == -1) {
            throw new PGNParseException("Invalid move: " + move.getFullMove());
        }

        if (board[fromhPos][fromvPos] != piece) {
            throw new PGNParseException("Invalid move: " + move.getFullMove());
        }

        move.setFromSquare(getChessCoords(fromhPos, fromvPos));
        move.setToSquare(getChessCoords(tohPos, tovPos));
    }

    private static void handleMoveType5(PGNMove move, String strippedMove, byte color, byte[][] board) throws PGNParseException {
        int fromhPos = getChessATOI(strippedMove.charAt(0));
        int tohPos = getChessATOI(strippedMove.charAt(1));
        int tovPos = strippedMove.charAt(2) - '1';
        byte piece = (byte)(BLACK_PAWN * color);
        int fromvPos = getPawnvPos(fromhPos, tovPos, piece, board);

        if (fromvPos == - 1) {
            throw new PGNParseException("Invalid move: " + move.getFullMove());
        }

        if (move.isCaptured()) {
            if (board[tohPos][tovPos] == EMPTY) {
                int enPassantvPos = tovPos - (tovPos - fromvPos);

                if (board[tohPos][enPassantvPos] == (byte)(-1 * BLACK_PAWN * color)) {
                    move.setEnpassantCapture(true);
                    move.setEnpassantPieceSquare(getChessCoords(tohPos, enPassantvPos));
                } else {
                    throw new PGNParseException("Invalid move (Enpassant capture expected): " + move.getFullMove());
                }
            }
        }

        move.setFromSquare(getChessCoords(fromhPos, fromvPos));
        move.setToSquare(getChessCoords(tohPos, tovPos));
    }

    private static void handleMoveType6(PGNMove move, String strippedMove, byte color, byte[][] board) throws PGNParseException {
        byte piece = WHITE_PAWN;
        int fromvPos = strippedMove.charAt(1) - '1';
        int tohPos = getChessATOI(strippedMove.charAt(2));
        int tovPos = strippedMove.charAt(3) - '1';
        int fromhPos = -1;

        if (strippedMove.charAt(0) == PAWN.charAt(0)) {
            throw new PGNParseException("Invalid move: " + move.getFullMove());
        } else if (strippedMove.charAt(0) == KNIGHT.charAt(0)) {
            piece = (byte)(BLACK_KNIGHT * color);
            fromhPos = getSingleMovePiecehPos(tohPos, tovPos, fromvPos, piece, board, KNIGHT_SEARCH_PATH);
        } else if (strippedMove.charAt(0) == BISHOP.charAt(0)) {
            piece = (byte)(BLACK_BISHOP * color);
            fromhPos = getMultiMovePiecehPos(tohPos, tovPos, fromvPos, piece, board, BISHOP_SEARCH_PATH);
        } else if (strippedMove.charAt(0) == ROOK.charAt(0)) {
            piece = (byte)(BLACK_ROOK * color);
            fromhPos = getMultiMovePiecehPos(tohPos, tovPos, fromvPos, piece, board, ROOK_SEARCH_PATH);
        } else if (strippedMove.charAt(0) == QUEEN.charAt(0)) {
            piece = (byte)(BLACK_QUEEN * color);
            fromhPos = getMultiMovePiecehPos(tohPos, tovPos, fromvPos, piece, board, QUEEN_KING_SEARCH_PATH);
        } else if (strippedMove.charAt(0) == KING.charAt(0)) {
            piece = (byte)(BLACK_KING * color);
            fromhPos = getSingleMovePiecehPos(tohPos, tovPos, fromvPos, piece, board, QUEEN_KING_SEARCH_PATH);
        }

        if (fromvPos == -1 || fromhPos == -1) {
            throw new PGNParseException("Invalid move: " + move.getFullMove());
        }

        move.setFromSquare(getChessCoords(fromhPos, fromvPos));
        move.setToSquare(getChessCoords(tohPos, tovPos));
    }

    private static void switchColor(int[] color) {
        if (color[0] < 0) {
            color[0] = BLACK;
        } else {
            color[0] = WHITE;
        }
    }

    private static int getChessATOI(char alfa) {
        return alfa - 'a';
    }

    private static String getChessCoords(int hPos, int vPos) {
        return (char)('a' + hPos) + "" + (vPos + 1);
    }

    private static int getPawnvPos(int hPos, int vPos, byte piece, byte[][] board) {
        if (board[hPos][vPos + piece] == piece) {
            return vPos + piece;
        } else if (board[hPos][vPos + 2 * piece] == piece) {
            return vPos + 2 * piece;
        }

        return -1;
    }

    private static int[] getSingleMovePiecePos(int hPos, int vPos, byte piece, byte[][] board, byte[][] moveData) {
        for (int i = 0; i < moveData.length; i++) {
            try {
                if (board[hPos + moveData[i][0]][vPos + moveData[i][1]] == piece) {
                    if (Math.abs(piece) != BLACK_KING) {
                        if (isKingInCheckAfterMove(board, (byte)(piece / Math.abs(piece)), hPos + moveData[i][0], vPos + moveData[i][1], hPos, vPos)) {
                            continue;
                        }
                    }

                    return new int[] { hPos + moveData[i][0], vPos + moveData[i][1] };
                }
            } catch (IndexOutOfBoundsException e) {}
        }

        return null;
    }

    private static int getSingleMovePiecevPos(int hPos, int vPos, int fromhPos, byte piece, byte[][] board, byte[][] moveData) {
        for (int i = 0; i < moveData.length; i++) {
            try {
                if (board[hPos + moveData[i][0]][vPos + moveData[i][1]] == piece && hPos + moveData[i][0] == fromhPos) {
                    if (Math.abs(piece) != BLACK_KING) {
                        if (isKingInCheckAfterMove(board, (byte)(piece / Math.abs(piece)), fromhPos, vPos + moveData[i][1], hPos, vPos)) {
                            continue;
                        }
                    }

                    return vPos + moveData[i][1];
                }
            } catch (IndexOutOfBoundsException e) {}
        }

        return -1;
    }

    private static int getSingleMovePiecehPos(int hPos, int vPos, int fromvPos, byte piece, byte[][] board, byte[][] moveData) {
        for (int i = 0; i < moveData.length; i++) {
            try {
                if (board[hPos + moveData[i][0]][vPos + moveData[i][1]] == piece && vPos + moveData[i][1] == fromvPos) {
                    if (Math.abs(piece) != BLACK_KING) {
                        if (isKingInCheckAfterMove(board, (byte)(piece / Math.abs(piece)), hPos + moveData[i][0], vPos + moveData[i][1], hPos, vPos)) {
                            continue;
                        }
                    }

                    return hPos + moveData[i][0];
                }
            } catch (IndexOutOfBoundsException e) {}
        }

        return -1;
    }

    private static int[] getMultiMovePiecePos(int hPos, int vPos, byte piece, byte[][] board, byte[][] moveData) {
        for (int i = 0; i < moveData.length; i++) {
            int[] position = getMultiMovePiecePosRec(hPos, vPos, hPos, vPos, moveData[i][0], moveData[i][1], piece, board);

            if (position != null) {
                return position;
            }
        }

        return null;
    }

    private static int[] getMultiMovePiecePosRec(int originalhPos, int originalvPos, int hPos, int vPos, int hAdd, int vAdd, byte piece, byte[][] board) {
        hPos += hAdd;
        vPos += vAdd;

        try {
            if (board[hPos][vPos] == piece) {
                if (Math.abs(piece) != BLACK_KING) {
                    if (isKingInCheckAfterMove(board, (byte)(piece / Math.abs(piece)), hPos, vPos, originalhPos, originalvPos)) {
                        return null;
                    }
                }

                return new int[] { hPos, vPos };
            } else if (board[hPos][vPos] != EMPTY) {
                return null;
            }
        } catch (IndexOutOfBoundsException e) {
            return null;
        }

        return getMultiMovePiecePosRec(originalhPos, originalvPos, hPos, vPos, hAdd, vAdd, piece, board);
    }

    private static int getMultiMovePiecevPos(int hPos, int vPos, int fromhPos, byte piece, byte[][] board, byte[][] moveData) {
        for (int i = 0; i < moveData.length; i++) {
            int fromvPos = getMultiMovePiecevPosRec(hPos, vPos, hPos, vPos, moveData[i][0], moveData[i][1], fromhPos, piece, board);

            if (fromvPos != -1) {
                return fromvPos;
            }
        }

        return -1;
    }

    private static int getMultiMovePiecevPosRec(int originalhPos, int originalvPos, int hPos, int vPos, int hAdd, int vAdd, int fromhPos, byte piece, byte[][] board) {
        hPos += hAdd;
        vPos += vAdd;

        try {
            if (board[hPos][vPos] == piece && hPos == fromhPos) {
                if (Math.abs(piece) != BLACK_KING) {
                    if (isKingInCheckAfterMove(board, (byte)(piece / Math.abs(piece)), hPos, vPos, originalhPos, originalvPos)) {
                        return -1;
                    }
                }

                return vPos;
            } else if (board[hPos][vPos] != EMPTY) {
                return -1;
            }
        } catch (IndexOutOfBoundsException e) {
            return -1;
        }

        return getMultiMovePiecevPosRec(originalhPos, originalvPos, hPos, vPos, hAdd, vAdd, fromhPos, piece, board);
    }

    private static int getMultiMovePiecehPos(int hPos, int vPos, int fromvPos, byte piece, byte[][] board, byte[][] moveData) {
        for (int i = 0; i < moveData.length; i++) {
            int fromhPos = getMultiMovePiecehPosRec(hPos, vPos, hPos, vPos, moveData[i][0], moveData[i][1], fromvPos, piece, board);

            if (fromhPos != -1) {
                return fromhPos;
            }
        }

        return -1;
    }

    private static int getMultiMovePiecehPosRec(int originalhPos, int originalvPos, int hPos, int vPos, int hAdd, int vAdd, int fromvPos, byte piece, byte[][] board) {
        hPos += hAdd;
        vPos += vAdd;

        try {
            if (board[hPos][vPos] == piece && vPos == fromvPos) {
                if (Math.abs(piece) != BLACK_KING) {
                    if (isKingInCheckAfterMove(board, (byte)(piece / Math.abs(piece)), hPos, vPos, originalhPos, originalvPos)) {
                        return -1;
                    }
                }

                return hPos;
            } else if (board[hPos][vPos] != EMPTY) {
                return -1;
            }
        } catch (IndexOutOfBoundsException e) {
            return -1;
        }

        return getMultiMovePiecehPosRec(originalhPos, originalvPos, hPos, vPos, hAdd, vAdd, fromvPos, piece, board);
    }

    private static boolean validateMove(PGNMove move) {
        String strippedMove = move.getMove();

        if (move.isCastle()) {
            return true;
        } else if (move.isEndGameMarked()) {
            return true;
        } else if (strippedMove.length() == MOVE_TYPE_1_LENGTH) {
            return strippedMove.matches(MOVE_TYPE_1_RE);
        } else if (strippedMove.length() == MOVE_TYPE_2_LENGTH) {
            return strippedMove.matches(MOVE_TYPE_2_RE) || strippedMove.matches(MOVE_TYPE_5_RE);
        } else if (strippedMove.length() == MOVE_TYPE_3_LENGTH) {
            return strippedMove.matches(MOVE_TYPE_3_RE) || strippedMove.matches(MOVE_TYPE_6_RE);
        } else if (strippedMove.length() == MOVE_TYPE_4_LENGTH) {
            return strippedMove.matches(MOVE_TYPE_4_RE);
        }

        return false;
    }

    private static boolean isKingInCheckAfterMove(byte[][] board, byte color, int hPos, int vPos, int tohPos, int tovPos) {
        try {
            byte king = (byte)(BLACK_KING * color);
            int kinghPos = -1;
            int kingvPos = -1;

            OUT : {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (board[j][i] == king) {
                            kinghPos = j;
                            kingvPos = i;
                            break OUT;
                        }
                    }
                }
            }

            if (kinghPos == -1 || kingvPos == -1) {
                return false;
            }

            byte piece = (byte)(-1 * color * BLACK_BISHOP);

            for (int i = 0; i < BISHOP_SEARCH_PATH.length; i++) {
                if (isKingInCheckAfterMoveRec(board, piece, kinghPos, kingvPos, hPos, vPos, tohPos, tovPos, BISHOP_SEARCH_PATH[i][0], BISHOP_SEARCH_PATH[i][1])) {
                    return true;
                }
            }

            piece = (byte)(-1 * color * BLACK_ROOK);

            for (int i = 0; i < ROOK_SEARCH_PATH.length; i++) {
                if (isKingInCheckAfterMoveRec(board, piece, kinghPos, kingvPos, hPos, vPos, tohPos, tovPos, ROOK_SEARCH_PATH[i][0], ROOK_SEARCH_PATH[i][1])) {
                    return true;
                }
            }

            piece = (byte)(-1 * color * BLACK_QUEEN);

            for (int i = 0; i < QUEEN_KING_SEARCH_PATH.length; i++) {
                if (isKingInCheckAfterMoveRec(board, piece, kinghPos, kingvPos, hPos, vPos, tohPos, tovPos, QUEEN_KING_SEARCH_PATH[i][0], QUEEN_KING_SEARCH_PATH[i][1])) {
                    return true;
                }
            }
        } catch (Exception e) {
        }

        return false;

    }

    private static boolean isKingInCheckAfterMoveRec(byte[][] board, byte piece, int hPos, int vPos, int skiphPos, int skipvPos, int tohPos, int tovPos, int hAdd, int vAdd) {
        hPos += hAdd;
        vPos += vAdd;

        if (hPos < 0 || hPos > 7 || vPos < 0 || vPos > 7 || (hPos == tohPos && vPos == tovPos)) {
            return false;
        }

        if (board[hPos][vPos] == EMPTY || (skiphPos == hPos && skipvPos == vPos)) {
            return isKingInCheckAfterMoveRec(board, piece, hPos, vPos, skiphPos, skipvPos, tohPos, tovPos, hAdd, vAdd);
        }

        return board[hPos][vPos] == piece;
    }

    private static byte[][] createDefaultBoard() {
        return new byte[][] {
                { WHITE_ROOK, WHITE_PAWN, EMPTY, EMPTY, EMPTY, EMPTY, BLACK_PAWN, BLACK_ROOK, },
                { WHITE_KNIGHT, WHITE_PAWN, EMPTY, EMPTY, EMPTY, EMPTY, BLACK_PAWN, BLACK_KNIGHT, },
                { WHITE_BISHOP, WHITE_PAWN, EMPTY, EMPTY, EMPTY, EMPTY, BLACK_PAWN, BLACK_BISHOP, },
                { WHITE_QUEEN, WHITE_PAWN, EMPTY, EMPTY, EMPTY, EMPTY, BLACK_PAWN, BLACK_QUEEN, },
                { WHITE_KING, WHITE_PAWN, EMPTY, EMPTY, EMPTY, EMPTY, BLACK_PAWN, BLACK_KING, },
                { WHITE_BISHOP, WHITE_PAWN, EMPTY, EMPTY, EMPTY, EMPTY, BLACK_PAWN, BLACK_BISHOP, },
                { WHITE_KNIGHT, WHITE_PAWN, EMPTY, EMPTY, EMPTY, EMPTY, BLACK_PAWN, BLACK_KNIGHT, },
                { WHITE_ROOK, WHITE_PAWN, EMPTY, EMPTY, EMPTY, EMPTY, BLACK_PAWN, BLACK_ROOK, },
        };
    }

    private static byte[][] createBoard(String fen) {
        return null;
    }

    private static byte[][] cloneBoard(byte[][] board) {
        byte[][] newBoard = new byte[board.length][];

        for(int i = 0; i < board.length; i++) {
            newBoard[i] = board[i].clone();
        }

        return newBoard;
    }

    private static byte pieceToByte(String piece, int color) {

        int p = EMPTY;

        if (piece.equals(PAWN)) {
            p = BLACK_PAWN * color;
        } else if (piece.equals(ROOK)) {
            p = BLACK_ROOK * color;
        } else if (piece.equals(KNIGHT)) {
            p = BLACK_KNIGHT * color;
        } else if (piece.equals(BISHOP)) {
            p = BLACK_BISHOP * color;
        } else if (piece.equals(QUEEN)) {
            p = BLACK_QUEEN * color;
        } else if (piece.equals(KING)) {
            p = BLACK_KING * color;
        }

        return (byte)p;
    }

}
