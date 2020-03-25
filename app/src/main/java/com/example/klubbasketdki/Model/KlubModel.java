package com.example.klubbasketdki.Model;

import com.google.firebase.database.Exclude;

public class KlubModel {

    private String name;
    private String imageURL;
    private String key;
    private String description;
    private String lokasi;
    private String latitude;
    private String longitude;
    private String telepon;
    private String instagram;

    private int position;

    public KlubModel() {
        //empty constructor needed
    }
    public KlubModel(int position){
        this.position = position;
    }
    public KlubModel(String name, String imageUrl , String Des, String Lokasi, String latitude, String longitude, String telepon, String instagram) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        this.name = name;
        this.imageURL = imageUrl;
        this.description = Des;
        this.lokasi = Lokasi;
        this.latitude = latitude;
        this.longitude = longitude;
        this.telepon= telepon;
        this.instagram = instagram;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
