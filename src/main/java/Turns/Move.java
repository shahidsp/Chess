package Turns;

import Board.*;
import Coordinate.*;
import Pieces.Color;
import Pieces.Piece;
import Pieces.Type;

import java.util.ArrayList;
import java.util.HashMap;

import static Pieces.Color.BLACK;
import static Pieces.Color.WHITE;

public class Move {

    private final boolean isCheck, isMate, isTaking;

    private final Piece piece;
    private final Coordinate newCord, oldCord;
    public Move(Piece piece, Coordinate newCord, Coordinate oldCord) {
        Board board = Board.getInstance();
        this.piece = piece;
        this.newCord = newCord;
        this.oldCord = oldCord;
        isTaking = board.getBoard().get(newCord.file()).get(newCord.rank()).hasPiece();
        isCheck = check();
        isMate = mate();
    }

    //TODO
    //  Finish implementing check()

    private boolean check() {
        Color pieceColor = piece.getColor();
        Color kingColor;
        switch (pieceColor) {
            case WHITE -> kingColor = Color.BLACK;
            case BLACK -> kingColor = WHITE;
            default -> kingColor = Color.PIECE;
        }

        Piece king = Board.getInstance().getKing(kingColor);
        HashMap<Character, HashMap<Integer, Square>> trialBoard = Board.getTrialBoard();
        trialBoard.get(oldCord.file()).get(oldCord.rank()).clear();
        trialBoard.get(newCord.file()).get(newCord.rank()).setPieceOnSquare(piece);
        switch (piece.getType()) {
            case PAWN -> {
                try {
                    switch (pieceColor) {
                        case WHITE -> {
                            Piece NW = trialBoard.get(newCord.getNorthWest(1).file()).get(newCord.getNorthWest(1).rank()).getPieceOnSquare();
                            Piece NE = trialBoard.get(newCord.getNorthEast(1).file()).get(newCord.getEast(1).rank()).getPieceOnSquare();
                            if (NW == null && NE == null) {
                                return false;
                            }
                            if ((NW != null && NW.getType() == Type.KING && NW.getColor() == BLACK) || (NE != null && NE.getType() == Type.KING && NE.getColor() == BLACK)) {
                                return true;
                            }
                        }
                        case BLACK -> {
                            Piece SW = trialBoard.get(newCord.getSouthWest(1).file()).get(newCord.getSouthWest(1).rank()).getPieceOnSquare();
                            Piece SE = trialBoard.get(newCord.getSouthEast(1).file()).get(newCord.getSouthEast(1).rank()).getPieceOnSquare();

                            if (SW == null && SE == null) {
                                return false;
                            }

                            if ((SW != null && SW.getType() == Type.KING && SW.getColor() == WHITE) || (SE != null && SE.getType() == Type.KING && SE.getColor() == WHITE)) {
                                return true;
                            }
                        }
                        default -> {
                            return false;
                        }

                    }
                } catch (CoordinateOutOfBoundsException e) {
                    return false;
                }
            }
            case ROOK -> {
                int i = 0;
                while (true) {
                    try {
                        Coordinate cord = newCord.getNorth(i);
                        if (!trialBoard.get(cord.file()).get(cord.rank()).hasPiece()) {
                            i++;
                            continue;
                        }
                        Piece pieceOnCord = trialBoard.get(cord.file()).get(cord.rank()).getPieceOnSquare();
                        if (pieceOnCord.getType() == Type.KING && pieceOnCord.getColor() == kingColor) {
                            return true;
                        }
                        break;
                    } catch (CoordinateOutOfBoundsException e) {
                        break;
                    }
                }

                i = 0;
                while (true) {
                    try {
                        Coordinate cord = newCord.getSouth(i);
                        if (!trialBoard.get(cord.file()).get(cord.rank()).hasPiece()) {
                            i++;
                            continue;
                        }
                        Piece pieceOnCord = trialBoard.get(cord.file()).get(cord.rank()).getPieceOnSquare();
                        if (pieceOnCord.getType() == Type.KING && pieceOnCord.getColor() == kingColor) {
                            return true;
                        }
                        break;
                    } catch (CoordinateOutOfBoundsException e) {
                        break;
                    }
                }

                i = 0;
                while (true) {
                    try {
                        Coordinate cord = newCord.getEast(i);
                        if (!trialBoard.get(cord.file()).get(cord.rank()).hasPiece()) {
                            i++;
                            continue;
                        }
                        Piece pieceOnCord = trialBoard.get(cord.file()).get(cord.rank()).getPieceOnSquare();
                        if (pieceOnCord.getType() == Type.KING && pieceOnCord.getColor() == kingColor) {
                            return true;
                        }
                        break;
                    } catch (CoordinateOutOfBoundsException e) {
                        break;
                    }
                }

                i = 0;
                while (true) {
                    try {
                        Coordinate cord = newCord.getWest(i);
                        if (!trialBoard.get(cord.file()).get(cord.rank()).hasPiece()) {
                            i++;
                            continue;
                        }
                        Piece pieceOnCord = trialBoard.get(cord.file()).get(cord.rank()).getPieceOnSquare();
                        if (pieceOnCord.getType() == Type.KING && pieceOnCord.getColor() == kingColor) {
                            return true;
                        }
                        break;
                    } catch (CoordinateOutOfBoundsException e) {
                        break;
                    }
                }
            }
            case KNIGHT -> {
                Coordinate NW, NE, WN, WS, EN, ES, SW, SE;
                try {
                    NW = newCord.getNorth(2).getWest(1);
                } catch (CoordinateOutOfBoundsException e) {
                    NW = null;
                }

                try {
                    NE = newCord.getNorth(2).getEast(1);
                } catch (CoordinateOutOfBoundsException e) {
                    NE = null;
                }


            }
            case BISHOP -> {}
            case QUEEN -> {}
            default -> {}
        }

        return false;
    }

    private boolean mate() {
        if (!isCheck) {
            return false;
        }
        HashMap<Character, HashMap<Integer, Square>> trial = Board.getTrialBoard();
        trial.get(oldCord.file()).get(oldCord.rank()).clear();
        trial.get(newCord.file()).get(newCord.rank()).setPieceOnSquare(piece);

        ArrayList<Move> legalMoves;
        if (piece.getColor() == WHITE) {
            legalMoves = BlackMove.getLegalMoves(trial);
        } else {
            legalMoves = WhiteMove.getLegalMoves(trial);
        }

        return legalMoves.size() == 0;

    }

    public boolean isCheck() {
        return isCheck;
    }

    public boolean isMate() {
        return isMate;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder(Character.toString(newCord.file()) + newCord.rank());

        if (isTaking) {
            string.insert(0,"x");
            if (piece.getType() == Type.PAWN) {
                string.insert(0, oldCord.file());
            }

        }

        if (isMate) {
            string.append("#");
        } else if (isCheck) {
            string.append("+");
        }

        switch (piece.getType()) {
            case ROOK -> string.insert(0,"R");
            case KNIGHT -> string.insert(0,"N");
            case BISHOP -> string.insert(0,"B");
            case QUEEN -> string.insert(0,"Q");
            case KING -> string.insert(0,"K");
        }
        return string.toString();
    }
}
