package controller;

import apptemplate.AppTemplate;
import data.GameData;
import data.GameInstance;
import data.GameMode;
import data.Profile;
import gui.GameState;
import gui.Workspace;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import propertymanager.PropertyManager;
import ui.AppMessageDialogSingleton;
import ui.YesNoCancelDialogSingleton;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
    private boolean temp;

    public BuzzwordController(AppTemplate appTemplate) {
        this.appTemplate = appTemplate;
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
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
                        ArrayList<List<StackPane>> letterNodes = workspace.getLetterNodes();
                        for(int i = 0; i < letterNodes.size(); i++){
                            for(int j = 0; j < letterNodes.get(i).size(); j++){
                                StackPane letterNode = letterNodes.get(i).get(j);

                                letterNode.setOnDragDetected(e ->{
                                    letterNode.startFullDrag();
                                    System.out.println("a");
                                    temp = false;   //drag is detected when selecting the first letter; disable mouse drag entered
                                    GridPane gridPaneTemp = (GridPane) letterNode.getParent();
                                    for(int k = 0; k < gridPaneTemp.getChildren().size(); k++)
                                        if(gridPaneTemp.getChildren().get(k).equals(letterNode))
                                            flaggedIndex = k;
                                    gameInstance.setFlagCell(flaggedIndex);
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
                                    if(temp && !gameInstance.getFlagCell(flaggedIndex)) {
                                        System.out.println("b");
                                        temp = false;
                                        Label labelTemp = (Label) letterNode.getChildren().get(1);
                                        Character c = new Character(labelTemp.getText().charAt(0));
                                        gameInstance.appendLetter(c);
                                        updateCurrentGuessGUI();
                                    }
                                    gameInstance.setFlagCell(flaggedIndex);
                                });

                                letterNode.setOnMouseDragReleased(e ->{
                                    System.out.println("check if guess is valid or not, then do stuff");
                                    gameInstance.resetFlaggedGrid();
                                    gameInstance.clearGuess();
                                    updateCurrentGuessGUI();
                                });

                                letterNode.setOnMouseDragExited(e ->{
                                    temp = true;    //mouse has left the node,
                                });
                            }
                        }
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
