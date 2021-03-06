package controller;

import apptemplate.AppTemplate;
import data.*;
import gui.GameState;
import gui.Workspace;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import propertymanager.PropertyManager;
import ui.AppMessageDialogSingleton;
import ui.YesNoCancelDialogSingleton;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static Buzzword.BuzzwordProperties.GAME_LOSE_MESSAGE;
import static Buzzword.BuzzwordProperties.GAME_RESULT_TITLE;
import static Buzzword.BuzzwordProperties.GAME_WIN_MESSAGE;
import static settings.AppPropertyType.*;

/**
 * Created by Stebbun on 11/6/2016.
 */
public class BuzzwordController implements FileController{
    private AppTemplate appTemplate;
    private int selectedModeIndex;
    private GameInstance gameInstance;
    public static GameData gameData;
    private int selectedIindex;
    private int selectedJindex;
    private int flaggedIndex = 0;
    private boolean won = false;
    private boolean temp;
    private int lastPos;
    private HashMap<String, Integer> lineMap;

    public BuzzwordController(AppTemplate appTemplate) {
        this.appTemplate = appTemplate;
        generateLineMap();
    }

    private void generateLineMap() {
        lineMap = new HashMap<>();
        int pos1 = 0;
        int pos2 = 1;
        int pos3 = 0;

        //horizontal
        for(int i = 0; i < 4; i++, pos1++, pos2++){
            for(int j = 0; j < 3; j++, pos1++, pos2++, pos3++){
                Coordinate pair = new Coordinate(pos1, pos2);
                lineMap.put(pair.toString(), pos3);
            }
        }
        pos1 = 0;
        pos2 = 4;
        pos3 = 12;

        //vertical
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++, pos1++, pos2++, pos3++){
                Coordinate pair = new Coordinate(pos1, pos2);
                lineMap.put(pair.toString(), pos3);
            }
        }

        //diagonal manually
        //top left to bottom right
        //least diagonal
        Coordinate pair = new Coordinate(8, 13);
        lineMap.put(pair.toString(), 24);

        //lesser diagonal
        pair = new Coordinate(4, 9);
        lineMap.put(pair.toString(), 25);
        pair = new Coordinate(9, 14);
        lineMap.put(pair.toString(), 26);

        //main diagonal
        pair = new Coordinate(0, 5);
        lineMap.put(pair.toString(), 27);
        pair = new Coordinate(5, 10);
        lineMap.put(pair.toString(), 28);
        pair = new Coordinate(10, 15);
        lineMap.put(pair.toString(), 29);

        //upper diagonal
        pair = new Coordinate(1, 6);
        lineMap.put(pair.toString(), 30);
        pair = new Coordinate(6, 11);
        lineMap.put(pair.toString(), 31);

        //uppest diagonal
        pair = new Coordinate(2, 7);
        lineMap.put(pair.toString(), 32);

        //bottom left to top right
        //uppest diagonal
        pair = new Coordinate(4, 1);
        lineMap.put(pair.toString(), 33);

        //upper diagonal
        pair = new Coordinate(8, 5);
        lineMap.put(pair.toString(), 34);
        pair = new Coordinate(5, 2);
        lineMap.put(pair.toString(), 35);

        //main diagonal
        pair = new Coordinate(12, 9);
        lineMap.put(pair.toString(), 36);
        pair = new Coordinate(9, 6);
        lineMap.put(pair.toString(), 37);
        pair = new Coordinate(6, 3);
        lineMap.put(pair.toString(), 38);

        //lower diagonal
        pair = new Coordinate(13, 10);
        lineMap.put(pair.toString(), 39);
        pair = new Coordinate(10, 7);
        lineMap.put(pair.toString(), 40);

        //lowest diagonal
        pair = new Coordinate(14, 11);
        lineMap.put(pair.toString(), 41);

    }

    private void ensureActivatedWorkspace() {

    }

    public void createProfilePrompt(){
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
        if(workspace.getState() == GameState.HOME_SCREEN){
            workspace.setState(GameState.HOME_SCREEN_LOG_PROMPT);
            workspace.reinitialize();
            workspace.setLastButtonClicked(workspace.getCreateProfileButton());
        }
    }

    public void handleLoginPrompt(){
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
        if(workspace.getState() == GameState.HOME_SCREEN){
            workspace.setState(GameState.HOME_SCREEN_LOG_PROMPT);
            workspace.reinitialize();
            workspace.setLastButtonClicked(workspace.getLoginButton());
        }
    }

    public void createProfile(){
        PropertyManager propertyManager = PropertyManager.getManager();
        AppMessageDialogSingleton messageDialog = AppMessageDialogSingleton.getSingleton();
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
        if(workspace.getState() == GameState.HOME_SCREEN_LOG_PROMPT){

            gameData = new GameData(appTemplate);
            String username = workspace.getLoginField().getText();
            String password = workspace.getPasswordField().getText();
            workspace.getLoginField().clear();
            workspace.getPasswordField().clear();

            try {
                if(username.isEmpty() || password.isEmpty())
                    throw new EmptyFieldException("Username and password cannot be empty");
                gameData.setProfile(new Profile(username, password));
                File base = new File("");
                String fullPath = base.getAbsolutePath() + "/Buzzword/saved/" + username + ".json";
                save(Paths.get(fullPath));

                workspace.setState(GameState.HOME_SCREEN_LOGGED);
                workspace.reinitialize();

                workspace.getProfileBoxData().add(username);
                workspace.getProfileBoxData().add("Log out");
                workspace.getProfileBox().setItems(workspace.getProfileBoxData());
                workspace.getProfileBox().setValue(username);

                for(int i = 0; i < gameData.getProfile().getGameModes().size(); i++)
                    workspace.getGameModesBoxData().add(gameData.getProfile().getGameModes().get(i).getCategory());
                workspace.getGameModesBox().setItems(workspace.getGameModesBoxData());
            } catch (EmptyFieldException e) {
                messageDialog.show(propertyManager.getPropertyValue(NEW_ERROR_TITLE),
                        propertyManager.getPropertyValue(EMPTY_LOGIN_ERROR));
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void handleLoginAttempt(){
        PropertyManager propertyManager = PropertyManager.getManager();
        AppMessageDialogSingleton messageDialog = AppMessageDialogSingleton.getSingleton();
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();

        if(workspace.getState() == GameState.HOME_SCREEN_LOG_PROMPT){
            String username = workspace.getLoginField().getText();
            String password = workspace.getPasswordField().getText();

            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(password.getBytes());

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

            File base = new File("");
            String fullPath = base.getAbsolutePath() + "/Buzzword/saved/" + username + ".json";
            try {
                load(Paths.get(fullPath));

                if(!gameData.getProfile().getPassword().equals(password))
                    throw new InvalidLoginException("");

                workspace.setState(GameState.HOME_SCREEN_LOGGED);
                workspace.reinitialize();

                workspace.getProfileBoxData().add(username);
                workspace.getProfileBoxData().add("Log out");
                workspace.getProfileBox().setItems(workspace.getProfileBoxData());
                workspace.getProfileBox().setValue(username);

                for(int i = 0; i < gameData.getProfile().getGameModes().size(); i++)
                    workspace.getGameModesBoxData().add(gameData.getProfile().getGameModes().get(i).getCategory());
                workspace.getGameModesBox().setItems(workspace.getGameModesBoxData());
                workspace.getGameModesBox().setValue(workspace.getGameModesBoxData().get(0));
            }
            catch(InvalidLoginException e){
                messageDialog.show(propertyManager.getPropertyValue(LOGIN_ERROR_TITLE),
                        propertyManager.getPropertyValue(LOGIN_ERROR));
            }
            catch(Exception e){
                //if data is corrupt
                e.printStackTrace();
            }

        }
    }

    public void exitLoginPrompt(){
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
        if(workspace.getState() == GameState.HOME_SCREEN_LOG_PROMPT) {
            workspace.getLoginField().clear();
            workspace.getPasswordField().clear();

            workspace.setState(GameState.HOME_SCREEN);
            workspace.reinitialize();
        }
    }

    public void handleLogOut(){
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
        workspace.setState(GameState.HOME_SCREEN);
        workspace.reinitialize();
        workspace.getProfileBoxData().clear();
        workspace.getGameModesBoxData().clear();
    }

    public void handleStartButton(){
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
        workspace.setState(GameState.LEVEL_SELECTION);
        workspace.reinitialize();

        selectedModeIndex = 0;
        for(int i = 0; i < gameData.getProfile().getGameModes().size(); i++)
            if(gameData.getProfile().getGameModes().get(i).getCategory().equals(workspace.getGameModesBox().getValue()))
                selectedModeIndex = i;

        for(int i = 0; i < 2; i++)
            for(int j = 0; j < 4; j++) {
                if(gameData.getProfile().getGameModes().get(selectedModeIndex).getMaxCompletedLevel() >= (i*4 + j)) {
                    workspace.getLevelNodes().get(i).get(j).getChildren().get(0).getStyleClass().add("circle-enabled");
                    workspace.getLevelNodes().get(i).get(j).getChildren().get(1).getStyleClass().add("letter-label-enabled");
                }
            }

        for(int i = 0; i < 2; i++)
            for(int j = 0; j < 4; j++) {
                selectedIindex = i;
                selectedJindex = j;
                workspace.getLevelNodes().get(i).get(j).setOnMouseClicked(e ->{
                    StackPane sp = (StackPane)e.getSource();
                    Label lab = (Label) sp.getChildren().get(1);

                    this.handleLevelSelection(Integer.parseInt(lab.getText()));
                });
            }
        workspace.getHomeButton().setOnAction(e ->{
            workspace.setState(GameState.HOME_SCREEN_LOGGED);
            workspace.buildHomeGrid();
            workspace.reinitialize();
        });
    }

    public void handleLevelSelection(int levelIndex){
        levelIndex = levelIndex - 1;
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
        GameMode selectedMode = gameData.getProfile().getGameModes().get(selectedModeIndex);

        //levelIndex 0-7
        if(selectedMode.getMaxCompletedLevel() >= levelIndex){
            gameInstance = new GameInstance(selectedMode, selectedMode.getLevels().get(levelIndex));
            workspace.setState(GameState.GAMEPLAY_SCREEN);
            workspace.reinitialize();

            //update lettergrid
            gameplayLetterGrid();

            //display targetscore
            workspace.getTargetBox().getChildren().set(1, new Label(Integer.toString(gameInstance.getTargetScore())));
            workspace.getTargetBox().getChildren().get(1).getStyleClass().add("target");

            //handle replay button
            workspace.getReplayLabel().setOnMouseClicked(e ->{
                this.handlePause();
            });

            workspace.getLevelLabel().setText("Level " + Integer.toString(levelIndex + 1));

            play();
        }
    }

    public void handlePause() {
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
        if(gameInstance.isPaused() == false) {
            workspace.getCurrentPane().getChildren().add(workspace.getPausePane());
            gameInstance.setPaused(true);
        }
        else{
            workspace.getCurrentPane().getChildren().remove(workspace.getPausePane());
            gameInstance.setPaused(false);
        }
    }

    public void handleExitFromGame(){
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
        PropertyManager propertyManager = PropertyManager.getManager();
        YesNoCancelDialogSingleton yesNoDialog = YesNoCancelDialogSingleton.getSingleton();
        yesNoDialog.show(propertyManager.getPropertyValue(SURE_CLOSE_TITLE),
                propertyManager.getPropertyValue(SURE_CLOSE_MESSAGE));

        if(yesNoDialog.getSelection().equals(YesNoCancelDialogSingleton.YES))
            System.exit(0);
    }

    public void handleExitGeneral(){
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
        System.exit(0);
    }

    private void gameplayLetterGrid() {
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++){
                ((Label)workspace.getLetterNodes().get(i).get(j).getChildren().get(1))
                        .setText(Character.toString(gameInstance.getLetterGrid().get(i).get(j)));
            }
        }
    }

    public void play(){
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
        workspace.getCurrentTimeLabel().setText(Integer.toString(gameInstance.getCurrentTimer()));

        Timeline tl = new Timeline();
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.getKeyFrames().add(new KeyFrame(Duration.seconds(1), (EventHandler) e ->{
            if(!gameInstance.isPaused()) {
                gameInstance.decrementTimer();
                int currentTimer = gameInstance.getCurrentTimer();
                workspace.getCurrentTimeLabel().setText(Integer.toString(currentTimer));
                if (currentTimer <= 0) {
                    tl.stop();
                }
            }
            else{
                //do nothing
            }
        }));
        tl.playFromStart();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(gameInstance.getCurrentTimer() <= 0){
                            won = false;
                            stop();
                        }

                        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
                        ArrayList<List<StackPane>> letterNodes = workspace.getLetterNodes();
                        for(int i = 0; i < letterNodes.size(); i++){
                            for(int j = 0; j < letterNodes.get(i).size(); j++){
                                StackPane letterNode = letterNodes.get(i).get(j);

                                letterNode.setOnDragDetected(e ->{
                                    letterNode.startFullDrag();
                                    temp = false;   //drag is detected when selecting the first letter; disable mouse drag entered
                                    GridPane gridPaneTemp = (GridPane) letterNode.getParent();
                                    for(int k = 0; k < gridPaneTemp.getChildren().size(); k++)
                                        if(gridPaneTemp.getChildren().get(k).equals(letterNode))
                                            flaggedIndex = k;
                                    letterNode.getChildren().get(0).getStyleClass().clear();
                                    letterNode.getChildren().get(1).getStyleClass().clear();
                                    letterNode.getChildren().get(0).getStyleClass().add("node-selected");
                                    letterNode.getChildren().get(1).getStyleClass().add("node-selected-text");
                                    lastPos = flaggedIndex;
                                    gameInstance.setFlagCell(flaggedIndex);
                                    gameInstance.makeAdjacencyGrid(flaggedIndex);
                                    Label labelTemp = (Label) letterNode.getChildren().get(1);
                                    Character c = new Character(labelTemp.getText().charAt(0));
                                    gameInstance.appendLetter(c);
                                    updateCurrentGuessGUI();
                                });

                                letterNode.setOnMouseDragEntered(e ->{
                                    GridPane gridPaneTemp = (GridPane) letterNode.getParent();
                                    for(int k = 0; k < gridPaneTemp.getChildren().size(); k++)
                                        if(gridPaneTemp.getChildren().get(k).equals(letterNode))
                                            flaggedIndex = k;
                                    if(temp && !gameInstance.getFlagCell(flaggedIndex) && gameInstance.getAdjacencyCell(flaggedIndex)) {
                                        letterNode.getChildren().get(0).getStyleClass().clear();
                                        letterNode.getChildren().get(1).getStyleClass().clear();
                                        letterNode.getChildren().get(0).getStyleClass().add("node-selected");
                                        letterNode.getChildren().get(1).getStyleClass().add("node-selected-text");
                                        temp = false;
                                        Label labelTemp = (Label) letterNode.getChildren().get(1);
                                        Character c = new Character(labelTemp.getText().charAt(0));
                                        gameInstance.appendLetter(c);
                                        updateCurrentGuessGUI();
                                        gameInstance.makeAdjacencyGrid(flaggedIndex);
                                        gameInstance.setFlagCell(flaggedIndex);
                                        //enable a line based on mapping
                                        boolean switchIt = false;
                                        try {
                                            Coordinate coord = new Coordinate(lastPos, flaggedIndex);
                                            Integer value = lineMap.get(coord.toString());
                                            workspace.getLinePane().getChildren().get(value).getStyleClass().add("line-selected");
                                            lastPos = flaggedIndex;
                                        }
                                        catch(NullPointerException ex){
                                            Coordinate coord = new Coordinate(flaggedIndex, lastPos);
                                            Integer value = lineMap.get(coord.toString());
                                            workspace.getLinePane().getChildren().get(value).getStyleClass().add("line-selected");
                                            lastPos = flaggedIndex;
                                        }
                                    }
                                });

                                letterNode.setOnMouseDragReleased(e ->{
                                    if(gameInstance.getValidWords().contains(gameInstance.getCurrentGuess())
                                            && !gameInstance.getWordsGuessed().contains(gameInstance.getCurrentGuess())){
                                        gameInstance.addValidWord(gameInstance.getCurrentGuess());
                                        int row = gameInstance.getWordsGuessed().size() - 1;
                                        int points = 0;
                                        if(gameInstance.getCurrentGuess().length() >= 5)
                                            points = gameInstance.getCurrentGuess().length() * 15;
                                        else
                                            points = gameInstance.getCurrentGuess().length() * 10;
                                        gameInstance.incrementCurrentScore(points);
                                        workspace.getWordGrid().add(new Label(gameInstance.getCurrentGuess()), 0, row);
                                        workspace.getWordGrid().add(new Label(Integer.toString(points)), 1, row);
                                        for(int k = 0; k < workspace.getWordGrid().getChildren().size(); k++){
                                            workspace.getWordGrid().getChildren().get(k).getStyleClass().add("words");
                                        }
                                        workspace.getTotalScoreBox().getChildren().remove(1);
                                        workspace.getTotalScoreBox().add(new Label(Integer.toString(gameInstance.getCurrentScore())), 1, 0);
                                        workspace.getTotalScoreBox().getChildren().get(1).getStyleClass().add("total");
                                    }
                                    for(int x = 0; x < workspace.getLetterNodes().size(); x++){
                                        for(int y = 0; y < workspace.getLetterNodes().get(x).size(); y++){
                                            workspace.getLetterNodes().get(x).get(y).getChildren().get(0).getStyleClass().clear();
                                            workspace.getLetterNodes().get(x).get(y).getChildren().get(0).getStyleClass().add("circle");
                                            workspace.getLetterNodes().get(x).get(y).getChildren().get(1).getStyleClass().clear();
                                            workspace.getLetterNodes().get(x).get(y).getChildren().get(1).getStyleClass().add("letter-label");
                                        }
                                    }
                                    Pane linePane = workspace.getLinePane();
                                    for(int p = 0; p < linePane.getChildren().size(); p++) {
                                        Line line = (Line) linePane.getChildren().get(p);
                                        line.getStyleClass().clear();
                                        line.getStyleClass().add("line");
                                    }
                                    gameInstance.resetFlaggedGrid();
                                    gameInstance.clearGuess();
                                    updateCurrentGuessGUI();
                                    if(gameInstance.getCurrentScore() >= gameInstance.getTargetScore()){
                                        won = true;
                                        stop();
                                    }
                                });

                                letterNode.setOnMouseDragExited(e ->{
                                    temp = true;    //mouse has left the node,
                                });
                            }
                        }

                        //typing portion
                        appTemplate.getGUI().getPrimaryScene().setOnKeyTyped((KeyEvent event) -> {
                            if(event.getCode().equals(KeyCode.ENTER) || event.getCharacter().equals("\r")){
                                //if enter
                                if(gameInstance.getValidWords().contains(gameInstance.getCurrentGuess())
                                        && !gameInstance.getWordsGuessed().contains(gameInstance.getCurrentGuess())){
                                    gameInstance.addValidWord(gameInstance.getCurrentGuess());
                                    int row = gameInstance.getWordsGuessed().size() - 1;
                                    int points = 0;
                                    if(gameInstance.getCurrentGuess().length() >= 5)
                                        points = gameInstance.getCurrentGuess().length() * 15;
                                    else
                                        points = gameInstance.getCurrentGuess().length() * 10;
                                    gameInstance.incrementCurrentScore(points);
                                    workspace.getWordGrid().add(new Label(gameInstance.getCurrentGuess()), 0, row);
                                    workspace.getWordGrid().add(new Label(Integer.toString(points)), 1, row);
                                    for(int k = 0; k < workspace.getWordGrid().getChildren().size(); k++){
                                        workspace.getWordGrid().getChildren().get(k).getStyleClass().add("words");
                                    }
                                    workspace.getTotalScoreBox().getChildren().remove(1);
                                    workspace.getTotalScoreBox().add(new Label(Integer.toString(gameInstance.getCurrentScore())), 1, 0);
                                    workspace.getTotalScoreBox().getChildren().get(1).getStyleClass().add("total");
                                }
                                Pane linePane = workspace.getLinePane();
                                for(int p = 0; p < linePane.getChildren().size(); p++) {
                                    Line line = (Line) linePane.getChildren().get(p);
                                    line.getStyleClass().clear();
                                    line.getStyleClass().add("line");
                                }
                                for (int i = 0; i < gameInstance.getLetterGrid().size(); i++) {
                                    for (int j = 0; j < gameInstance.getLetterGrid().get(i).size(); j++) {
                                        workspace.getLetterNodes().get(i).get(j).getChildren().get(0).getStyleClass().clear();
                                        workspace.getLetterNodes().get(i).get(j).getChildren().get(1).getStyleClass().clear();
                                        workspace.getLetterNodes().get(i).get(j).getChildren().get(0).getStyleClass()
                                                .add("circle");
                                        workspace.getLetterNodes().get(i).get(j).getChildren().get(1).getStyleClass()
                                                .add("letter-label");
                                    }
                                }
                                gameInstance.setFirstTimeTyped(true);
                                gameInstance.resetFlaggedGrid();
                                gameInstance.clearGuess();
                                updateCurrentGuessGUI();
                                if(gameInstance.getCurrentScore() >= gameInstance.getTargetScore()){
                                    won = true;
                                    stop();
                                }
                            }
                            else {
                                Character letter = event.getCharacter().toUpperCase().charAt(0);
                                boolean isAddedThisLoop = false;

                                for (int i = 0; i < gameInstance.getLetterGrid().size(); i++) {
                                    for (int j = 0; j < gameInstance.getLetterGrid().get(i).size(); j++) {
                                        if (letter.equals(gameInstance.getLetterGrid().get(i).get(j))) {
                                            if (gameInstance.isFirstTimeTyped()) {
                                                if(!isAddedThisLoop) {
                                                    gameInstance.appendLetter(letter);
                                                    isAddedThisLoop = true;
                                                }
                                                workspace.getLetterNodes().get(i).get(j).getChildren().get(0).getStyleClass().clear();
                                                workspace.getLetterNodes().get(i).get(j).getChildren().get(1).getStyleClass().clear();
                                                workspace.getLetterNodes().get(i).get(j).getChildren().get(0).getStyleClass()
                                                        .add("node-selected");
                                                workspace.getLetterNodes().get(i).get(j).getChildren().get(1).getStyleClass()
                                                        .add("node-selected-text");
                                                gameInstance.setFirstTimeTyped(false);
                                                gameInstance.setFlagCell(i*4 + j);
                                                lastPos = i*4 + j;
                                                gameInstance.makeAdjacencyGrid(i*4 + j);
                                                updateCurrentGuessGUI();

                                            }
                                            else if(gameInstance.getAdjacencyCell(i*4 + j)){
                                                if(!isAddedThisLoop) {
                                                    gameInstance.appendLetter(letter);
                                                    isAddedThisLoop = true;
                                                }
                                                try {
                                                    Coordinate coord = new Coordinate(lastPos, i*4 + j);
                                                    Integer value = lineMap.get(coord.toString());
                                                    workspace.getLinePane().getChildren().get(value).getStyleClass().add("line-selected");
                                                    lastPos = i*4 + j;
                                                }
                                                catch(NullPointerException ex){
                                                    Coordinate coord = new Coordinate(i*4 + j, lastPos);
                                                    Integer value = lineMap.get(coord.toString());
                                                    workspace.getLinePane().getChildren().get(value).getStyleClass().add("line-selected");
                                                    lastPos = i*4 + j;
                                                }
                                                workspace.getLetterNodes().get(i).get(j).getChildren().get(0).getStyleClass().clear();
                                                workspace.getLetterNodes().get(i).get(j).getChildren().get(1).getStyleClass().clear();
                                                workspace.getLetterNodes().get(i).get(j).getChildren().get(0).getStyleClass()
                                                        .add("node-selected");
                                                workspace.getLetterNodes().get(i).get(j).getChildren().get(1).getStyleClass()
                                                        .add("node-selected-text");
                                                gameInstance.setFirstTimeTyped(false);
                                                gameInstance.setFlagCell(i*4 + j);
                                                gameInstance.makeAdjacencyGrid(i*4 + j);
                                                updateCurrentGuessGUI();
                                            }
                                        }
                                    }
                                }

                            }
                        });
                    }
                });
            }

            @Override
            public void stop() {
                super.stop();
                end();
            }
        };
        timer.start();
    }

    private void end() {
        PropertyManager propertyManager = PropertyManager.getManager();
        AppMessageDialogSingleton messageDialog = AppMessageDialogSingleton.getSingleton();
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
        workspace.setState(GameState.HOME_SCREEN_LOGGED);
        workspace.buildHomeGrid();
        workspace.reinitialize();

        if(won) {
            //update gameData
            try {
                messageDialog.show(propertyManager.getPropertyValue(GAME_RESULT_TITLE),
                        propertyManager.getPropertyValue(GAME_WIN_MESSAGE));
            }
            catch(Exception e){

            }

            for(int i = 0; i < gameData.getProfile().getGameModes().size(); i++){
                if(gameData.getProfile().getGameModes().get(i).equals(gameInstance.getGameModeSelected())){
                    for(int j = 0; j < gameData.getProfile().getGameModes().get(i).getLevels().size(); j++)
                        if(gameData.getProfile().getGameModes().get(i).getLevels().get(j).equals(gameInstance.getLevelSelected())){
                            //j is the level that was completed
                            int levelUnlocked = j + 1;
                            gameData.getProfile().getGameModes().get(i).setMaxCompletedLevel(levelUnlocked);
                            File base = new File("");
                            String username = gameData.getProfile().getUsername();
                            String fullPath = base.getAbsolutePath() + "/Buzzword/saved/" + username + ".json";
                            try {
                                save(Paths.get(fullPath));
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                }
            }
        }
        else{
            String words = " The words were:\n";
            for(int i = 0; i < gameInstance.getGuaranteedWords().size(); i++)
                words += gameInstance.getGuaranteedWords().get(i) + "\n";
            try {
                messageDialog.show(propertyManager.getPropertyValue(GAME_RESULT_TITLE),
                        propertyManager.getPropertyValue(GAME_LOSE_MESSAGE) + words);
            }
            catch(Exception e){

            }
        }
    }

    private void updateCurrentGuessGUI(){
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
        workspace.getCurrentGuessLabel().setText(gameInstance.getCurrentGuess());
    }

    private void save(Path target) throws IOException {
        appTemplate.getFileComponent().saveData(gameData, target.toAbsolutePath());
    }

    private void load(Path target) throws IOException {
        appTemplate.getFileComponent().loadData(gameData, target.toAbsolutePath());
    }

    public GameData getGameData() {
        return gameData;
    }

    public static void setGameData(GameData gamedata) {
        gameData = gamedata;
    }
}
