package com.comix.User;

import com.comix.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * The class representing a comix user
 */
public class User {
    private final String username;

    /**
     * Constructor for the User class
     *
     * @param username The user's username
     */
    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Verifies that the username exists in the database
     *
     * @param username The specific username inputted by the user
     * @return {@code true} if the user exists and has a collection, {@code false} otherwise
     */
    public static boolean checkUserExists(String username) {
        try {
            Connection connection = DBConnection.connect();

            connection.setAutoCommit(true);
            String stmt = "SELECT EXISTS ("
                    + "SELECT FROM pg_tables "
                    + "WHERE schemaname = 'public' AND tablename = ?)";
            PreparedStatement sql = connection.prepareStatement(stmt);
            sql.setString(1, username);
            ResultSet result = sql.executeQuery();
            result.next();
            return result.getBoolean(1);

        } catch (Exception ie) {
            ie.printStackTrace();
            return false;
        }
    }
}
