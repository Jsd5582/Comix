package com.comix.UI;

import com.comix.ComixApp;
import com.comix.Commands.CommandStack;
import com.comix.Commands.RemoveCommand;
import com.comix.User.User;
import com.comix.model.Comic;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RemovePage implements Page {
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 50;

    private final VBox layout;
    private final Button backButton;
    private final Stage primaryStage;
    private Page backPage;

    /***
     * Allows the user to remove a comic from their personal collection
     * @param primaryStage The main stage that the page is built on
     */
    public RemovePage(Stage primaryStage, ComixApp app){
        this.primaryStage = primaryStage;

        layout = new VBox();
        String buttonStyle = "-fx-font: normal normal 24px Arial; -fx-text-fill: WHITE; -fx-background-color: ";

        Label textLabel = new Label("Are you sure you want to remove the selected comic?");
        textLabel.setStyle("-fx-font: normal bold 24px Arial;");


        HBox choices = new HBox();
        choices.setAlignment(Pos.CENTER);
        choices.setSpacing(BUTTON_HEIGHT);
        backButton = new Button("No");
        backButton.setStyle(buttonStyle + "RED");
        backButton.setPrefWidth(BUTTON_WIDTH);
        backButton.setPrefHeight(BUTTON_HEIGHT);
        Button yesButton = new Button("Yes");
        yesButton.setStyle(buttonStyle + "GREEN");
        yesButton.setPrefWidth(BUTTON_WIDTH);
        yesButton.setPrefHeight(BUTTON_HEIGHT);
        choices.getChildren().addAll(backButton, yesButton);
        HBox.setHgrow(backButton, Priority.ALWAYS);
        HBox.setHgrow(yesButton, Priority.ALWAYS);

        layout.getChildren().addAll(textLabel, choices);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20px");
        VBox.setVgrow(choices, Priority.ALWAYS);

        backButton.setOnAction(e -> primaryStage.getScene().setRoot(backPage.getLayout()));

        yesButton.setOnAction(e -> {
            try {
                RemoveCommand command = new RemoveCommand();
                User user = app.getUser();
                if (user == null) {
                    throw new IllegalAccessException("Must be signed in to perform this action");
                }
                Comic selected = app.getCurrentSelection();
                if (selected == null) {
                    throw new IllegalArgumentException("Must select a comic to remove");
                }
                command.setUser(user);
                command.setComic(selected);
                command.execute();
                CommandStack stack = CommandStack.instance();
                stack.clearRestore();
                stack.add(command);
                app.refreshCollection();
            } catch (Exception ex) {
                app.setMainPageErrorText(ex.getMessage());
            }
            primaryStage.getScene().setRoot(backPage.getLayout());
        });
    }

    public void setBackButtonPage(Page page) {
        this.backPage = page;
    }

    @Override
    public Parent getLayout() {
        return layout;
    }
}
