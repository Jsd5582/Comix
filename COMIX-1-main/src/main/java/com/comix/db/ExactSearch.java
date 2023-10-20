package com.comix.db;

import com.comix.model.Comic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * A search strategy which only returns exact matches for search parameters
 */
public class ExactSearch implements DBSearcher {

    /**
     * Uses the exact search method to search for comic books with a specific title
     * @param conn The connection to the database
     * @param param The parameter for the specific title
     * @return The list of comic books with the title
     */
    @Override
    public List<Comic> searchByTitle(Connection conn, String param) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM comix WHERE full_title ~* ('^' || ? || '$');"
        );
        return DBSearcher.searchHelper(statement, param);
    }

    /**
     * Uses the exact search method to search for comic books in a certain series
     * @param conn The connection to the database
     * @param param The parameter for the specific title
     * @return The list of comic books within the series
     */
    @Override
    public List<Comic> searchBySeries(Connection conn, String param) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM comix WHERE series ~* ('^' || ? || '$');"
        );
        return DBSearcher.searchHelper(statement, param);
    }

    /**
     * Uses the exact search method to search for comic books of a certain issue
     * @param conn The connection to the database
     * @param param The parameter for the specific issue
     * @return The list of comic books of the certain issue
     */
    @Override
    public List<Comic> searchByIssue(Connection conn, String param) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM comix WHERE issue ~* ('^' || ? || '$');"
        );
        return DBSearcher.searchHelper(statement, param);
    }

    /***
     * Uses the exact search method to search for comic books with a certain publisher
     * @param conn The connection to the database
     * @param param The parameter for the specific publisher
     * @return The list of comic books with the certain publisher
     */
    @Override
    public List<Comic> searchByPublisher(Connection conn, String param) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM comix WHERE publisher ~* ('^' || ? || '$');"
        );
        return DBSearcher.searchHelper(statement, param);
    }

    /**
     * Uses the exact search method to search for comic books with a certain release date
     * @param conn The connection to the database
     * @param param The parameter for the specific release date
     * @return The list of comic books with the certain release date
     */
    @Override
    public List<Comic> searchByReleaseDate(Connection conn, String param) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM comix WHERE release_date ~* ('^' || ? || '$');"
        );
        return DBSearcher.searchHelper(statement, param);
    }

    /***
     * Uses the exact search method to search for comic books based on the creators
     * @param conn The connection to the database
     * @param param The parameter for the specific creators
     * @return The list of comic books with the certain creators
     */
    @Override
    public List<Comic> searchByCreators(Connection conn, String param) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM comix WHERE creators ~* ('^' || ? || '$');"
        );
        return DBSearcher.searchHelper(statement, param);
    }
}
