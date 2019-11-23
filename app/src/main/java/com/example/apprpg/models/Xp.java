package com.example.apprpg.models;

public class Xp {
    private MyCustomDate date;
    private int xpAmount;


    public Xp() {
    }

    public Xp(MyCustomDate date, int xpAmount) {
        this.date = date;
        this.xpAmount = xpAmount;
    }

    public MyCustomDate getDate() {
        return date;
    }

    public void setDate(MyCustomDate date) {
        this.date = date;
    }

    public int getXpAmount() {
        return xpAmount;
    }

    public void setXpAmount(int xpAmount) {
        this.xpAmount = xpAmount;
    }
}
