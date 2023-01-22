package com.example.barbershopfx.Database;

import java.sql.Connection;
import java.sql.DriverManager;


public class DBConnection {

    public static Connection connection;


    public static void OpenConnection(){

        try {
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName); // here is the ClassNotFoundException

            String serverName = "localhost";
            String myDatabase = "barber";
            String url = "jdbc:mysql://" + serverName + "/" + myDatabase;

            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    public static void CloseConnection(){
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

