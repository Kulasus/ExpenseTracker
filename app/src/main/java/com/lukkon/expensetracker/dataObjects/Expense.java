package com.lukkon.expensetracker.dataObjects;

import java.io.Serializable;

public class Expense implements Serializable {
    private int expense_id;
    private String user_username;
    private String category_name;
    private int amount;
    private String title;
    private String description;

    public Expense(int expense_id, String user_username, String category_name, int amount, String title, String description) {
        this.expense_id = expense_id;
        this.user_username = user_username;
        this.category_name = category_name;
        this.amount = amount;
        this.title = title;
        this.description = description;
    }

    public int getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(int expense_id) {
        this.expense_id = expense_id;
    }

    public String getUser_username() {
        return user_username;
    }

    public void setUser_username(String user_username) {
        this.user_username = user_username;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "expense_id=" + expense_id +
                ", user_username='" + user_username + '\'' +
                ", category_name='" + category_name + '\'' +
                ", amount=" + amount +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
