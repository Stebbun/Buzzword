package controller;

import apptemplate.AppTemplate;
import data.GameData;
import data.Profile;
import gui.GameState;
import gui.Workspace;

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
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
        if(workspace.getState() == GameState.HOME_SCREEN_LOG_PROMPT){
            workspace.setState(GameState.HOME_SCREEN_LOGGED);
            workspace.reinitialize();
            gameData = new GameData(appTemplate);
            String username = workspace.getLoginField().getText();
            String password = workspace.getPasswordField().getText();
            workspace.getLoginField().clear();
            workspace.getPasswordField().clear();
            gameData.setProfile(new Profile(username, password));
        }
    }

    public void handleLoginAttempt(){

    }

}
