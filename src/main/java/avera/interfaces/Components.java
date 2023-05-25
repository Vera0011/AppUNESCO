package avera.interfaces;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import avera.interfaces.scenes.DescriptionScene;

/**
 * This class represents the builder for the components (top of Application, etc)
 *
 * @author Vera
 * */
public final class Components
{
    public static HBox createTop (String title)
    {
        HBox box = new HBox();
        Button fowardButton = new Button();
        Label labelTitle = new Label(title);
        ImageView imageButton = new ImageView("icons/fowardPage.png");

        imageButton.setFitWidth(25);
        imageButton.setFitHeight(25);

        fowardButton.setGraphic(imageButton);
        fowardButton.setAlignment(Pos.CENTER_LEFT);
        fowardButton.setPrefSize(20, 20);
        fowardButton.getStyleClass().add("title-button");

        /* Event Handler */
        fowardButton.setOnMouseClicked(event -> CodeController.createMainGui());

        labelTitle.getStyleClass().add("title");
        labelTitle.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(labelTitle, Priority.ALWAYS);
        labelTitle.setAlignment(Pos.CENTER);
        labelTitle.setPadding(new Insets(10));

        box.getChildren().addAll(fowardButton, labelTitle);
        box.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(box, Priority.ALWAYS);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(5));
        box.getStyleClass().add("title-hbox");

        return box;
    }

    public static HBox createBottomMain()
    {
        Button btnSearch = new Button("Buscar");
        Button btnNextPage = new Button();
        ImageView imageButton = new ImageView("/icons/nextPage.png");
        Button btnPreviousPage = new Button();
        ImageView imageButton2 = new ImageView("/icons/fowardPage.png");
        HBox bottomPanel = new HBox();

        imageButton.setFitWidth(25);
        imageButton.setFitHeight(25);
        imageButton2.setFitHeight(25);
        imageButton2.setFitHeight(25);

        btnSearch.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnSearch.getStyleClass().add("btn");

        btnNextPage.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnNextPage.getStyleClass().add("btn");
        btnNextPage.setGraphic(imageButton);

        btnPreviousPage.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnPreviousPage.getStyleClass().add("btn");
        btnPreviousPage.setGraphic(imageButton2);

        btnNextPage.setOnMouseClicked(event -> CodeController.nextPage());
        btnPreviousPage.setOnMouseClicked(event -> CodeController.previousPage());
        //btnSearch.setOnMouseClicked(event -> CodeController.searchSpecificMonument());

        bottomPanel.getStyleClass().add("btn-search-box");
        bottomPanel.setSpacing(5);
        bottomPanel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        bottomPanel.setPadding(new Insets(10, 10, 10, 10));
        HBox.setHgrow(btnSearch, Priority.ALWAYS);
        HBox.setHgrow(bottomPanel, Priority.ALWAYS);

        bottomPanel.getChildren().addAll(btnPreviousPage, btnSearch, btnNextPage);

        return bottomPanel;
    }

    public static GridPane createCenterMain()
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

        return centerPanel;
    }

    public static void createGridContent(GridPane grid, int rowPosition, String data, String labelButton, String labelButton2)
    {
        RowConstraints row = new RowConstraints();
        Label nameLabel = new Label(data);
        HBox mainBox = new HBox();
        Button addButton = new Button(labelButton);
        Button detailsButton = new Button(labelButton2);
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

        detailsButton.setOnMouseClicked(event -> DescriptionScene.openDescriptionWindow(data));
        addButton.setOnMouseClicked(event -> CodeController.addRoute(data));

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
    }
}
