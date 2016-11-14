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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PGNMove {

	private String move;

	private String fullMove;
	
	private String fromSquare;
	
	private String toSquare;
	
	private String piece;
	
	private Color color;

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

    void setMove(String move) {
        this.move = move;
    }

    public String getFullMove() {
        return fullMove;
    }

    void setFullMove(String fullMove) {
        this.fullMove = fullMove;
    }

    public String getFromSquare() {
        return fromSquare;
    }

    void setFromSquare(String fromSquare) {
        this.fromSquare = fromSquare;
    }

    public String getToSquare() {
        return toSquare;
    }

    void setToSquare(String toSquare) {
        this.toSquare = toSquare;
    }

    public String getPiece() {
        return piece;
    }

    void setPiece(String piece) {
        this.piece = piece;
    }

    public Color getColor() {
        return color;
    }

    void setColor(Color color) {
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

    void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isChecked() {
        return checked;
    }

    void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isCheckMated() {
        return checkMated;
    }

    void setCheckMated(boolean checkMated) {
        this.checkMated = checkMated;
    }

    public boolean isCaptured() {
        return captured;
    }

    void setCaptured(boolean captured) {
        this.captured = captured;
    }

    public boolean isPromoted() {
        return promoted;
    }

    void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public String getPromotion() {
        return promotion;
    }

    void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public boolean isEndGameMarked() {
        return endGameMarked;
    }

    void setEndGameMarked(boolean endGameMarked) {
        this.endGameMarked = endGameMarked;
    }

    public String getEndGameMark() {
        return endGameMark;
    }

    void setEndGameMark(String endGameMark) {
        this.endGameMark = endGameMark;
    }

    public boolean isKingSideCastle() {
        return kingSideCastle;
    }

    void setKingSideCastle(boolean kingSideCastle) {
        this.kingSideCastle = kingSideCastle;
    }

    public boolean isQueenSideCastle() {
        return queenSideCastle;
    }

    void setQueenSideCastle(boolean queenSideCastle) {
        this.queenSideCastle = queenSideCastle;
    }

    public boolean isEnpassant() {
        return enpassant;
    }

    void setEnpassant(boolean enpassant) {
        this.enpassant = enpassant;
    }

    public boolean isEnpassantCapture() {
        return enpassantCapture;
    }

    void setEnpassantCapture(boolean enpassantCapture) {
        this.enpassantCapture = enpassantCapture;
    }

    public String getEnpassantPieceSquare() {
        return enpassantPieceSquare;
    }

    void setEnpassantPieceSquare(String enpassantPieceSquare) {
        this.enpassantPieceSquare = enpassantPieceSquare;
    }

    public boolean isCastle() {
        return isKingSideCastle() || isQueenSideCastle();
    }

    void addVariation(PGNVariation variation) {
        if (variations == null) {
            variations = new ArrayList<PGNVariation>();
        }

        variations.add(variation);
    }

    public boolean hasVariations() {
        return variations != null && variations.size() > 0;
    }

    public Iterator<PGNVariation> getVariationsIterator() {
        return new PGNUnmodifiableIterator<PGNVariation>(variations.iterator());
    }
}
