package gui;

import apptemplate.AppTemplate;
import components.AppWorkspaceComponent;
import controller.BuzzwordController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    BuzzwordController controller;

    Label headingLabel;   // workspace (GUI) heading label

    HBox baseHBox; //left is menuBox, right is rest of the gui
    VBox rightVBox; // headerpane, categoryPane, currentPane
    Pane currentPane; // homeScreenPane/levelSelectPane/playPane, hudPane

    GameState state;


    Button createProfileButton;
    Button loginButton;
    VBox menuBox;
    Pane homeScreenPane;
    GridPane letterNodeContainer;
    GridPane levelNodeContainer;
    HBox headerPane;
    HBox fillerPane;
    HBox categoryPane;
    Label exitLabel;
    HBox levelLabelPane;
    Label levelLabel;
    VBox gameplayVBox;
    HBox outerGameHBox;
    HBox bottomHBox;

    TextField loginField;
    TextField passwordField;

    ComboBox<String> profileBox;
    ObservableList<String> profileBoxData = FXCollections.observableArrayList();
    ComboBox<String> gameModesBox;
    ObservableList<String> gameModesBoxData = FXCollections.observableArrayList();
    Button startPlayingButton;
    Button homeButton; //returns to homeLoggedScreen

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
    ArrayList<List<StackPane>> levelNodes;
    ArrayList<List<StackPane>> letterNodes;


    public Workspace(AppTemplate initApp) throws IOException {
        app = initApp;
        gui = app.getGUI();
        layoutGUI();     // initialize all the workspace (GUI) components including the containers and their layout
        //logInPrompt();

        state = GameState.HOME_SCREEN_LOGGED;
        reinitialize();

        state = GameState.LEVEL_SELECTION;
        reinitialize();


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
        rightVBox.getChildren().addAll(headerPane, categoryPane, outerGameHBox, bottomHBox);


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

                Label letterLabel = new Label();
                letterLabel.getStyleClass().add("letter-label");

                letterNode.getChildren().addAll(circle, letterLabel);
                stackArray.add(letterNode);
            }
            letterNodes.add(stackArray);
        }

        ((Label)letterNodes.get(0).get(0).getChildren().get(1)).setText("B");
        ((Label)letterNodes.get(0).get(1).getChildren().get(1)).setText("U");
        ((Label)letterNodes.get(1).get(0).getChildren().get(1)).setText("Z");
        ((Label)letterNodes.get(1).get(1).getChildren().get(1)).setText("Z");
        ((Label)letterNodes.get(2).get(2).getChildren().get(1)).setText("W");
        ((Label)letterNodes.get(2).get(3).getChildren().get(1)).setText("O");
        ((Label)letterNodes.get(3).get(2).getChildren().get(1)).setText("R");
        ((Label)letterNodes.get(3).get(3).getChildren().get(1)).setText("D");

        letterNodeContainer = new GridPane();

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++)
                letterNodeContainer.add(letterNodes.get(i).get(j), j, i);
        }
        letterNodeContainer.setHgap(40);
        letterNodeContainer.setVgap(40);
        letterNodeContainer.setPadding(new Insets(40, 40, 40, 40));

    }

    private void buildDemoLevelSelect(){

        levelNodes = new ArrayList<List<StackPane>>();
        for(int i = 0; i < 2; i++) {
            ArrayList<StackPane> stackArray = new ArrayList<StackPane>();
            for(int j = 0; j < 4; j++){
                StackPane levelNode = new StackPane();

                Circle circle = new Circle(50);
                circle.getStyleClass().add("circle");

                Label levelLabel = new Label();
                levelLabel.getStyleClass().add("letter-label");

                levelNode.getChildren().addAll(circle, levelLabel);
                stackArray.add(levelNode);
            }
            levelNodes.add(stackArray);
        }

        //CHANGE LATER IN ACCORDANCE TO GAME DATA!!! *******
        for(int i = 0; i < 2; i++)
            for(int j = 0, k = 1; j < 4; j++, k++) {
                ((Label) levelNodes.get(i).get(j).getChildren().get(1)).setText(Integer.toString(k));
                //FAKE DATA
                if(i == 0) {
                    levelNodes.get(i).get(j).getChildren().get(0).getStyleClass().add("circle-enabled");
                    levelNodes.get(i).get(j).getChildren().get(1).getStyleClass().add("letter-label-enabled");
                }
            }


        levelNodeContainer = new GridPane();

        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 4; j++)
                levelNodeContainer.add(levelNodes.get(i).get(j), j, i);
        }
        levelNodeContainer.setHgap(40);
        levelNodeContainer.setVgap(40);
        levelNodeContainer.setPadding(new Insets(40, 40, 40, 40));
    }

    private void setupHandlers(){
        controller = new BuzzwordController(app);
        loginButton.setOnAction( e ->{

        });

    }

    public void reinitialize(){

        //clear certain panes beforehand
        menuBox.getChildren().clear();
        categoryPane.getChildren().clear();
        currentPane.getChildren().clear();
        levelLabelPane.getChildren().clear();
        bottomHBox.getChildren().clear();
        hudPane.getChildren().clear();
        //DO GAMEPLAY RELATED ONES LATER!!!


        switch(state) {
            case HOME_SCREEN:
                setUpHomeScreen();
                break;
            case HOME_SCREEN_LOG_PROMPT:
                logInPrompt();
                break;
            case HOME_SCREEN_LOGGED:
                setUpHomeLogged();
                break;
            case LEVEL_SELECTION:
                setUpLevelSelection();
                break;
            case GAMEPLAY_SCREEN:
                setUpGameplayScreen();
                break;
        }
    }

    private void setUpGameplayScreen() {
        //menuBox
        menuBox.getChildren().addAll(profileBox, homeButton);

        
    }

    private void setUpLevelSelection() {
        //menuBox
        homeButton = new Button("Home");
        menuBox.getChildren().addAll(profileBox, homeButton);

        //curentPane
        buildDemoLevelSelect();
        homeScreenPane = levelNodeContainer;
        currentPane.getChildren().add(homeScreenPane);

        //categoryPane
        categoryLabel = new Label(gameModesBox.getValue());
        categoryLabel.getStyleClass().add("category-label");
        HBox blankLeftBox = new HBox();
        HBox.setHgrow(blankLeftBox, Priority.ALWAYS);
        blankLeftBox.setMaxWidth(475);
        HBox blankRightBox = new HBox();
        HBox.setHgrow(blankRightBox, Priority.ALWAYS);

        categoryPane.getChildren().addAll(blankLeftBox, categoryLabel, blankRightBox);
    }

    private void setUpHomeScreen(){

    }

    private void setUpHomeLogged(){
        //menuBox
        profileBox = new ComboBox<String>();
        //REPLACE LATER!!!
        profileBoxData.add("Steven");
        profileBoxData.add("Log out");
        profileBox.setItems(profileBoxData);
        //*****
        profileBox.setValue("Steven");

        gameModesBox = new ComboBox<String>();
        //REPLACE LATER!!!
        gameModesBoxData.add("Famous People");
        gameModesBoxData.add("Animals");
        gameModesBox.setItems(gameModesBoxData);
        //*****
        gameModesBox.setValue("Select Mode");

        startPlayingButton = new Button("Start Playing");

        menuBox.getChildren().addAll(profileBox, gameModesBox, startPlayingButton);

        //currentPane
        homeScreenPane = letterNodeContainer;
        homeScreenPane.setMinHeight(600);
        homeScreenPane.setMinWidth(600);
        homeScreenPane.getStyleClass().add("temp");

        hudPane = new VBox();
        hudPane.setMinWidth(200);

        currentPane.getChildren().add(homeScreenPane);

    }

    private void logInPrompt(){
        StackPane paneHolder = new StackPane();
        paneHolder.setMinHeight(baseHBox.getHeight());
        paneHolder.setMinHeight(baseHBox.getWidth());
        workspace.getChildren().add(paneHolder);

        GridPane loginPane = new GridPane();
        paneHolder.getChildren().add(loginPane);
        paneHolder.setPadding(new Insets(300, 300, 300, 300));

        Label loginLabel = new Label("Profile Name ");
        Label passwordLabel = new Label("Profile Password ");
        loginPane.add(loginLabel, 0, 0);
        loginPane.add(passwordLabel, 0, 1);

        loginField = new TextField();
        passwordField = new TextField();
        loginPane.add(loginField, 1, 0);
        loginPane.add(passwordField, 1, 1);

        loginPane.setMinHeight(300);
        loginPane.setMinWidth(600);
        loginPane.setMaxHeight(300);
        loginPane.setMaxWidth(600);
        loginPane.getStyleClass().add("login-box");
        loginPane.setAlignment(Pos.CENTER);

        loginLabel.getStyleClass().add("login-label");
        passwordLabel.getStyleClass().add("login-label");
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
