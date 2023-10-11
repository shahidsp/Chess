package Turns;

import Board.Board;
import Board.Square;
import Coordinate.Coordinate;
import Coordinate.CoordinateOutOfBoundsException;
import Pieces.Color;
import Pieces.Piece;

import java.util.ArrayList;
import java.util.HashMap;

public class WhiteMove {
    private ArrayList<Move> legalMoves;

    private WhiteMove(HashMap<Character, HashMap<Integer, Square>> board) {
        setLegalMoves(board);
    }

    public static ArrayList<Move> getLegalMoves(HashMap<Character, HashMap<Integer, Square>> board) {
        return new WhiteMove(board).legalMoves;
    }

    private void setLegalMoves(HashMap<Character, HashMap<Integer, Square>> board) {
        legalMoves = new ArrayList<>();
    }
}
