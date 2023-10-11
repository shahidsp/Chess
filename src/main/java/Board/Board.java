package Board;

import Coordinate.Coordinate;
import Pieces.Color;
import Pieces.Piece;
import Pieces.Type;
import Pieces.White.King;
import Turns.Move;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {

    private static Board boardInstance;
    private Coordinate wKingCord = new Coordinate('e', 2);
    private Coordinate bKingCord = new Coordinate('e', 8);

    //Represents a full chess board. The Hashmap<Integer, Square> represents a file on the board
    private final HashMap<Character, HashMap<Integer, Square>> board;

    public static Board getInstance() {
        if (boardInstance == null) {
            boardInstance = new Board();
        }
        return boardInstance;
    }
    private Board() {
        //instantiation of a chess board
        board = new HashMap<>();

        //generates files with squares that have an unchanging coordinate
        for (char currentFile = 'a'; currentFile < 105; currentFile++) { //iterates through each file
            HashMap<Integer, Square> file = new HashMap<>();
            for (int currentRank = 1; currentRank < 9; currentRank++) { //iterates through each rank in a file
                //instantiates the relevant square and adds it to the file
                Square square = new Square(currentFile, currentRank);
                file.put(currentRank, square);
            }

            //adds the file to the full board
            board.put(currentFile, file);
        }

        for (char currentFile = 'a'; currentFile < 105; currentFile++) { //sets the board for the beginning of play
            //puts a pawns on the 2nd and 7th ranks
            board.get(currentFile).get(2).setPieceOnSquare(new Pieces.White.Pawn(currentFile, 2));
            board.get(currentFile).get(7).setPieceOnSquare(new Pieces.Black.Pawn(currentFile, 7));

            switch ((int) currentFile) {
                case 97, 104 -> { //puts rook on the a and h file
                    board.get(currentFile).get(1).setPieceOnSquare(new Pieces.White.Rook(currentFile, 1));
                    board.get(currentFile).get(8).setPieceOnSquare(new Pieces.Black.Rook(currentFile, 8));
                }
                case 98, 103 -> {//puts knights on the b ang g file
                    board.get(currentFile).get(1).setPieceOnSquare(new Pieces.White.Knight(currentFile, 1));
                    board.get(currentFile).get(8).setPieceOnSquare(new Pieces.Black.Knight(currentFile, 8));
                }
                case 99, 102 -> {//puts bishops on the c and f file
                    board.get(currentFile).get(1).setPieceOnSquare(new Pieces.White.Bishop(currentFile, 1));
                    board.get(currentFile).get(8).setPieceOnSquare(new Pieces.Black.Bishop(currentFile, 8));
                }
                case 100 -> {//puts queens on the d file
                    board.get(currentFile).get(1).setPieceOnSquare(new Pieces.White.Queen(currentFile, 1));
                    board.get(currentFile).get(8).setPieceOnSquare(new Pieces.Black.Queen(currentFile, 8));
                }
                case 101 -> {//puts kings on the e file
                    board.get(currentFile).get(1).setPieceOnSquare(new Pieces.White.King(currentFile, 1));
                    setWKingCord(new Coordinate('e', 1));
                    board.get(currentFile).get(8).setPieceOnSquare(new Pieces.Black.King(currentFile, 8));
                    setBKingCord(new Coordinate('e', 8));
                }
            }
        }
    }

    public static HashMap<Character, HashMap<Integer, Square>> getTrialBoard() {
        HashMap<Character, HashMap<Integer, Square>> trialBoard = new HashMap<>();

        for (char file = 'a'; file < 'i'; file++) {
            HashMap<Integer, Square> File = new HashMap<>();
            for (int rank = 1; rank < 9; rank++) {
                if (boardInstance.board.get(file).get(rank).hasPiece()) {
                    Type type = boardInstance.board.get(file).get(rank).getPieceOnSquare().getType();
                    Color color = boardInstance.board.get(file).get(rank).getPieceOnSquare().getColor();
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
            trialBoard.put(file, File);
        }
        return trialBoard;
    }

    public Coordinate getWKingCord() {
        return wKingCord;
    }

    public void setWKingCord(Coordinate wKingCord) {
        this.wKingCord = wKingCord;
    }

    public Coordinate getBKingCord() {
        return bKingCord;
    }

    public void setBKingCord(Coordinate bKingCord) {
        this.bKingCord = bKingCord;
    }

    public HashMap<Character, HashMap<Integer, Square>> getBoard() {
        return board;
    }

    public King getWKing() {
        return  (King) board.get(wKingCord.file()).get(wKingCord.rank()).getPieceOnSquare();
    }

    public Pieces.Black.King getBking() {
        return (Pieces.Black.King) board.get(bKingCord.file()).get(bKingCord.rank()).getPieceOnSquare();
    }

    public Piece getKing(Color kingColor) {
        switch (kingColor) {
            case WHITE -> {
                return getWKing();
            }
            case BLACK -> {
                return getBking();
            }
            default -> {
                return null;
            }
        }
    }


    public void clearBoard() {

    }
}
