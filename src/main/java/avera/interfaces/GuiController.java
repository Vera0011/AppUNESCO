package avera.interfaces;

import avera.interfaces.scenes.MainScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Creation of the main Application
 *
 * @author Vera
 * */

public class GuiController extends Application
{
    private BorderPane root;

    /**
     * Creating Application and main Stage
     *
     * @param stage Stage of the main Application
     * */
    @Override
    public void start(Stage stage)
    {
        CodeController.initiateDatabase();
        this.root = new MainScene().createGui();
        this.root.getStyleClass().add("root");

        Scene scene = new Scene(root, 800, 800);
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
}
