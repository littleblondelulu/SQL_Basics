package com.ironyard.charlotte;

import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.*;
import java.util.ArrayList;
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

                    HashMap<String, Object> m = new HashMap<>();
                    ArrayList<Restaurant> restaurants = selectRestaurants(conn);
                    m.put("restaurant", restaurants);
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

        Spark.post(
                "/delete-restaurant",
                ((request, response) -> {
                    String stringId = request.queryParams("id");

                    int id = Integer.parseInt(stringId);
                    deleteRestaurant(conn, id);

                    response.redirect("/");
                    return "";
                })

        );

        Spark.get(
                "/edit-restaurant/{{id}}",
                ((request, response) -> {

                    HashMap<String, Object> m = new HashMap<>();
                    ArrayList<Restaurant> restaurants = selectRestaurants(conn);
                    m.put("restaurant", restaurants);
                    m.put("id", restaurants.get(restaurant.id));
                    return new ModelAndView(m, "edit.html");
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "edit-restaurant",
                ((request, response) -> {
                    String name = request.queryParams("updateName");
                    String openOrNot = request.queryParams("updateIsOpen");
                    String priceString = request.queryParams("updatePrice");

                    boolean isOpen = openOrNot.equals("on");
                    double price = Double.valueOf(priceString);

                    Restaurant restaurant = new Restaurant(name, isOpen, price);
                    updateRestaurant(conn, restaurant);

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

    public static void deleteRestaurant(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM restaurants WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    //method that returns all the restaurants from the database -- not sure if this one or the following is correct
    public static ArrayList<Restaurant> selectRestaurants(Connection conn) throws SQLException {
        ArrayList<Restaurant> items = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM restaurants");
        while (results.next()) {
            int id = results.getInt("id");
            String name = results.getString("name");
            boolean isOpen = results.getBoolean("is_open");
            double price = results.getDouble("price");
            items.add(new Restaurant(id, name, isOpen, price));
        }
        return items;
    }

    public static void updateRestaurant(Connection conn, Restaurant newRestaurant) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE restaurants set name = ?, is_open = ?, price = ? where id = ?");
        stmt.setString(1, newRestaurant.getName());
        stmt.setBoolean(2, newRestaurant.isOpen());
        stmt.setDouble(3, newRestaurant.getPrice());
        stmt.execute();
    }


}
