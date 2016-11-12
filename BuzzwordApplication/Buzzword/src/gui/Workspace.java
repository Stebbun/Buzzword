package gui;

import apptemplate.AppTemplate;
import components.AppWorkspaceComponent;
import controller.BuzzwordController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import propertymanager.PropertyManager;
import ui.AppGUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Buzzword.BuzzwordProperties.*;

/**
 * @author Steven Li
 */
public class Workspace extends AppWorkspaceComponent {

    AppTemplate app; // the actual application
    AppGUI gui; // the GUI inside which the application sits

    Label headingLabel;   // workspace (GUI) heading label

    HBox baseHBox; //left is menuBox, right is rest of the gui
    VBox rightVBox; // headerpane, categoryPane, currentPane
    Pane currentPane; // homeScreenPane/levelSelectPane/playPane, hudPane


    Button createProfileButton;
    Button loginButton;
    VBox menuBox;
    Pane homeScreenPane;
    GridPane letterNodeContainer;
    GridPane levelSelectPane;
    HBox headerPane;
    HBox fillerPane;
    HBox categoryPane;
    Label exitLabel;
    HBox levelLabelPane;
    Label levelLabel;
    VBox gameplayVBox;
    HBox outerGameHBox;
    HBox bottomHBox;

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
    ArrayList<List<StackPane>> levelButtons;
    ArrayList<List<StackPane>> letterNodes;
    Button profileButton;

    public Workspace(AppTemplate initApp) throws IOException {
        app = initApp;
        gui = app.getGUI();
        layoutGUI();     // initialize all the workspace (GUI) components including the containers and their layout
        setupHandlers(); // ... and set up event handling
        activateWorkspace(gui.getAppPane());
    }

    private void layoutGUI(){
        PropertyManager propertyManager = PropertyManager.getManager();

        createProfileButton = new Button(propertyManager.getPropertyValue(CREATE_PROFILE_BUTTON));
        loginButton = new Button(propertyManager.getPropertyValue(LOGIN_BUTTON));
        menuBox = new VBox();
        menuBox.getChildren().addAll(createProfileButton, loginButton);
        menuBox.setMinWidth(300);
        menuBox.setSpacing(50);
        menuBox.setAlignment(Pos.BASELINE_RIGHT);
        menuBox.setPadding(new Insets(150, 0, 0, 0));

        headingLabel = new Label(propertyManager.getPropertyValue(WORKSPACE_HEADING_LABEL));
        exitLabel = new Label(propertyManager.getPropertyValue(EXIT_BUTTON));

        HBox blankLeftBox = new HBox();
        blankLeftBox.setMaxWidth(450);
        HBox blankRightBox = new HBox();
        headerPane = new HBox();
        headerPane.getChildren().addAll(blankLeftBox, headingLabel, blankRightBox, exitLabel);
        HBox.setHgrow(blankLeftBox, Priority.ALWAYS);
        HBox.setHgrow(blankRightBox, Priority.ALWAYS);

        fillerPane = new HBox(); //empty
        fillerPane.setMinHeight(50);

        categoryPane = new HBox(); //empty
        categoryPane.setMinHeight(50);

        buildHomeGrid();


        homeScreenPane = letterNodeContainer;
        homeScreenPane.setMinHeight(600);
        homeScreenPane.setMinWidth(600);
        homeScreenPane.getStyleClass().add("temp");

        hudPane = new VBox();
        hudPane.setMinWidth(200);

        currentPane = new StackPane();
        currentPane.getChildren().add(homeScreenPane);


        levelLabelPane = new HBox();
        levelLabelPane.setMinHeight(100);

        gameplayVBox = new VBox();
        gameplayVBox.getChildren().addAll(currentPane, levelLabelPane);


        VBox blankLeftBox2 = new VBox();
        blankLeftBox2.setMinWidth(250);
        outerGameHBox = new HBox();
        outerGameHBox.getChildren().addAll(blankLeftBox2, gameplayVBox, hudPane);

        bottomHBox = new HBox();
        bottomHBox.setMinHeight(100);

        rightVBox = new VBox();
        rightVBox.getChildren().addAll(headerPane, fillerPane, categoryPane, outerGameHBox, bottomHBox);


        baseHBox = new HBox();
        baseHBox.getChildren().addAll(menuBox, rightVBox);
        HBox.setHgrow(rightVBox, Priority.ALWAYS);


        workspace = new StackPane();//bottom layer is application, top layer is login
        workspace.getChildren().add(baseHBox);
    }

    private void buildHomeGrid(){

        letterNodes = new ArrayList<List<StackPane>>();
        for(int i = 0; i < 4; i++) {
            ArrayList<StackPane> stackArray = new ArrayList<StackPane>();
            for(int j = 0; j < 4; j++){
                StackPane letterNode = new StackPane();

                Circle circle = new Circle(50);
                circle.getStyleClass().add("circle");

                Label letterLabel = new Label("A");
                letterLabel.getStyleClass().add("letter-label");

                letterNode.getChildren().addAll(circle, letterLabel);
                stackArray.add(letterNode);
            }
            letterNodes.add(stackArray);
        }

        letterNodeContainer = new GridPane();

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++)
                letterNodeContainer.add(letterNodes.get(i).get(j), j, i);
        }
        letterNodeContainer.setHgap(40);
        letterNodeContainer.setVgap(40);
        letterNodeContainer.setPadding(new Insets(40, 40, 40, 40));
    }

    private void setupHandlers(){
        BuzzwordController controller = new BuzzwordController(app);

    }

    public void reinitialize(){

    }

    @Override
    public void initStyle() {
        PropertyManager propertyManager = PropertyManager.getManager();

        rightVBox.getStyleClass().add("right-gray");

        headingLabel.getStyleClass().add("heading-label");

        exitLabel.getStyleClass().add("exit-label");

        menuBox.getStyleClass().add("menu-box");
    }

    @Override
    public void reloadWorkspace() {

    }
}
