package Pieces;


import Board.Square;
import Coordinate.Coordinate;
import Coordinate.CoordinateOutOfBoundsException;
import Turns.Move;

import java.util.ArrayList;

public abstract class Piece {
    private Coordinate position;
    private Square square;
    protected Type type = Type.PIECE;
    protected Color color = Color.PIECE;

    private boolean hasMoved = false;

    public Coordinate getPosition() {
        return position;
    }

    public Piece(char file, int rank) {
        file = Character.toLowerCase(file);
        this.position = new Coordinate(file, rank);
        this.hasMoved = false;
    }
    public Piece() {}

    public Piece(Coordinate coordinate) {
        this.position = coordinate;
        this.hasMoved = false;
    }

    public Color getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }


    public boolean hasMoved() {
        return hasMoved;
    }


    void move(Square square) {
        this.square = square;
        position = square.getCoordinate();
        hasMoved = true;
    }
    public void setPosition(Square square) {
        this.square = square;
        position = square.getCoordinate();
    }

    public Square getSquare() {
        return square;
    }

    public abstract ArrayList<Move> getMoves();

    public PinCondition isPinned() throws CoordinateOutOfBoundsException {
        return null;
    }

    public String toString() {
        return color.name() + " " + type.name();
    }
}
