package com.comix.UI;

import com.comix.ComixApp;
import com.comix.Commands.*;
import com.comix.User.User;
import com.comix.model.Comic;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditPage extends AddPage {
    private Comic currentComic;

    public EditPage(Stage primaryStage, ComixApp app) {
        super(primaryStage, "Edit Comic", app);
        currentComic = null;
        this.setSubmitButtonAction(e -> {
            try {
                Command command = new EditCommand();
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
                ComicMemento comicMemento = currentComic.createMemento();
                currentComic.setTitle(getTitleField().getText());
                currentComic.setSeries(getSeriesField().getText());
                currentComic.setIssue(getIssueField().getText());
                currentComic.setPublisher(getPublisherField().getText());
                currentComic.setCreators(getCreatorsField().getText());
                currentComic.setPublicationDate(getDateField().getText());
                currentComic.setBaseValue(valueDecimal);
                command.setUser(currentUser);
                command.setComic(currentComic);
                command.execute();
                CommandStack stack = CommandStack.instance();
                stack.clearRestore();
                stack.add(command);
                MementoStack mementoStack = MementoStack.instance();
                mementoStack.add(comicMemento);
                app.refreshCollection();
            } catch (Exception ex) {
                app.setMainPageErrorText(ex.getMessage());
            }
            this.clear();
            primaryStage.getScene().setRoot(getBackPage().getLayout());
        });
    }

    public void setCurrentComic(Comic comic) {
        currentComic = comic;
        if (currentComic != null) {
            this.setTitleValue(comic.getTitle());
            this.setSeriesValue(comic.getSeries());
            this.setIssueValue(comic.getIssue());
            this.setPublisherValue(comic.getPublisher());
            this.setCreatorsValue(comic.getCreators());
            this.setComicValue(comic.getBaseValue());
            this.setDateValue(comic.getPublicationDate());
        } else {
            this.clear();
        }
    }
}
