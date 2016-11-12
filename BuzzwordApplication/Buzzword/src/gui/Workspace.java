package gui;

import apptemplate.AppTemplate;
import components.AppWorkspaceComponent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import ui.AppGUI;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Steven Li
 */
public class Workspace extends AppWorkspaceComponent {

    AppTemplate app; // the actual application
    AppGUI gui; // the GUI inside which the application sits

    Label headingLabel;   // workspace (GUI) heading label

    HBox baseHBox; //left is menuBox, right is rest of the gui
    VBox rightVBox; // headerpane, categoryPane, currentPane
    HBox currentPane; // homeScreenPane/levelSelectPane/playPane, hudPane


    Button createProfileButton;
    Button loginButton;
    VBox menuBox;
    Pane homeScreenPane;
    GridPane levelSelectPane;
    HBox headerPane;
    HBox categoryPane;
    Label exitLabel;

    Label categoryLabel;
    HBox timePane;
    Label remainingTimeLabel;
    Label currentTimeLabel;
    StackPane playPane;
    BorderPane appPane;
    VBox hudPane; //
    Label currentGuessLabel;
    VBox scorePane;
    HBox totalScoreBox;
    Label totalLabel;
    Label totalScoreLabel;
    VBox wordPointVBox;
    HBox wordPointLine;
    Label targetPointLabel;
    Label targetLabel;
    Button replayButton;
    ArrayList<ArrayList<StackPane>> levelButtons;
    ArrayList<ArrayList<StackPane>> letterNodes;
    Button profileButton;

    public Workspace(AppTemplate initApp) throws IOException {
        app = initApp;
        gui = app.getGUI();
        layoutGUI();     // initialize all the workspace (GUI) components including the containers and their layout
        setupHandlers(); // ... and set up event handling
    }

    private void layoutGUI(){

        createProfileButton = new Button("Create New Profile");
        loginButton = new Button("Login");
        menuBox = new VBox();
        menuBox.getChildren().addAll(createProfileButton, loginButton);


        rightVBox = new VBox();
        rightVBox.getChildren().addAll(headerPane, categoryPane, currentPane);

        baseHBox = new HBox();
        baseHBox.getChildren().addAll(menuBox, rightVBox);


        workspace = new StackPane();//bottom layer is application, top layer is login
        workspace.getChildren().add(baseHBox);
    }

    private void setupHandlers(){

    }

    public void reinitialize(){

    }

    @Override
    public void initStyle() {

    }

    @Override
    public void reloadWorkspace() {

    }
}
