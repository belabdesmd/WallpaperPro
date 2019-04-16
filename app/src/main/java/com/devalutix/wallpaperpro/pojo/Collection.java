package com.devalutix.wallpaperpro.pojo;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class Collection {

    @Getter @Setter
    private String collectioName;

    @Getter
    private ArrayList<Image> collectionPictures;

    //Constructor
    public Collection(String collectioName, ArrayList<Image> collectionPictures) {
        this.collectioName = collectioName;
        this.collectionPictures = collectionPictures;
    }
}
