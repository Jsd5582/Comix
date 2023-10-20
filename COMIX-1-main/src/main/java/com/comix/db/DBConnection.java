package com.comix.db;

import com.comix.model.Comic;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Handles the connection to the database and the search strategy pattern
 */
public class DBConnection {
    // Static properties
    private static final String HOSTNAME = "hostname";
    private static final String PORT = "port";
    private static final String DATABASE = "database";
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private static final String[] CRED_FIELDS = {HOSTNAME, PORT, DATABASE, USER, PASSWORD};

    /**
     * A search strategy
     */
    private DBSearcher searchStrategy;

    /**
     * Constructs a DBConnection class with a search strategy
     *
     * @param searchStrategy A search strategy
     */
    public DBConnection(DBSearcher searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    /**
     * Changes the search strategy used by the DBConnection
     *
     * @param searchStrategy A search strategy
     */
    public void setSearch(DBSearcher searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    /**
     * Searches the COMIX database for comic books with a certain title
     * @param parameter The title of interest
     * @return A list of all comic books in the database with that specific title
     * @throws SQLException If there is an error in the SQL query
     * @throws IOException If the db.yml file cannot be found
     */
    public List<Comic> searchTitle(String parameter) throws SQLException, IOException {

            Connection conn = connect();
            List<Comic> comics = searchStrategy.searchByTitle(conn, parameter);
            conn.close();
            return comics;
    }

    /**
     * Searches the COMIX database for comic books in a certain series
     * @param parameter The series of interest
     * @return A list of all comic books in the database in that specific series
     * @throws SQLException If there is an error in the SQL query
     * @throws IOException If the db.yml file cannot be found
     */
    public List<Comic> searchSeries(String parameter) throws SQLException, IOException {
        Connection conn = connect();
        List<Comic> comics = searchStrategy.searchBySeries(conn, parameter);
        conn.close();
        return comics;
    }

    /**
     * Searches the COMIX database for comic books in a certain issue
     * @param parameter The issue of interest
     * @return A list of comic books in the database with the specific issue number
     * @throws SQLException If there is an error in the SQL query
     * @throws IOException If the db.yml file cannot be found
     */
    public List<Comic> searchIssue(String parameter) throws SQLException, IOException {
        Connection conn = connect();
        List<Comic> comics = searchStrategy.searchByIssue(conn, parameter);
        conn.close();
        return comics;
    }

    /**
     * Searches the COMIX database for comic books made by certain publishers
     * @param parameter The specific publisher of interest
     * @return A list of all comics from the database that match the publisher
     * @throws SQLException If there is an error in the SQL query
     * @throws IOException If the db.yml file cannot be found
     */
    public List<Comic> searchPublisher(String parameter) throws SQLException, IOException {
        Connection conn = connect();
        List<Comic> comics = searchStrategy.searchByPublisher(conn, parameter);
        conn.close();
        return comics;
    }

    /**
     * Searches the COMIX database for comic books released on a certain date
     * @param parameter The specific date of interest
     * @return All the comic books released on this date
     * @throws SQLException If there is an error in the SQL query
     * @throws IOException If the db.yml file cannot be found
     */
    public List<Comic> searchReleaseDate(String parameter) throws SQLException, IOException {
        Connection conn = connect();
        List<Comic> comics = searchStrategy.searchByReleaseDate(conn, parameter);
        conn.close();
        return comics;
    }

    /**
     * Searches the COMIX database for comic books made by certain Creators
     * @param parameter The specific creators in interest
     * @return A list of all comics from the database that match the creators
     * @throws SQLException If there is an error in the SQL query
     * @throws IOException If the db.yml file cannot be found
     */
    public List<Comic> searchCreators(String parameter) throws SQLException, IOException {
        Connection conn = connect();
        List<Comic> comics = searchStrategy.searchByCreators(conn, parameter);
        conn.close();
        return comics;
    }


    // Static methods

    /**
     * Creates a connection to a PostgreSQL database
     *
     * @return An SQL connection
     * @throws IOException If the db.yml file cannot be found or read from
     * @throws IllegalArgumentException If any of the required db.yml arguments are missing
     * @throws SQLException If there is a problem with the SQL connection, including invalid credentials
     */
    public static Connection connect() throws IOException, IllegalArgumentException, SQLException {
        // Load db.yml
        Yaml yaml = new Yaml();
        InputStream yamlFileStream = new FileInputStream("src/main/resources/yml/db.yml");
        Map<String, String> credentials = yaml.load(yamlFileStream);
        yamlFileStream.close();

        // Make sure file contains required fields
        for (String field : CRED_FIELDS) {
            if (!credentials.containsKey(field)) {
                throw new IllegalArgumentException(
                  String.format("Missing db.yml argument '%s'", field)
                );
            }
        }

        // Connect to the database
        String url = String.format("jdbc:postgresql://%s:%s/%s",
                credentials.get(HOSTNAME),
                credentials.get(PORT),
                credentials.get(DATABASE)
        );

        Properties props = new Properties();
        props.setProperty(USER, credentials.get(USER));
        props.setProperty(PASSWORD, credentials.get(PASSWORD));

        return DriverManager.getConnection(url, props);
    }
}
