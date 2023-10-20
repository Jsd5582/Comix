package com.comix.UI;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.File;


public class ImportPage implements Page {
    private final GridPane layout;
    private final Button backButton;
    private final Button button;
    private final Stage primaryStage;

    /***
     * Allows the user to pick a file(JSON, CSV, XML) they'd like to import
     * @param primaryStage The main stage that the page is built on
     */
    public ImportPage(Stage primaryStage){
        this.primaryStage = primaryStage;

        layout = new GridPane();
        layout.setStyle("-fx-background-color: BEIGE;");

        //Filechooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Data files", "*.csv", "*.xml", "*.json"));
        Stage stage = new Stage();
        button = new Button("Select File");
        button.setOnAction(e -> {
            File selectedFile;
            selectedFile = fileChooser.showOpenDialog(stage);
        });

        //Adding label and back button
        Text Label = new Text("Choose a file you'd like to import");
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
