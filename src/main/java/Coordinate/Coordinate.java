package Coordinate;

public record Coordinate(char file, int rank) {


    public Coordinate getNorth(int numPositions) throws CoordinateOutOfBoundsException {
        int newRank = rank() + numPositions;
        if (newRank > 8 || newRank < 1) {
            throw new CoordinateOutOfBoundsException("Rank out of Bounds", ExceptionType.RANK);
        } else {
            return new Coordinate(file(), newRank);
        }
    }

    public Coordinate getSouth(int numPositions) throws CoordinateOutOfBoundsException {
        int newRank = rank() - numPositions;
        if (newRank > 8 || newRank < 1) {
            throw new CoordinateOutOfBoundsException("Rank out of Bounds", ExceptionType.RANK);
        } else {
            return new Coordinate(file(), newRank);
        }
    }

    public Coordinate getEast(int numPositions) throws CoordinateOutOfBoundsException {
        char newFile = (char) (file() + numPositions);
        if (newFile > 'h' || newFile < 'a') {
            throw new CoordinateOutOfBoundsException("File out of Bounds", ExceptionType.FILE);
        } else {
            return new Coordinate(newFile, rank());
        }
    }

    public Coordinate getWest(int numPositions) throws CoordinateOutOfBoundsException {
        char newFile = (char) (file() - numPositions);
        if (newFile > 'h' || newFile < 'a') {
            throw new CoordinateOutOfBoundsException("File out of Bounds", ExceptionType.FILE);
        } else {
            return new Coordinate(newFile, rank());
        }
    }

    public Coordinate getNorthEast(int numPositions) throws CoordinateOutOfBoundsException {
        char newFile = (char) (file() + numPositions);
        int newRank = rank() + numPositions;
        checkCoordinateOutOfBounds(newFile, newRank);
        return new Coordinate(newFile, newRank);
    }

    public Coordinate getSouthEast(int numPositions) throws CoordinateOutOfBoundsException {
        char newFile = (char) (file() + numPositions);
        int newRank = rank() - numPositions;
        checkCoordinateOutOfBounds(newFile, newRank);
        return new Coordinate(newFile, newRank);
    }

    public Coordinate getNorthWest(int numPositions) throws CoordinateOutOfBoundsException {
        char newFile = (char) (file() - numPositions);
        int newRank = rank() + numPositions;
        checkCoordinateOutOfBounds(newFile, newRank);
        return new Coordinate(newFile, newRank);
    }

    public Coordinate getSouthWest(int numPositions) throws CoordinateOutOfBoundsException {
        char newFile = (char) (file() - numPositions);
        int newRank = rank() - numPositions;
        checkCoordinateOutOfBounds(newFile, newRank);
        return new Coordinate(newFile, newRank);
    }

    private void checkCoordinateOutOfBounds(char newFile, int newRank) throws CoordinateOutOfBoundsException {
        boolean fileOOB = newFile > 'h' || newFile < 'a';
        boolean rankOOB = newRank > 8 || newFile < 1;

        if (fileOOB && rankOOB) {
            throw new CoordinateOutOfBoundsException("Rank and File out of Bounds", ExceptionType.BOTH);
        }
        if (fileOOB) {
            throw new CoordinateOutOfBoundsException("File out of Bounds", ExceptionType.FILE);
        }
        if (rankOOB) {
            throw new CoordinateOutOfBoundsException("Rank out of Bounds", ExceptionType.RANK);
        }
    }


    @Override
    public String toString() {
        return Character.toString(file) + rank;
    }


    public boolean equals(Coordinate coordinate) {
        boolean sameFile = this.file == coordinate.file;
        boolean sameRank = this.rank == coordinate.rank;

        return sameFile && sameRank;
    }

    /**
     * Used to determine the difference in file and rank between two coordinates
     * @param anotherCoordinate The Coordinate the caller will be compared to
     * @return An int[] where int[0] is the difference in file and int[1] is the difference in rank
     */
    public int[] minus(Coordinate anotherCoordinate) {
        return new int[]{this.file - anotherCoordinate.file, this.rank - anotherCoordinate.rank};
    }

    /**
     * Used to compare two Coordinates and determine which is larger. A larger file is the file with the later letter in
     * the alphabet ('b' is greater that 'a') and a larger rank is the rank with a larger number.
     * @param anotherCoordinate The Coordinate the caller is compared to
     * @return An int[] where int[0] is the comparison of file and int[1] is the comparison of rank. If the caller was
     * larger, the result will be 1. If anotherCoordinate is larger, the result is -1. If both are equal, the result
     * is 0.
     */
    public int[] bigger(Coordinate anotherCoordinate) {
        int[] result = new int[]{0,0};

        if (this.file > anotherCoordinate.file) {
            result[0] = 1;
        } else if (this.file < anotherCoordinate.file) {
            result[0] = -1;
        }

        if (this.rank > anotherCoordinate.rank) {
            result[1] = 1;
        } else if (this.rank < anotherCoordinate.rank) {
            result[1] = -1;
        }

        return result;
    }
}
