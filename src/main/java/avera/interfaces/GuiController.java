package avera.interfaces;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Creation of the main Application
 *
 * @author Vera
 * */

public final class GuiController extends Application
{
    private static BorderPane mainPanel;
    private static VBox rightPanel;
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

    public static void start(Scene scene, String title)
    {
        scene.getStylesheets().add(GuiController.class.getResource("/styles.css").toExternalForm());

        stageAtt.resizableProperty().setValue(false);
        stageAtt.getIcons().add(new Image("/icons/main.png"));
        stageAtt.setScene(scene);
        stageAtt.setTitle(title);
        stageAtt.show();
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
        HBox topPanel = Components.createTop("Lugares Disponibles");
        Button displayContent = new Button();
        ImageView imageButton = new ImageView("/icons/displayContent_modified.png");

        imageButton.setFitWidth(25);
        imageButton.setFitHeight(25);

        displayContent.setGraphic(imageButton);
        displayContent.setAlignment(Pos.CENTER_RIGHT);
        displayContent.setPrefSize(20, 20);
        displayContent.getStyleClass().add("title-button");

        /* Event Handler */
        displayContent.setOnMouseClicked(event -> CodeController.displayRightPanel(rightPanel, mainPanel));

        topPanel.getChildren().remove(0);
        topPanel.getChildren().add(displayContent);

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
        Button seeRoute = new Button("Ver ruta actual");
        //Button searchMonuments = new Button("Buscar");

        seeRoute.setMaxSize(Double.MAX_VALUE, 50);
        seeRoute.getStyleClass().add("btn");
        /*searchMonuments.setMaxSize(Double.MAX_VALUE, 50);
        searchMonuments.getStyleClass().add("btn");*/

        VBox.setVgrow(rightPanel, Priority.ALWAYS);
        VBox.setVgrow(seeRoute, Priority.ALWAYS);
        //VBox.setVgrow(searchMonuments, Priority.ALWAYS);

        seeRoute.setOnMouseClicked(event -> CodeController.getlRoute());
        //searchMonuments.setOnMouseClicked(event -> CodeController.searchSpecificMonument());

        rightPanel.setSpacing(15);
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setPadding(new Insets(5));
        rightPanel.setPrefSize(150, 150);
        rightPanel.getChildren().addAll(seeRoute/*, searchMonuments*/);
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
        GridPane centerPanel = Components.createCenterMain();
        int indexPage = CodeController.getIndex()[0];
        int indexPreviousPage = CodeController.getIndex()[1];

        if(CodeController.getFinalPage()) CodeController.createGridContentMainScene(centerPanel, "");
        else if(indexPage == 0) CodeController.createGridContentMainScene(centerPanel, "normal");
        else if(indexPage >= 1 && (indexPage == 1 + indexPreviousPage)) CodeController.createGridContentMainScene(centerPanel, "sum");
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
        HBox box =  Components.createBottomMain();

        if(CodeController.getFinalPage()) box.getChildren().remove(2);
        else if(indexPage == 0) box.getChildren().remove(0);

        return box;
    }
}
