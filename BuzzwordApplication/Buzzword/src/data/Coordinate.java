package data;

/**
 * Created by stebbun on 11/27/2016.
 */
public class Coordinate {
    int x;
    int y;
    boolean flagged;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
        this.flagged = false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public Coordinate setFlagged(boolean flagged) {
        this.flagged = flagged;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        return flagged == that.flagged;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + (flagged ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
