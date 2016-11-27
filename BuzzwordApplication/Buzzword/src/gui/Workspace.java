package gui;

import apptemplate.AppTemplate;
import components.AppWorkspaceComponent;
import controller.BuzzwordController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
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
    StackPane currentPane; // homeScreenPane/levelSelectPane/playPane, hudPane

    GameState state;
    Button lastButtonClicked;


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
    GridPane loginPane = new GridPane();

    ChoiceBox<String> profileBox;
    ObservableList<String> profileBoxData = FXCollections.observableArrayList();
    ChoiceBox<String> gameModesBox;
    ObservableList<String> gameModesBoxData = FXCollections.observableArrayList();
    Button startPlayingButton;
    Button homeButton; //returns to homeLoggedScreen

    Pane linePane;

    Label categoryLabel;
    HBox timePane;
    Label remainingTimeLabel;
    Label currentTimeLabel;
    StackPane playPane;
    BorderPane appPane;
    VBox hudPane; //
    Label currentGuessLabel;
    HBox currentGuessBox;
    ScrollPane wordScrollPane;
    GridPane wordGrid;
    VBox scorePane;
    GridPane totalScoreBox;
    VBox targetBox;
    Label totalLabel;
    Label totalScoreLabel;
    VBox wordPointVBox;
    HBox wordPointLine;
    Label targetPointLabel;
    Label targetLabel;
    Label replayLabel;
    ArrayList<List<StackPane>> levelNodes;
    ArrayList<List<StackPane>> letterNodes;

    ArrayList<GameState> stateArray;
    int stateIndex;

    public Workspace(AppTemplate initApp) throws IOException {
        System.setProperty("glass.accessible.force", "false");
        app = initApp;
        gui = app.getGUI();
        layoutGUI();     // initialize all the workspace (GUI) components including the containers and their layout
        state = GameState.HOME_SCREEN;

        //for viewing purposes only
       /* stateArray = new ArrayList<GameState>();
        stateArray.add(GameState.HOME_SCREEN_LOG_PROMPT);
        stateArray.add(GameState.HOME_SCREEN_LOGGED);
        stateArray.add(GameState.LEVEL_SELECTION);
        stateArray.add(GameState.GAMEPLAY_SCREEN);
        gui.getPrimaryScene().setOnMouseClicked(e -> {
            state = stateArray.get(stateIndex);
            reinitialize();
            stateIndex++;
        });*/

        //logInPrompt();

        //state = GameState.HOME_SCREEN_LOGGED;
        //reinitialize();

        //state = GameState.LEVEL_SELECTION;
        //reinitialize();

        //state = GameState.GAMEPLAY_SCREEN;
        //reinitialize();


        setupHandlers(); // ... and set up event handling
        activateWorkspace(gui.getAppPane());
    }

    private void layoutGUI(){
        PropertyManager propertyManager = PropertyManager.getManager();

        createProfileButton = new Button(propertyManager.getPropertyValue(CREATE_PROFILE_BUTTON));
        loginButton = new Button(propertyManager.getPropertyValue(LOGIN_BUTTON));
        menuBox = new VBox();
        menuBox.getChildren().addAll(createProfileButton, loginButton);
        menuBox.setMinWidth(200);
        menuBox.setSpacing(50);
        menuBox.setAlignment(Pos.BASELINE_RIGHT);
        menuBox.setPadding(new Insets(150, 0, 0, 0));

        headingLabel = new Label(propertyManager.getPropertyValue(WORKSPACE_HEADING_LABEL));
        exitLabel = new Label(propertyManager.getPropertyValue(EXIT_BUTTON));

        HBox blankLeftBox = new HBox();
        blankLeftBox.setMaxWidth(410);
        HBox blankRightBox = new HBox();
        headerPane = new HBox();
        headerPane.getChildren().addAll(blankLeftBox, headingLabel, blankRightBox, exitLabel);
        HBox.setHgrow(blankLeftBox, Priority.ALWAYS);
        HBox.setHgrow(blankRightBox, Priority.ALWAYS);

        categoryPane = new HBox(); //empty
        categoryPane.setMinHeight(50);

        buildHomeGrid();
        drawLines();

        homeScreenPane = letterNodeContainer;
        homeScreenPane.setMinHeight(600);
        homeScreenPane.setMinWidth(600);
        homeScreenPane.getStyleClass().add("temp");

        hudPane = new VBox();
        hudPane.setMinWidth(200);

        currentPane = new StackPane();
        currentPane.getChildren().add(homeScreenPane);


        levelLabelPane = new HBox();
        levelLabelPane.setMinHeight(50);

        gameplayVBox = new VBox();
        gameplayVBox.getChildren().addAll(currentPane, levelLabelPane);


        VBox blankLeftBox2 = new VBox();
        blankLeftBox2.setMinWidth(200);
        VBox blankityBox = new VBox();
        blankityBox.setMinWidth(75);

        outerGameHBox = new HBox();
        outerGameHBox.getChildren().addAll(blankLeftBox2, gameplayVBox, blankityBox, hudPane);

        bottomHBox = new HBox();
        bottomHBox.setMinHeight(100);

        rightVBox = new VBox();
        rightVBox.getChildren().addAll(headerPane, categoryPane, outerGameHBox, bottomHBox);


        baseHBox = new HBox();
        baseHBox.getChildren().addAll(menuBox, rightVBox);
        HBox.setHgrow(rightVBox, Priority.ALWAYS);

        profileBox = new ChoiceBox<String>();

        gameModesBox = new ChoiceBox<String>();


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

    private void drawLines(){

        linePane = new Pane();
        linePane.setPickOnBounds(false);
        linePane.setMinHeight(600);
        linePane.setMinWidth(600);

        ArrayList<Line> lineArrayList = new ArrayList<Line>();
        for(int i = 0; i < 42; i++)
            lineArrayList.add(new Line());

        int lineCounter = 0;
        //draw horizontal
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 3; j++, lineCounter++){
                lineArrayList.get(lineCounter).setStartX(140 + (j*140));
                lineArrayList.get(lineCounter).setStartY(90 + (i*140));
                lineArrayList.get(lineCounter).setEndX(180 + (j*140));
                lineArrayList.get(lineCounter).setEndY(90 + (i*140));
            }
        //draw vertical
        for(int j = 0; j < 3; j++)
            for(int i = 0; i < 4; i++, lineCounter++){
                lineArrayList.get(lineCounter).setStartX(90 + (i*140));
                lineArrayList.get(lineCounter).setStartY(140 + (j*140));
                lineArrayList.get(lineCounter).setEndX(90 + (i*140));
                lineArrayList.get(lineCounter).setEndY(180 + (j*140));
            }


        for(int i = 0; i < lineArrayList.size(); i++)
            linePane.getChildren().add(lineArrayList.get(i));
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

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    private void setupHandlers(){
        controller = new BuzzwordController(app);
        loginButton.setOnAction( e ->{
            controller.handleLoginPrompt();
        });

        createProfileButton.setOnAction(e ->{
            controller.createProfilePrompt();
        });

        //for login pane
        gui.getPrimaryScene().setOnKeyPressed(e ->{
            if(e.getCode().equals(KeyCode.ENTER)) {
                if (lastButtonClicked.equals(createProfileButton)) {
                    controller.createProfile();
                } else {
                    controller.handleLoginAttempt();
                }
            }
            else if(e.getCode().equals(KeyCode.ESCAPE))
                controller.exitLoginPrompt();
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
        workspace.getChildren().clear();
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
        workspace.getChildren().add(baseHBox);
        //menuBox
        menuBox.getChildren().addAll(profileBox, homeButton);

        //categoryPane
        HBox blankLeftBox = new HBox();
        HBox.setHgrow(blankLeftBox, Priority.ALWAYS);
        blankLeftBox.setMaxWidth(435);
        HBox blankRightBox = new HBox();
        HBox.setHgrow(blankRightBox, Priority.ALWAYS);
        blankRightBox.setMaxWidth(270);

        remainingTimeLabel = new Label("Time Remaining: ");
        remainingTimeLabel.getStyleClass().add("time-label");
        currentTimeLabel = new Label("60 seconds");
        currentTimeLabel.getStyleClass().add("time-label");
        timePane = new HBox();
        timePane.getChildren().addAll(remainingTimeLabel, currentTimeLabel);
        timePane.getStyleClass().add("time-pane");
        timePane.setPadding(new Insets(0, 10, 0, 10));
        categoryPane.getChildren().addAll(blankLeftBox, categoryLabel, blankRightBox, timePane);
        categoryPane.setPadding(new Insets(0, 50, 0, 0));

        //currentPane
        homeScreenPane = letterNodeContainer;
        currentPane.getChildren().add(homeScreenPane);

        //levelLabelPane
        levelLabel = new Label("Level 1");
        levelLabel.getStyleClass().add("category-label");

        HBox blankLeftBox1 = new HBox();
        HBox.setHgrow(blankLeftBox1, Priority.ALWAYS);
        blankLeftBox1.setMaxWidth(340);
        HBox blankRightBox1 = new HBox();
        HBox.setHgrow(blankRightBox1, Priority.ALWAYS);
        levelLabelPane.getChildren().addAll(blankLeftBox1, levelLabel, blankRightBox1);

        //bottomHBox
        try {
            replayLabel = new Label("");
            Image image = new Image(Workspace.class.getResourceAsStream("/images/play.png"));
            replayLabel.setGraphic(new ImageView(image));
        }catch(Exception e){

        }

        HBox blankLeftBox2 = new HBox();
        HBox.setHgrow(blankLeftBox2, Priority.ALWAYS);
        blankLeftBox2.setMaxWidth(470);
        HBox blankRightBox2 = new HBox();
        HBox.setHgrow(blankRightBox2, Priority.ALWAYS);
        bottomHBox.getChildren().addAll(blankLeftBox2, replayLabel, blankRightBox2);

        //hudPane
        currentGuessLabel = new Label("B U");
        currentGuessLabel.getStyleClass().add("guess-label");
        currentGuessBox = new HBox();
        currentGuessBox.getChildren().add(currentGuessLabel);
        currentGuessBox.getStyleClass().add("guess-pane");

        wordGrid = new GridPane();
        wordGrid.getStyleClass().add("words-pane");
        wordGrid.getColumnConstraints().add(new ColumnConstraints(200));
        wordGrid.getColumnConstraints().add(new ColumnConstraints(50));
        ArrayList<String> words = new ArrayList<>();
        words.add("WAR");
        words.add("RAW");
        words.add("DRAW");
        ArrayList<String> points = new ArrayList<>();
        points.add("10");
        points.add("10");
        points.add("20");
        for(int i = 0; i < 3; i++) {
            wordGrid.add(new Label(words.get(i)), 0, i);
            wordGrid.add(new Label(points.get(i)), 1, i);
        }
        for(int i = 3; i < 7; i++)
            wordGrid.add(new Label(""), 0, i);

        for(int i = 0; i < 6; i++){
            wordGrid.getChildren().get(i).getStyleClass().add("words");
        }
        wordScrollPane = new ScrollPane();
        wordScrollPane.setContent(wordGrid);
        wordScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        totalScoreBox = new GridPane();
        totalScoreBox.getStyleClass().add("total-pane");
        totalScoreBox.getColumnConstraints().add(new ColumnConstraints(200));
        totalScoreBox.getColumnConstraints().add(new ColumnConstraints(50));
        totalScoreBox.add(new Label("TOTAL"), 0, 0);
        totalScoreBox.add(new Label("40"), 1, 0);
        for(int i = 0; i < 2; i++){
            totalScoreBox.getChildren().get(i).getStyleClass().add("total");
        }

        HBox fillerPane = new HBox();
        fillerPane.setMinHeight(30);
        HBox filler1Pane = new HBox();
        filler1Pane.setMinHeight(30);

        targetBox = new VBox();
        targetBox.getStyleClass().add("target-pane");
        targetBox.getChildren().add(new Label("Target"));
        targetBox.getChildren().add(new Label("75 Points"));
        targetBox.getChildren().get(0).getStyleClass().add("target");
        targetBox.getChildren().get(1).getStyleClass().add("target");

        hudPane.getChildren().addAll(currentGuessBox, fillerPane, wordScrollPane, totalScoreBox, filler1Pane, targetBox);
        hudPane.setPadding(new Insets(30, 0, 0, 0));

        //draw lines
        currentPane.getChildren().add(linePane);
    }

    private void setUpLevelSelection() {
        workspace.getChildren().add(baseHBox);
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
        blankLeftBox.setMaxWidth(430);
        HBox blankRightBox = new HBox();
        HBox.setHgrow(blankRightBox, Priority.ALWAYS);

        categoryPane.getChildren().addAll(blankLeftBox, categoryLabel, blankRightBox);
    }

    private void setUpHomeScreen(){
        workspace.getChildren().add(baseHBox);
        menuBox.getChildren().addAll(createProfileButton, loginButton);
        currentPane.getChildren().add(homeScreenPane);
    }

    private void setUpHomeLogged(){
        workspace.getChildren().add(baseHBox);

        startPlayingButton = new Button("Start Playing");

        menuBox.getChildren().addAll(profileBox, gameModesBox, startPlayingButton);

        //currentPane
        homeScreenPane = letterNodeContainer;
        homeScreenPane.setMinHeight(600);
        homeScreenPane.setMinWidth(600);
        homeScreenPane.getStyleClass().add("temp");

        currentPane.getChildren().add(homeScreenPane);

    }

    private void logInPrompt(){
        PropertyManager propertyManager = PropertyManager.getManager();
        workspace.getChildren().add(baseHBox);

        //menuBox
        menuBox.getChildren().addAll(createProfileButton, loginButton);

        //currentPane
        currentPane.getChildren().add(homeScreenPane);

        StackPane paneHolder = new StackPane();
        paneHolder.setMinHeight(baseHBox.getHeight());
        paneHolder.setMinWidth(baseHBox.getWidth());
        workspace.getChildren().add(paneHolder);

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
        loginPane.getStyleClass().add(propertyManager.getPropertyValue(LOGIN_BOX));
        loginPane.setAlignment(Pos.CENTER);

        loginLabel.getStyleClass().add(propertyManager.getPropertyValue(LOGIN_LABEL));
        passwordLabel.getStyleClass().add(propertyManager.getPropertyValue(LOGIN_LABEL));
    }

    @Override
    public void initStyle() {
        PropertyManager propertyManager = PropertyManager.getManager();

        rightVBox.getStyleClass().add(propertyManager.getPropertyValue(RIGHT_GRAY));

        headingLabel.getStyleClass().add(propertyManager.getPropertyValue(HEADING_LABEL));

        exitLabel.getStyleClass().add(propertyManager.getPropertyValue(EXIT_LABEL));

        menuBox.getStyleClass().add(propertyManager.getPropertyValue(MENU_BOX));
    }

    @Override
    public void reloadWorkspace() {

    }

    public GridPane getLoginPane() {
        return loginPane;
    }

    public void setLoginPane(GridPane loginPane) {
        this.loginPane = loginPane;
    }

    public Button getLastButtonClicked() {
        return lastButtonClicked;
    }

    public void setLastButtonClicked(Button lastButtonClicked) {
        this.lastButtonClicked = lastButtonClicked;
    }

    public AppTemplate getApp() {
        return app;
    }

    public void setApp(AppTemplate app) {
        this.app = app;
    }

    public AppGUI getGui() {
        return gui;
    }

    public void setGui(AppGUI gui) {
        this.gui = gui;
    }

    public BuzzwordController getController() {
        return controller;
    }

    public void setController(BuzzwordController controller) {
        this.controller = controller;
    }

    public Label getHeadingLabel() {
        return headingLabel;
    }

    public void setHeadingLabel(Label headingLabel) {
        this.headingLabel = headingLabel;
    }

    public HBox getBaseHBox() {
        return baseHBox;
    }

    public void setBaseHBox(HBox baseHBox) {
        this.baseHBox = baseHBox;
    }

    public VBox getRightVBox() {
        return rightVBox;
    }

    public void setRightVBox(VBox rightVBox) {
        this.rightVBox = rightVBox;
    }

    public StackPane getCurrentPane() {
        return currentPane;
    }

    public void setCurrentPane(StackPane currentPane) {
        this.currentPane = currentPane;
    }

    public Button getCreateProfileButton() {
        return createProfileButton;
    }

    public void setCreateProfileButton(Button createProfileButton) {
        this.createProfileButton = createProfileButton;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(Button loginButton) {
        this.loginButton = loginButton;
    }

    public VBox getMenuBox() {
        return menuBox;
    }

    public void setMenuBox(VBox menuBox) {
        this.menuBox = menuBox;
    }

    public Pane getHomeScreenPane() {
        return homeScreenPane;
    }

    public void setHomeScreenPane(Pane homeScreenPane) {
        this.homeScreenPane = homeScreenPane;
    }

    public GridPane getLetterNodeContainer() {
        return letterNodeContainer;
    }

    public void setLetterNodeContainer(GridPane letterNodeContainer) {
        this.letterNodeContainer = letterNodeContainer;
    }

    public GridPane getLevelNodeContainer() {
        return levelNodeContainer;
    }

    public void setLevelNodeContainer(GridPane levelNodeContainer) {
        this.levelNodeContainer = levelNodeContainer;
    }

    public HBox getHeaderPane() {
        return headerPane;
    }

    public void setHeaderPane(HBox headerPane) {
        this.headerPane = headerPane;
    }

    public HBox getFillerPane() {
        return fillerPane;
    }

    public void setFillerPane(HBox fillerPane) {
        this.fillerPane = fillerPane;
    }

    public HBox getCategoryPane() {
        return categoryPane;
    }

    public void setCategoryPane(HBox categoryPane) {
        this.categoryPane = categoryPane;
    }

    public Label getExitLabel() {
        return exitLabel;
    }

    public void setExitLabel(Label exitLabel) {
        this.exitLabel = exitLabel;
    }

    public HBox getLevelLabelPane() {
        return levelLabelPane;
    }

    public void setLevelLabelPane(HBox levelLabelPane) {
        this.levelLabelPane = levelLabelPane;
    }

    public Label getLevelLabel() {
        return levelLabel;
    }

    public void setLevelLabel(Label levelLabel) {
        this.levelLabel = levelLabel;
    }

    public VBox getGameplayVBox() {
        return gameplayVBox;
    }

    public void setGameplayVBox(VBox gameplayVBox) {
        this.gameplayVBox = gameplayVBox;
    }

    public HBox getOuterGameHBox() {
        return outerGameHBox;
    }

    public void setOuterGameHBox(HBox outerGameHBox) {
        this.outerGameHBox = outerGameHBox;
    }

    public HBox getBottomHBox() {
        return bottomHBox;
    }

    public void setBottomHBox(HBox bottomHBox) {
        this.bottomHBox = bottomHBox;
    }

    public TextField getLoginField() {
        return loginField;
    }

    public void setLoginField(TextField loginField) {
        this.loginField = loginField;
    }

    public TextField getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(TextField passwordField) {
        this.passwordField = passwordField;
    }

    public ChoiceBox<String> getProfileBox() {
        return profileBox;
    }

    public void setProfileBox(ChoiceBox<String> profileBox) {
        this.profileBox = profileBox;
    }

    public ObservableList<String> getProfileBoxData() {
        return profileBoxData;
    }

    public void setProfileBoxData(ObservableList<String> profileBoxData) {
        this.profileBoxData = profileBoxData;
    }

    public ChoiceBox<String> getGameModesBox() {
        return gameModesBox;
    }

    public void setGameModesBox(ChoiceBox<String> gameModesBox) {
        this.gameModesBox = gameModesBox;
    }

    public ObservableList<String> getGameModesBoxData() {
        return gameModesBoxData;
    }

    public void setGameModesBoxData(ObservableList<String> gameModesBoxData) {
        this.gameModesBoxData = gameModesBoxData;
    }

    public Button getStartPlayingButton() {
        return startPlayingButton;
    }

    public void setStartPlayingButton(Button startPlayingButton) {
        this.startPlayingButton = startPlayingButton;
    }

    public Button getHomeButton() {
        return homeButton;
    }

    public void setHomeButton(Button homeButton) {
        this.homeButton = homeButton;
    }

    public Pane getLinePane() {
        return linePane;
    }

    public void setLinePane(Pane linePane) {
        this.linePane = linePane;
    }

    public Label getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(Label categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    public HBox getTimePane() {
        return timePane;
    }

    public void setTimePane(HBox timePane) {
        this.timePane = timePane;
    }

    public Label getRemainingTimeLabel() {
        return remainingTimeLabel;
    }

    public void setRemainingTimeLabel(Label remainingTimeLabel) {
        this.remainingTimeLabel = remainingTimeLabel;
    }

    public Label getCurrentTimeLabel() {
        return currentTimeLabel;
    }

    public void setCurrentTimeLabel(Label currentTimeLabel) {
        this.currentTimeLabel = currentTimeLabel;
    }

    public StackPane getPlayPane() {
        return playPane;
    }

    public void setPlayPane(StackPane playPane) {
        this.playPane = playPane;
    }

    public BorderPane getAppPane() {
        return appPane;
    }

    public void setAppPane(BorderPane appPane) {
        this.appPane = appPane;
    }

    public VBox getHudPane() {
        return hudPane;
    }

    public void setHudPane(VBox hudPane) {
        this.hudPane = hudPane;
    }

    public Label getCurrentGuessLabel() {
        return currentGuessLabel;
    }

    public void setCurrentGuessLabel(Label currentGuessLabel) {
        this.currentGuessLabel = currentGuessLabel;
    }

    public HBox getCurrentGuessBox() {
        return currentGuessBox;
    }

    public void setCurrentGuessBox(HBox currentGuessBox) {
        this.currentGuessBox = currentGuessBox;
    }

    public ScrollPane getWordScrollPane() {
        return wordScrollPane;
    }

    public void setWordScrollPane(ScrollPane wordScrollPane) {
        this.wordScrollPane = wordScrollPane;
    }

    public GridPane getWordGrid() {
        return wordGrid;
    }

    public void setWordGrid(GridPane wordGrid) {
        this.wordGrid = wordGrid;
    }

    public VBox getScorePane() {
        return scorePane;
    }

    public void setScorePane(VBox scorePane) {
        this.scorePane = scorePane;
    }

    public GridPane getTotalScoreBox() {
        return totalScoreBox;
    }

    public void setTotalScoreBox(GridPane totalScoreBox) {
        this.totalScoreBox = totalScoreBox;
    }

    public VBox getTargetBox() {
        return targetBox;
    }

    public void setTargetBox(VBox targetBox) {
        this.targetBox = targetBox;
    }

    public Label getTotalLabel() {
        return totalLabel;
    }

    public void setTotalLabel(Label totalLabel) {
        this.totalLabel = totalLabel;
    }

    public Label getTotalScoreLabel() {
        return totalScoreLabel;
    }

    public void setTotalScoreLabel(Label totalScoreLabel) {
        this.totalScoreLabel = totalScoreLabel;
    }

    public VBox getWordPointVBox() {
        return wordPointVBox;
    }

    public void setWordPointVBox(VBox wordPointVBox) {
        this.wordPointVBox = wordPointVBox;
    }

    public HBox getWordPointLine() {
        return wordPointLine;
    }

    public void setWordPointLine(HBox wordPointLine) {
        this.wordPointLine = wordPointLine;
    }

    public Label getTargetPointLabel() {
        return targetPointLabel;
    }

    public void setTargetPointLabel(Label targetPointLabel) {
        this.targetPointLabel = targetPointLabel;
    }

    public Label getTargetLabel() {
        return targetLabel;
    }

    public void setTargetLabel(Label targetLabel) {
        this.targetLabel = targetLabel;
    }

    public Label getReplayLabel() {
        return replayLabel;
    }

    public void setReplayLabel(Label replayLabel) {
        this.replayLabel = replayLabel;
    }

    public ArrayList<List<StackPane>> getLevelNodes() {
        return levelNodes;
    }

    public void setLevelNodes(ArrayList<List<StackPane>> levelNodes) {
        this.levelNodes = levelNodes;
    }

    public ArrayList<List<StackPane>> getLetterNodes() {
        return letterNodes;
    }

    public void setLetterNodes(ArrayList<List<StackPane>> letterNodes) {
        this.letterNodes = letterNodes;
    }

    public ArrayList<GameState> getStateArray() {
        return stateArray;
    }

    public void setStateArray(ArrayList<GameState> stateArray) {
        this.stateArray = stateArray;
    }

    public int getStateIndex() {
        return stateIndex;
    }

    public void setStateIndex(int stateIndex) {
        this.stateIndex = stateIndex;
    }
}
