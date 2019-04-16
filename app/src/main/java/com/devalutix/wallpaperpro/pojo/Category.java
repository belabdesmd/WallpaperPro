package com.devalutix.wallpaperpro.pojo;

import lombok.Getter;

public class Category {

    @Getter
    private String categoryName;

    @Getter
    private String categoryImageUrl;

    //Constructor
    public Category(String categoryName, String categoryImageUrl) {
        this.categoryName = categoryName;
        this.categoryImageUrl = categoryImageUrl;
    }
}
