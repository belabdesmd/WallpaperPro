package com.devalutix.wallpaperpro.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ImageS {

    @SerializedName("pk")
    @Expose
    private int pk;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("categories")
    @Expose
    private ArrayList<CategoryS> categories;
    @SerializedName("downloads")
    @Expose
    private int downloads;

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<CategoryS> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CategoryS> categories) {
        this.categories = categories;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

}