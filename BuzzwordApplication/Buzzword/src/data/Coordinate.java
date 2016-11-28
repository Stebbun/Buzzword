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

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
}
