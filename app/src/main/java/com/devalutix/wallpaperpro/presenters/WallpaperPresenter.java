package com.devalutix.wallpaperpro.presenters;

import android.os.Handler;

import com.devalutix.wallpaperpro.contracts.WallpaperContract;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.ui.activities.WallpaperActivity;
import com.devalutix.wallpaperpro.utils.Config;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class WallpaperPresenter implements WallpaperContract.Presenter {
    private static String TAG = "WallpaperPresenter";

    //Declarations
    private WallpaperActivity mView;
    private Gson gson;
    private SharedPreferencesHelper mSharedPrefsHelper;

    //Constructor
    public WallpaperPresenter(Gson gson, SharedPreferencesHelper sharedPreferencesHelper) {
        this.gson = gson;
        mSharedPrefsHelper = sharedPreferencesHelper;
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
    public void savePicture(int position) {

    }

    @Override
    public void likePicture(int position) {
        mView.enableLikes();
    }

    @Override
    public void dislikePicture(int position) {
        mView.enableDislikes();
    }

    @Override
    public void addToFavorites(Image image) {
        mView.showFavorite();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mView.hideFavorite();
            }
        }, 3000);


    }

    @Override
    public void addRating(Image image) {

    }

    @Override
    public void sharePicture(int position) {

    }

    @Override
    public ArrayList<Image> getImages(String images) {
        Image[] imageUrlsArray = gson.fromJson(images, Image[].class);
        return new ArrayList<Image>(Arrays.asList(imageUrlsArray));
    }

    @Override
    public ArrayList<Collection> getCollections() {
        return mSharedPrefsHelper.getCollections();
    }
}
