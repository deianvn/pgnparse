package pgnparse;

public class PGNParseException extends Exception {

  private static final long serialVersionUID = -2253519940443925317L;

  public PGNParseException() {
  }

  /**
   *
   */
  public PGNParseException(String message) {
    super(message);
  }

  /**
   *
   */
  public PGNParseException(Throwable cause) {
    super(cause);
  }

  /**
   *
   */
  public PGNParseException(String message, Throwable cause) {
    super(message, cause);
  }

}
