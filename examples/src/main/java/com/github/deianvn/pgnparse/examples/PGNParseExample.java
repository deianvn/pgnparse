package com.github.deianvn.pgnparse.examples;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import pgnparse.PGNGame;
import pgnparse.PGNMove;
import pgnparse.PGNMoveContainer;
import pgnparse.PGNParseException;
import pgnparse.PGNSource;
import pgnparse.PGNVariation;

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
        System.out.print("Result: " + move.getEndGameMark());
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
