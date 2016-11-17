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

public class FENParser implements PGN {

    public static FENPosition parse(String fen) throws PGNParseException {

        String[] tokens = fen.split("\\s+|/");

        if (tokens.length != 13) {
            throw new PGNParseException("Invalid FEN position: " + fen);
        }

        FENPosition position = new FENPosition();
        parsePieces(position, tokens, fen);
        parseCastling(position, tokens, fen);
        parseEnpassant(position, tokens, fen);
        parseHalfMoves(position, tokens, fen);
        parseFullMoves(position, tokens, fen);

        return position;
    }

    private static void parsePieces(FENPosition position, String[] tokens, String fen) throws PGNParseException {

        for (int i = 0; i < 8; i++) {
            String line = tokens[i];

            for (int j = 0; j < line.length(); j++) {
                char ch = line.charAt(j);

                if (ch >= '1' && ch <= '8') {
                    j += ch - '1';
                    continue;
                }

                Piece piece = new Piece();

                if (ch >= 'a' && ch <= 'z') {
                    piece.setColor(BLACK);
                } else if (ch >= 'A' && ch <= 'Z') {
                    piece.setColor(WHITE);
                } else {
                    throw new PGNParseException("Invalid FEN position: " + fen);
                }

                piece.setType(String.valueOf(ch).toUpperCase());
                position.addPiece(String.valueOf((char)('a' + j)) + String.valueOf(8 - i), piece);
            }
        }

        if (tokens[8].charAt(0) == 'w') {
            position.setPlayerToMove(WHITE);
        } else {
            position.setPlayerToMove(BLACK);
        }

    }

    private static void parseCastling(FENPosition position, String[] tokens, String fen) throws PGNParseException {
        String castlingToken = tokens[9];

        for (int i = 0; i < castlingToken.length(); i++) {
            if (castlingToken.charAt(i) == '-') {
                if (castlingToken.length() != 1) {
                    throw new PGNParseException("Invalid FEN position: " + fen);
                }

                break;
            } else if (castlingToken.charAt(i) == 'K') {
                position.setWhiteKingCastleAvailable(true);
            } else if (castlingToken.charAt(i) == 'Q') {
                position.setWhiteQueenCastleAvailable(true);
            } else if (castlingToken.charAt(i) == 'k') {
                position.setBlackKingCastleAvailable(true);
            } else if (castlingToken.charAt(i) == 'q') {
                position.setBlackQueenCastleAvailable(true);
            }
        }
    }

    private static void parseEnpassant(FENPosition position, String[] tokens, String fen) throws PGNParseException {
        String enpassentToken = tokens[10];

        if (enpassentToken.length() == 2) {
            if (enpassentToken.charAt(0) >= 'a' && enpassentToken.charAt(0) <= 'h' &&
                    enpassentToken.charAt(1) >= '1' && enpassentToken.charAt(1) <= '8') {
                position.setEnpassantSquare(enpassentToken);
            } else {
                throw new PGNParseException("Invalid FEN position: " + fen);
            }
        } else {
            if (!enpassentToken.equals("-")) {
                throw new PGNParseException("Invalid FEN position: " + fen);
            }
        }
    }

    private static void parseHalfMoves(FENPosition position, String[] tokens, String fen) throws PGNParseException {
        try {
            int halfMoves = Integer.parseInt(tokens[11]);
            position.setHalfMoves(halfMoves);
        } catch (NumberFormatException e) {
            throw new PGNParseException("Invalid FEN position: " + fen);
        }
    }

    private static void parseFullMoves(FENPosition position, String[] tokens, String fen) throws PGNParseException {
        try {
            int fullMoves = Integer.parseInt(tokens[12]);
            position.setFullMoves(fullMoves);
        } catch (NumberFormatException e) {
            throw new PGNParseException("Invalid FEN position: " + fen);
        }
    }

}
