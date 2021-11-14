package com.example.smartwallet.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Payment {
    private double cost;
    private String name,type;
    private String date;

    public Payment() {
    }

    public Payment(double cost, String name, String type) {
        this.cost = cost;
        this.name = name;
        this.type = type;
    }

    public Payment(double cost, String name, String type, String date) {
        this.cost = cost;
        this.name = name;
        this.type = type;
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static int getColorForType(String type)
    {
        if(type.equals("food"))
        {
            return 0xFFF44436;
        }
        else if(type.equals("electronics"))
        {
            return 0xFF00BCD4;
        }
        else
        {
            return 0xFFCCCCCC;
        }
    }
}
