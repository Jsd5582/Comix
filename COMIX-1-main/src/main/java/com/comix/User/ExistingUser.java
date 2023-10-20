package com.comix.User;

import java.sql.*;
import java.util.Scanner;

import com.comix.db.DBConnection;
import com.comix.ComixApp;

/**
 * This class contains static functions for signing in an existing user
 */
public class ExistingUser {

    /**
     * Prompts a user to sign in and checks if the username matches any already existing users
     *
     * @param userInput Scanner for user input
     * @return Returns the user if the username already exists, {@code null} otherwise
     */
    public static User signIn(Scanner userInput) {
        System.out.println("Enter your user name:");
        String username = userInput.nextLine().toLowerCase();
        if (username.equals(ComixApp.DB_TABLE_NAME)) {
            throw new IllegalArgumentException(String.format("Username cannot be %s", ComixApp.DB_TABLE_NAME));
        }
        System.out.println("Signing in...");

        boolean userExists = User.checkUserExists(username);
        if (userExists) {
            return new User(username);
        }
        System.out.println("User doesn't exist.");
        return null;
    }

    public static User signIn(String username) {
        if (username.equals(ComixApp.DB_TABLE_NAME)) {
            throw new IllegalArgumentException(String.format("Username cannot be %s", ComixApp.DB_TABLE_NAME));
        }

        boolean userExists = User.checkUserExists(username);
        if (userExists) {
            return new User(username);
        }
        return null;
    }
}
