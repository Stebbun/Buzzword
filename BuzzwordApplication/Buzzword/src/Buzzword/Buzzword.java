package Buzzword;

import apptemplate.AppTemplate;
import components.AppComponentsBuilder;
import components.AppDataComponent;
import components.AppFileComponent;
import components.AppWorkspaceComponent;
import data.GameData;
import data.GameDataFile;
import gui.Workspace;

public class Buzzword extends AppTemplate {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public AppComponentsBuilder makeAppBuilderHook() {
        return new AppComponentsBuilder() {
            @Override
            public AppDataComponent buildDataComponent() throws Exception {
                return new GameData(Buzzword.this);
            }

            @Override
            public AppFileComponent buildFileComponent() throws Exception {
                return new GameDataFile();
            }

            @Override
            public AppWorkspaceComponent buildWorkspaceComponent() throws Exception {
                return new Workspace(Buzzword.this);
            }
        };
    }
}
