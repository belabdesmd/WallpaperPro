package com.devalutix.wallpaperpro.pojo;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class Image {

    @Getter
    private String imageID;

    @Getter
    private String imageTitle;

    @Getter
    private String imageUrl;

    @Getter
    private float imageRating;

    @Getter @Setter
    private int imageViews;

    @Getter @Setter
    private int imageDownloads;

    @Getter
    private ArrayList<String> imageCategories;

    @Getter
    private ArrayList<String> imageTags;

    //Constructor
    public Image(String imageID, String imageTitle, String imageUrl, float imageRating,
                 int imageViews, int imageDownloads, ArrayList<String> imageCategories,
                 ArrayList<String> imageTags) {
        this.imageID = imageID;
        this.imageTitle = imageTitle;
        this.imageUrl = imageUrl;
        this.imageRating = imageRating;
        this.imageViews = imageViews;
        this.imageDownloads = imageDownloads;
        this.imageCategories = imageCategories;
        this.imageTags = imageTags;
    }
}
