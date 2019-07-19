package com.example.jahadulrakib.servicehut.Transection;
public class AdminTransection {
    private String date;
    private String userType;
    private int totalAmount;

    public AdminTransection() {
    }

    public AdminTransection(String date, String userType, int totalAmount) {
        this.date = date;
        this.userType = userType;
        this.totalAmount = totalAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
}
