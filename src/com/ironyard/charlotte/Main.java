package com.ironyard.charlotte;

import org.h2.tools.Server;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        //Create a database connect to the aforementioned JDBC URL:
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");

        //Execute a query to create a restaurants table that stores the restaurant name and other attributes.
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS restaurants (id IDENTITY, name VARCHAR, is_open BOOLEAN, price DOUBLE)");

        Scanner scanner = new Scanner();


        while (true) {
            System.out.println("1. Create restaurants item");
            System.out.println("2. Toggle restaurants isOpen");
            System.out.println("3. List restaurants items");

            String option = scanner.nextLine();

            if (option.equals("1")) {
                System.out.println("Enter your restaurant:");
                String text = scanner.nextLine();

                //Restaurants restaurants = new Restaurants(int id, String name, boolean isOpen, double price);
                //restaurants.add(restaurant);
                insertRestaurant(conn, name, isOpen, price);
            }

           /*
            else if (option.equals("2")) {
                System.out.println("Enter the number of the restaurant you want to toggle:");
                int itemNum = Integer.valueOf(scanner.nextLine());
                //Restaurants restaurant = restaurants.get(itemNum-1);
                //restaurant.isOpen = !restaurant.isOpen;
                toggleToDo(conn, itemNum);
            }

            */

            else if (option.equals("3")) {
                ArrayList<Restaurants> restaurants = selectRestaurants(conn);
                //int i = 1;
                for (Restaurants restaurant : restaurants) {
                    String checkbox = "[ ]";
                    if (restaurant.isOpen) {
                        checkbox = "[x]";
                    }
                    System.out.printf("%s %d. %s\n", restaurant.name, restaurant.isOpen, restaurant.price);
                    //i++;
                }
            }
            else {
                System.out.println("Invalid option");
            }
        }

    }



    // Write a static method insertRestaurant and run it in the /create-restaurant route. It should insert a new row with the user-supplied information.
    public static void insertRestaurant(Connection conn, String name, boolean isOpen, double price) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO restaurants VALUES (NULL, ?, ?, ?)");
        stmt.setString(1, name);
        stmt.setBoolean(2, isOpen);
        stmt.setDouble(3, price);
        stmt.execute();
    }

    //method that returns all the restaurants from the database -- not sure if this one or the following is correct
    /*public static ArrayList<Restaurants> selecRestaurants(Connection conn) throws SQLException {
        ArrayList<Restaurants> items = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM restaurants");
        while (results.next()) {
            int id = results.getInt("id");
            String name = results.getString("name");
            boolean isOpen = results.getBoolean("isOpen");
            double price = results.getDouble("price");
            items.add(new Restaurants(id, name, isOpen, price));
        }
        return restaurants;
    }*/

    private static ArrayList<Restaurants> selectRestaurants(Connection conn, String name, boolean isOpen, double price) throws SQLException {
        PreparedStatement stmt3 = conn.prepareStatement("SELECT * FROM restaurants");
        ResultSet results = stmt3.executeQuery();
        while (results.next()) {
            String restaurantName = results.getString("name");
            boolean restaurantIsOpen = results.getBoolean("isOpen");
            double restaurantPrice = results.getDouble("price");
            System.out.printf("%s %f\n", restaurantName, restaurantIsOpen, restaurantPrice);
        }

        return selectRestaurants();
    }

    //toggle the isOpen value:
    public static void toggleRestaurants(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE restaurants SET isOpen =  NOT isOpen WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }


}
