import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;

public class Main {
    // attempt at solving without placing pieces, works for given puzzles but probably not for all
    static int solveWithoutPlacing(List<Piece> pieces) {
        if (!isSolvable(pieces)) return 0;

        // count frequencies of all pieces
        Hashtable<String, Integer> hashtable = new Hashtable<>();

        for (Piece piece: pieces) {
            String key = new String(piece.sides);

            if (hashtable.containsKey(key)) {
                hashtable.put(key, hashtable.get(key) + 1);
            } else {
                hashtable.put(key, 1);
            }
        }

        int solutions = 1;

        // each set of identical pieces increases number of solutions by a factor equal to the number of permutations
        for (int val: hashtable.values()) {
            solutions *= factorial(val);
        }

        return solutions;
    }

    static int factorial(int n) {
        int factorial = 1;

        for (int i = 1; i <= n; i++) {
            factorial *= i;
        }

        return factorial;
    }

    // entry point for placing solution
    static int solveByPlacing(List<Piece> pieces) {
        if (!isSolvable(pieces)) return 0;

        return solveByPlacing(new Puzzle(getDimensions(pieces)), pieces, 0);
    }

    // recursively generates all solutions
    static int solveByPlacing(Puzzle puzzle, List<Piece> pieces, int solutions) {
        if (pieces.size() > 0) {
            Point pos = puzzle.getNextPos();

            String template = puzzle.buildRegexTemplate(pos);

            List<Piece> matches = pieces.stream().filter(piece -> piece.matches(template)).toList();

            for (Piece match: matches) {
                List<Piece> newPieces = pieces.stream().filter(p -> !p.equals(match)).toList();
                Puzzle newPuzzle = new Puzzle(puzzle);

                newPuzzle.setPiece(match, pos);
                solutions = solveByPlacing(newPuzzle, newPieces, solutions);
            }

            return solutions;
        } else {
            return solutions + 1;
        }
    }

    // performs cursory solvability check
    static boolean isSolvable(List<Piece> pieces) {
        int[] ins = new int[4];
        int[] outs = new int[4];
        int[] edges = new int[4];

        for (Piece piece: pieces) {
            for (int side = 0; side < 4; side++) {
                switch (piece.getSide(side)) {
                    case 'R' -> edges[side]++;
                    case 'I' -> ins[side]++;
                    case 'U' -> outs[side]++;
                }
            }
        }

        // puzzle is only solvable if matching amount of in and out pieces that face each other exist
        for (int side = 0; side < 4; side++) {
            if (ins[side] != outs[(side + 2) % 4]) {
                return false;
            }
        }

        // number of edges also has to match
        for (int side = 0; side < 2; side++) {
            if (edges[side] != edges[side+2]) {
                return false;
            }
        }

        return true;
    }

    // uses the number of edge pieces to calculate puzzle dimensions
    static Dimension getDimensions(List<Piece> pieces) {
        int[] dimensions = new int[2];

        for (Piece piece: pieces) {
            for (int side = 0; side < 2; side++) {
                if (piece.isStraight(side)) {
                    dimensions[side]++;
                }
            }
        }

        return new Dimension(dimensions[0], dimensions[1]);
    }

    // reads and validates input file
    static List<Piece> readInput(String path) {
        try {
            List<String> fileContent = Files.readAllLines(Paths.get(path));
            List<Piece> pieces = new ArrayList<>();

            for (String s: fileContent.subList(1, fileContent.size())) {
                if (s.length() != 4) {
                    throw new Exception("Invalid input file.");
                }

                pieces.add(new Piece(s));
            }

            if (Integer.parseInt(fileContent.get(0)) != pieces.size()) {
                throw new Exception("Invalid input file.");
            }

            return pieces;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }

    static void printOutput(int nofSolutions) {
        if (nofSolutions > 0) {
            System.out.printf("Pusslet går att lägga. Det finns %d lösningar\n", nofSolutions);
        } else {
            System.out.println("Pusslet går inte att lägga");
        }
    }

    public static void main(String[] args) {
        List<Piece> pieces = readInput(args[0]);

        System.out.println("Placeringslösning:");
        printOutput(solveByPlacing(pieces));

        System.out.println("Icke-placeringslösning:");
        printOutput(solveWithoutPlacing(pieces));
    }


}