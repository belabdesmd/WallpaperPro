package com.devalutix.wallpaperpro.pojo;


public class Category {

    private String categoryName;
    private String categoryIconUrl;
    private String categoryThumbnailUrl;

    //Constructor
    public Category(String categoryName, String categoryImageUrl, String categoryThumbnailUrl) {
        this.categoryName = categoryName;
        this.categoryIconUrl = categoryImageUrl;
        this.categoryThumbnailUrl = categoryThumbnailUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryIconUrl() {
        return categoryIconUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return categoryName.equals(category.categoryName);
    }

    public String getCategoryThumbnailUrl() {
        return categoryThumbnailUrl;
    }
}
