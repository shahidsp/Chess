package Turns;

import Board.*;
import Coordinate.*;
import Pieces.Color;
import Pieces.Piece;
import Pieces.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static Pieces.Color.BLACK;
import static Pieces.Color.WHITE;

public class Move {

    private final boolean isCheck, isMate, isTaking;

    private final Piece piece;
    private final Coordinate newCord, oldCord;
    private TrialBoard trialBoard;
    Color pieceColor;
    Color kingColor;
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
    //  Reimplement check() from king's perspective.
    //  Much easier method you fucking moron

    private boolean check() {
        pieceColor = piece.getColor();
        switch (pieceColor) {
            case WHITE -> kingColor = Color.BLACK;
            case BLACK -> kingColor = WHITE;
            default -> kingColor = Color.PIECE;
        }

        Piece king = Board.getInstance().getKing(kingColor);
        trialBoard = new TrialBoard();
        trialBoard.getBoard().get(oldCord.file()).get(oldCord.rank()).clear();
        trialBoard.getBoard().get(newCord.file()).get(newCord.rank()).setPieceOnSquare(piece);
        switch (piece.getType()) {
            case PAWN -> {
                try {
                    switch (pieceColor) {
                        case WHITE -> {
                            Piece NW = trialBoard.getBoard().get(newCord.getNorthWest(1).file()).get(newCord.getNorthWest(1).rank()).getPieceOnSquare();
                            Piece NE = trialBoard.getBoard().get(newCord.getNorthEast(1).file()).get(newCord.getEast(1).rank()).getPieceOnSquare();
                            if (NW == null && NE == null) {
                                return false;
                            }
                            if ((NW != null && NW.getType() == Type.KING && NW.getColor() == BLACK) || (NE != null && NE.getType() == Type.KING && NE.getColor() == BLACK)) {
                                return true;
                            }
                        }
                        case BLACK -> {
                            Piece SW = trialBoard.getBoard().get(newCord.getSouthWest(1).file()).get(newCord.getSouthWest(1).rank()).getPieceOnSquare();
                            Piece SE = trialBoard.getBoard().get(newCord.getSouthEast(1).file()).get(newCord.getSouthEast(1).rank()).getPieceOnSquare();

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
                return kingOnVerticals();
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

                try {
                    WN = newCord.getNorth(1).getWest(2);
                } catch (CoordinateOutOfBoundsException e) {
                    WN = null;
                }

                try {
                    WS = newCord.getSouth(1).getWest(2);
                } catch (CoordinateOutOfBoundsException e) {
                    WS = null;
                }

                try {
                    EN = newCord.getNorth(1).getEast(2);
                } catch (CoordinateOutOfBoundsException e) {
                    EN = null;
                }

                try {
                    ES = newCord.getSouth(1).getEast(2);
                } catch (CoordinateOutOfBoundsException e) {
                    ES = null;
                }

                try {
                    SW = newCord.getSouth(2).getWest(1);
                } catch (CoordinateOutOfBoundsException e) {
                    SW = null;
                }

                try {
                    SE = newCord.getNorth(2).getEast(1);
                } catch (CoordinateOutOfBoundsException e) {
                    SE = null;
                }

                ArrayList<Coordinate> attackedPositions = new ArrayList<Coordinate>();
                attackedPositions.add(NE);
                attackedPositions.add(NW);
                attackedPositions.add(EN);
                attackedPositions.add(ES);
                attackedPositions.add(SE);
                attackedPositions.add(SW);
                attackedPositions.add(WS);
                attackedPositions.add(WN);

                attackedPositions.removeIf(Objects::isNull);

                for (Coordinate coordinate : attackedPositions) {
                    if (coordinate == king.getPosition()) {
                        return true;
                    }
                }
                return false;
            }
            case BISHOP -> {
                return kingOnDiagonals();
            }
            case QUEEN -> {
                return kingOnDiagonals() || kingOnVerticals();
            }
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

        return legalMoves.isEmpty();

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

    private boolean kingOnVerticals() {
        int i = 1;
        try {
            while (true) {
                if (hasKing(newCord.getNorth(i))) {
                    return true;
                }
                i++;
            }
        } catch (CoordinateOutOfBoundsException ignored) {}

        i = 1;
        try {
            while (true) {
                if (hasKing(newCord.getSouth(i))) {
                    return true;
                }
                i++;
            }
        } catch (CoordinateOutOfBoundsException ignored) {}

        i = 1;
        try {
            while (true) {
                if (hasKing(newCord.getEast(i))) {
                    return true;
                }
                i++;
            }
        } catch (CoordinateOutOfBoundsException ignored) {}

        i = 1;
        try {
            while (true) {
                if (hasKing(newCord.getWest(i))) {
                    return true;
                }
                i++;
            }
        } catch (CoordinateOutOfBoundsException ignored) {}

        return false;
    }

    private boolean kingOnDiagonals() {
        int i = 1;
        try {
            while (true) {
                if (hasKing(newCord.getNorthEast(i))) {
                    return true;
                }
                i++;
            }
        } catch (CoordinateOutOfBoundsException ignored) {}

        i = 1;
        try {
            while (true) {
                if (hasKing(newCord.getSouthEast(i))) {
                    return true;
                }
                i++;
            }
        } catch (CoordinateOutOfBoundsException ignored) {}

        i = 1;
        try {
            while (true) {
                if (hasKing(newCord.getNorthWest(i))) {
                    return true;
                }
                i++;
            }
        } catch (CoordinateOutOfBoundsException ignored) {}

        i = 1;
        try {
            while (true) {
                if (hasKing(newCord.getSouthWest(i))) {
                    return true;
                }
                i++;
            }
        } catch (CoordinateOutOfBoundsException ignored) {}

        return false;
    }

    private boolean hasKing(Coordinate coordinate) {
        if (!trialBoard.getSquare(coordinate).hasPiece()) {
            return false;
        }

        Piece pieceOnSquare = trialBoard.getPieceOnSquare(coordinate);
        return pieceOnSquare.getType() == Type.KING && pieceOnSquare.getColor() == kingColor;

    }
}
