package com.comix.UI;

import com.comix.ComixApp;
import com.comix.Commands.Command;
import com.comix.Commands.CommandStack;
import com.comix.Commands.SlabCommand;
import com.comix.User.User;
import com.comix.model.Comic;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SlabPage implements Page {
    private final BorderPane layout;
    private final HBox midBox;
    private final HBox topBox;
    private final Button backButton;
    private final Button submitButton;
    private final Stage primaryStage;
    private Page backPage;

    /***
     * Allows a user to slab a comic which is a one time action to increase its value
     * @param primaryStage The main stage that the page is built on
     */
    public SlabPage(Stage primaryStage, ComixApp app) {
        this.primaryStage = primaryStage;
        //Instantiation of boxes and layout
        layout = new BorderPane();
        midBox = new HBox();
        topBox = new HBox();

        String buttonStyle = "-fx-font: normal normal 12px Arial; -fx-text-fill: WHITE; -fx-background-color: ";
        backButton   = new Button("Back");
        submitButton = new Button("Submit");
        Text confirmation = new Text("Would you like to slab this comic?");

        //Styling
        submitButton.setStyle(buttonStyle +"GREEN");
        backButton.setStyle(buttonStyle + "RED");
        layout.setStyle("-fx-background-color: BEIGE;");


        //Setting box alignment
        midBox.setAlignment(Pos.CENTER);
        topBox.setAlignment(Pos.CENTER);
        midBox.setSpacing(20.5);

        //Adding the boxes with their elements
        midBox.getChildren().addAll(submitButton, backButton);
        topBox.getChildren().addAll(confirmation);

        //Placing the boxes in the correct location
        layout.setCenter(midBox);
        layout.setTop(topBox);

        backButton.setOnAction(e -> primaryStage.getScene().setRoot(backPage.getLayout()));

        submitButton.setOnAction(e -> {
            try {
                Command command = new SlabCommand();
                User currentUser = app.getUser();
                if (currentUser == null) {
                    throw new IllegalAccessException("Must be signed in to do this action");
                }

                Comic currentComic = app.getCurrentSelection();
                if (currentComic == null) {
                    throw new IllegalArgumentException("Must select a comic to slab");
                }

                if (currentComic.getIsSlabbed()) {
                    throw new IllegalArgumentException("Cannot re-slab a comic");
                }

                command.setUser(currentUser);
                command.setComic(currentComic);
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

    public void setBackPage(Page page) {
        this.backPage = page;
    }


    @Override
    public Parent getLayout() {
        return layout;
    }
}
