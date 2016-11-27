package data;

import com.fasterxml.jackson.databind.ObjectMapper;
import components.AppDataComponent;
import components.AppFileComponent;
import controller.BuzzwordController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Stebbun on 11/6/2016.
 */
public class GameDataFile implements AppFileComponent {
    @Override
    public void saveData(AppDataComponent data, Path to) {
        GameData gameData = (GameData) data;

        gameData.setAppTemplate(null);
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(to.toString()), gameData);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void loadData(AppDataComponent data, Path from) throws IOException {
        GameData gameData = (GameData) data;

        ObjectMapper mapper = new ObjectMapper();
        gameData = mapper.readValue(new File(from.toString()), GameData.class);

        //check if valid later

        BuzzwordController.setGameData(gameData);
    }

    /** This method will be used if we need to export data into other formats. */
    @Override
    public void exportData(AppDataComponent data, Path filePath) throws IOException { }
}
