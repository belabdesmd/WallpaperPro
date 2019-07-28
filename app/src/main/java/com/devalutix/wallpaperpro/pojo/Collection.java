package com.devalutix.wallpaperpro.pojo;

import java.util.ArrayList;
import java.util.Objects;

public class Collection {

    private String collectionName;
    private ArrayList<Wallpaper> collectionPictures;

    //Constructor
    public Collection(String collectionName, ArrayList<Wallpaper> collectionPictures) {
        this.collectionName = collectionName;
        this.collectionPictures = collectionPictures;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public ArrayList<Wallpaper> getCollectionPictures() {
        return collectionPictures;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collection that = (Collection) o;
        return Objects.equals(collectionName, that.collectionName);
    }
}
