package com.example.klubbasketdki.Model;

import com.google.firebase.database.Exclude;

public class PelatihModel {

    private String nama;
    private String imageURL;
    private String key;
    private String keterangan;


    private int position;

    public PelatihModel() {
        //empty constructor needed
    }
    public PelatihModel(int position){
        this.position = position;
    }

    public PelatihModel(String nama, String imageURL, String keterangan) {
        if (nama.trim().equals("")){
            nama = "No Name";
        }
        this.nama = nama;
        this.imageURL = imageURL;
        this.keterangan = keterangan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
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
