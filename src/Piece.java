
public class Piece {
    String sides;

    public Piece(String sides) {
        this.sides = sides;
    }

    public char getSide(int side) {
        return sides.charAt(side);
    }

    public char getOppositeSide(int side) {
        return sides.charAt((side + 2) % 4);
    }

    public boolean isStraight(int side) {
        return (sides.charAt(side) == 'R');
    }

    public boolean matches(String template) {
        return sides.matches(template);
    }

    @Override
    public String toString() {
        return sides;
    }
}