package com.example.kisan_udyog.models;

public class PostModel {

    String pImage, pTitle, pDescription,pQuantity,pPrice,pUsername,profile_pic;

    public PostModel() {
    }

    public PostModel(String pImage, String pTitle, String pDescription, String pQuantity, String pPrice, String pUsername, String profile_pic) {
        this.pImage = pImage;
        this.pTitle = pTitle;
        this.pDescription = pDescription;
        this.pQuantity = pQuantity;
        this.pPrice = pPrice;
        this.pUsername = pUsername;
        this.profile_pic = profile_pic;
    }

    public String getpQuantity() {
        return pQuantity;
    }

    public String getpUsername() {
        return pUsername;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public void setpUsername(String pUsername) {
        this.pUsername = pUsername;
    }

    public void setpQuantity(String pQuantity) {
        this.pQuantity = pQuantity;
    }

    public String getpPrice() {
        return pPrice;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDescription() {
        return pDescription;
    }

    public void setpDescription(String pDescription) {
        this.pDescription = pDescription;
    }
}
