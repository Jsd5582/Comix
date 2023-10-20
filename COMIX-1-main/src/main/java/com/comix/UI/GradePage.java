package com.comix.UI;

import com.comix.ComixApp;
import com.comix.Commands.ComicMemento;
import com.comix.Commands.CommandStack;
import com.comix.Commands.GradeCommand;
import com.comix.Commands.MementoStack;
import com.comix.User.User;
import com.comix.model.Comic;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GradePage implements Page {
    private final BorderPane layout;
    private final Button backButton;
    private final Button submitButton;
    private final Stage primaryStage;
    private Page backPage;

    /***
     * Allows a user to grade a comic
     * @param primaryStage The main stage that the page is built on
     */
    public GradePage(Stage primaryStage, ComixApp app) {
        this.primaryStage = primaryStage;

        //Layout
        layout = new BorderPane();
        HBox options = new HBox();
        HBox bs = new HBox();

        //Creation of buttons
        backButton = new Button("Back");
        submitButton = new Button("Submit");

        String buttonStyle = "-fx-font: normal normal 12px Arial; -fx-text-fill: WHITE; -fx-background-color: ";
        submitButton.setStyle(buttonStyle +"GREEN");
        backButton.setStyle(buttonStyle + "RED");
        layout.setStyle("-fx-background-color: BEIGE;");

        options.getChildren().addAll(submitButton,backButton);
        options.setAlignment(Pos.CENTER);
        options.setSpacing(15.0);

        //Allows a user to pick a number 1-10 for the grade of a selected comic
        Text gradeField = new Text("Enter grade: ");
        Spinner<Integer> spinner = new Spinner<Integer>();
        Label pageTitle = new Label("Grade Comic");


        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10);

        spinner.setValueFactory(valueFactory);

        bs.getChildren().addAll(gradeField,spinner);
        bs.setAlignment(Pos.CENTER);

        //Adding components
        layout.setTop(pageTitle);
        layout.setCenter(bs);
        layout.setBottom(options);

        backButton.setOnAction(e -> primaryStage.getScene().setRoot(backPage.getLayout()));

        submitButton.setOnAction(e -> {
            try {
                GradeCommand command = new GradeCommand();
                User currentUser = app.getUser();
                if (currentUser == null) {
                    throw new IllegalAccessException("Must be signed in to do this action");
                }

                Comic currentComic = app.getCurrentSelection();
                if (currentComic == null) {
                    throw new IllegalArgumentException("Must select a comic to grade");
                }

                if (spinner.getValue() == 0) {
                    throw new IllegalArgumentException("Must provide a grade between 1-10");
                }

                ComicMemento memento = currentComic.createMemento();
                currentComic.setGrade(spinner.getValue());

                command.setUser(currentUser);
                command.setComic(currentComic);
                command.execute();
                MementoStack mementoStack = MementoStack.instance();
                CommandStack commandStack = CommandStack.instance();
                mementoStack.add(memento);
                commandStack.clearRestore();
                commandStack.add(command);
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
