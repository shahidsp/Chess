package Pieces.Black;

import Pieces.Color;
import Pieces.Piece;
import Pieces.Type;
import Turns.Move;

import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(char file, int rank) {
        super(file, rank);
        type = Type.ROOK;
        color = Color.BLACK;
    }

    //TODO
    //  implement getMoves()
    @Override
    public ArrayList<Move> getMoves() {
        return null;
    }
}
