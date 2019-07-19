package com.example.jahadulrakib.servicehut.AddItems;

public class AddItem {

    private String jobId;
    private String itemId;
    private String itemDetails;
    private double amountOfMoney;

    public AddItem() {
    }

    public AddItem(String jobId, String itemId, String itemDetails, double amountOfMoney) {
        this.jobId = jobId;
        this.itemId = itemId;
        this.itemDetails = itemDetails;
        this.amountOfMoney = amountOfMoney;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(String itemDetails) {
        this.itemDetails = itemDetails;
    }

    public double getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(double amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }
}
