package com.github.deianvn.pgnparse.examples;

import java.io.File;
import java.io.IOException;
import com.github.deianvn.pgnparse.PGNParseException;
import com.github.deianvn.pgnparse.PGNSource;

public class PGNParseBenchmark {

  public static void main(String[] args)
      throws IOException, PGNParseException {
    if (args.length == 0) {
      System.out.println("Usage:");
      System.out.println("\tpgn_directory_path");
      return;
    }

    File file = new File(args[0]);

    if (!file.exists() || !file.isDirectory()) {
      System.out.println("Directory does not exist!");
      return;
    }

    String[] pgnFileNames = file.list((dir, name) -> name.endsWith(".pgn"));

    if (pgnFileNames == null) {
      System.out.println("Error reading files");
    }

    long startTime = System.currentTimeMillis();
    int filesParsed = 0;
    int gamesParsed = 0;

    for (String pgnFileName : pgnFileNames) {
      File pgnFile = new File(file.getAbsolutePath() + File.separator + pgnFileName);
      PGNSource source = new PGNSource(pgnFile);
      gamesParsed += source.listGames().size();
      filesParsed++;
    }

    long duration = System.currentTimeMillis() - startTime;
    System.out.println("Files parsed: " + filesParsed);
    System.out.println("Games parsed: " + gamesParsed);
    System.out.println("Duration: " + duration);

  }

}
