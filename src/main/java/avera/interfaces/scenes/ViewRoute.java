package avera.interfaces.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import avera.interfaces.CodeController;
import avera.interfaces.Components;
import avera.interfaces.GuiController;

public class ViewRoute
{
    private static BorderPane root;

    public static void createViewRoute ()
    {
        createGui();

        Scene scene = new Scene(root, 800, 800);
        GuiController.start(scene, "Ruta actual");
    }

    private static void createGui()
    {
        root = new BorderPane();
        Button startButton = new Button();
        ImageView imageButton2 = new ImageView("icons/start_route.png");
        HBox hb = Components.createTop("Ruta actual");

        imageButton2.setFitWidth(25);
        imageButton2.setFitHeight(25);

        startButton.setGraphic(imageButton2);
        startButton.setAlignment(Pos.CENTER_RIGHT);
        startButton.setPrefSize(20, 20);
        startButton.getStyleClass().add("title-button");

        startButton.setOnMouseClicked(event -> CodeController.createRoute());

        hb.getChildren().add(startButton);

        root.setTop(hb);
        root.setCenter(createCenter());
    }

    public static GridPane createCenter()
    {
        GridPane grid = Components.createCenterMain();

        CodeController.createGridContentRoutes(grid);

        return grid;
    }
}
