package com.comix.Commands;

import java.util.Scanner;

public class guestCommandPage {
    public static void commands(Scanner scanner){
        System.out.println("What would you like to do?");
        System.out.println(" Search \n Sign in \n End ");
        String command = scanner.nextLine().toLowerCase();
        while(command.compareTo("end") != 0){
            if(command.compareTo("search") == 0){
                commandPage.SearchPrecision(scanner);
                
            }else{
                commandPage.signIn(scanner);
                break;
            }
            System.out.println("What would you like to do?");
            System.out.println(" Search \n Sign in \n End");
            command = scanner.nextLine().toLowerCase();
        }
        System.out.println("Goodbye!");
        scanner.close();
    }
}
