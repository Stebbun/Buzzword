package controller;

import apptemplate.AppTemplate;
import data.GameData;
import data.Profile;
import gui.GameState;
import gui.Workspace;
import propertymanager.PropertyManager;
import ui.AppMessageDialogSingleton;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static settings.AppPropertyType.*;

/**
 * Created by Stebbun on 11/6/2016.
 */
public class BuzzwordController implements FileController{
    private AppTemplate appTemplate;
    public static GameData gameData;

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

    public void handleLevelSelection(){
        
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
