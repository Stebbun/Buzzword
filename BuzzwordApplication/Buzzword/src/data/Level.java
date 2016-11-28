package data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Stebbun on 11/6/2016.
 */
public class Level {
    boolean completed;
    //level will not have a letter grid. It will be randomly generated based on the words set
    ArrayList<String> words;
    int targetScore;

    public Level(){

    }

    public Level(boolean completed, ArrayList<String> words, int targetScore) {
        this.completed = completed;
        this.words = new ArrayList<>();
        for(int i = 0; i < words.size(); i++)
            this.words.add(words.get(i));
        this.targetScore = targetScore;
    }

    public boolean isCompleted() {

        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public int getTargetScore() {
        return targetScore;
    }

    public void setTargetScore(int targetScore) {
        this.targetScore = targetScore;
    }
}
