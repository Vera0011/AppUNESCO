package avera.interfaces.scenes;

import avera.interfaces.GuiController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

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
        root = createGui(dataEntryKey);

        Scene scene = new Scene(root, 500, 500);
        Stage stage = new Stage();
        stage.setScene(scene);

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
    private static BorderPane createGui (String dataEntryKey)
    {
        BorderPane mainPane = new BorderPane();

        mainPane.setTop(createTop());
        mainPane.setCenter(createCenter());

        return mainPane;
    }

    /**
     * Create the center contents of the Scene
     *
     * @return A GridPane to include in the Scene
     * */
    private static GridPane createCenter ()
    {
        return null;
    }

    /**
     * Create the top contents of the Scene
     *
     * @return A HBOX to include in the Scene
     * */
    private static HBox createTop ()
    {
        HBox bottomPanel = new HBox();
        Button btnFowardPage = new Button();
        ImageView imageButton = new ImageView("/icons/forwardPage.png");

        imageButton.setFitWidth(25);
        imageButton.setFitHeight(25);

        btnFowardPage.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnFowardPage.getStyleClass().add("btn");
        btnFowardPage.setGraphic(imageButton);

        bottomPanel.getChildren().addAll(btnFowardPage);
        bottomPanel.getStyleClass().add("btn-search-box");
        bottomPanel.setSpacing(5);
        bottomPanel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        bottomPanel.setPadding(new Insets(10, 10, 10, 10));
        HBox.setHgrow(bottomPanel, Priority.ALWAYS);

        return bottomPanel;
    }
}
