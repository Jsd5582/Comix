package com.comix.UI;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;

public class SignComicPage implements Page {
    private final GridPane layout;
    private final Button backButton;
    private final Stage primaryStage;

    /***
     * Allows the user to add signatures to a comic to increase its value
     * @param primaryStage The main stage that the page is built on
     */
    public SignComicPage(Stage primaryStage) {
        this.primaryStage = primaryStage;

        layout = new GridPane();

        //Creation of all buttons
        backButton = new Button("Back");
        Button submit = new Button("Submit");
        String buttonStyle = "-fx-font: normal normal 12px Arial; -fx-text-fill: WHITE; -fx-background-color: ";

        //Styling
        submit.setStyle(buttonStyle +"GREEN");
        backButton.setStyle(buttonStyle + "RED");
        layout.setStyle("-fx-background-color: BEIGE;");

        //Prompting user for an answer for the amount of signatures
        Text prompt = new Text("Amount of signatures: ");
        TextField answer = new TextField();


        //Adding components to the layout
        layout.add(prompt,0,0);
        layout.add(answer,1,0);
        layout.add(backButton,0,1);
        layout.add(submit,1,1);

        layout.setAlignment(Pos.CENTER);
    }

    public void setBackButtonPage(Page page) {
        backButton.setOnAction(e -> primaryStage.getScene().setRoot(page.getLayout()));
    }


    @Override
    public Parent getLayout() {
        return layout;
    }
}
