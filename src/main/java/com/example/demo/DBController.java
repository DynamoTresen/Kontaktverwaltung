package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBController {
    private final String url = "jdbc:postgresql://localhost:5432/Kontaktverwaltung";
    private final String user = "postgres";
    private final String password = "passwort";

    public Connection getConnection(){

        Connection conn = null;

        try {

            Class.forName("org.postgresql.Driver");

            conn = DriverManager.getConnection(url,user,password);

            System.out.println("Postgres Verbindung wurde erfolgreich hergestellt.");

            return conn;
        } catch(SQLException e){
            System.out.println(e.getMessage());

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return null;

    }

}