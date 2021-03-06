package com.example.jahadulrakib.servicehut.UserPojo;

import java.io.Serializable;

public class HireLabour implements Serializable {
    private String userId;
    private String labourId;
    private String jobId;
    private String jobStatus;
    private String carType;
    private int hireForDay;
    private String dateOfHire;
    private String detailInfo;
    private String number;
    private int totals;
    private double lat;
    private double lon;

    public HireLabour() {
    }

    public HireLabour(String userId, String labourId, String jobId, String jobStatus, String carType, int hireForDay, String dateOfHire, String detailInfo, String number, int totals, double lat, double lon) {
        this.userId = userId;
        this.labourId = labourId;
        this.jobId = jobId;
        this.jobStatus = jobStatus;
        this.carType = carType;
        this.hireForDay = hireForDay;
        this.dateOfHire = dateOfHire;
        this.detailInfo = detailInfo;
        this.number = number;
        this.totals = totals;
        this.lat = lat;
        this.lon = lon;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLabourId() {
        return labourId;
    }

    public void setLabourId(String labourId) {
        this.labourId = labourId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public int getHireForDay() {
        return hireForDay;
    }

    public void setHireForDay(int hireForDay) {
        this.hireForDay = hireForDay;
    }

    public String getDateOfHire() {
        return dateOfHire;
    }

    public void setDateOfHire(String dateOfHire) {
        this.dateOfHire = dateOfHire;
    }

    public String getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
