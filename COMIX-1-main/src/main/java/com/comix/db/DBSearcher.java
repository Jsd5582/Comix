package com.comix.db;

import com.comix.model.Comic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface for a DB search strategy
 */
public interface DBSearcher {

    /**
     * Searches the database by title
     *
     * @param conn An SQL connection
     * @param param A search parameter for the title
     * @return Returns a list of comics that match
     * @throws SQLException If there is a problem with the SQL search
     */
    List<Comic> searchByTitle(Connection conn, String param) throws SQLException;

    /**
     * Searches the database by series
     *
     * @param conn An SQL connection
     * @param param A search parameter for the series
     * @return Returns a list of comics that match
     * @throws SQLException If there is a problem with the SQL search
     */
    List<Comic> searchBySeries(Connection conn, String param) throws SQLException;

    /**
     * Searches the database by issue
     *
     * @param conn An SQL connection
     * @param param A search parameter for the issue
     * @return Returns a list of comics that match
     * @throws SQLException If there is a problem with the SQL search
     */
    List<Comic> searchByIssue(Connection conn, String param) throws SQLException;

    /**
     * Searches the database by publisher
     *
     * @param conn An SQL connection
     * @param param A search parameter for the publisher
     * @return Returns a list of comics that match
     * @throws SQLException If there is a problem with the SQL search
     */
    List<Comic> searchByPublisher(Connection conn, String param) throws SQLException;

    /**
     * Searches the database by release date
     *
     * @param conn An SQL connection
     * @param param A search parameter for the release date
     * @return Returns a list of comics that match
     * @throws SQLException If there is a problem with the SQL search
     */
    List<Comic> searchByReleaseDate(Connection conn, String param) throws SQLException;

    /**
     * Searches the database by creators
     *
     * @param conn An SQL connection
     * @param param A search parameter for the creators
     * @return Returns a list of comics that match
     * @throws SQLException If there is a problem with the SQL search
     */
    List<Comic> searchByCreators(Connection conn, String param) throws SQLException;

    // Static helper method for both search types

    /**
     * Helper method for any search type to reduce repeated code
     *
     * @param statement A prepared statement with a parameter for a search parameter
     * @param param A search parameter
     * @return A list of comics that match the search parameter
     * @throws SQLException If an error occurs with the SQL search
     */
    static List<Comic> searchHelper(PreparedStatement statement, String param) throws SQLException {
        statement.setString(1, param);
        ResultSet result = statement.executeQuery();
        List<Comic> comics = new ArrayList<>();
        while (result.next()) {
            int comicID = result.getInt("ID");
            String series = result.getString("SERIES");
            String issue = result.getString("ISSUE");
            String title = result.getString("FULL_TITLE");
            String publisher = result.getString("PUBLISHER");
            String releaseDate = result.getString("RELEASE_DATE");
            String creators = result.getString("CREATORS");
            int grade = result.getInt("GRADE");
            Double value = result.getDouble("VALUE");
            boolean slabbed = result.getBoolean("SLABBED");
            Comic comic = new Comic(comicID, title, series, issue, releaseDate, creators, publisher, value, grade, slabbed);
            comics.add(comic);
        }
        return comics;
    }
}
