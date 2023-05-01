import java.awt.*;

public class Puzzle {
    Piece[][] board;
    int width;
    int height;
    Point nextPos;

    public Puzzle(Dimension d) {
        width = d.width;
        height = d.height;
        nextPos = new Point(-1,0);

        board = new Piece[width][height];
    }

    public Puzzle(Puzzle puzzle) {
        width = puzzle.width;
        height = puzzle.height;
        board = new Piece[width][height];

        // deep copy
        this.nextPos = new Point(puzzle.nextPos);

        for (int i = 0; i < width; i++) {
            System.arraycopy(puzzle.board[i], 0, board[i], 0, height);
        }
    }

    public void setPiece(Piece piece, Point pos) {
        board[pos.x][pos.y] = piece;
    }

    public Piece getPiece(Point pos) {
        return board[pos.x][pos.y];
    }

    // returns whether a given point is within the bounds of the puzzle
    public Boolean withinBounds(Point pos) {
        return !((pos.x < 0 || pos.y < 0) || (pos.x >= width || pos.y >= height));
    }

    public String buildRegexTemplate(Point pos) {
        StringBuilder builder = new StringBuilder();

        for (int side = 0; side < 4; side++) {
            Point adj = new Point(pos);

            // traverse to point adjacent to current side
            switch (side) {
                case 0 -> adj.y--;
                case 1 -> adj.x++;
                case 2 -> adj.y++;
                case 3 -> adj.x--;
            }

            if (withinBounds(adj)) {
                Piece adjPiece = getPiece(adj);

                if (adjPiece == null) {
                    // no adjacent piece, side can be either in our out
                    builder.append("[UI]");
                } else {
                    // adjacent piece, side can be opposite of adjacent pieces facing side
                    builder.append(adjPiece.getOppositeSide(side) == 'U' ? "I" : 'U');
                }
            } else {
                // only straight sides can be adjacent to bounds
                builder.append("R");
            }
        }

        return builder.toString();
    }

    // gets next empty pos
    public Point getNextPos() {
        nextPos.x++;

        if (nextPos.x == width) {
            nextPos.x = 0;
            nextPos.y++;
        }

        return nextPos;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int y = 0; y < board[0].length; y++) {
            for (int x = 0; x < board.length; x++) {
                Piece p = getPiece(new Point(x, y));

                if (p == null) {
                    builder.append("NULL ");
                } else {
                    builder.append(p);
                    builder.append(" ");
                }
            }

            builder.append("\n");
        }

        return builder.toString();
    }
}

