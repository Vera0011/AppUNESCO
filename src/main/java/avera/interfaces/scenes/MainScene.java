package avera.interfaces.scenes;

import avera.interfaces.CodeController;
import avera.interfaces.GuiController;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * Creator of the main Scene
 *
 * @author Vera
 * */

public class MainScene extends GuiController
{
    private BorderPane mainPanel;
    private static VBox rightPanel;

    /**
     * Scene creator for the GUI
     *
     * @return Returns a BorderPane to include in the Scene of the window
     * */
    public BorderPane createGui()
    {
        this.mainPanel = new BorderPane();

        this.mainPanel.setTop(this.createTop());
        this.mainPanel.setCenter(this.createCenter());
        this.mainPanel.setBottom(this.createBottom());

        return this.mainPanel;
    }

    /**
     * Create the top contents of the Scene
     *
     * @return A HBOX to include in the Scene
     * */
    protected HBox createTop()
    {
        HBox topPanel = new HBox();
        Button displayContent = new Button();
        Label title = new Label("Lugares Disponibles");
        ImageView imageButton = new ImageView("/icons/displayContent_modified.png");

        imageButton.setFitWidth(25);
        imageButton.setFitHeight(25);

        displayContent.setGraphic(imageButton);
        displayContent.setAlignment(Pos.CENTER_RIGHT);
        displayContent.setPrefSize(20, 20);
        displayContent.getStyleClass().add("title-button");

        /* Event Handler */
        displayContent.setOnMouseClicked(event -> CodeController.displayRightPanel(rightPanel, this.mainPanel));

        title.getStyleClass().add("title");
        title.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setAlignment(Pos.CENTER);
        title.setPadding(new Insets(10));

        topPanel.getChildren().addAll(title, displayContent);
        topPanel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(topPanel, Priority.ALWAYS);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setPadding(new Insets(5));
        topPanel.getStyleClass().add("title-hbox");

        return topPanel;
    }

    /**
     * Create the center contents of the Scene
     *
     * @return A GridPane to include in the Scene
     * */
    protected GridPane createCenter()
    {
        GridPane centerPanel = new GridPane();

        /* Grid Styling */
        centerPanel.setAlignment(Pos.TOP_CENTER);
        centerPanel.setVgap(5);
        centerPanel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        centerPanel.setPadding(new Insets(10));

        /* Creating Constraints */
        ColumnConstraints nameCol = new ColumnConstraints();
        ColumnConstraints directionColumn = new ColumnConstraints();
        ColumnConstraints addColumn = new ColumnConstraints();

        nameCol.setHalignment(HPos.CENTER);
        nameCol.setHgrow(Priority.ALWAYS);

        directionColumn.setHgrow(Priority.ALWAYS);
        directionColumn.setHalignment(HPos.CENTER);

        addColumn.setHgrow(Priority.ALWAYS);
        addColumn.setHalignment(HPos.CENTER);

        centerPanel.getColumnConstraints().addAll(nameCol, directionColumn, addColumn);

        return CodeController.createGridContent(centerPanel);
    }

    /**
     * Create the bottom contents of the Scene
     *
     * @return A HBOX to include in the Scene
     * */
    protected HBox createBottom()
    {
        HBox bottomPanel = new HBox();
        Button btnSearch = new Button("Buscar");
        Button btnNextPage = new Button();
        ImageView imageButton = new ImageView("/icons/nextPage.png");

        imageButton.setFitWidth(25);
        imageButton.setFitHeight(25);

        btnSearch.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnSearch.getStyleClass().add("btn");

        btnNextPage.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnNextPage.getStyleClass().add("btn");
        btnNextPage.setGraphic(imageButton);

        bottomPanel.getChildren().addAll(btnSearch, btnNextPage);
        bottomPanel.getStyleClass().add("btn-search-box");
        bottomPanel.setSpacing(5);
        bottomPanel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        bottomPanel.setPadding(new Insets(10, 10, 10, 10));
        HBox.setHgrow(btnSearch, Priority.ALWAYS);
        HBox.setHgrow(bottomPanel, Priority.ALWAYS);

        return bottomPanel;
    }

    /**
     * Create the right contents of the Scene (options bar)
     *
     * @return A VBox to include in the Scene
     * */
    public static VBox createRight()
    {
        rightPanel = new VBox();
        Button verRuta = new Button("Ver ruta actual");
        Button verRutasAntiguas = new Button("Ver antiguas rutas");
        Button searchMonumentos = new Button("Buscar");

        verRuta.setMaxSize(Double.MAX_VALUE, 50);
        verRuta.getStyleClass().add("btn");
        verRutasAntiguas.setMaxSize(Double.MAX_VALUE, 50);
        verRutasAntiguas.getStyleClass().add("btn");
        searchMonumentos.setMaxSize(Double.MAX_VALUE, 50);
        searchMonumentos.getStyleClass().add("btn");

        VBox.setVgrow(rightPanel, Priority.ALWAYS);
        VBox.setVgrow(verRuta, Priority.ALWAYS);
        VBox.setVgrow(verRutasAntiguas, Priority.ALWAYS);
        VBox.setVgrow(searchMonumentos, Priority.ALWAYS);

        rightPanel.setSpacing(15);
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setPadding(new Insets(5));
        rightPanel.setPrefSize(150, 150);
        rightPanel.getChildren().addAll(verRuta, verRutasAntiguas, searchMonumentos);
        rightPanel.getStyleClass().add("right-panel");

        return rightPanel;
    }
}
