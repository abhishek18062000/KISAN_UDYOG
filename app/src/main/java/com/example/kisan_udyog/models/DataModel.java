package com.example.kisan_udyog.models;

public class DataModel {
    private String name;
    private String imgUrl;

    public DataModel() {

    }
    public DataModel(String name, String imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
