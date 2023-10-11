package Turns;

import Board.Square;

import java.util.ArrayList;
import java.util.HashMap;

public class BlackMove {
    private ArrayList<Move> legalMoves;

    private BlackMove(HashMap<Character, HashMap<Integer, Square>> board) {
        setLegalMoves(board);
    }


    private void setLegalMoves(HashMap<Character, HashMap<Integer, Square>> board) {
        legalMoves = new ArrayList<>();
    }

    public static ArrayList<Move> getLegalMoves(HashMap<Character, HashMap<Integer, Square>> board) {
        BlackMove blackMove = new BlackMove(board);
        return blackMove.legalMoves;
    }
}
