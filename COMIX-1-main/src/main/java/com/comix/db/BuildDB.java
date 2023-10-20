package com.comix.db;

import java.io.FileReader;
import java.sql.*;

import com.comix.ComixApp;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * Manages the construction of the full database from the comics.csv file
 */
public class BuildDB {

    /**
     * Builds the COMIX database to store all comic books listed in comics.csv
     * 
     * @param connection An SQL database connection
     */
    public static void build(Connection connection) {
        String filePath = "comics.csv";
        try {
            Statement sql = connection.createStatement();
            connection.setAutoCommit(true);
            String sql_stmt = "CREATE TABLE IF NOT EXISTS " + ComixApp.DB_TABLE_NAME + " (" +
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
                    "value FLOAT," +
                    "grade INTEGER," +
                    "slabbed BOOLEAN" +
                    ");";
            sql.executeUpdate(sql_stmt);
            sql.close();
        } catch (SQLException ie) {
            ie.printStackTrace();
        }
        try {
            Statement checkTableStatement = connection.createStatement();
            String checkTableSql = "SELECT COUNT(*) FROM comix";
            ResultSet result = checkTableStatement.executeQuery(checkTableSql);
            result.next();
            int rowCount = result.getInt("COUNT");
            checkTableStatement.close();
            result.close();

            if (rowCount == 0) {

                FileReader fileReader = new FileReader(filePath);
                CSVParser parser = new CSVParserBuilder().withSeparator(',').build();
                CSVReader csvReader = new CSVReaderBuilder(fileReader).withCSVParser(parser).build();
                String[] comic;
                csvReader.readNext();
                while ((comic = csvReader.readNext()) != null) {
                    PreparedStatement sql = connection.prepareStatement(
                            "INSERT INTO comix (series, issue, full_title, variant, publisher, release_date, format, " +
                                    "added_date, creators, value, grade, slabbed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 0.0, 0, false)");

                    for (int i = 0; i < 9; i++) {
                        sql.setString(i + 1, comic[i]);
                    }

                    sql.executeUpdate();
                    sql.close();
                }
                csvReader.close();

            }
        } catch (Exception ie) {
            ie.printStackTrace();
        }
    }
}
