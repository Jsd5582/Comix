package com.comix.PersonalCollection;

import com.comix.db.DBConnection;
import com.comix.db.PartialSearch;
import com.comix.model.Comic;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PersonalCollection {

    /***
     * Displays the personal collection of a specific user
     * 
     * @param username The specific user
     */
    public static List<Comic> getCollection(String username) {
        List<Comic> comics = new ArrayList<>();
        try {
            Connection connection = DBConnection.connect();
            Statement sql = connection.createStatement();
            connection.setAutoCommit(true);
            String count_stmt = "SELECT COUNT(*) FROM " + username + ";";
            String sql_stmt = "SELECT id, series, issue, full_title, publisher, release_date, creators, grade, " +
                    "value, slabbed FROM " + username + " ORDER BY id ASC;";
            ResultSet count = sql.executeQuery(count_stmt);
            count.next();
            int numRows = count.getInt("count");
            if (numRows != 0) {
                ResultSet result = sql.executeQuery(sql_stmt);
                while (result.next()) {
                    int id = result.getInt("id");
                    String series = result.getString("series");
                    String issue = result.getString("issue");
                    String title = result.getString("full_title");
                    String publisher = result.getString("publisher");
                    String release = result.getString("release_date");
                    String creators = result.getString("creators");
                    int grade = result.getInt("grade");
                    double value = result.getDouble("value");
                    boolean isSlabbed = result.getBoolean("slabbed");
                    Comic comic = new Comic(id, title, series, issue, release, creators, publisher, value, grade, isSlabbed);
                    comics.add(comic);
                }
            }

        } catch (Exception ie) {
            ie.printStackTrace();
        }
        return comics;
    }

    public static void displayCollection(String username) {

    }

    /**
     * Retrieves a list of comics based on the user input for the title
     * 
     * @param username The specific user
     */
    public static void getComicAndAdd(Scanner userInput, String username) {
        try {
            System.out.println("Enter title of comic: ");
            String input = userInput.nextLine();
            DBConnection db = new DBConnection(new PartialSearch());
            List<Comic> comics = db.searchTitle(input);
            if (comics.isEmpty()) {
                System.out.println("Comic doesn't exist in database :(");
                //manualEnter(userInput, username);
            } else if (comics.size() == 1) {
                Comic comic = comics.get(0);
                System.out.println("Adding comic...");
                addComic(comic, username);
            } else {
                System.out.println("Comics Found:");
                for (Comic comic : comics) {
                    System.out.println(comic.toString());
                }
                System.out.println("Enter issue of Comic:");
                String issue = userInput.nextLine();
                for (int i = 0; i < comics.size(); i++) {
                    Comic c = comics.get(i);
                    if (c.getIssue().toLowerCase().compareTo(issue) == 0) {
                        addComic(c, username);
                        i = comics.size();
                    }
                }
            }

        } catch (Exception ie) {
            ie.printStackTrace();
        }

    }

    /**
     * Adds a new comic book to the user's personal collection
     * 
     * @param newComic the new comic wished to be added
     * @param username the specific user
     */
    public static void addComic(Comic newComic, String username) { // May need to be removed since system is text-based
        try {

            Connection connection = DBConnection.connect();

            String sql = "INSERT INTO " + username
                    + " (series, issue, full_title, publisher, release_date, creators, value, grade, slabbed) VALUES " +
                    "(?,?,?,?,?,?,?,?,?) RETURNING id;";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, newComic.getSeries());
            statement.setString(2, newComic.getIssue());
            statement.setString(3, newComic.getTitle());
            statement.setString(4, newComic.getPublisher());
            statement.setString(5, newComic.getPublicationDate());
            statement.setString(6, newComic.getCreators());
            statement.setDouble(7, newComic.getBaseValue());
            statement.setInt(8, newComic.getGrade());
            statement.setBoolean(9, newComic.getIsSlabbed());
            statement.executeUpdate();

            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                newComic.setComicID(result.getInt("id"));
            }
            statement.close();
            // System.out.println("Comic added to your personal Collection!");
        } catch (Exception ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Deletes a specific comic book from the user's personal collection
     * 
     * @param username The specific user
     * @param comic    The comic book wished to be deleted
     */
    public static void remove(String username, Comic comic) {
        try {
            Connection connection = DBConnection.connect();
            String sql = "DELETE FROM " + username + " WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, comic.getComicID());
            statement.executeUpdate();
        } catch (Exception ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Retrieves the size of the result set
     * 
     * @param set The specific set
     * @return the size
     */
    public static int getResultSetSize(ResultSet set) {
        try {
            System.out.println(set.getInt(1));
            return set.getInt(1);
        } catch (Exception ie) {
            return 0;
        }
    }

    public static void editComic(Comic editedComic, String username) throws SQLException, IOException {
        Connection conn = DBConnection.connect();
        String sql = "UPDATE " + username
                + " SET series = ?, issue = ?, full_title = ?, publisher = ?, release_date = ?, " +
                "creators = ? , value = ? , grade = ? , slabbed = ? WHERE id = ?;";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, editedComic.getSeries());
        st.setString(2, editedComic.getIssue());
        st.setString(3, editedComic.getTitle());
        st.setString(4, editedComic.getPublisher());
        st.setString(5, editedComic.getPublicationDate());
        st.setString(6, editedComic.getCreators());
        st.setDouble(7, editedComic.getValue());
        st.setInt(8, editedComic.getGrade());
        st.setBoolean(9, editedComic.getIsSlabbed());
        st.setInt(10, editedComic.getComicID());

        st.executeUpdate();
        conn.close();
    }

    /**
     * Allows a user to give a comic a certain grade
     * 
     * @param comic The specific comic wished to be graded
     * @param grade The grade the user wishes to give the comic
     */
    public static void gradeComic(Comic comic, int grade) {
        // System.out.println("What grade would you like to assign the comic? (Must be a
        // value 1-10)");
        if (grade == 1) {
            comic.setBaseValue(comic.getValue() * 0.10);
            comic.setGrade(grade);
        } else if (grade >= 2 && grade <= 10) {
            comic.setBaseValue(comic.getValue() * Math.log10(grade));
            comic.setGrade(grade);
        } else {
            System.out.println("Grade values must be between 1 and 10.");
        }
    }

    public static void gradeComic(Comic comic, String username) throws SQLException, IOException {
        Connection conn = DBConnection.connect();
        String sql = "UPDATE " + username + " SET grade = ?, value = ? WHERE id = ?";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, comic.getGrade());
        st.setFloat(2, (float) (comic.getBaseValue()));
        st.setInt(3, comic.getComicID());

        st.executeUpdate();
        conn.close();
    }

    /**
     * Slabs a comic
     * 
     * @param comic The specific comic that is to be slabbed
     */
    public static void slabComic(Comic comic) {
        if (comic.getGrade() >= 1 && comic.getGrade() <= 10 && !comic.getIsSlabbed()) {
            comic.slabComic();
            // comic.setValue(comic.getValue() * 2);
        } else
            throw new IllegalArgumentException("The comic is unable to be slabbed at this time. Please double check " +
                    "that you have graded the comic first!");
    }

    public static void slabComic(Comic comic, String username) throws SQLException, IOException {
        slabComic(comic);
        Connection conn = DBConnection.connect();
        String sql = "UPDATE " + username + " SET slabbed = ?, value = ? WHERE ID = ?";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setBoolean(1, comic.getIsSlabbed());
        st.setFloat(2, (float) (comic.getBaseValue()));
        st.setInt(3, comic.getComicID());

        st.executeUpdate();
        conn.close();
    }

}
