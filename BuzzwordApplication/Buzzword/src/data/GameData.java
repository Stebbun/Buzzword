package data;

import apptemplate.AppTemplate;
import components.AppDataComponent;

/**
 * Created by Stebbun on 11/6/2016.
 */
public class GameData implements AppDataComponent {
    private Profile profile;
    public  AppTemplate    appTemplate;

    public GameData(AppTemplate appTemplate) {
        this.appTemplate = appTemplate;
    }

    @Override
    public void reset() {

    }
}
