package Pieces.White;


import Board.Board;
import Board.Square;
import Coordinate.Coordinate;
import Coordinate.CoordinateOutOfBoundsException;
import Pieces.Color;
import Pieces.Piece;
import Pieces.PinCondition;
import Pieces.Type;
import Turns.Move;

import java.util.ArrayList;
import java.util.HashMap;

public class Pawn extends Piece {
    public Pawn(char file, int rank) {
        super(file, rank);
        type = Type.PAWN;
        color = Color.WHITE;
    }

    @Override
    public ArrayList<Move> getMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        if (this.getPosition().rank() == 8) {
            return moves;
        }

        HashMap<Character, HashMap<Integer, Square>> board = Board.getInstance().getBoard();
        boolean hasPieceOnNorth = board.get(this.getPosition().file()).get(this.getPosition().rank()+1).hasPiece();
        boolean hasPieceOnNorthNorth = board.get(this.getPosition().file()).get(this.getPosition().rank()+2).hasPiece();

        boolean hasPieceOnNorthWest;
        if (this.getPosition().file() != 'a') {
            hasPieceOnNorthWest = board.get((char) (this.getPosition().file() - 1)).get(this.getPosition().rank() + 1).hasPiece();
        } else {
            hasPieceOnNorthWest = false;
        }

        boolean hasPieceOnNorthEast;
        if (this.getPosition().file() != 'h') {
            hasPieceOnNorthEast = board.get((char) (this.getPosition().file() + 1)).get(this.getPosition().rank() + 1).hasPiece();
        } else {
            hasPieceOnNorthEast = false;
        }




        try {
            switch (isPinned()) {
                case VERTICAL -> {
                    if (!hasPieceOnNorth) {
                        try {
                            moves.add(new Move(this, this.getPosition().getNorth(1), this.getPosition()));
                            if (!hasMoved() && !hasPieceOnNorthNorth) {
                                moves.add(new Move(this, this.getPosition().getNorth(2), this.getPosition()));
                            }
                        } catch (CoordinateOutOfBoundsException ignored) {
                        }
                    }
                }

                case HORIZONTAL -> {
                }
                case POSITIVE_DIAGONAL -> {
                    if (this.getPosition().file() < 'h' && this.getPosition().rank() < 8) {
                        Piece pieceOnNorthEast = board.get((char) (this.getPosition().file() + 1)).get(this.getPosition().rank() + 1).getPieceOnSquare();

                        if (pieceOnNorthEast.getColor() == Color.BLACK) {
                            try {
                                moves.add(new Move(this, this.getPosition().getNorthEast(1), this.getPosition()));
                            } catch (CoordinateOutOfBoundsException ignored) {
                            }
                        }
                    }
                }
                case NEGATIVE_DIAGONAL -> {
                    if (this.getPosition().file() > 'a' && this.getPosition().rank() < 8) {
                        Piece pieceOnNorthWest = board.get((char) (this.getPosition().file() - 1)).get(this.getPosition().rank() + 1).getPieceOnSquare();

                        if (pieceOnNorthWest.getColor() == Color.BLACK) {
                            try {
                                moves.add(new Move(this, this.getPosition().getNorthWest(1), this.getPosition()));
                            } catch (CoordinateOutOfBoundsException ignored) {
                            }
                        }

                    }
                }

                case NOT_PINNED -> {
                    try {
                        if (!hasPieceOnNorth) {
                            moves.add(new Move(this, this.getPosition().getNorth(1), this.getPosition()));
                            if (!hasMoved() && !hasPieceOnNorthNorth) {
                                moves.add(new Move(this, this.getPosition().getNorth(2), this.getPosition()));
                            }
                        }


                        if (this.getPosition().file() < 'h' && this.getPosition().rank() < 8) {
                            Piece pieceOnNorthEast = board.get((char) (this.getPosition().file() + 1)).get(this.getPosition().rank() + 1).getPieceOnSquare();

                            if (pieceOnNorthEast != null && pieceOnNorthEast.getColor() == Color.BLACK) {
                                moves.add(new Move(this, this.getPosition().getNorthEast(1), this.getPosition()));
                            }
                        }

                        if (this.getPosition().file() > 'a' && this.getPosition().rank() < 8) {
                            Piece pieceOnNorthWest = board.get((char) (this.getPosition().file() - 1)).get(this.getPosition().rank() + 1).getPieceOnSquare();

                            if (pieceOnNorthWest != null && pieceOnNorthWest.getColor() == Color.BLACK) {
                                moves.add(new Move(this, this.getPosition().getNorthWest(1), this.getPosition()));
                            }
                        }

                    } catch (CoordinateOutOfBoundsException ignored) {
                    }
                }
            }
        } catch (CoordinateOutOfBoundsException ignored) {}



        return moves;
    }

    public PinCondition isPinned() throws CoordinateOutOfBoundsException {
        int[] biggerResult = this.getPosition().bigger(Board.getInstance().getWKingCord());
        int[] minusResult = this.getPosition().minus(Board.getInstance().getWKingCord());
        boolean onSameRank = this.getPosition().rank() == Board.getInstance().getWKingCord().rank();
        boolean onSameFile = this.getPosition().file() == Board.getInstance().getWKingCord().file();
        boolean onSameDiagonal = Math.abs(minusResult[0]) == Math.abs(minusResult[1]);


        //these steps look for a vertical pin
        if (onSameFile) {
            Piece northPiece = null;
            switch (biggerResult[1]) { //checks if king or pawn is more northern
                case 1 -> northPiece = this;
                case -1 -> northPiece = Board.getInstance().getWKing();
                default -> {
                }
            }

            if (northPiece != null) { //not null assertion

                //checks that there are no pieces between the pawn and king
                for (int i = 0; i < Math.abs(minusResult[1]) - 1; i++) {
                    Coordinate south = northPiece.getPosition().getSouth(1);
                    Square southSquare = Board.getInstance().getBoard().get(south.file()).get(south.rank());

                    if (southSquare.hasPiece()) { //returns not pinned if a piece is found
                        return PinCondition.NOT_PINNED;
                    }
                }


                int i = 1;
                while (true) {
                    try { // checks if there is a rook or queen on the other side of the pawn pinning the piece
                        Coordinate currentCord = northPiece instanceof Pawn ? this.getPosition().getNorth(i) : Board.getInstance().getWKing().getPosition().getSouth(i);
                        Square currentSquare = Board.getInstance().getBoard().get(currentCord.file()).get(currentCord.rank());
                        if (!currentSquare.hasPiece()) {
                            i++;
                            continue;
                        }

                        if (!(currentSquare.getPieceOnSquare() instanceof Pieces.Black.Queen) && !(currentSquare.getPieceOnSquare() instanceof Pieces.Black.Rook)) {
                            return PinCondition.NOT_PINNED;
                        } else {
                            return PinCondition.VERTICAL;
                        }
                    } catch (CoordinateOutOfBoundsException ignored) {
                        return PinCondition.NOT_PINNED;
                    }
                }
            }
        }

        //these steps look for a horizontal pin
        if (onSameRank) {
            Piece eastPiece = null;
            switch (biggerResult[0]) { //checks which piece is on the east and west side
                case 1 -> eastPiece = this;
                case -1 -> eastPiece = Board.getInstance().getWKing();
                default -> {
                }
            }

            if (eastPiece != null) { //not null assertion

                for (int i = 0; i < Math.abs(minusResult[0]) - 1; i++) { //checks that there is no piece between the pawn and king
                    Coordinate west = eastPiece.getPosition().getWest(1);
                    Square westSquare = Board.getInstance().getBoard().get(west.file()).get(west.rank());

                    if (westSquare.hasPiece()) {
                        return PinCondition.NOT_PINNED;
                    }
                }

                int i = 1;
                while (true) {
                    try {
                        Coordinate currentCord = eastPiece instanceof Pawn ? this.getPosition().getEast(i) : this.getPosition().getWest(i);
                        Square currentSquare = Board.getInstance().getBoard().get(currentCord.file()).get(currentCord.rank());
                        if (!currentSquare.hasPiece()) {
                            i++;
                            continue;
                        }

                        if (!(currentSquare.getPieceOnSquare() instanceof Pieces.Black.Queen) && !(currentSquare.getPieceOnSquare() instanceof Pieces.Black.Rook)) {
                            return PinCondition.NOT_PINNED;
                        } else {
                            return PinCondition.HORIZONTAL;
                        }
                    } catch (CoordinateOutOfBoundsException ignored) {
                        return PinCondition.NOT_PINNED;
                    }

                }

            }
        }

        if (onSameDiagonal) { //these steps look for a diagonal pin
            if (biggerResult[0] * biggerResult[1] == 1) {
                Piece southWestPiece = null;
                switch (biggerResult[0]) {
                    case -1 -> southWestPiece = this;
                    case 1 -> southWestPiece = Board.getInstance().getWKing();
                    default -> {
                    }
                }

                if (southWestPiece != null) {
                    for (int i = 0; i < Math.abs(minusResult[0]) - 1; i++) {
                        Coordinate currentCord = southWestPiece.getPosition().getNorthEast(1);
                        Square currentSquare = Board.getInstance().getBoard().get(currentCord.file()).get(currentCord.rank());

                        if (currentSquare.hasPiece()) {
                            return PinCondition.NOT_PINNED;
                        }
                    }

                    int i = 1;
                    while (true) try {
                        Coordinate currentCord = southWestPiece instanceof Pawn ? this.getPosition().getSouthWest(i) : this.getPosition().getNorthEast(i);

                        Square currentSquare = Board.getInstance().getBoard().get(currentCord.file()).get(currentCord.rank());
                        if (!currentSquare.hasPiece()) {
                            i++;
                            continue;
                        }

                        if (!(currentSquare.getPieceOnSquare() instanceof Pieces.Black.Queen) && !(currentSquare.getPieceOnSquare() instanceof Pieces.Black.Bishop)) {
                            return PinCondition.NOT_PINNED;
                        } else {
                            return PinCondition.POSITIVE_DIAGONAL;
                        }
                    } catch (CoordinateOutOfBoundsException ignored) {
                        return PinCondition.NOT_PINNED;
                    }
                }
            } else {
                Piece northWestPiece = null;
                switch (biggerResult[0]) {
                    case -1 -> northWestPiece = this;
                    case 1 -> northWestPiece = Board.getInstance().getWKing();
                    default -> {
                    }
                }

                if (northWestPiece != null) {
                    for (int i = 0; i < minusResult[0]; i++) {
                        Coordinate currentCord = northWestPiece.getPosition().getSouthEast(1);
                        Square currentSquare = Board.getInstance().getBoard().get(currentCord.file()).get(currentCord.rank());

                        if (currentSquare.hasPiece()) {
                            return PinCondition.NOT_PINNED;
                        }
                    }

                    int i = 1;
                    while (true) {
                        try {
                            Coordinate currentCord = northWestPiece instanceof Pawn ? this.getPosition().getNorthWest(i) : this.getPosition().getSouthEast(i);
                            Square currentSquare = Board.getInstance().getBoard().get(currentCord.file()).get(currentCord.rank());
                            if (!currentSquare.hasPiece()) {
                                i++;
                                continue;
                            }

                            if (!(currentSquare.getPieceOnSquare() instanceof Pieces.Black.Queen) && !(currentSquare.getPieceOnSquare() instanceof Pieces.Black.Bishop)) {
                                return PinCondition.NOT_PINNED;
                            } else {
                                return PinCondition.NEGATIVE_DIAGONAL;
                            }
                        } catch (CoordinateOutOfBoundsException ignored) {
                            return PinCondition.NOT_PINNED;
                        }
                    }


                }
            }
        }
        return PinCondition.NOT_PINNED;
    }
}
