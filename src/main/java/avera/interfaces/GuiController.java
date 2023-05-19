package avera.interfaces;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Creation of the main Application
 *
 * @author Vera
 * */

public class GuiController extends Application
{
    private static BorderPane mainPanel;
    private static HBox bottomPanel;
    private static VBox rightPanel;
    private static GridPane centerPanel;
    private static Stage stageAtt;
    /**
     * Creating Application and main Stage
     *
     * @param stage Stage of the main Application
     * */
    @Override
    public void start(Stage stage)
    {
        CodeController.initiateDatabase();

        stageAtt = stage;

        Scene scene = createScene();
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.resizableProperty().setValue(false);
        stage.getIcons().add(new Image("/icons/main.png"));
        stage.setScene(scene);
        stage.setTitle("Listado del Patrimonio de la UNESCO");
        stage.show();
    }

    /**
     * Running the Applications
     *
     * @param args The arguments passed by the Application
     * @see Application
     * */
    public static void runApplication(String[] args)
    {
        launch(args);
    }

    /**
     * Scene creator for the GUI
     *
     * @return Returns a BorderPane to include in the Scene of the window
     */
    public static Scene createScene()
    {
        mainPanel = new BorderPane();
        mainPanel.setTop(createTop());
        mainPanel.setCenter(createCenter());
        mainPanel.setBottom(createBottom());
        mainPanel.getStyleClass().add("root");

        Scene scene =  new Scene(mainPanel, 800, 800);
        scene.getStylesheets().add(GuiController.class.getResource("/styles.css").toExternalForm());

        return scene;
    }

    /**
     * Update the actual Scene in the main Stage
     * */
    public static void updateScene()
    {
        stageAtt.setScene(createScene());
    }

    /**
     * Create the top contents of the Scene
     *
     * @return A HBOX to include in the Scene
     * */

    public static HBox createTop()
    {
        HBox topPanel = new HBox();
        Button displayContent = new Button();
        Label title = new Label("Lugares Disponibles");
        ImageView imageButton = new ImageView("/icons/displayContent_modified.png");

        imageButton.setFitWidth(25);
        imageButton.setFitHeight(25);

        displayContent.setGraphic(imageButton);
        displayContent.setAlignment(Pos.CENTER_RIGHT);
        displayContent.setPrefSize(20, 20);
        displayContent.getStyleClass().add("title-button");

        /* Event Handler */
        displayContent.setOnMouseClicked(event -> CodeController.displayRightPanel(rightPanel, mainPanel));

        title.getStyleClass().add("title");
        title.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setAlignment(Pos.CENTER);
        title.setPadding(new Insets(10));

        topPanel.getChildren().addAll(title, displayContent);
        topPanel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(topPanel, Priority.ALWAYS);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setPadding(new Insets(5));
        topPanel.getStyleClass().add("title-hbox");

        return topPanel;
    }

    /**
     * Create the right contents of the Scene (options bar)
     *
     * @return A VBox to include in the Scene
     * */
    public static VBox createRight()
    {
        rightPanel = new VBox();
        Button verRuta = new Button("Ver ruta actual");
        Button verRutasAntiguas = new Button("Ver antiguas rutas");
        Button searchMonumentos = new Button("Buscar");

        verRuta.setMaxSize(Double.MAX_VALUE, 50);
        verRuta.getStyleClass().add("btn");
        verRutasAntiguas.setMaxSize(Double.MAX_VALUE, 50);
        verRutasAntiguas.getStyleClass().add("btn");
        searchMonumentos.setMaxSize(Double.MAX_VALUE, 50);
        searchMonumentos.getStyleClass().add("btn");

        VBox.setVgrow(rightPanel, Priority.ALWAYS);
        VBox.setVgrow(verRuta, Priority.ALWAYS);
        VBox.setVgrow(verRutasAntiguas, Priority.ALWAYS);
        VBox.setVgrow(searchMonumentos, Priority.ALWAYS);

        rightPanel.setSpacing(15);
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setPadding(new Insets(5));
        rightPanel.setPrefSize(150, 150);
        rightPanel.getChildren().addAll(verRuta, verRutasAntiguas, searchMonumentos);
        rightPanel.getStyleClass().add("right-panel");

        return rightPanel;
    }

    /**
     * Create the center contents of the Scene
     *
     * @return A GridPane to include in the Scene
     * */
    public static GridPane createCenter()
    {
        centerPanel = new GridPane();

        /* Grid Styling */
        centerPanel.setAlignment(Pos.TOP_CENTER);
        centerPanel.setVgap(5);
        centerPanel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        centerPanel.setPadding(new Insets(10));

        /* Creating Constraints */
        ColumnConstraints nameCol = new ColumnConstraints();
        ColumnConstraints directionColumn = new ColumnConstraints();
        ColumnConstraints addColumn = new ColumnConstraints();

        nameCol.setHalignment(HPos.CENTER);
        nameCol.setHgrow(Priority.ALWAYS);

        directionColumn.setHgrow(Priority.ALWAYS);
        directionColumn.setHalignment(HPos.CENTER);

        addColumn.setHgrow(Priority.ALWAYS);
        addColumn.setHalignment(HPos.CENTER);

        centerPanel.getColumnConstraints().addAll(nameCol, directionColumn, addColumn);

        int indexPage = CodeController.getIndex()[0];
        int indexPreviousPage = CodeController.getIndex()[1];

        if(CodeController.getFinalPage()) CodeController.createGridContentMainScene(centerPanel, "");
        else if(indexPage == 0) CodeController.createGridContentMainScene(centerPanel, "normal");
        else if(indexPage >= 1 && (indexPage == 1 +indexPreviousPage)) CodeController.createGridContentMainScene(centerPanel, "sum");
        else CodeController.createGridContentMainScene(centerPanel, "");

        return centerPanel;
    }

    /**
     * Create the bottom contents of the Scene
     *
     * @return A HBOX to include in the Scene
     * */
    public static HBox createBottom()
    {
        int indexPage = CodeController.getIndex()[0];
        Button btnSearch = new Button("Buscar");
        Button btnNextPage = new Button();
        ImageView imageButton = new ImageView("/icons/nextPage.png");
        Button btnPreviousPage = new Button();
        ImageView imageButton2 = new ImageView("/icons/fowardPage.png");
        bottomPanel = new HBox();

        imageButton.setFitWidth(25);
        imageButton.setFitHeight(25);
        imageButton2.setFitHeight(25);
        imageButton2.setFitHeight(25);

        btnSearch.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnSearch.getStyleClass().add("btn");

        btnNextPage.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnNextPage.getStyleClass().add("btn");
        btnNextPage.setGraphic(imageButton);

        btnPreviousPage.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnPreviousPage.getStyleClass().add("btn");
        btnPreviousPage.setGraphic(imageButton2);

        btnNextPage.setOnMouseClicked(event -> CodeController.nextPage());
        btnPreviousPage.setOnMouseClicked(event -> CodeController.previousPage());

        bottomPanel.getStyleClass().add("btn-search-box");
        bottomPanel.setSpacing(5);
        bottomPanel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        bottomPanel.setPadding(new Insets(10, 10, 10, 10));
        HBox.setHgrow(btnSearch, Priority.ALWAYS);
        HBox.setHgrow(bottomPanel, Priority.ALWAYS);

        if(CodeController.getFinalPage()) bottomPanel.getChildren().addAll(btnPreviousPage, btnSearch);
        else if(indexPage == 0) bottomPanel.getChildren().addAll(btnSearch, btnNextPage);
        else bottomPanel.getChildren().addAll(btnPreviousPage, btnSearch, btnNextPage);

        return bottomPanel;
    }
}
