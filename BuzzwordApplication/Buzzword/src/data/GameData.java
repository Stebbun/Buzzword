package data;

import apptemplate.AppTemplate;
import components.AppDataComponent;

/**
 * Created by Stebbun on 11/6/2016.
 */
public class GameData implements AppDataComponent {
    private Profile profile;
    private AppTemplate appTemplate;

    public GameData(){

    }

    public GameData(AppTemplate appTemplate){
        this.appTemplate = appTemplate;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public void reset() {

    }

    public AppTemplate getAppTemplate() {
        return appTemplate;
    }

    public void setAppTemplate(AppTemplate appTemplate) {
        this.appTemplate = appTemplate;
    }
}
