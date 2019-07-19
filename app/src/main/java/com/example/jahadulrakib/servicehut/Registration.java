package com.example.jahadulrakib.servicehut;

import java.io.Serializable;

public class Registration implements Serializable {

    private String userName;
    private String fullName;
    private String userType;
    private String uEmail;
    private String uPass;
    private String uPhone;
    private String uAddress;
    private String uId;
    private String urlImage;


    public Registration() {
    }

    public Registration(String userName, String fullName, String userType, String uEmail, String uPass, String uPhone, String uAddress, String uId, String urlImage) {
        this.userName = userName;
        this.fullName = fullName;
        this.userType = userType;
        this.uEmail = uEmail;
        this.uPass = uPass;
        this.uPhone = uPhone;
        this.uAddress = uAddress;
        this.uId = uId;
        this.urlImage = urlImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuPass() {
        return uPass;
    }

    public void setuPass(String uPass) {
        this.uPass = uPass;
    }

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public String getuAddress() {
        return uAddress;
    }

    public void setuAddress(String uAddress) {
        this.uAddress = uAddress;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
