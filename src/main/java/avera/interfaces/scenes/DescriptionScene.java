package avera.interfaces.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import avera.interfaces.CodeController;
import avera.interfaces.Components;

/**
 * Modeling the Description Interface (for Monuments)
 *
 * @author Vera
 * */
public final class DescriptionScene
{
    private static BorderPane root;

    /**
     * Function to open the description window
     *
     * @param dataEntryKey The data to be displayed
     * */
    public static void openDescriptionWindow(String dataEntryKey)
    {
        Stage stage = new Stage();
        root = createGui(dataEntryKey, stage);

        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add(DescriptionScene.class.getResource("/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.getIcons().add(new Image("/icons/main.png"));
        stage.resizableProperty().setValue(false);
        stage.setTitle("Descripcion del lugar");
        stage.show();
    }

    /**
     * Scene creator for the GUI
     *
     * @param dataEntryKey The data to be displayed
     * @return Returns a BorderPane to include in the Scene of the window
     * */
    private static BorderPane createGui (String dataEntryKey, Stage stage)
    {
        BorderPane mainPane = new BorderPane();

        mainPane.setTop(createTop(dataEntryKey, stage));
        mainPane.setCenter(createCenter(dataEntryKey));

        return mainPane;
    }

    /**
     * Create the center contents of the Scene
     *
     * @return A GridPane to include in the Scene
     * */
    private static GridPane createCenter (String dataEntryKey)
    {
        GridPane gp = Components.createCenterMain();

        gp.getColumnConstraints().remove(2);
        gp.getColumnConstraints().get(0).setPrefWidth(10);
        gp.getColumnConstraints().get(0).setPrefWidth(120);

        return CodeController.createGridContentDescriptionScene(gp, dataEntryKey);
    }

    /**
     * Create the top contents of the Scene
     *
     * @return A HBOX to include in the Scene
     * */
    private static HBox createTop (String dataEntryKey, Stage stage)
    {
        HBox bottomPanel = new HBox();
        Button btnFowardPage = new Button();
        Pane midPanel = new Pane();
        Button addButton = new Button();
        ImageView imageButton = new ImageView("icons/fowardPage.png");
        ImageView imageButton2 = new ImageView("icons/addIcon.png");

        imageButton.setFitWidth(25);
        imageButton.setFitHeight(25);
        imageButton2.setFitWidth(25);
        imageButton2.setFitHeight(25);

        HBox.setHgrow(midPanel, Priority.ALWAYS);

        btnFowardPage.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnFowardPage.getStyleClass().add("btn");
        btnFowardPage.setGraphic(imageButton);
        btnFowardPage.setAlignment(Pos.CENTER_LEFT);

        addButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        addButton.getStyleClass().add("btn");
        addButton.setGraphic(imageButton2);
        addButton.setAlignment(Pos.CENTER_RIGHT);

        btnFowardPage.setOnMouseClicked(event -> CodeController.closeDescriptionWindow(stage));
        addButton.setOnMouseClicked(event -> CodeController.addRoute(dataEntryKey, stage));

        bottomPanel.getChildren().addAll(btnFowardPage, midPanel, addButton);
        bottomPanel.getStyleClass().add("btn-search-box");
        bottomPanel.setSpacing(5);
        bottomPanel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        bottomPanel.setPadding(new Insets(10, 10, 10, 10));
        HBox.setHgrow(bottomPanel, Priority.ALWAYS);

        return bottomPanel;
    }
}
