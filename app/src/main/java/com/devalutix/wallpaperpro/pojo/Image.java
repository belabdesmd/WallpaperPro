package com.devalutix.wallpaperpro.pojo;

import java.util.ArrayList;
import java.util.Date;

public class Image {

    private String imageID;

    private String imageTitle;

    private String imageUrl;

    private int imageViews;

    private int imageDownloads;

    private ArrayList<String> imageCategories;

    private ArrayList<String> imageTags;

    private String imageDateAdded;

    private int imageLikes;

    private int imageDislikes;

    private String imageOwner;

    //Constructor
    public Image(String imageID, String imageTitle, String imageUrl,
                 int imageViews, int imageDownloads, ArrayList<String> imageCategories,
                 ArrayList<String> imageTags, String imageDateAdded, int imageLikes, int imageDislikes, String imageOwner) {
        this.imageID = imageID;
        this.imageTitle = imageTitle;
        this.imageUrl = imageUrl;
        this.imageViews = imageViews;
        this.imageDownloads = imageDownloads;
        this.imageCategories = imageCategories;
        this.imageTags = imageTags;
        this.imageDateAdded = imageDateAdded;
        this.imageLikes = imageLikes;
        this.imageDislikes = imageDislikes;
        this.imageOwner = imageOwner;
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

    public String getImageDateAdded() {
        return imageDateAdded;
    }

    public int getImageLikes() {
        return imageLikes;
    }

    public int getImageDislikes() {
        return imageDislikes;
    }

    public String getImageOwner() {
        return imageOwner;
    }
}
