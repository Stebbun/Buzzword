package data;

import controller.BuzzwordController;

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
    private boolean[][] flaggedGrid;
    private boolean[][] adjacencyGrid;
    private int currentTimer;
    private ArrayList<String> guaranteedWords;
    private Set<String> validWords;
    private Set<String> wordsGuessed;
    private int targetScore;
    private int currentScore;
    private String currentGuess;
    private boolean isPaused;
    private boolean firstTimeTyped;

    public GameInstance(GameMode gameModeSelected, Level levelSelected) {
        this.gameModeSelected = gameModeSelected;
        this.levelSelected = levelSelected;
        flaggedGrid = new boolean[4][4];
        generateTargetScore();
        generateGuaranteedWords();
        initializeLetterGrid();
        generateLetterGrid();
        currentTimer = 60;
        generateValidWords();
        wordsGuessed = new HashSet<>();
        currentScore = 0;
        currentGuess = "";
        isPaused = false;
        firstTimeTyped = true;
    }

    private void initializeLetterGrid(){
        letterGrid = new ArrayList<ArrayList<Character>>();
        for(int i = 0; i < 4; i++){
            letterGrid.add(new ArrayList<>());
        }
        for(int j = 0; j < 4; j++){
            //char c = (char)('A' + ((int)(Math.random()* 26)));
            letterGrid.get(j).add(new Character((char)('A' + ((int)(Math.random()* 26)))));
            letterGrid.get(j).add(new Character((char)('A' + ((int)(Math.random()* 26)))));
            letterGrid.get(j).add(new Character((char)('A' + ((int)(Math.random()* 26)))));
            letterGrid.get(j).add(new Character((char)('A' + ((int)(Math.random()* 26)))));
        }
    }

    private void generateGuaranteedWords() {
        guaranteedWords = new ArrayList<>();
        guaranteedWords = levelSelected.getWords();
    }

    private void generateTargetScore() {
        targetScore = levelSelected.getTargetScore();
    }

    private void generateValidWords() {
        validWords = new HashSet<>();
        if(gameModeSelected.getCategory().equals("Animals")) {
            try {
                InputStream is = Profile.class.getResourceAsStream("/initialdata/valid-animals.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String words = br.readLine();
                String[] wordArray = words.split(" ");
                for (int i = 0; i < wordArray.length; i++)
                    validWords.add(wordArray[i].toUpperCase());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(gameModeSelected.getCategory().equals("Fruits and Veggies")){
            try {
                InputStream is = Profile.class.getResourceAsStream("/initialdata/valid-fruitsveggies.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String words = br.readLine();
                String[] wordArray = words.split(" ");
                for (int i = 0; i < wordArray.length; i++)
                    validWords.add(wordArray[i]);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(gameModeSelected.getCategory().equals("Birds")){
            try {
                InputStream is = Profile.class.getResourceAsStream("/initialdata/valid-birds.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String words = br.readLine();
                String[] wordArray = words.split(" ");
                for (int i = 0; i < wordArray.length; i++)
                    validWords.add(wordArray[i]);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void generateLetterGrid() {
        SquareCoordinateGenerator scg = new SquareCoordinateGenerator(4);
        ArrayList<Coordinate> coordList;
        coordList = scg.getCoordinateList();

        for(int i = 0; i < guaranteedWords.size(); i++) {

            int randomIndex = (int) (Math.random() * coordList.size());
            while(coordList.get(randomIndex).isFlagged()){
                randomIndex = (int) (Math.random() * coordList.size());
            }
            Coordinate firstCoord = coordList.get(randomIndex);
            coordList.set(randomIndex, firstCoord.setFlagged(true));
            Coordinate currentCoord = firstCoord;

            letterGrid.get(firstCoord.getX()).set(firstCoord.getY(), new Character(Character.toUpperCase(guaranteedWords.get(i).charAt(0))));
            for(int j = 1; j < guaranteedWords.get(i).length(); j++){
                ArrayList<Coordinate> validAdjacencyCoord = new ArrayList<>();
                //add valid coordinates to this list and pick a random one
                /*if x and y are between 0-3 inclusive
                if x and y are within 1 value of the cursor
                if flagged = false
                then add it to validAdjacencyCoord*/
                //go through entire coordList
                for(int k = 0; k < coordList.size(); k++){
                    if(Math.abs((currentCoord.getX() - coordList.get(k).getX())) <= 1)
                        if(Math.abs((currentCoord.getY() - coordList.get(k).getY())) <= 1)
                            if(!coordList.get(k).isFlagged())
                                if(!((currentCoord.getX() == coordList.get(k).getX())
                                        && (currentCoord.getY() == coordList.get(k).getY())))
                                        validAdjacencyCoord.add(coordList.get(k));

                }
                //pick a random one from valid adjacency coord
                if(validAdjacencyCoord.size() != 0) {
                    int randomAdjIndex = (int) (Math.random() * validAdjacencyCoord.size());
                    Coordinate randomCoord = validAdjacencyCoord.get(randomAdjIndex);
                    for (int l = 0; l < coordList.size(); l++) {
                        if (coordList.get(l).equals(randomCoord)) {
                            randomCoord = randomCoord.setFlagged(true);
                            coordList.set(l, randomCoord);
                            currentCoord = randomCoord;
                            letterGrid.get(randomCoord.getX()).set(randomCoord.getY(),
                                    new Character(Character.toUpperCase(guaranteedWords.get(i).charAt(j))));
                        }
                    }
                }else{
                    //deduct from target score since i couldnt fit the word
                    targetScore = targetScore - (guaranteedWords.get(i).length() * 10);
                }
            }
        }
    }

    public void decrementTimer(){
        currentTimer--;
    }

    public void addValidWord(String word){
        wordsGuessed.add(word);
    }

    public void incrementCurrentScore(int points){
        currentScore += points;
    }

    public void appendLetter(Character c){
        currentGuess += c;
    }

    public void clearGuess(){
        currentGuess = "";
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

    public ArrayList<String> getGuaranteedWords() {
        return guaranteedWords;
    }

    public void setGuaranteedWords(ArrayList<String> guaranteedWords) {
        this.guaranteedWords = guaranteedWords;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
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

    public boolean[][] getFlaggedGrid() {
        return flaggedGrid;
    }

    public void setFlaggedGrid(boolean[][] flaggedGrid) {
        this.flaggedGrid = flaggedGrid;
    }

    public void setFlagCell(int position){
        int i = position / 4;
        int j = position % 4;
        flaggedGrid[i][j] = true;
    }

    public boolean getFlagCell(int position){
        int i = position / 4;
        int j = position % 4;
        return flaggedGrid[i][j];
    }

    public void resetFlaggedGrid(){
        flaggedGrid = new boolean[4][4];
    }

    public void makeAdjacencyGrid(int pos){
        //use last flagged cell position and everything around that position will be a valid adjacency
        adjacencyGrid = new boolean[4][4];
        //pos + 1
        if(pos + 1 >= 0 && pos + 1 <= 15)
            setAdjacencyCell(pos + 1);
        //pos - 1
        if((pos - 1) >= 0 && (pos - 1) <= 15)
            setAdjacencyCell(pos - 1);
        //pos - 4
        if(pos - 4 >= 0 && pos - 4 <= 15)
            setAdjacencyCell(pos - 4);
        //pos - 3
        if(pos - 3 >= 0 && pos - 3 <= 15)
            setAdjacencyCell(pos - 3);
        //pos - 5
        if(pos - 5 >= 0 && pos - 5 <= 15)
            setAdjacencyCell(pos - 5);
        //pos + 4
        if(pos + 4 >= 0 && pos + 4 <= 15)
            setAdjacencyCell(pos + 4);
        //pos + 3
        if(pos + 3 >= 0 && pos + 3 <= 15)
            setAdjacencyCell(pos + 3);
        //pos + 5
        if(pos + 5 >= 0 && pos + 5 <= 15)
            setAdjacencyCell(pos + 5);
    }

    private void setAdjacencyCell(int pos){
        int i = pos / 4;
        int j = pos % 4;
        adjacencyGrid[i][j] = true;
    }

    public boolean getAdjacencyCell(int pos){
        int i = pos / 4;
        int j = pos % 4;
        return adjacencyGrid[i][j];
    }

    public boolean[][] getAdjacencyGrid() {
        return adjacencyGrid;
    }

    public void setAdjacencyGrid(boolean[][] adjacencyGrid) {
        this.adjacencyGrid = adjacencyGrid;
    }

    public boolean isFirstTimeTyped() {
        return firstTimeTyped;
    }

    public void setFirstTimeTyped(boolean firstTimeTyped) {
        this.firstTimeTyped = firstTimeTyped;
    }
}
