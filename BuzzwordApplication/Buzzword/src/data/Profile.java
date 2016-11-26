package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Stebbun on 11/6/2016.
 */
public class Profile {
    private String username;
    private String password;
    private ArrayList<GameMode> gameModes;

    public Profile(){

    }

    public Profile(String username, String password, ArrayList<GameMode> gameModes){
        generateInitData();
    }

    private void generateInitData() {
        //Generate Animals game mode
        ArrayList<Level> animalLevels = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(Profile.class.getResource("/initialData/animals.txt").getFile()));
            String words = br.readLine();
            String[] wordArray = words.split(" ");
            Set<String> wordSet = new HashSet<>();
            int k = 0;
            int targetScore = 0;

            for(int i = 0; i < 8; i++){
                targetScore = wordArray[k].length() * 10;
                for(int j = 0; j < 3; j++) {
                    wordSet.add(wordArray[k]);
                    k++;
                }


                Level level = new Level(false, wordSet, targetScore);
            }

            GameMode animals = new GameMode("Animals", animalLevels, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<GameMode> getGameModes() {
        return gameModes;
    }

    public void setGameModes(ArrayList<GameMode> gameModes) {
        this.gameModes = gameModes;
    }
}
