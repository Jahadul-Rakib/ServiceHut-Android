package com.example.jahadulrakib.servicehut.UserPojo;

public class FavouritePojo {
    private String favId, userId, toLetId;

    public FavouritePojo() {
    }

    public FavouritePojo(String favId, String userId, String toLetId) {
        this.favId = favId;
        this.userId = userId;
        this.toLetId = toLetId;
    }

    public String getFavId() {
        return favId;
    }

    public void setFavId(String favId) {
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
}
