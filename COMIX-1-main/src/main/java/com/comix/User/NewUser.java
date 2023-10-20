package com.comix.User;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

import com.comix.ComixApp;
import com.comix.PersonalCollection.PersonalCollection;
import com.comix.db.DBConnection;

/**
 * This class contains static functions for creating a new user
 */
public class NewUser {
    /**
     * Prompts a user to enter a username for a new user
     *
     * @param userInput The user input scanner
     * @return Returns a User
     */
    public static User createUser(Scanner userInput) {
        System.out.println("Enter a user name:");
        String username = userInput.nextLine();
        if (username.equals(ComixApp.DB_TABLE_NAME)) {
            throw new IllegalArgumentException(String.format("Username cannot be %s", ComixApp.DB_TABLE_NAME));
        }
        createUserCollection(userInput, username);
        return new User(username);
    }

    public static User createUser(String username) {
        boolean alreadyExists = User.checkUserExists(username);
        if (alreadyExists) {
            throw new IllegalArgumentException(String.format("User %s already exists.", username));
        }
        if (username.equals(ComixApp.DB_TABLE_NAME)) {
            throw new IllegalArgumentException(String.format("Username cannot be %s", ComixApp.DB_TABLE_NAME));
        }
        createEmptyCollection(username);
        return new User(username);
    }

    public static void createEmptyCollection(String username) {
        try {
            Connection connection = DBConnection.connect();
            connection.setAutoCommit(true);
            Statement statement = connection.createStatement();
            String stmt = "CREATE TABLE IF NOT EXISTS " + username + "(" +
                    "id SERIAL," +
                    "series VARCHAR(255)," +
                    "issue VARCHAR(10)," +
                    "full_title VARCHAR(255)," +
                    "variant VARCHAR(255)," +
                    "publisher VARCHAR(100)," +
                    "release_date VARCHAR(30)," +
                    "format VARCHAR(30)," +
                    "added_date VARCHAR(30)," +
                    "creators VARCHAR(255)," +
                    "value REAL NOT NULL DEFAULT 0.0," +
                    "grade INTEGER NOT NULL DEFAULT 0," +
                    "slabbed BOOLEAN NOT NULL DEFAULT False" +
                    ")";

            statement.executeUpdate(stmt);
            statement.close();
            connection.close();
        } catch (Exception ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Sends an SQL query to create a new collection for a user if it doesn't already exist.
     *
     * @param username Specific new user
     */
    public static void createUserCollection(Scanner userInput, String username) {
        try {
            Connection connection = DBConnection.connect();
            connection.setAutoCommit(true);
            Statement statement = connection.createStatement();
            String stmt = "CREATE TABLE IF NOT EXISTS " + username + "(" +
                    "id SERIAL," +
                    "series VARCHAR(255)," +
                    "issue VARCHAR(10)," +
                    "full_title VARCHAR(255)," +
                    "variant VARCHAR(255)," +
                    "publisher VARCHAR(100)," +
                    "release_date VARCHAR(30)," +
                    "format VARCHAR(30)," +
                    "added_date VARCHAR(30)," +
                    "creators VARCHAR(255)," +
                    "value REAL NOT NULL DEFAULT 0.0," +
                    "grade INTEGER NOT NULL DEFAULT 0," +
                    "slabbed BOOLEAN NOT NULL DEFAULT False" +
                    ")";
            
            statement.executeUpdate(stmt);
            statement.close();
            System.out.println("Welcome " + username + "!");
            System.out.println("Would you like to add a comic to start your personal collection?");
            String agreeString = userInput.nextLine().toLowerCase();
            if(agreeString.compareTo("yes") ==  0){
                PersonalCollection.getComicAndAdd(userInput, username);
            }

            connection.close();
        } catch (Exception ie) {
            ie.printStackTrace();
        }

    }
}
