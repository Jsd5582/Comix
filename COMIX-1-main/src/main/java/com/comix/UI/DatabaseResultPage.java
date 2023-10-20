package com.comix.UI;

import com.comix.ComixApp;
import com.comix.Commands.AddCommand;
import com.comix.Commands.Command;
import com.comix.Commands.CommandStack;
import com.comix.User.User;
import com.comix.model.Comic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class DatabaseResultPage implements Page {
    private final VBox layout;
    private final TableView<Comic> resultsView;
    private ObservableList<Comic> results;
    private Page backPage;

    public DatabaseResultPage(Stage primaryStage, ComixApp app) {
        layout = new VBox();
        resultsView = new TableView<>();
        results = FXCollections.emptyObservableList();
        resultsView.setItems(results);

        String buttonStyle = "-fx-font: normal normal 18px Arial; -fx-text-fill: WHITE; -fx-background-color: ";
        layout.setStyle("-fx-padding: 10px");
        layout.setSpacing(10);

        Label pageTitle = new Label("Search Results");
        Label errorText = new Label("");
        HBox bottom = new HBox();
        Button backButton = new Button("Back");
        Button addButton = new Button("Add to collection");

        pageTitle.setStyle("-fx-font: normal bold 36px Arial;");
        pageTitle.setAlignment(Pos.CENTER);
        errorText.setStyle("-fx-text-fill: RED");

        bottom.setSpacing(10);

        backButton.setStyle(buttonStyle + "BLUE;");
        addButton.setStyle(buttonStyle + "GREEN;");

        TableColumn<Comic, String> series      = new TableColumn<>("Series");
        TableColumn<Comic, String> issue       = new TableColumn<>("Issue");
        TableColumn<Comic, String> title       = new TableColumn<>("Full Title");
        TableColumn<Comic, String> publisher   = new TableColumn<>("Publisher");
        TableColumn<Comic, String> releaseDate = new TableColumn<>("Release Date");
        TableColumn<Comic, String> creators    = new TableColumn<>("Creators");

        series.setCellValueFactory(new PropertyValueFactory<>("series"));
        issue.setCellValueFactory(new PropertyValueFactory<>("issue"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        publisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        releaseDate.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));
        creators.setCellValueFactory(new PropertyValueFactory<>("creators"));

        resultsView.getColumns().add(series);
        resultsView.getColumns().add(issue);
        resultsView.getColumns().add(title);
        resultsView.getColumns().add(publisher);
        resultsView.getColumns().add(releaseDate);
        resultsView.getColumns().add(creators);

        bottom.getChildren().addAll(backButton, addButton);
        bottom.setAlignment(Pos.CENTER);
        HBox.setHgrow(backButton, Priority.ALWAYS);
        HBox.setHgrow(addButton, Priority.ALWAYS);
        layout.getChildren().addAll(pageTitle, errorText, resultsView, bottom);
        layout.setAlignment(Pos.CENTER);

        backButton.setOnAction(e -> primaryStage.getScene().setRoot(backPage.getLayout()));

        Command command = new AddCommand();

        addButton.setOnAction(e -> {
            try {
                User user = app.getUser();
                if (user == null) {
                    throw new IllegalAccessException("Must be signed in to add to personal collection");
                }

                Comic comic = resultsView.getSelectionModel().getSelectedItem();

                if (comic == null) {
                    throw new IllegalArgumentException("Must select comic to add to personal collection");
                }

                command.setUser(user);
                command.setComic(comic);
                command.execute();
                CommandStack commandStack = CommandStack.instance();
                commandStack.clearRestore();
                commandStack.add(command);
                app.refreshCollection();
                primaryStage.getScene().setRoot(backPage.getLayout());
                errorText.setText("");
            } catch (Exception ex) {
                errorText.setText(ex.getMessage());
            }
        });
    }

    public void setResults(List<Comic> comics) {
        results = FXCollections.observableList(comics);
        resultsView.setItems(results);
    }

    @Override
    public Parent getLayout() {
        return layout;
    }

    public void setBackPage(Page backPage) {
        this.backPage = backPage;
    }
}
