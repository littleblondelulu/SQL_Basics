package com.ironyard.charlotte;

/**
 * Created by lindseyringwald on 9/11/16.
 */
public class Restaurant {
    String name;
    boolean isOpen;
    double price;
    int id;


    public Restaurant(String name, boolean isOpen, double price) {
        this.name = name;
        this.isOpen = isOpen;
        this.price = price;
    }

    public Restaurant(int id, String name, boolean isOpen, double price) {
        this.id = id;
        this.name = name;
        this.isOpen = isOpen;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", isOpen=" + isOpen +
                ", price=" + price +
                ", id=" + id +
                '}';
    }
}
