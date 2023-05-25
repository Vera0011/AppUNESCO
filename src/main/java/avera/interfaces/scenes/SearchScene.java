package avera.interfaces.scenes;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SearchScene
{
    private static BorderPane root;

    public static void openSearchWindow()
    {
        Stage stage = new Stage();
        createGui(stage);

        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add(DescriptionScene.class.getResource("/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.getIcons().add(new Image("/icons/main.png"));
        stage.resizableProperty().setValue(false);
        stage.setTitle("Busqueda de lugares");
        stage.show();
    }

    private static void createGui(Stage stage)
    {
        root = new BorderPane();
        root.setTop(createTop(stage));
        root.setBottom(createBottom(stage));
    }

    private static HBox createTop(Stage stage)
    {
        HBox bottomPanel = new HBox();

        return bottomPanel;
    }

    private static HBox createBottom(Stage stage)
    {
        HBox hb = new HBox();
        return hb;
    }
}
