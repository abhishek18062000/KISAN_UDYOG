package com.example.kisan_udyog.models;

public class PostModel {

    String pImage, pTitle, pDescription,pQuantity,pPrice;

    public PostModel() {
    }

    public PostModel(String pImage, String pTitle, String pDescription, String pQuantity, String pPrice) {
        this.pImage = pImage;
        this.pTitle = pTitle;
        this.pDescription = pDescription;
        this.pQuantity = pQuantity;
        this.pPrice = pPrice;
    }

    public String getpQuantity() {
        return pQuantity;
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
