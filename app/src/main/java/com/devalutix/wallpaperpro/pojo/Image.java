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

    private ArrayList<Category> imageCategories;

    private ArrayList<String> imageTags;

    private Date imageDateAdded;

    private int imageLikes;

    private int imageDislikes;

    private String imageOwner;

    //Constructor
    public Image(String imageID, String imageTitle, String imageUrl,
                 int imageViews, int imageDownloads, ArrayList<Category> imageCategories,
                 ArrayList<String> imageTags, Date imageDateAdded, int imageLikes, int imageDislikes, String imageOwner) {
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

    public float getImageRating() {
        return imageRating;
    }

    public int getImageViews() {
        return imageViews;
    }

    public int getImageDownloads() {
        return imageDownloads;
    }

    public ArrayList<Category> getImageCategories() {
        return imageCategories;
    }

    public ArrayList<String> getImageTags() {
        return imageTags;
    }

    public Date getImageDateAdded() {
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
