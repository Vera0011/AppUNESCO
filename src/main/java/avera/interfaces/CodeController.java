package avera.interfaces;

import avera.database.DatabaseManager;
import avera.interfaces.scenes.DescriptionScene;
import avera.interfaces.scenes.MainScene;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Code Controller for interactions (or events) of the Application
 *
 * @author Vera
 * */

public class CodeController
{
    private static Dotenv dotenv = Dotenv.load();

    /**
     * Open or close the right panel (options panel)
     *
     * @param rightPanel A VBox of the panel
     * @param root The main BorderPane (first child of the Scene)
     *
     * @see avera.interfaces.GuiController
     * */
    public static void displayRightPanel(VBox rightPanel, BorderPane root)
    {
        if(rightPanel == null || !rightPanel.isVisible())
        {
            rightPanel = MainScene.createRight();
            root.setRight(rightPanel);
            rightPanel.setVisible(true);

        }
        else
        {
            rightPanel.setVisible(false);
            root.setRight(null);
        }
    }

    /**
     * Creates all the grids displayed in the main Scene (in GridPane)
     *
     * @param grid The GridPane where the contents are located
     * @return GridPane Returns the same GridPane as the param, but modified
     *
     * @see avera.interfaces.GuiController
     * @see avera.database.DatabaseManager
     * */
    public static GridPane createGridContent(GridPane grid)
    {
        Iterator<Map.Entry<String, List<String>>> data = DatabaseManager.query("MATCH (n:Monument) RETURN n.name, n.description LIMIT 12", "SELECT").entrySet().iterator();
        int rowPosition = 0;

        while(data.hasNext())
        {
            Map.Entry<String, List<String>> entry = data.next();
            RowConstraints row = new RowConstraints();
            Label nameLabel = new Label(entry.getKey());
            HBox mainBox = new HBox();

            /*if(entry.getValue().get(0).isBlank()) descriptionLabel = new Label("Sin descripcion");
            else if (entry.getValue().get(0).length() < 5) descriptionLabel = new Label(entry.getValue().get(0).substring(1));
            else descriptionLabel = new Label(entry.getValue().get(0).substring(0, 30));*/

            Button addButton = new Button("Añadir");
            Button detailsButton = new Button("Ver detalles");
            HBox containerName = new HBox();
            HBox containerDescription = new HBox();
            HBox containerButton = new HBox();

            row.setPrefHeight(50);

            containerName.getStyleClass().addAll("background-items-grid", "background-name-grid");
            containerName.getChildren().add(nameLabel);
            containerName.setAlignment(Pos.CENTER_LEFT);
            containerName.setMaxSize(300, 300);
            containerName.setPadding(new Insets(10));
            HBox.setHgrow(containerName, Priority.ALWAYS);

            containerDescription.getStyleClass().addAll("background-items-grid", "background-description-grid");
            containerDescription.getChildren().add(detailsButton);
            containerDescription.setAlignment(Pos.CENTER);
            containerDescription.setMaxSize(300, 300);
            containerDescription.setPadding(new Insets(10));
            HBox.setHgrow(containerButton, Priority.ALWAYS);
            HBox.setHgrow(detailsButton, Priority.ALWAYS);

            detailsButton.setOnMouseClicked(event -> DescriptionScene.openDescriptionWindow(entry.getKey()));

            detailsButton.getStyleClass().add("btn");
            detailsButton.setMaxSize(Double.MAX_VALUE, 10);

            containerButton.getStyleClass().addAll("background-items-grid", "background-button-grid");
            containerButton.getChildren().add(addButton);
            containerButton.setAlignment(Pos.CENTER);
            containerButton.setMaxSize(300, 300);
            containerButton.setPadding(new Insets(10));
            HBox.setHgrow(containerButton, Priority.ALWAYS);
            HBox.setHgrow(addButton, Priority.ALWAYS);

            grid.getRowConstraints().add(row);

            addButton.getStyleClass().add("btn");
            addButton.setMaxSize(Double.MAX_VALUE, 10);

            mainBox.getChildren().addAll(containerName, containerDescription, containerButton);
            mainBox.setAlignment(Pos.CENTER);

            grid.add(mainBox, 0, rowPosition, 3, 1);

            rowPosition++;
        }

        return grid;
    }

    /**
     * Start the connection to the database
     *
     * @see avera.database.DatabaseManager
     * */
    public static void initiateDatabase()
    {
        DatabaseManager.startConnection(dotenv.get("NEO4J_URI"), dotenv.get("NEO4J_USERNAME"), dotenv.get("NEO4J_PASSWORD"));
    }
}
