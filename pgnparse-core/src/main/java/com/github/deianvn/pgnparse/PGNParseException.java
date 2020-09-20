package com.github.deianvn.pgnparse;

public class PGNParseException extends Exception {

  final String message;

  /**
   *
   */
  public PGNParseException(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
