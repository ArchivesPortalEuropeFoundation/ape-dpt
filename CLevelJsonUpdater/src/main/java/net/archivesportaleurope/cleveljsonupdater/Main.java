/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.archivesportaleurope.cleveljsonupdater;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author mahbub
 */
public class Main {
    private final String url = "jdbc:postgresql://localhost:5432/";
    private String dbName = "";
    private String dbUser = "";
    private String uPassword = "";
    
    public Main(String name, String user, String pass){
        dbName = name;
        dbUser = user;
        uPassword = pass;
    }
 
    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url+dbName, dbUser, uPassword);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
 
        return conn;
    }
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String name="ape", user="apenet_dashboard", password="";
        if (args.length>0) {
            for (int i = 0; i < args.length;i++) {
                if (args[i].equals("-n")) {
                    name=args[i+1];
                    i++;
                }
                if (args[i].equals("-u")) {
                    user=args[i+1];
                    i++;
                }
                if (args[i].equals("-p")) {
                    password=args[i+1];
                    i++;
                }
                if (args[i].equals("-h")){
                    System.out.println("Options: \n"
                            + "-n Name of database\n"
                            + "-u User name of database\n"
                            + "-p password of the db user");
                }
            }
        }
        Main app = new Main(name, user, password);
        app.connect();
    }
}
