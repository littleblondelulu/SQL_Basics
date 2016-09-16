package com.ironyard.charlotte;

import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.*;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws SQLException {
        //Create a database connect to the aforementioned JDBC URL:
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");

        //Execute a query to create a restaurants table that stores the restaurant name and other attributes.
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS restaurants (id IDENTITY, name VARCHAR, is_open BOOLEAN, price DOUBLE)");

        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {

                    HashMap<String, Restaurant> m = new HashMap<>();
                    Restaurant showmars = new Restaurant("Showmars", true, 10.0);
                    m.put("restaurant", showmars);
                    return new ModelAndView(m, "home.html");
                }),

                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-restaurant",
                ((request, response) -> {
                    String name = request.queryParams("name");
                    String openOrNot = request.queryParams("isOpen");
                    String priceString = request.queryParams("price");

                    boolean isOpen = openOrNot.equals("on");
                    double price = Double.valueOf(priceString);

                    Restaurant restaurant = new Restaurant(name, isOpen, price);
                    insertRestaurant(conn, restaurant);

                    response.redirect("/");
                    return "";
                })
        );
    }



    // Write a static method insertRestaurant and run it in the /create-restaurant route. It should insert a new row with the user-supplied information.
    public static void insertRestaurant(Connection conn, Restaurant restaurant) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO restaurants VALUES (NULL, ?, ?, ?)");
        stmt.setString(1, restaurant.getName());
        stmt.setBoolean(2, restaurant.isOpen());
        stmt.setDouble(3, restaurant.getPrice());
        stmt.execute();
    }

    //method that returns all the restaurants from the database -- not sure if this one or the following is correct
    /*public static ArrayList<Restaurant> selecRestaurants(Connection conn) throws SQLException {
        ArrayList<Restaurant> items = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM restaurants");
        while (results.next()) {
            int id = results.getInt("id");
            String name = results.getString("name");
            boolean isOpen = results.getBoolean("isOpen");
            double price = results.getDouble("price");
            items.add(new Restaurant(id, name, isOpen, price));
        }
        return restaurants;
    }*/

  /*  private static ArrayList<Restaurant> selectRestaurants(Connection conn, String name, boolean isOpen, double price) throws SQLException {
        PreparedStatement stmt3 = conn.prepareStatement("SELECT * FROM restaurants");
        ResultSet results = stmt3.executeQuery();
        while (results.next()) {
            String restaurantName = results.getString("name");
            boolean restaurantIsOpen = results.getBoolean("isOpen");
            double restaurantPrice = results.getDouble("price");
            System.out.printf("%s %f\n", restaurantName, restaurantIsOpen, restaurantPrice);
        }

       // return selectRestaurants();
    }

    //toggle the isOpen value:
    public static void toggleRestaurants(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE restaurants SET isOpen =  NOT isOpen WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }
*/

}
