package com.example.smartwallet.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MonthlyExpenses {
    private float income,expenses;

    public MonthlyExpenses() {
    }

    public MonthlyExpenses(float income, float expenses) {
        this.income = income;
        this.expenses = expenses;
    }

    public float getIncome() {
        return income;
    }

    public float getExpenses() {
        return expenses;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public void setExpenses(float expenses) {
        this.expenses = expenses;
    }
}
