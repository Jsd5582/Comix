package com.comix.UI;

import com.comix.ComixApp;
import com.comix.db.DBConnection;
import com.comix.db.ExactSearch;
import com.comix.db.PartialSearch;
import com.comix.model.Comic;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SearchPage implements Page {
    private static final int PREF_WIDTH = 150;
    private static final String DEFAULT_FIELD = "Title";
    private static final String DEFAULT_TYPE = "Partial";

    private final VBox layout;
    private final Button backButton;
    private final Button submitButton;
    private final TextField searchField;
    private final ChoiceBox<String> searchByFields;
    private final ChoiceBox<String> exactOrPartial;
    private final Stage primaryStage;
    private Page backPage;
    private DatabaseResultPage resultPage;

    /***
     * This page allows a user to search for a comic by selecting either an exact or partial search
     * @param primaryStage The main stage that the page is built on
     */
    public SearchPage(Stage primaryStage, ComixApp app) {
        this.primaryStage = primaryStage;

        layout = new VBox();
        String buttonStyle = "-fx-font: normal normal 12px Arial; -fx-text-fill: WHITE; -fx-background-color: ";

        // Top bar with a back button
        HBox topBar = new HBox();
        backButton = new Button("< Back");
        backButton.setCancelButton(true);
        backButton.setStyle(buttonStyle + "MIDNIGHTBLUE");
        topBar.getChildren().add(backButton);
        topBar.setStyle("-fx-background-color: LIGHTBLUE; -fx-padding: 10px");

        Label pageTitle = new Label("Search Database");
        pageTitle.setStyle("-fx-font: normal bold 36px Arial; -fx-padding: 20px;");
        pageTitle.setAlignment(Pos.CENTER);

        // Search field and buttons
        Text searchFieldText = new Text("Search: ");
        searchField = new TextField();
        searchField.setPrefWidth(PREF_WIDTH);
        Text searchByFieldText = new Text("Search by: ");
        Text exactOrPartialText = new Text("Search type: ");
        searchByFields = new ChoiceBox<>();
        exactOrPartial = new ChoiceBox<>();
        searchByFields.setPrefWidth(PREF_WIDTH);
        exactOrPartial.setPrefWidth(PREF_WIDTH);
        submitButton = new Button("Submit");
        Button clearButton = new Button("Clear");
        submitButton.setStyle(buttonStyle + "GREEN");
        submitButton.setPrefWidth(PREF_WIDTH);
        clearButton.setStyle(buttonStyle + "RED");
        clearButton.setOnAction(e -> searchField.clear());
        clearButton.setPrefWidth(PREF_WIDTH);

        //Styling
        String textFontStyle = "-fx-font: normal normal 16px Arial";
        searchByFieldText.setStyle(textFontStyle);
        searchFieldText.setStyle(textFontStyle);
        exactOrPartialText.setStyle(textFontStyle);

        searchByFields.getItems().addAll(DEFAULT_FIELD, "Issue", "Series", "Publisher", "Release Date", "Creators");
        searchByFields.setValue(DEFAULT_FIELD);

        exactOrPartial.getItems().addAll(DEFAULT_TYPE, "Exact");
        exactOrPartial.setValue(DEFAULT_TYPE);

        // Search field grid pane
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.add(searchFieldText, 0, 0);
        pane.add(searchField, 1, 0);
        pane.add(searchByFieldText, 0, 1);
        pane.add(searchByFields, 1, 1);
        pane.add(exactOrPartialText, 0, 2);
        pane.add(exactOrPartial, 1, 2);
        pane.add(clearButton, 0, 3);
        pane.add(submitButton, 1, 3);

        pane.setStyle("-fx-padding: 10px");

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setHalignment(HPos.RIGHT);
        pane.getColumnConstraints().add(col0);

        pane.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(topBar, pageTitle, pane);
        layout.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(pane, Priority.ALWAYS);

        backButton.setOnAction(e -> {
            primaryStage.getScene().setRoot(backPage.getLayout());
            clear();
        });

        DBConnection dbConnection = new DBConnection(new PartialSearch());

        submitButton.setOnAction(e -> {
            try {
                String parameter = searchField.getText();

                if (parameter.equals("")) {
                    throw new IllegalArgumentException("Must enter a search term");
                }

                if (exactOrPartial.getValue().equals("Exact")) {
                    dbConnection.setSearch(new ExactSearch());
                } else {
                    dbConnection.setSearch(new PartialSearch());
                }

                List<Comic> results = new ArrayList<>();

                switch (searchByFields.getValue()) {
                    case "Title" -> results = dbConnection.searchTitle(parameter);
                    case "Issue" -> results = dbConnection.searchIssue(parameter);
                    case "Series" -> results = dbConnection.searchSeries(parameter);
                    case "Publisher" -> results = dbConnection.searchPublisher(parameter);
                    case "Release Date" -> results = dbConnection.searchReleaseDate(parameter);
                    case "Creators" -> results = dbConnection.searchCreators(parameter);
                }

                resultPage.setResults(results);
                primaryStage.getScene().setRoot(resultPage.getLayout());
                clear();
            } catch (Exception ex) {
                app.setMainPageErrorText(ex.getMessage());
                primaryStage.getScene().setRoot(backPage.getLayout());
                clear();
            }
        });
    }

    private void clear() {
        searchField.clear();
        searchByFields.setValue(DEFAULT_FIELD);
        exactOrPartial.setValue(DEFAULT_TYPE);
    }

    public void setBackPage(Page page) {
        this.backPage = page;
    }

    @Override
    public Parent getLayout() {
        return layout;
    }

    public void setResultPage(DatabaseResultPage resultPage) {
        this.resultPage = resultPage;
    }
}
