package com.comix.UI;

import com.comix.ComixApp;
import com.comix.User.ExistingUser;
import com.comix.User.NewUser;
import com.comix.User.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginPage implements Page {
    private final GridPane layout;
    private final Stage primaryStage;
    private final TextField usernameField;
    private Page backPage;

    /***
     * Login page for either new or existing users to sign in
     * @param primaryStage The main stage that the page is built on
     */
    public LoginPage(Stage primaryStage, ComixApp app) {
        this.primaryStage = primaryStage;

        //texts for username and password
        Text userText          = new Text("Username");
        usernameField = new TextField();

        //Creation of all buttons
        Button submitButton = new Button("Submit");
        Button clearButton     = new Button("Clear");
        Button newUser         = new Button("Create User");

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(600, 400);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        //Align all grid pane
        gridPane.setAlignment(Pos.CENTER);

        //Adding the elements to the gridPane in their desired location
        gridPane.add(userText, 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(submitButton, 0, 1);
        gridPane.add(clearButton, 1, 1);
        gridPane.add(newUser, 2, 1);

        //Styling of text fields adn buttons
        submitButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        clearButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        newUser.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        userText.setStyle("-fx-font: normal bold 20px 'serif' ");
        gridPane.setStyle("-fx-background-color: BEIGE;");

        layout = gridPane;

        submitButton.setOnAction(e -> {
            String usernameInput = usernameField.getText();
            try {
                User user = ExistingUser.signIn(usernameInput);
                if (user == null) {
                    throw new IllegalArgumentException("User not found.");
                }
                app.setUser(user);
            } catch (Exception err) {
                app.setMainPageErrorText(err.getMessage());
            }

            primaryStage.getScene().setRoot(backPage.getLayout());
        });

        newUser.setOnAction(e -> {
            String usernameInput = usernameField.getText();
            try {
                User user = NewUser.createUser(usernameInput);
                app.setUser(user);
            } catch (Exception err) {
                app.setMainPageErrorText(err.getMessage());
            }

            primaryStage.getScene().setRoot(backPage.getLayout());
        });
    }

    public void setBackPage(Page page) {
        this.backPage = page;
    }

    @Override
    public Parent getLayout() {
        return layout;
    }
}
