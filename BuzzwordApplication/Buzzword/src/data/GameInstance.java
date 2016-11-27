package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Stebbun on 11/6/2016.
 */
public class GameInstance {
    private GameMode gameModeSelected;
    private Level levelSelected;
    private ArrayList<ArrayList<Character>> letterGrid;
    private int currentTimer;
    private Set<String> guaranteedWords;
    private Set<String> validWords;
    private Set<String> wordsGuessed;
    private int targetScore;
    private int currentScore;
    private String currentGuess;

    public GameInstance(GameMode gameModeSelected, Level levelSelected) {
        this.gameModeSelected = gameModeSelected;
        this.levelSelected = levelSelected;

        generateLetterGrid();
        currentTimer = 60;
        generateGuaranteedWords();
        generateValidWords();
        wordsGuessed = new HashSet<>();
        generateTargetScore();
        currentScore = 0;
        currentGuess = "";
    }

    private void generateGuaranteedWords() {
        guaranteedWords = new HashSet<>();
        guaranteedWords = levelSelected.getWords();
    }

    private void generateTargetScore() {
        targetScore = levelSelected.getTargetScore();
    }

    private void generateValidWords() {
        validWords = new HashSet<>();
        try {
            InputStream is = Profile.class.getResourceAsStream("/initialdata/valid-animals.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String words = br.readLine();
            String[] wordArray = words.split(" ");
            for(int i = 0; i < wordArray.length; i++)
                validWords.add(wordArray[i]);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void generateLetterGrid() {

    }

    public GameMode getGameModeSelected() {
        return gameModeSelected;
    }

    public void setGameModeSelected(GameMode gameModeSelected) {
        this.gameModeSelected = gameModeSelected;
    }

    public Level getLevelSelected() {
        return levelSelected;
    }

    public void setLevelSelected(Level levelSelected) {
        this.levelSelected = levelSelected;
    }

    public ArrayList<ArrayList<Character>> getLetterGrid() {
        return letterGrid;
    }

    public void setLetterGrid(ArrayList<ArrayList<Character>> letterGrid) {
        this.letterGrid = letterGrid;
    }

    public int getCurrentTimer() {
        return currentTimer;
    }

    public void setCurrentTimer(int currentTimer) {
        this.currentTimer = currentTimer;
    }

    public Set<String> getValidWords() {
        return validWords;
    }

    public void setValidWords(Set<String> validWords) {
        this.validWords = validWords;
    }

    public Set<String> getWordsGuessed() {
        return wordsGuessed;
    }

    public void setWordsGuessed(Set<String> wordsGuessed) {
        this.wordsGuessed = wordsGuessed;
    }

    public int getTargetScore() {
        return targetScore;
    }

    public void setTargetScore(int targetScore) {
        this.targetScore = targetScore;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public String getCurrentGuess() {
        return currentGuess;
    }

    public void setCurrentGuess(String currentGuess) {
        this.currentGuess = currentGuess;
    }
}
