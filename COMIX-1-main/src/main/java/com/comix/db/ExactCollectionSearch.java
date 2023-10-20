package com.comix.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.comix.model.Comic;

public class ExactCollectionSearch implements PCSearcher{

    @Override
    public List<Comic> searchByTitle(Connection conn, String param, String username) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
            "SELECT * FROM " + username +" WHERE full_title ~* ('^' || ? || '$');"
        );
        return PCSearcher.searchHelper(statement, param);
    }

    @Override
    public List<Comic> searchBySeries(Connection conn, String param, String username) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
            "SELECT * FROM " + username +" WHERE series ~* ('^' || ? || '$');"
        );
        return PCSearcher.searchHelper(statement, param);
    }

    @Override
    public List<Comic> searchByIssue(Connection conn, String param, String username) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM " + username +" WHERE issue ~* ('^' || ? || '$');"
        );
        return PCSearcher.searchHelper(statement, param);
    }

    @Override
    public List<Comic> searchByPublisher(Connection conn, String param, String username) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM " + username +" WHERE publisher ~* ('^' || ? || '$');"
        );
        return PCSearcher.searchHelper(statement, param);
    }

    @Override
    public List<Comic> searchByReleaseDate(Connection conn, String param, String username) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
            "SELECT * FROM " + username +" WHERE release_date ~* ('^' || ? || '$');"
        );
        return PCSearcher.searchHelper(statement, param);
    }

    @Override
    public List<Comic> searchByCreators(Connection conn, String param, String username) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM " + username +" WHERE creators ~* ('^' || ? || '$');"
        );
        return PCSearcher.searchHelper(statement, param);
    }

    

   
    
    
}
