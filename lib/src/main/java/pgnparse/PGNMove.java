package pgnparse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PGNMove {

  private String move;

  private String fullMove;

  private String fromSquare;

  private String toSquare;

  private String piece;

  private String color;

  private String capturedPiece;

  private String comment = "";

  private boolean checked;

  private boolean checkMated;

  private boolean captured;

  private boolean promoted;

  private String promotion;

  private boolean endGameMarked;

  private String endGameMark;

  private boolean kingSideCastle;

  private boolean queenSideCastle;

  private boolean enpassant;

  private boolean enpassantCapture;

  private String enpassantPieceSquare;

  private List<PGNVariation> variations;

  public String getMove() {
    return move;
  }

  public void setMove(String move) {
    this.move = move;
  }

  public String getFullMove() {
    return fullMove;
  }

  public void setFullMove(String fullMove) {
    this.fullMove = fullMove;
  }

  public String getFromSquare() {
    return fromSquare;
  }

  public void setFromSquare(String fromSquare) {
    this.fromSquare = fromSquare;
  }

  public String getToSquare() {
    return toSquare;
  }

  public void setToSquare(String toSquare) {
    this.toSquare = toSquare;
  }

  public String getPiece() {
    return piece;
  }

  public void setPiece(String piece) {
    this.piece = piece;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getCapturedPiece() {
    return capturedPiece;
  }

  public void setCapturedPiece(String capturedPiece) {
    this.capturedPiece = capturedPiece;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public boolean isChecked() {
    return checked;
  }

  public void setChecked(boolean checked) {
    this.checked = checked;
  }

  public boolean isCheckMated() {
    return checkMated;
  }

  public void setCheckMated(boolean checkMated) {
    this.checkMated = checkMated;
  }

  public boolean isCaptured() {
    return captured;
  }

  public void setCaptured(boolean captured) {
    this.captured = captured;
  }

  public boolean isPromoted() {
    return promoted;
  }

  public void setPromoted(boolean promoted) {
    this.promoted = promoted;
  }

  public String getPromotion() {
    return promotion;
  }

  public void setPromotion(String promotion) {
    this.promotion = promotion;
  }

  public boolean isEndGameMarked() {
    return endGameMarked;
  }

  public void setEndGameMarked(boolean endGameMarked) {
    this.endGameMarked = endGameMarked;
  }

  public String getEndGameMark() {
    return endGameMark;
  }

  public void setEndGameMark(String endGameMark) {
    this.endGameMark = endGameMark;
  }

  public boolean isKingSideCastle() {
    return kingSideCastle;
  }

  public void setKingSideCastle(boolean kingSideCastle) {
    this.kingSideCastle = kingSideCastle;
  }

  public boolean isQueenSideCastle() {
    return queenSideCastle;
  }

  public void setQueenSideCastle(boolean queenSideCastle) {
    this.queenSideCastle = queenSideCastle;
  }

  public boolean isEnpassant() {
    return enpassant;
  }

  public void setEnpassant(boolean enpassant) {
    this.enpassant = enpassant;
  }

  public boolean isEnpassantCapture() {
    return enpassantCapture;
  }

  public void setEnpassantCapture(boolean enpassantCapture) {
    this.enpassantCapture = enpassantCapture;
  }

  public String getEnpassantPieceSquare() {
    return enpassantPieceSquare;
  }

  public void setEnpassantPieceSquare(String enpassantPieceSquare) {
    this.enpassantPieceSquare = enpassantPieceSquare;
  }

  public boolean isCastle() {
    return isKingSideCastle() || isQueenSideCastle();
  }

  public void addVariation(PGNVariation variation) {
    if (variations == null) {
      variations = new ArrayList<PGNVariation>();
    }

    variations.add(variation);
  }

  public boolean hasVariations() {
    return variations != null && variations.size() > 0;
  }

  public List<PGNVariation> getVariations() {
    return Collections.unmodifiableList(variations);
  }

  public Iterator<PGNVariation> getVariationsIterator() {
    return Collections.unmodifiableList(variations).iterator();
  }
}
