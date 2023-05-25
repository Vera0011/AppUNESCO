package avera.interfaces;

import avera.code.ListMonuments;
import avera.interfaces.scenes.MessageDisplayer;
import avera.interfaces.scenes.ViewRoute;
import avera.database.DatabaseManager;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

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
        Object[] data;

        if(action.equalsIgnoreCase("normal")) data = DatabaseManager.query("MATCH (n:Monument) RETURN n.name LIMIT 12", "SELECT");
        else if(action.equalsIgnoreCase("sum"))
        {
            SKIP += 12;
            data = DatabaseManager.query("MATCH (n:Monument) RETURN n.name SKIP " + SKIP + " LIMIT 12", "SELECT");
        }
        else
        {
            SKIP -= 12;
            data = DatabaseManager.query("MATCH (n:Monument) RETURN n.name SKIP " + SKIP + " LIMIT 12", "SELECT");
        }

        int rowPosition = 0;

        while(data[0] != null && ((Result) data[0]).hasNext())
        {
            Components.createGridContent(grid, rowPosition, ((Result) data[0]).next().values().get(0).asString(), "Añadir", "Ver detalles");
            rowPosition++;
            lengthIt++;
        }

        finalPage = lengthIt < 12;

        ((Session) data[1]).close();
    }

    public static void createGridContentRoutes(GridPane grid)
    {
        if(ListMonuments.getRoute() != null)
        {
            int rowPosition = 0;

            for (String item : ListMonuments.getRoute())
            {
                Components.createGridContent(grid, rowPosition, item, "Eliminar", "");
                ((HBox) grid.getChildren().get(rowPosition)).getChildren().remove(1);
                HBox hbox = ((HBox) grid.getChildren().get(rowPosition));
                Node removeButton = hbox.getChildren().get(1);

                removeButton.setOnMouseClicked(null);
                removeButton.setOnMouseClicked(event -> CodeController.removeRoute(item));

                rowPosition++;
            }
        }
        else
        {
            Label lb = new Label("Ruta actual vacia");
            RowConstraints row = new RowConstraints();

            lb.setAlignment(Pos.BOTTOM_CENTER);

            grid.getRowConstraints().add(row);
            grid.add(lb, 1, 1, 2, 2);
        }
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
        Object[] result = DatabaseManager.query("MATCH (n:Monument) WHERE n.name = '" + dataEntryKey + "' RETURN n.name, n.description, n.category, " +
                "n.httplink, n.region, n.state", "SELECT");
        String[] labels = { "Nombre completo:", "Descripcion", "Categoria", "Enlace a web UNESCO", "Region", "Estado(s)" };

        int rowPos = 0;

        while(result[0] != null && ((Result) result[0]).hasNext())
        {
            Record dataRecord = ((Result) result[0]).next();
            String[] dataValues = { dataRecord.values().get(0).asString(), dataRecord.values().get(1).asString(), dataRecord.values().get(2).asString(),
                    dataRecord.values().get(3).asString(), dataRecord.values().get(4).asString(), dataRecord.values().get(5).asString()};

            for (int i = 0; i < dataValues.length; i++)
            {
                TextArea data;

                if(dataValues[i].isBlank()) data = new TextArea("Dato no encontrado");
                else data = new TextArea(dataValues[i]);

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

        ((Session) result[1]).close();

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

    /*public static void searchSpecificMonument()
    {

    }*/

    public static void removeRoute(String entryName)
    {
        ListMonuments.removeRoute(entryName);
        ViewRoute.createViewRoute();
    }

    public static void getlRoute()
    {
        ViewRoute.createViewRoute();
    }

    public static void addRoute(String name, Stage stage)
    {
        ListMonuments.addMonument(name);
        closeDescriptionWindow(stage);
    }

    public static void addRoute(String name)
    {
        ListMonuments.addMonument(name);
    }

    public static void createMainGui()
    {
        GuiController.start(GuiController.createScene(), "Listado del Patrimonio de la UNESCO");
    }

    public static void createRoute ()
    {
        LinkedList<String> namesMonuments = new LinkedList<>(ListMonuments.getRoute());
        ListIterator<String> iteratorNode = new ArrayList<>(ListMonuments.getRoute()).listIterator();
        LinkedList<Double> listDistances = new LinkedList<>();
        LinkedList<String> finalResult = new LinkedList<>();

        iteratorNode.add(namesMonuments.get(0));
        finalResult.add(namesMonuments.get(0));
        namesMonuments.remove(0);

        while(iteratorNode.hasNext())
        {
            String nodeSearched = iteratorNode.next();

            for (int i = 0; i < namesMonuments.size(); i++)
            {
                if(!nodeSearched.equals(namesMonuments.get(i)))
                {
                    Object[] data = DatabaseManager.query("MATCH (n1:Monument)-[r:DISTANCE]-(n2:Monument) WHERE n1.name = '" + nodeSearched +
                            "' AND n2.name = '" + namesMonuments.get(i) + "' RETURN r.km", "SELECT");

                    if(data[0] != null && ((Result) data[0]).hasNext()) listDistances.add(((Result) data[0]).next().values().get(0).asDouble());

                    ((Session) data[1]).close();
                }
            }

            if(listDistances.size() != 0)
            {
                int lessDistance = CodeController.getLessDistance(listDistances);

                iteratorNode.add(namesMonuments.get(lessDistance));
                finalResult.add(namesMonuments.get(lessDistance));

                namesMonuments.remove(lessDistance);

                listDistances.clear();
            }
        }

        StringBuilder finalMessage = new StringBuilder();
        int counter = 1;

        for (String i : finalResult)
        {
            finalMessage.append(counter).append(" - ").append(i).append("\n");
            counter++;
        }

        MessageDisplayer.displayMessage("Ruta realizada",
                "La ruta mas corta es yendo en el siguiente orden:\n\n", Alert.AlertType.INFORMATION, finalMessage.toString());
    }

    private static int getLessDistance(List<Double> listDistances)
    {
        double min = listDistances.get(0);
        int position = 0;

        for (int i = 1; i < listDistances.size(); i++)
        {
            if(listDistances.get(i) < min)
            {
                min = listDistances.get(i);
                position = i;
            }
        }

        return position;
    }
}
