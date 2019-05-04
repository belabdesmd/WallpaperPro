package com.devalutix.wallpaperpro.pojo;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class Collection {

    private String collectioName;

    private ArrayList<Image> collectionPictures;

    //Constructor
    public Collection(String collectioName, ArrayList<Image> collectionPictures) {
        this.collectioName = collectioName;
        this.collectionPictures = collectionPictures;
    }

    public String getCollectioName() {
        return collectioName;
    }

    public ArrayList<Image> getCollectionPictures() {
        return collectionPictures;
    }

    public void setCollectioName(String collectioName) {
        this.collectioName = collectioName;
    }
}
