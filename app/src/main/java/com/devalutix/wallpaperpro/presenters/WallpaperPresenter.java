package com.devalutix.wallpaperpro.presenters;

import com.devalutix.wallpaperpro.contracts.CategoriesContract;
import com.devalutix.wallpaperpro.contracts.WallpaperContract;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.ui.activities.MainActivity;
import com.devalutix.wallpaperpro.ui.activities.WallpaperActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

public class WallpaperPresenter implements WallpaperContract.Presenter {
    private static String TAG = "WallpaperPresenter";

    //Declarations
    private WallpaperActivity mView;
    private Gson gson;

    //Constructor
    public WallpaperPresenter(Gson gson) {
        this.gson = gson;
    }

    //Essential Methods
    @Override
    public void attach(WallpaperContract.View view) {
        mView = (WallpaperActivity) view;
    }

    @Override
    public void dettach() {
        mView = null;
    }

    @Override
    public boolean isAttached() {
        return !(mView == null);
    }

    //Methods
    @Override
    public void savePicture(String url) {

    }

    @Override
    public void addToFavorites() {

    }

    @Override
    public void addRating(Image image) {

    }

    @Override
    public void sharePicture() {

    }

    @Override
    public ArrayList<Image> getImages(String images) {
        Image[] imageUrlsArray = gson.fromJson(images, Image[].class);
        return new ArrayList<Image>(Arrays.asList(imageUrlsArray));
    }
}
