package Coordinate;

public class CoordinateOutOfBoundsException extends Exception {
    public ExceptionType type;

    public CoordinateOutOfBoundsException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

