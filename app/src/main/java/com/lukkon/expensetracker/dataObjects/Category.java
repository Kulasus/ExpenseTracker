package com.lukkon.expensetracker.dataObjects;

public class Category {
    private String name;
    private String color;

    public Category(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
