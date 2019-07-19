package com.example.jahadulrakib.servicehut.Transection;

public class Transection {
    public String jobId;
    public String driverId;
    public int total;
    public double rating;

    public Transection() {
    }

    public Transection(String jobId, String driverId, int total) {
        this.jobId = jobId;
        this.driverId = driverId;
        this.total = total;
    }

    public Transection(String jobId, String driverId, int total, double rating) {
        this.jobId = jobId;
        this.driverId = driverId;
        this.total = total;
        this.rating = rating;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
