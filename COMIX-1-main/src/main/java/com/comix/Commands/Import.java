package com.comix.Commands;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.comix.db.DBConnection;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class Import {
    
    public static boolean importCSV(String username, String filePath){
        try{
            Connection connection = DBConnection.connect();
            FileReader fileReader = new FileReader(filePath);
            CSVParser parser = new CSVParserBuilder().withSeparator(',').build();
            CSVReader csvReader = new CSVReaderBuilder(fileReader).withCSVParser(parser).build();
            String[] comic;
            csvReader.readNext();
            while ((comic = csvReader.readNext()) != null) {
                PreparedStatement sql = connection.prepareStatement(
                        "INSERT INTO "+ username + " (full_title, series, issue, publisher, release_date, grade, " +
                                "slabbed,id) VALUES (?, ?, ?, ?, ?, ?,false,?)");

                for (int i = 0; i < 7; i++) {
                    
                    sql.setString(i + 1, comic[i]);
                    if(i == 5 || i == 7){
                        sql.setString(i+1, comic[i]);
                    }
                }

                sql.executeUpdate();
                sql.close();
            }
            return true;
        }catch(Exception ie){
            ie.printStackTrace();
            return false;   
        }
               
            }
}
