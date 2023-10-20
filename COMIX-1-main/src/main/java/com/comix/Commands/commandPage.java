package com.comix.Commands;

import com.comix.PersonalCollection.PersonalCollection;
import com.comix.User.ExistingUser;
import com.comix.User.NewUser;
import com.comix.User.User;
import com.comix.db.*;
import com.comix.model.Comic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class commandPage {
    public static void commands(Scanner scanner, String username){
        System.out.println("What would you like to do?");
        System.out.println(" Search Database \n Search Collection \n Add Comic \n Remove Comic \n Edit Comic \n Grade Comic \n Slab Comic \n Display Collection \n End");
        String command = scanner.nextLine().toLowerCase();
        while(command.compareTo("end") != 0){
            if(command.compareTo("search database") == 0){
                SearchPrecision(scanner);
                
            }else if(command.compareTo("search collection") == 0){
                CollectionSearchPrecision(scanner,username);
            }
            else if(command.compareTo("add") == 0){
                PersonalCollection.getComicAndAdd(scanner, username);
            }else if(command.compareTo("remove") == 0){
            }else if(command.compareTo("edit") == 0){
                Comic comic = getComic(username, scanner);
            }else if(command.compareTo("grade") == 0){
                Comic comic = getComic(username, scanner);
                // PersonalCollection.gradeComic(scanner, username, comic);
            }else if(command.compareTo("slab") == 0){
                Comic comic = getComic(username, scanner);
                // PersonalCollection.slabComic(scanner, username, comic);
            }else if(command.compareTo("display") == 0){
                PersonalCollection.displayCollection(username);
            }
            System.out.println("What would you like to do?");
            System.out.println(" Search Database \n Search Collection \n Add Comic \n Remove Comic \n Edit Comic \n Grade Comic \n Slab Comic \n Display Collection \n End");
            command = scanner.nextLine().toLowerCase();
        }
        scanner.close();
    }

    public static Comic getComic(String username, Scanner scanner){
        try{
        System.out.println("Enter title of comic: ");
        String title = scanner.nextLine();
        Connection connection = DBConnection.connect();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + username + " WHERE full_title = ?");
        List<Comic> comics = DBSearcher.searchHelper(statement, title);
        if(comics.size() == 0){
            System.out.println("Comic doesn't exist in personal collection");
        }else if (comics.size() == 1){
            Comic comic = comics.get(0);
            return comic;
        }else{
            System.out.println("Comics Found:");
            for (Comic comic : comics) {
                System.out.println(comic.toString());
                }
            System.out.println("Enter issue of Comic:");
            String issue = scanner.nextLine();
            for (int i = 0; i<comics.size(); i++){
                Comic c = comics.get(i);
                if(c.getIssue().toLowerCase().compareTo(issue) == 0){
                    return c;
                }
        }
        }
        }catch(Exception ie){
            ie.printStackTrace();
        }

        return null;
    }
    public static String SearchPreference(Scanner scanner){
        System.out.println("Search by title, series, issue, publisher, release date or creators?");
        return scanner.nextLine().toLowerCase();

    }
    public static void CollectionSearchPrecision(Scanner scanner,String username){
        System.out.println("Exact or Partial Search?");
        String precision = scanner.nextLine();
        String preference = SearchPreference(scanner);
        PCConnection searcher;
        if(precision.toLowerCase().compareTo("exact") == 0){
            searcher = new PCConnection(new ExactCollectionSearch());
        }else{
            searcher = new PCConnection(new PartialCollectionSearch());
        }try{
        if(preference.compareTo("title") == 0 ){
            System.out.println("Enter title of comic");
            String parameter = scanner.nextLine();
            List<Comic> results = searcher.searchTitle(parameter,username);
            displaySearch(results);
        }else if(preference.compareTo("series")==0){
            System.out.println("Enter series of comic");
            String parameter = scanner.nextLine();
            List<Comic> results =  searcher.searchSeries(parameter,username);
            displaySearch(results);
        }else if(preference.compareTo("issue")==0){
            System.out.println("Enter issue of comic");
            String parameter = scanner.nextLine();
            List<Comic> results =  searcher.searchIssue(parameter,username);
            displaySearch(results);
        }else if(preference.compareTo("publisher")==0){
            System.out.println("Enter publisher of comic");
            String parameter = scanner.nextLine();
            List<Comic> results =  searcher.searchPublisher(parameter,username);
            displaySearch(results);
        }else if(preference.compareTo("release date")==0){
            System.out.println("Enter release date of comic");
            String parameter = scanner.nextLine();
            List<Comic> results =  searcher.searchReleaseDate(parameter,username);
            displaySearch(results);
        }else if(preference.compareTo("creators")==0){
            System.out.println("Enter creators of comic");
            String parameter = scanner.nextLine();
            List<Comic> results =  searcher.searchCreators(parameter,username);
            displaySearch(results);
        }else{
            searcher.searchSlabbed(username);
        }
    }catch(Exception ie){
        ie.printStackTrace();
    }
    }
    public static void SearchPrecision(Scanner scanner){
        System.out.println("Exact or Partial Search?");
        String precision = scanner.nextLine();
        String preference = SearchPreference(scanner);
        DBConnection searcher;
        if(precision.toLowerCase().compareTo("exact") == 0){
            searcher = new DBConnection(new ExactSearch());
        }else{
            searcher = new DBConnection(new PartialSearch());
        }try{
        if(preference.compareTo("title") == 0 ){
            System.out.println("Enter title of comic");
            String parameter = scanner.nextLine();
            List<Comic> results = searcher.searchTitle(parameter);
            displaySearch(results);
        }else if(preference.compareTo("series")==0){
            System.out.println("Enter series of comic");
            String parameter = scanner.nextLine();
            List<Comic> results =  searcher.searchSeries(parameter);
            displaySearch(results);
        }else if(preference.compareTo("issue")==0){
            System.out.println("Enter issue of comic");
            String parameter = scanner.nextLine();
            List<Comic> results =  searcher.searchIssue(parameter);
            displaySearch(results);
        }else if(preference.compareTo("publisher")==0){
            System.out.println("Enter publisher of comic");
            String parameter = scanner.nextLine();
            List<Comic> results =  searcher.searchPublisher(parameter);
            displaySearch(results);
        }else if(preference.compareTo("release date")==0){
            System.out.println("Enter release date of comic");
            String parameter = scanner.nextLine();
            List<Comic> results =  searcher.searchReleaseDate(parameter);
            displaySearch(results);
        }else if(preference.compareTo("creators")==0){
            System.out.println("Enter creators of comic");
            String parameter = scanner.nextLine();
            List<Comic> results =  searcher.searchCreators(parameter);
            displaySearch(results);
        }
    }catch(Exception ie){
        ie.printStackTrace();
    }
    }
    public static void displaySearch(List<Comic> results){
        System.out.println("Search Result(s)");
        for (Comic comic : results) {
            System.out.println(comic.toString());
        }
    }
    public static User signIn(Scanner scanner){
        System.out.println("Welcome to Comix!");
        System.out.println("Are you a new or returning user (Enter new or returning):");
        String new_returning = scanner.nextLine().toLowerCase();
        if (new_returning.compareTo("new") == 0) {
            User user = NewUser.createUser(scanner);
            if (user != null) {
                PersonalCollection.displayCollection(user.getUsername());
                commandPage.commands(scanner, user.getUsername());
            }
            return user;
        } else if (new_returning.compareTo("returning") == 0) {
            User user = ExistingUser.signIn(scanner);
            if (user != null) {
                PersonalCollection.displayCollection(user.getUsername());
                commandPage.commands(scanner, user.getUsername());
            }
            return user;
        }
        return null;
    }
    public static void printResultSet(ResultSet result)throws SQLException {
        while(result.next()){
            int comicID = result.getInt("ID");
            String series = result.getString("SERIES");
            String issue = result.getString("ISSUE");
            String title = result.getString("FULL_TITLE");
            String publisher = result.getString("PUBLISHER");
            String releaseDate = result.getString("RELEASE_DATE");
            String creators = result.getString("CREATORS");
            int grade = result.getInt("GRADE");
            Double value = result.getDouble("VALUE");
            Boolean slab = result.getBoolean("SLABBED");
            Comic comic = new Comic(comicID, title, series, issue, releaseDate, creators, publisher, value, grade,slab);
            System.out.println(comic.toString());
        }
    }
}
