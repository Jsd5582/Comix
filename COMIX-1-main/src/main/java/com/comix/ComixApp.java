package com.comix;

import com.comix.UI.*;
import com.comix.User.User;
import com.comix.db.BuildDB;
import com.comix.db.DBConnection;
import com.comix.model.Comic;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * The main class of the comix application.
 */
public class ComixApp extends Application {
    /**
     * The constant table name for the full comics database
     */
    public static final String DB_TABLE_NAME = "comix";
    private MainPage mainPage;

    private void setupDB() throws IOException, SQLException {
        Connection conn = DBConnection.connect();
        BuildDB.build(conn);
        conn.close();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setMinHeight(400);
        stage.setMinWidth(600);

        setupDB();

        LoginPage loginPage = new LoginPage(stage, this);

        SearchPage dbSearchPage = new SearchPage(stage, this);

        AddPage addPage = new AddPage(stage, this);

        SlabPage slabPage = new SlabPage(stage, this);

        GradePage gradePage = new GradePage(stage, this);

        RemovePage removePage = new RemovePage(stage, this);

        EditPage editPage = new EditPage(stage, this);

        ImportPage importPage = new ImportPage(stage);

        ExportPage exportPage = new ExportPage(stage, this);

        SignComicPage signComicPage = new SignComicPage(stage);

        DatabaseResultPage dbResultPage = new DatabaseResultPage(stage, this);

        mainPage = new MainPage(stage);

        //Linking each back button to lead back to the Main Page
        addPage.setBackPage(mainPage);
        slabPage.setBackPage(mainPage);
        gradePage.setBackPage(mainPage);
        dbSearchPage.setBackPage(mainPage);
        editPage.setBackPage(mainPage);
        importPage.setBackButtonPage(mainPage);
        exportPage.setBackButtonPage(mainPage);
        signComicPage.setBackButtonPage(mainPage);
        removePage.setBackButtonPage(mainPage);
        loginPage.setBackPage(mainPage);
        dbSearchPage.setBackPage(mainPage);
        dbResultPage.setBackPage(mainPage);

        dbSearchPage.setResultPage(dbResultPage);


        //Linking all the buttons on the Main Page to their respective pages
        mainPage.setAddButtonPage(addPage);
        mainPage.setSlabButtonPage(slabPage);
        mainPage.setGradeButtonPage(gradePage);
        mainPage.setDbSearchButtonPage(dbSearchPage);
        mainPage.setEditButtonPage(editPage);
        mainPage.setImportButtonPage(importPage);
        mainPage.setExportButtonPage(exportPage);
        mainPage.setSignComicButtonPage(signComicPage);
        mainPage.setRemoveButtonPage(removePage);

        //Logout button takes the user to the login page and ends the session
        mainPage.setLogoutButtonPage(loginPage);

        Scene primaryScene = new Scene(mainPage.getLayout());
        stage.setScene(primaryScene);
        stage.setTitle("COMIX");
        stage.show();
    }

    /**
     * Connects to the Comix database
     */
    public static void main(String[] args) throws SQLException, IOException {
        launch();
    }

    public void setUser(User user) {
        mainPage.setUser(user);
    }

    public User getUser() {
        return mainPage.getUser();
    }

    public void setMainPageErrorText(String text) {
        mainPage.setErrorText(text);
    }

    public void refreshCollection() {
        mainPage.refresh();
    }

    public Comic getCurrentSelection() {
        return mainPage.getCurrentSelection();
    }
}
