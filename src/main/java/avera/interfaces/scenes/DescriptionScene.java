package avera.interfaces.scenes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DescriptionScene
{
    public static void openDescriptionWindow(String dataEntryKey)
    {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 500, 500);
        Stage stage = new Stage();
        stage.setScene( scene );

        stage.resizableProperty().setValue(false);
        stage.setTitle("Descripcion del lugar");

        stage.show();
    }
}
