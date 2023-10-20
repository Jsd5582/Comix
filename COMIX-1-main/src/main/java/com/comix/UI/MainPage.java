package com.comix.UI;

import com.comix.Commands.Command;
import com.comix.Commands.CommandStack;
import com.comix.PersonalCollection.PersonalCollection;
import com.comix.User.User;
import com.comix.model.Comic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class MainPage implements Page {
    private static final String TITLE_FORMAT = "Personal Collection (%s)";
    private static final String GUEST_MODE = "Guest Mode";
    private final Label collection;
    private User user;
    private final BorderPane layout;
    private final Stage primaryStage;
    private final Button add;
    private final Button slab;
    private final Button gradeButton;
    private final Button dbSearch;
    private final Button edit;
    private final Button Import;
    private final Button export;
    private final Button signComic;
    private final Button logout;
    private final Button undo;
    private final Button redo;
    private final Button remove;
    private final TableView<Comic> personal;
    private ObservableList<Comic> tableEntries;
    private final Label errorLabel;

    /***
     * Main page where personal collection is shown along with action buttons
     * @param primaryStage The main stage that the page is built on
     */
    public MainPage(Stage primaryStage) {
        user = null;
        this.primaryStage = primaryStage;

        layout = new BorderPane();
        layout.setPadding(new Insets(10, 20, 10, 20));

        // Creation of VBoxes and HBoxes to fill with the proper elements
        VBox rightBox      = new VBox();
        VBox leftBox       = new VBox();
        HBox midBox        = new HBox();
        HBox lowerBox      = new HBox();
        VBox upperBox      = new VBox();

        //Initialize the table
        personal = new TableView<>();
        tableEntries = FXCollections.emptyObservableList();
        personal.setItems(tableEntries);

        rightBox.setSpacing(20.0);
        leftBox.setSpacing(15.0);
        midBox.setSpacing(15.0);
        lowerBox.setSpacing(15.0);
        lowerBox.setAlignment(Pos.CENTER);
        upperBox.setAlignment(Pos.CENTER);

        layout.setStyle("-fx-background-color: BEIGE;");

        //Label to show the personal Collection of the user
        collection = new Label(String.format(TITLE_FORMAT, GUEST_MODE));
        Label pC         = new Label("Personal Collection Search");
        Label dB         = new Label("Database Search");
        Label uCommand   = new Label("Undo Action");
        Label rCommand   = new Label("Redo Action");
        errorLabel       = new Label("");

        errorLabel.setStyle("-fx-text-fill: RED");

        //All buttons are created
        add       = new Button("Add");
        edit      = new Button("Edit");
        remove = new Button("Remove");
        slab      = new Button("Slab");
        gradeButton = new Button("Grade");
        export    = new Button("Export");
        Import    = new Button("Import");
        Button pcSearch = new Button("P.C Search");
        dbSearch  = new Button("D.B Search");
        signComic = new Button("Sign Comic");
        undo      = new Button("Undo");
        redo      = new Button("Redo");
        logout    = new Button("Sign in");

        //Creation of the table columns which represent an attribute of the comic
        TableColumn<Comic, String> series      = new TableColumn<>("Series");
        TableColumn<Comic, String> issue       = new TableColumn<>("Issue");
        TableColumn<Comic, String> title       = new TableColumn<>("Full Title");
        TableColumn<Comic, String> publisher   = new TableColumn<>("Publisher");
        TableColumn<Comic, String> releaseDate = new TableColumn<>("Release Date");
        TableColumn<Comic, String> creators    = new TableColumn<>("Creators");
        TableColumn<Comic, Integer> grade      = new TableColumn<>("Grade");
        TableColumn<Comic, Double> value       = new TableColumn<>("Value");
        TableColumn<Comic, Boolean> isSlabbed  = new TableColumn<>("Slabbed");

        series.setCellValueFactory(new PropertyValueFactory<>("series"));
        issue.setCellValueFactory(new PropertyValueFactory<>("issue"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        publisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        releaseDate.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));
        creators.setCellValueFactory(new PropertyValueFactory<>("creators"));
        grade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        value.setCellValueFactory(new PropertyValueFactory<>("value"));
        isSlabbed.setCellValueFactory(new PropertyValueFactory<>("isSlabbed"));


        // Adds all columns to the table in the correct order
        // addAll() was throwing a warning, so use add() instead here
        personal.getColumns().add(series);
        personal.getColumns().add(issue);
        personal.getColumns().add(title);
        personal.getColumns().add(publisher);
        personal.getColumns().add(releaseDate);
        personal.getColumns().add(creators);
        personal.getColumns().add(grade);
        personal.getColumns().add(value);
        personal.getColumns().add(isSlabbed);

        //Adding all the children to the appropriate box
        midBox.getChildren().addAll(personal);
        rightBox.getChildren().addAll(add, edit, remove, slab, gradeButton, signComic);
        lowerBox.getChildren().addAll(export, Import, logout);
        leftBox.getChildren().addAll(pC,pcSearch,dB, dbSearch, uCommand,undo,rCommand,redo);
        upperBox.getChildren().addAll(collection, errorLabel);

        //Setting the boxes in their location in the border pane
        layout.setRight(rightBox);
        layout.setCenter(midBox);
        layout.setBottom(lowerBox);
        layout.setLeft(leftBox);
        layout.setTop(upperBox);

        undo.setOnAction(e -> {
            try {
                if (user == null) {
                    throw new IllegalAccessException("Must be signed in");
                }
                Command command = CommandStack.instance().removeLast();
                if (command == null) {
                    throw new IllegalArgumentException("No commands to undo.");
                }
                command.undo();
                refresh();
            } catch (Exception ex) {
                setErrorText(ex.getMessage());
            }
        });

        redo.setOnAction(e -> {
            try {
                if (user == null) {
                    throw new IllegalAccessException("Must be signed in");
                }
                Command command = CommandStack.instance().restore();
                if (command == null) {
                    throw new IllegalArgumentException("No commands to redo");
                }
                command.execute();
                refresh();
            } catch (Exception ex) {
                setErrorText(ex.getMessage());
            }
        });
    }

    public void refresh() {
        List<Comic> collection = PersonalCollection.getCollection(user.getUsername());
        tableEntries = FXCollections.observableList(collection);
        personal.setItems(tableEntries);
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            collection.setText(String.format(TITLE_FORMAT, user.getUsername()));
            logout.setText("Logout");
            refresh();
        } else {
            collection.setText(String.format(TITLE_FORMAT, GUEST_MODE));
            logout.setText("Sign in");
            tableEntries = FXCollections.emptyObservableList();
        }
        personal.setItems(tableEntries);
    }

    public Comic getCurrentSelection() {
        return personal.getSelectionModel().getSelectedItem();
    }

    public User getUser() {
        return this.user;
    }

    public void setErrorText(String text) {
        errorLabel.setText(text);
    }

    public void clearErrorText() {
        errorLabel.setText("");
    }

    private EventHandler<ActionEvent> pageSetHelper(Page page) {
        return e -> {
            this.clearErrorText();
            primaryStage.getScene().setRoot(page.getLayout());
        };
    }

    //Button actions made
    public void setAddButtonPage(Page page) {
        add.setOnAction(pageSetHelper(page));
    }

    public void setSlabButtonPage(Page page){
        slab.setOnAction(pageSetHelper(page));
    }

    public void setGradeButtonPage(Page page){
        gradeButton.setOnAction(pageSetHelper(page));
    }

    public void setDbSearchButtonPage(Page page) {
        dbSearch.setOnAction(pageSetHelper(page));
    }

    public void setEditButtonPage(EditPage page) {
        edit.setOnAction(e -> {
            Comic current = getCurrentSelection();
            if (current == null) {
                setErrorText("Must select a comic to edit.");
                return;
            }
            page.setCurrentComic(current);
            primaryStage.getScene().setRoot(page.getLayout());
            this.clearErrorText();
        });
    }

    public void setImportButtonPage(Page page) {
        Import.setOnAction(pageSetHelper(page));
    }

    public void setExportButtonPage(Page page) {
        export.setOnAction(pageSetHelper(page));
    }

    public void setSignComicButtonPage(Page page) {
        signComic.setOnAction(pageSetHelper(page));
    }

    public void setLogoutButtonPage(Page page) {
        logout.setOnAction(e -> {
            this.clearErrorText();
            primaryStage.getScene().setRoot(page.getLayout());
            this.setUser(null);
        });
    }

    public void setUndoButtonPage(Page page) {
        undo.setOnAction(pageSetHelper(page));
    }

    public void setRedoButtonPage(Page page) {
        redo.setOnAction(pageSetHelper(page));
    }

    public void setRemoveButtonPage(Page page) {
        remove.setOnAction(pageSetHelper(page));
    }



    @Override
    public Parent getLayout() {
        return layout;
    }
}
