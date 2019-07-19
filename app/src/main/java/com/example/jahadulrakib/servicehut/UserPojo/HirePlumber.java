package com.example.jahadulrakib.servicehut.UserPojo;

import java.io.Serializable;

public class HirePlumber implements Serializable {
    private String userId;
    private String plumberId;
    private String jobId;
    private String jobStatus;
    private String dateOfHire;
    private String detailInfo;
    private String number;
    private int totals;
    private double lat;
    private double lon;

    public HirePlumber() {
    }

    public HirePlumber(String userId, String plumberId, String jobId, String jobStatus, String dateOfHire, String detailInfo, String number, int totals, double lat, double lon) {
        this.userId = userId;
        this.plumberId = plumberId;
        this.jobId = jobId;
        this.jobStatus = jobStatus;
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

    public String getPlumberId() {
        return plumberId;
    }

    public void setPlumberId(String plumberId) {
        this.plumberId = plumberId;
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
