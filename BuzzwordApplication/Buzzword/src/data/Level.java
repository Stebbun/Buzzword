package data;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Stebbun on 11/6/2016.
 */
public class Level {
    boolean completed;
    //level will not have a letter grid. It will be randomly generated based on the words set
    Set<String> words;
    int targetScore;

    public Level(boolean completed, Set<String> words, int targetScore) {
        this.completed = completed;
        this.words = words;
        this.targetScore = targetScore;
    }

    public boolean isCompleted() {

        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Set<String> getWords() {
        return words;
    }

    public void setWords(Set<String> words) {
        this.words = words;
    }

    public int getTargetScore() {
        return targetScore;
    }

    public void setTargetScore(int targetScore) {
        this.targetScore = targetScore;
    }
}
