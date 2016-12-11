package data;

import java.io.*;
import java.security.MessageDigest;
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

    public Profile(String username, String password){
        this.username = username;
        generatePasswordHash(password);
        generateInitData();
    }

    private void generatePasswordHash(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(pass.getBytes());

            byte bytes[] = md.digest();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                buffer.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            password = buffer.toString();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void generateInitData() {
        //Generate Animals game mode
        try {
            gameModes = new ArrayList<GameMode>();
            ArrayList<Level> animalLevels = new ArrayList<>();
            InputStream is = Profile.class.getResourceAsStream("/initialdata/animals.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String words = br.readLine();
            String[] wordArray = words.split(" ");
            ArrayList<String> wordSet = new ArrayList<>();
            int k = 0;

            for(int i = 0; i < 8; i++){
                int targetScore = 0;
                for(int j = 0; j < 3; j++) {
                    wordSet.add(wordArray[k]);
                    if(wordArray[k].length() >= 5)
                        targetScore += wordArray[k].length() * 15;
                    else
                        targetScore += wordArray[k].length() * 10;
                    k++;
                }


                Level level = new Level(false, wordSet, targetScore);
                animalLevels.add(level);
                wordSet.clear();

            }

            GameMode animals = new GameMode("Animals", animalLevels, 0);
            gameModes.add(animals);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Generate Fruits and Veggies game mode
        try {
            ArrayList<Level> animalLevels = new ArrayList<>();
            InputStream is = Profile.class.getResourceAsStream("/initialdata/fruitsveggies.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String words = br.readLine();
            String[] wordArray = words.split(" ");
            ArrayList<String> wordSet = new ArrayList<>();
            int k = 0;

            for(int i = 0; i < 8; i++){
                int targetScore = 0;
                for(int j = 0; j < 3; j++) {
                    wordSet.add(wordArray[k]);
                    if(wordArray[k].length() >= 5)
                        targetScore += wordArray[k].length() * 15;
                    else
                        targetScore += wordArray[k].length() * 10;
                    k++;
                }


                Level level = new Level(false, wordSet, targetScore);
                animalLevels.add(level);
                wordSet.clear();

            }

            GameMode animals = new GameMode("Fruits and Vegetables", animalLevels, 0);
            gameModes.add(animals);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Generate Fruits and Veggies game mode
        try {
            ArrayList<Level> animalLevels = new ArrayList<>();
            InputStream is = Profile.class.getResourceAsStream("/initialdata/birds.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String words = br.readLine();
            String[] wordArray = words.split(" ");
            ArrayList<String> wordSet = new ArrayList<>();
            int k = 0;

            for(int i = 0; i < 8; i++){
                int targetScore = 0;
                for(int j = 0; j < 3; j++) {
                    wordSet.add(wordArray[k]);
                    if(wordArray[k].length() >= 5)
                        targetScore += wordArray[k].length() * 15;
                    else
                        targetScore += wordArray[k].length() * 10;
                    k++;
                }


                Level level = new Level(false, wordSet, targetScore);
                animalLevels.add(level);
                wordSet.clear();

            }

            GameMode animals = new GameMode("Birds", animalLevels, 0);
            gameModes.add(animals);

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
