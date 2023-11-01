package Board;

import Coordinate.Coordinate;
import Pieces.Color;
import Pieces.Piece;
import Pieces.Type;
import Turns.Move;

import java.util.ArrayList;
import java.util.HashMap;

public class TrialBoard {

    private final HashMap<Character, HashMap<Integer, Square>> board;



    public TrialBoard() {
        board = new HashMap<>();

        for (char file = 'a'; file < 'i'; file++) {
            HashMap<Integer, Square> File = new HashMap<>();
            for (int rank = 1; rank < 9; rank++) {
                if (Board.getInstance().getBoard().get(file).get(rank).hasPiece()) {
                    Type type = Board.getInstance().getBoard().get(file).get(rank).getPieceOnSquare().getType();
                    Color color = Board.getInstance().getBoard().get(file).get(rank).getPieceOnSquare().getColor();
                    Piece piece;
                    if (color == Color.WHITE) {
                        switch (type) {
                            case PAWN -> piece = new Pieces.White.Pawn(file,rank);
                            case ROOK -> piece = new Pieces.White.Rook(file,rank);
                            case KNIGHT -> piece = new Pieces.White.Knight(file, rank);
                            case BISHOP -> piece = new Pieces.White.Bishop(file, rank);
                            case QUEEN -> piece = new Pieces.White.Queen(file,rank);
                            case KING -> piece= new Pieces.White.King(file,rank);
                            default -> piece = new Piece() {
                                @Override
                                public ArrayList<Move> getMoves() {
                                    return null;
                                }
                            };
                        }
                    } else {
                        switch (type) {
                            case PAWN -> piece = new Pieces.Black.Pawn(file,rank);
                            case ROOK -> piece = new Pieces.Black.Rook(file,rank);
                            case KNIGHT -> piece = new Pieces.Black.Knight(file, rank);
                            case BISHOP -> piece = new Pieces.Black.Bishop(file, rank);
                            case QUEEN -> piece = new Pieces.Black.Queen(file,rank);
                            case KING -> piece= new Pieces.Black.King(file,rank);
                            default -> piece = new Piece() {
                                @Override
                                public ArrayList<Move> getMoves() {
                                    return null;
                                }
                            };
                        }
                    }
                    Square square = new Square(file, rank);
                    square.setPieceOnSquare(piece);
                    File.put(rank, square);
                } else {
                    File.put(rank, new Square(file, rank));
                }
            }
            board.put(file, File);
        }
    }

    public HashMap<Character, HashMap<Integer, Square>> getBoard() {return board;}

    public Square getSquare(Coordinate coordinate) {
        return this.board.get(coordinate.file()).get(coordinate.rank());
    }

    public Piece getPieceOnSquare(Coordinate coordinate) {
        return getSquare(coordinate).getPieceOnSquare();
    }
}
