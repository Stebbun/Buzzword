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

import static settings.AppPropertyType.NEW_ERROR_MESSAGE;
import static settings.AppPropertyType.NEW_ERROR_TITLE;

/**
 * Created by Stebbun on 11/6/2016.
 */
public class BuzzwordController implements FileController{
    private AppTemplate appTemplate;
    private GameData gameData;

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
                    throw new BuzzwordException("Username and password cannot be empty");
                gameData.setProfile(new Profile(username, password));
                File base = new File("");
                String fullPath = base.getAbsolutePath() + "/Buzzword/saved/" + username + ".json";
                save(Paths.get(fullPath));

                workspace.setState(GameState.HOME_SCREEN_LOGGED);
                workspace.reinitialize();
            } catch (BuzzwordException e) {
                messageDialog.show(propertyManager.getPropertyValue(NEW_ERROR_TITLE), propertyManager.getPropertyValue(NEW_ERROR_MESSAGE));
            } catch(Exception e){

            }



        }
    }

    public void handleLoginAttempt(){

    }

    private void save(Path target) throws IOException {
        appTemplate.getFileComponent().saveData(gameData, target.toAbsolutePath());
    }

    private void load(Path target) throws IOException {
        appTemplate.getFileComponent().loadData(gameData, target.toAbsolutePath());
    }

}
