package avera.interfaces.scenes;

import avera.interfaces.CodeController;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Modeling the Description Interface (for Monuments)
 *
 * @author Vera
 * */
public class DescriptionScene
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

        mainPane.setTop(createTop(stage));
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
        GridPane gp = new GridPane();

        gp.setAlignment(Pos.TOP_CENTER);
        gp.setVgap(5);
        gp.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gp.setPadding(new Insets(10));

        /* Constraints */
        ColumnConstraints colNombre = new ColumnConstraints();
        ColumnConstraints colValor = new ColumnConstraints();

        colNombre.setHalignment(HPos.CENTER);
        colNombre.setHgrow(Priority.ALWAYS);

        colValor.setHalignment(HPos.CENTER);
        colValor.setHgrow(Priority.ALWAYS);

        colNombre.setPrefWidth(10);
        colValor.setPrefWidth(120);

        gp.getColumnConstraints().addAll(colNombre, colValor);

        return CodeController.createGridContentDescriptionScene(gp, dataEntryKey);
    }

    /**
     * Create the top contents of the Scene
     *
     * @return A HBOX to include in the Scene
     * */
    private static HBox createTop (Stage stage)
    {
        HBox bottomPanel = new HBox();
        Button btnFowardPage = new Button();
        ImageView imageButton = new ImageView("icons/fowardPage.png");

        imageButton.setFitWidth(25);
        imageButton.setFitHeight(25);

        btnFowardPage.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnFowardPage.getStyleClass().add("btn");
        btnFowardPage.setGraphic(imageButton);

        btnFowardPage.setOnMouseClicked(event -> CodeController.closeDescriptionWindow(stage));

        bottomPanel.getChildren().addAll(btnFowardPage);
        bottomPanel.getStyleClass().add("btn-search-box");
        bottomPanel.setSpacing(5);
        bottomPanel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        bottomPanel.setPadding(new Insets(10, 10, 10, 10));
        HBox.setHgrow(bottomPanel, Priority.ALWAYS);

        return bottomPanel;
    }
}
