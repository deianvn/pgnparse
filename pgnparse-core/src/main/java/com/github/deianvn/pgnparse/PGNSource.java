package com.github.deianvn.pgnparse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PGNSource {

  private String source;

  public PGNSource(String pgn) {
    Objects.requireNonNull(pgn);
    this.source = pgn;
  }

  public PGNSource(File file) throws IOException {
    this(new FileInputStream(file));
  }

  public PGNSource(URL url) throws IOException {
    this(url.openStream());
  }

  public PGNSource(InputStream inputStream) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
    String line;
    StringBuilder buffer = new StringBuilder();

    while ((line = br.readLine()) != null) {
      buffer.append(line).append("\n");
    }

    br.close();
    this.source = buffer.toString();
  }

  @Override
  public String toString() {
    return source;
  }

  public List<PGNGame> listGames() throws PGNParseException {
    List<String> pgns = PGNParser.split(source);
    ArrayList<PGNGame> games = new ArrayList<>();

    for (String pgn : pgns) {
      games.add(PGNParser.parse(pgn));
    }

    return games;
  }

  public List<PGNGame> forceListGames() {
    List<String> pgns = PGNParser.split(source);
    ArrayList<PGNGame> games = new ArrayList<>();

    for (String pgn : pgns) {
      try {
        games.add(PGNParser.parse(pgn));
      } catch (PGNParseException e) {
      }
    }

    return games;
  }

}
