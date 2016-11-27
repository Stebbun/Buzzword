package data;

import java.util.ArrayList;

/**
 * Created by Stebbun on 11/6/2016.
 */
public class GameMode {
    String category;
    ArrayList<Level> levels;
    int maxCompletedLevel;

    public GameMode(){

    }

    public GameMode(String category, ArrayList<Level> levels, int maxCompletedLevel) {
        this.category = category;
        this.levels = levels;
    }

    public String getCategory() {

        return category;
    }

    public int getMaxCompletedLevel() {
        return maxCompletedLevel;
    }

    public void setMaxCompletedLevel(int maxCompletedLevel) {
        this.maxCompletedLevel = maxCompletedLevel;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<Level> getLevels() {
        return levels;
    }

    public void setLevels(ArrayList<Level> levels) {
        this.levels = levels;
    }
}
