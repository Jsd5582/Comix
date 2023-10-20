package com.comix.UI;

import com.comix.ComixApp;
import com.comix.Commands.Export;
import com.comix.User.User;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class ExportPage implements Page {
    private final GridPane layout;
    private final Button backButton;
    private final Button button;
    private final Stage primaryStage;

    /***
     * Allows the user to pick a file(JSON, CSV, XML) they'd like to export
     * @param primaryStage The main stage that the page is built on
     */
    public ExportPage(Stage primaryStage, ComixApp app) {
        this.primaryStage = primaryStage;

        layout = new GridPane();
        layout.setStyle("-fx-background-color: BEIGE;");

        //File Chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV file", "*.csv"));
        fileChooser.setInitialFileName("comics.csv");
        Stage stage = new Stage();
        button = new Button("Select File");
        button.setOnAction(e -> {
            try {
                User user = app.getUser();
                if (user == null) {
                    throw new IllegalAccessException("Must be signed in to do this action");
                }
                File selectedFile = fileChooser.showSaveDialog(stage);
                Export.exportCSV(user.getUsername(), selectedFile);
            } catch (Exception ex) {
                app.setMainPageErrorText(ex.getMessage());
            }
        });

        //Creation of the Label and back button
        Text Label = new Text("Choose a file you'd like to export");
        backButton = new Button("Back");

        //Adding components to the layout
        layout.setAlignment(Pos.CENTER);
        layout.add(Label, 0,0);
        layout.add(button,0,1);
        layout.add(backButton,0,2);
    }

    public void setBackButtonPage(Page page) {
        backButton.setOnAction(e -> primaryStage.getScene().setRoot(page.getLayout()));
    }

    @Override
    public Parent getLayout() {
        return layout;
    }
}
