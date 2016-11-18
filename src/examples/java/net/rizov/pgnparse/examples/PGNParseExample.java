/*
 * This file is part of PGNParse.
 *
 * PGNParse is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PGNParse is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PGNParse.  If not, see <http://www.gnu.org/licenses/>. 
 */
package net.rizov.pgnparse.examples;

import net.rizov.pgnparse.*;
import net.rizov.pgnparse.PGNParseException;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;


/**
 *
 * @author Deyan Rizov
 *
 */
public class PGNParseExample {

	public static void main(String[] args) throws IOException, PGNParseException {
		if (args.length == 0) {
			System.out.println("Usage:");
			System.out.println("\tpgn_path");
			return;
		}
		
		File file = new File(args[0]);
		
		if (!file.exists()) {
			System.out.println("File does not exist!");
			return;
		}
		
		PGNSource source = new PGNSource(file);
		Iterator<PGNGame> i = source.listGames().iterator();
		
		while (i.hasNext()) {
			PGNGame game = i.next();
			
			System.out.println("############################");
			Iterator<String> tagsIterator = game.getTagKeysIterator();
			
			while (tagsIterator.hasNext()) {
				String key = tagsIterator.next();
				System.out.println(key + " {" + game.getTag(key) + "}");
			}

			if (game.isCustomInitialPositionUsed()) {
				System.out.println("FEN Custom initial position is used!");
			}
			
			System.out.println();
			printMoves(game, 1, "");
			System.out.println();
		}
	}

	private static void printMoves(PGNMoveContainer container, int startMove, String tab) {
		Iterator<PGNMove> movesIterator = container.getMovesIterator();
		int num = startMove;

		while (movesIterator.hasNext()) {
			PGNMove move = movesIterator.next();

			boolean isPairStart = num % 2 == 1;

			if (isPairStart && !move.isEndGameMarked()) {
				System.out.println(tab + ((num + 1) / 2) + ". ");
			}

            if (move.isEndGameMarked()) {
				System.out.println();
				System.out.print("Result: " + move.getMove());
			} else {
				System.out.print(tab + "\t " + move.getColor() + ": ");

                if (move.isKingSideCastle()) {
                    System.out.print("[O-O]\t");
                } else if (move.isQueenSideCastle()) {
                    System.out.print("[O-O-O]\t");
                } else {
                    System.out.print("[" + move.getFromSquare() + "]->[" + move.getToSquare() + "]\t");
                }
            }

			if (move.getComment().length() > 0) {
				System.out.print("// " + move.getComment());
			}

			System.out.println();

			if (move.hasVariations()) {
                if (isPairStart) {
                    System.out.println();
                }

                printVariations(move, num, tab);
            }

            num++;
		}
	}

	private static void printVariations(PGNMove move, int currentMoveNum, String tab) {
        Iterator<PGNVariation> vi = move.getVariationsIterator();

        while (vi.hasNext()) {
            printMoves(vi.next(), currentMoveNum, tab + "\t");
        }
    }

}
