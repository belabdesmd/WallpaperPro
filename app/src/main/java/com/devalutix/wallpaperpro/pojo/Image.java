package com.devalutix.wallpaperpro.pojo;

import java.util.ArrayList;
import java.util.Date;

public class Image {

    private String imageID;

    private String imageTitle;

    private String imageUrl;

    private float imageRating;

    private int imageViews;

    private int imageDownloads;

    private ArrayList<String> imageCategories;

    private ArrayList<String> imageTags;

    private Date imageDateAdded;

    //Constructor
    public Image(String imageID, String imageTitle, String imageUrl, float imageRating,
                 int imageViews, int imageDownloads, ArrayList<String> imageCategories,
                 ArrayList<String> imageTags, Date imageDateAdded) {
        this.imageID = imageID;
        this.imageTitle = imageTitle;
        this.imageUrl = imageUrl;
        this.imageRating = imageRating;
        this.imageViews = imageViews;
        this.imageDownloads = imageDownloads;
        this.imageCategories = imageCategories;
        this.imageTags = imageTags;
        this.imageDateAdded = imageDateAdded;
    }

    public String getImageID() {
        return imageID;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public float getImageRating() {
        return imageRating;
    }

    public int getImageViews() {
        return imageViews;
    }

    public int getImageDownloads() {
        return imageDownloads;
    }

    public ArrayList<String> getImageCategories() {
        return imageCategories;
    }

    public ArrayList<String> getImageTags() {
        return imageTags;
    }

    public Date getImageDateAdded() {
        return imageDateAdded;
    }
}
