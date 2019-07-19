package com.example.jahadulrakib.servicehut.UserPojo;

public class CreateToLetPojo {
    private String userId;
    private String toLetId;
    private String toLetImageURL;
    private String toLetPhone;
    private String toLetMonthRent;
    private String toLetAdvance;
    private String toLetAddress;
    private String toLetMonth;
    private String toLetType;
    private String toLetDetails;
    private String type;
    private String favId;
    private double lat;
    private double lon;

    public CreateToLetPojo() {
    }

    public CreateToLetPojo(String userId, String toLetId, String toLetImageURL, String toLetPhone, String toLetMonthRent, String toLetAdvance, String toLetAddress, String toLetMonth, String toLetType, String toLetDetails, String type,  double lat, double lon) {
        this.userId = userId;
        this.toLetId = toLetId;
        this.toLetImageURL = toLetImageURL;
        this.toLetPhone = toLetPhone;
        this.toLetMonthRent = toLetMonthRent;
        this.toLetAdvance = toLetAdvance;
        this.toLetAddress = toLetAddress;
        this.toLetMonth = toLetMonth;
        this.toLetType = toLetType;
        this.toLetDetails = toLetDetails;
        this.type = type;
        this.lat = lat;
        this.lon = lon;
    }

    public CreateToLetPojo(String userId, String toletId) {
        this.userId = userId;
        this.toLetId = toletId;
    }

    public CreateToLetPojo(String userId, String toLetId, String favId) {
        this.userId = userId;
        this.toLetId = toLetId;
        this.favId = favId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToLetId() {
        return toLetId;
    }

    public void setToLetId(String toLetId) {
        this.toLetId = toLetId;
    }

    public String getToLetImageURL() {
        return toLetImageURL;
    }

    public void setToLetImageURL(String toLetImageURL) {
        this.toLetImageURL = toLetImageURL;
    }

    public String getToLetPhone() {
        return toLetPhone;
    }

    public void setToLetPhone(String toLetPhone) {
        this.toLetPhone = toLetPhone;
    }

    public String getToLetMonthRent() {
        return toLetMonthRent;
    }

    public void setToLetMonthRent(String toLetMonthRent) {
        this.toLetMonthRent = toLetMonthRent;
    }

    public String getToLetAdvance() {
        return toLetAdvance;
    }

    public void setToLetAdvance(String toLetAdvance) {
        this.toLetAdvance = toLetAdvance;
    }

    public String getToLetAddress() {
        return toLetAddress;
    }

    public void setToLetAddress(String toLetAddress) {
        this.toLetAddress = toLetAddress;
    }

    public String getToLetMonth() {
        return toLetMonth;
    }

    public void setToLetMonth(String toLetMonth) {
        this.toLetMonth = toLetMonth;
    }

    public String getToLetType() {
        return toLetType;
    }

    public void setToLetType(String toLetType) {
        this.toLetType = toLetType;
    }

    public String getToLetDetails() {
        return toLetDetails;
    }

    public void setToLetDetails(String toLetDetails) {
        this.toLetDetails = toLetDetails;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFavId() {
        return favId;
    }

    public void setFavId(String favId) {
        this.favId = favId;
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
