package avera.interfaces;

import avera.database.DatabaseManager;
import avera.interfaces.scenes.DescriptionScene;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Code Controller for interactions (or events) of the Application
 *
 * @author Vera
 * */

public final class CodeController
{
    private static Dotenv dotenv = Dotenv.load();
    private static int SKIP = 0;
    private static int indexPage = 0;
    private static int indexPreviewPage = -1;
    private static boolean finalPage = false;

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
            rightPanel = GuiController.createRight();
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
     * @param action The action to make (depending on the scene location and index)
     *
     * @see avera.interfaces.GuiController
     * @see avera.database.DatabaseManager
     * */
    public static void createGridContentMainScene(GridPane grid, String action)
    {
        int lengthIt = 0;
        Iterator<String> data;

        if(action.equalsIgnoreCase("normal")) data = DatabaseManager.query("MATCH (n:Monument) RETURN n.name LIMIT 12", "SELECT").keySet().iterator();
        else if(action.equalsIgnoreCase("sum"))
        {
            SKIP += 12;
            data = DatabaseManager.query("MATCH (n:Monument) RETURN n.name SKIP " + SKIP + " LIMIT 12", "SELECT").keySet().iterator();
        }
        else
        {
            SKIP -= 12;
            data = DatabaseManager.query("MATCH (n:Monument) RETURN n.name SKIP " + SKIP + " LIMIT 12", "SELECT").keySet().iterator();
        }

        int rowPosition = 0;

        while(data.hasNext())
        {
            String entry = data.next();
            RowConstraints row = new RowConstraints();
            Label nameLabel = new Label(entry);
            HBox mainBox = new HBox();
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

            detailsButton.setOnMouseClicked(event -> DescriptionScene.openDescriptionWindow(entry));

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
            lengthIt++;
        }

        finalPage = lengthIt < 12;
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

    /**
     * Creates all the grids displayed in the Description Scene (in GridPane)
     *
     * @param grid The GridPane where the contents are located
     * @param dataEntryKey The name of the monument
     * @return GridPane Returns the same GridPane as the param, but modified
     *
     * @see avera.interfaces.GuiController
     * @see avera.database.DatabaseManager
     * */
    public static GridPane createGridContentDescriptionScene(GridPane grid, String dataEntryKey)
    {
        Iterator<Map.Entry<String, List<String>>> it = DatabaseManager.query("MATCH (n:Monument) WHERE n.name = '" + dataEntryKey + "' RETURN " +
                "n.name, n.description, n.category, n.httplink, n.region, n.state", "SELECT").entrySet().iterator();
        String[] labels = { "Nombre completo:", "Descripcion", "Categoria", "Enlace a web UNESCO", "Region", "Estado(s)" };
        int rowPos = 0;

        while(it.hasNext())
        {
            Map.Entry<String, List<String>> entry = it.next();
            List<String> list = entry.getValue();

            list.add(0, entry.getKey());

            for (int i = 0; i < list.size(); i++)
            {
                TextArea data;

                if(list.get(i).isBlank()) data = new TextArea("Dato no encontrado");
                else data = new TextArea(list.get(i));

                RowConstraints row = new RowConstraints();
                Label value = new Label(labels[i]);
                HBox entryContainer = new HBox();
                HBox dataContainer = new HBox();
                HBox mainBox = new HBox();
                Region region = new Region();

                row.setPrefHeight(100);

                value.setWrapText(true);
                data.setWrapText(true);

                data.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                data.getStyleClass().add("text-area-grid-description");
                data.setEditable(false);

                value.setTextAlignment(TextAlignment.LEFT);

                entryContainer.getChildren().add(value);
                dataContainer.getChildren().add(data);

                entryContainer.setAlignment(Pos.CENTER_LEFT);
                entryContainer.setPadding(new Insets(10));

                dataContainer.setMaxSize(300, 100);
                dataContainer.setAlignment(Pos.CENTER_RIGHT);
                dataContainer.setPadding(new Insets(10));

                HBox.setHgrow(mainBox, Priority.ALWAYS);
                HBox.setHgrow(region, Priority.ALWAYS);
                mainBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                mainBox.getChildren().addAll(entryContainer, region, dataContainer);
                mainBox.getStyleClass().add("background-grid-description");

                grid.getRowConstraints().add(row);
                grid.add(mainBox, 0, rowPos, 2, 1);

                rowPos++;
            }
        }

        return grid;
    }

    /**
     * Close the description window
     *
     * @param stage The Stage instance (basically, the window)
     * @see avera.interfaces.scenes.DescriptionScene
     * */
    public static void closeDescriptionWindow(Stage stage)
    {
        stage.close();
        stage.hide();
    }

    /**
     * Get the pagination index and the previous index (for display control)
     *
     * @return Array of ints
     * */
    public static int[] getIndex()
    {
        return new int[]{ indexPage, indexPreviewPage };
    }

    /**
     * Status of the pagination (if there are no more monuments to visit)
     *
     * @return Boolean
     * */
    public static boolean getFinalPage()
    {
        return finalPage;
    }

    /**
     * Increments the index of the pagination
     * */
    public static void nextPage()
    {
        indexPreviewPage = indexPage;
        indexPage++;
        GuiController.updateScene();
    }

    /**
     * Reduces the index of the pagination
     * */
    public static void previousPage()
    {
        indexPreviewPage = indexPage;
        indexPage--;
        if(indexPage < 0) indexPage = 0;
        GuiController.updateScene();
    }
}
