package com.comix.UI;

import com.comix.ComixApp;
import com.comix.Commands.AddCommand;
import com.comix.Commands.Command;
import com.comix.Commands.CommandStack;
import com.comix.User.User;
import com.comix.model.Comic;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddPage implements Page {
    private final VBox layout;
    private final Button backButton;
    private final Button submitButton;
    private final TextField titleField;
    private final TextField seriesField;
    private final TextField issueField;
    private final TextField publisherField;
    private final TextField creatorsField;
    private final TextField valueField;
    private final TextField dateField;
    private final TextField[] fields;
    private final Stage primaryStage;
    private Page backPage;

    protected void clear() {
        for (TextField field : getFields()) {
            field.clear();
        }
    }

    public AddPage(Stage primaryStage, ComixApp app) {
        this(primaryStage, "Add Comic", app);
    }

    public AddPage(Stage primaryStage, String title, ComixApp app) {
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

        Label pageTitle = new Label(title);
        pageTitle.setStyle("-fx-font: normal bold 36px Arial; -fx-padding: 20px;");
        pageTitle.setAlignment(Pos.CENTER);

        String textFontStyle = "-fx-font: normal normal 16px Arial";
        Text titleText      = new Text("Title: ");
        Text seriesText     = new Text("Series: ");
        Text issueText      = new Text("Issue: ");
        Text publisherText  = new Text("Publisher: ");
        Text dateText       = new Text("Release date: ");
        Text creatorsText   = new Text("Creators: ");
        Text valueText      = new Text("Base Value: ");
        titleText.setStyle(textFontStyle);
        seriesText.setStyle(textFontStyle);
        issueText.setStyle(textFontStyle);
        publisherText.setStyle(textFontStyle);
        dateText.setStyle(textFontStyle);
        creatorsText.setStyle(textFontStyle);
        valueText.setStyle(textFontStyle);

        titleField = new TextField();
        seriesField = new TextField();
        issueField = new TextField();
        publisherField = new TextField();
        creatorsField = new TextField();
        valueField = new TextField();
        dateField = new TextField();
        fields = new TextField[]{getTitleField(), getSeriesField(), getIssueField(), getPublisherField(), getCreatorsField(),
                getValueField(), getDateField()};

        HBox date = new HBox();
        date.getChildren().addAll(dateText, getDateField());
        date.setAlignment(Pos.CENTER);
        date.setPrefWidth(150);
        date.setSpacing(10);
        HBox.setHgrow(dateText, Priority.ALWAYS);
        HBox.setHgrow(getDateField(), Priority.ALWAYS);

        Button clearButton = new Button("Clear");
        submitButton = new Button("Submit");
        clearButton.setStyle(buttonStyle + "RED");
        submitButton.setStyle(buttonStyle + "GREEN");

        HBox buttons = new HBox();
        buttons.getChildren().addAll(clearButton, submitButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
        HBox.setHgrow(clearButton, Priority.ALWAYS);
        HBox.setHgrow(submitButton, Priority.ALWAYS);

        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.add(seriesText, 0, 0);
        pane.add(getSeriesField(), 1, 0);
        pane.add(issueText, 2, 0);
        pane.add(getIssueField(), 3, 0);
        pane.add(titleText, 0, 1);
        pane.add(getTitleField(), 1, 1);
        pane.add(publisherText, 2, 1);
        pane.add(getPublisherField(), 3, 1);
        pane.add(creatorsText, 0, 2);
        pane.add(getCreatorsField(), 1, 2);
        pane.add(valueText, 2, 2);
        pane.add(getValueField(), 3, 2);
        pane.add(date, 0, 3, 4, 1);
        pane.add(buttons, 0, 4, 4, 1);

        pane.setStyle("-fx-padding: 10px");
        pane.setAlignment(Pos.CENTER);

        ColumnConstraints normal        = new ColumnConstraints();
        ColumnConstraints rightAligned  = new ColumnConstraints();
        rightAligned.setHalignment(HPos.RIGHT);

        pane.getColumnConstraints().addAll(rightAligned, normal, rightAligned, normal);

        layout.getChildren().addAll(topBar, pageTitle, pane);
        VBox.setVgrow(pane, Priority.ALWAYS);
        layout.setAlignment(Pos.TOP_CENTER);

        clearButton.setOnAction(e -> this.clear());

        submitButton.setOnAction(e -> {
            Command command = new AddCommand();
            try {
                User currentUser = app.getUser();
                if (currentUser == null) {
                    throw new IllegalAccessException("Must be signed in to do this action");
                }
                for (TextField field : getFields()) {
                    if (field.getText().equals("")) throw new IllegalArgumentException("Missing attribute.");
                }
                String valueValue = getValueField().getText();
                double valueDecimal;
                try {
                    valueDecimal = Double.parseDouble(valueValue);
                } catch (NumberFormatException nfe) {
                    throw new NumberFormatException("Value not a decimal.");
                }
                String titleValue = getTitleField().getText();
                String seriesValue = getSeriesField().getText();
                String issueValue = getIssueField().getText();
                String publisherValue = getPublisherField().getText();
                String creatorsValue = getCreatorsField().getText();
                String dateValue = getDateField().getText();
                Comic newComic = new Comic(titleValue, seriesValue, issueValue, dateValue, creatorsValue, publisherValue,
                        valueDecimal);
                command.setUser(currentUser);
                command.setComic(newComic);
                command.execute();
                CommandStack stack = CommandStack.instance();
                stack.clearRestore();
                stack.add(command);
                app.refreshCollection();
            } catch (Exception ex) {
                app.setMainPageErrorText(ex.getMessage());
            }
            this.clear();
            primaryStage.getScene().setRoot(getBackPage().getLayout());
        });

        backButton.setOnAction(e -> {
            primaryStage.getScene().setRoot(getBackPage().getLayout());
            this.clear();
        });
    }

    public void setBackPage(Page page) {
        this.backPage = page;
    }

    protected void setSubmitButtonAction(EventHandler<ActionEvent> function) {
        submitButton.setOnAction(function);
    }

    protected void setTitleValue(String value) {
        getTitleField().setText(value);
    }

    protected void setSeriesValue(String value) {
        getSeriesField().setText(value);
    }

    protected void setIssueValue(String value) {
        getIssueField().setText(value);
    }

    protected void setPublisherValue(String value) {
        getPublisherField().setText(value);
    }

    protected void setCreatorsValue(String value) {
        getCreatorsField().setText(value);
    }

    protected void setComicValue(double value) {
        getValueField().setText(Double.toString(value));
    }

    protected void setDateValue(String value) {
        getDateField().setText(value);
    }

    @Override
    public Parent getLayout() {
        return layout;
    }

    protected TextField getTitleField() {
        return titleField;
    }

    protected TextField getSeriesField() {
        return seriesField;
    }

    protected TextField getIssueField() {
        return issueField;
    }

    protected TextField getPublisherField() {
        return publisherField;
    }

    protected TextField getCreatorsField() {
        return creatorsField;
    }

    protected TextField getValueField() {
        return valueField;
    }

    protected TextField getDateField() {
        return dateField;
    }

    protected TextField[] getFields() {
        return fields;
    }

    protected Page getBackPage() {
        return backPage;
    }
}
