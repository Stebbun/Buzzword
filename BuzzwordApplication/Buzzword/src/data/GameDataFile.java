package data;

import components.AppDataComponent;
import components.AppFileComponent;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Stebbun on 11/6/2016.
 */
public class GameDataFile implements AppFileComponent {
    @Override
    public void saveData(AppDataComponent data, Path to) {
        GameData gameData = (GameData) data;
    }

    @Override
    public void loadData(AppDataComponent data, Path from) throws IOException {

    }

    /** This method will be used if we need to export data into other formats. */
    @Override
    public void exportData(AppDataComponent data, Path filePath) throws IOException { }
}
