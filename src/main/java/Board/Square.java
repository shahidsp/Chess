package Board;

import Coordinate.Coordinate;
import Pieces.Piece;

public class Square {
    private final Coordinate coordinate;
    private Piece pieceOnSquare;
    private boolean hasPiece = false;

    public Square(char file, int rank) {
        this.coordinate = new Coordinate(file, rank);
    }

    public boolean hasPiece() {
        return hasPiece;
    }

    public Piece getPieceOnSquare() {
        return pieceOnSquare;
    }

    public void setPieceOnSquare(Piece pieceOnSquare) {
        pieceOnSquare.setPosition(this);
        this.pieceOnSquare = pieceOnSquare;
        hasPiece = true;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void clear() {
        pieceOnSquare = null;
        hasPiece = false;
    }



    @Override
    public String toString() {
        if (hasPiece) {
            return pieceOnSquare.toString();
        }

        return "Empty";
    }
}
