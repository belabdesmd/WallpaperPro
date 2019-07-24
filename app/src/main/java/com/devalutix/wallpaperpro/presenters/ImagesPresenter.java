package com.devalutix.wallpaperpro.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.devalutix.wallpaperpro.contracts.ImagesContract;
import com.devalutix.wallpaperpro.di.annotations.ApplicationContext;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.ui.activities.ImagesActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ImagesPresenter implements ImagesContract.Presenter {
    private static String TAG = "ImagesPresenter";

    /***************************************** Declarations ***************************************/
    private ImagesActivity mView;
    private Gson gson;
    private Context mContext;
    private String mode;
    private SharedPreferencesHelper mSharedPrefsHelper;

    /***************************************** Constructor ****************************************/
    public ImagesPresenter(@ApplicationContext Context mContext, Gson gson, SharedPreferencesHelper mSharedPrefsHelper) {
        this.gson = gson;
        this.mContext = mContext;
        this.mSharedPrefsHelper = mSharedPrefsHelper;
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(ImagesContract.View view) {
        mView = (ImagesActivity) view;
    }

    @Override
    public void dettach() {
        mView = null;
    }

    @Override
    public boolean isAttached() {
        return !(mView == null);
    }

    /***************************************** Methods ********************************************/
    @Override
    public boolean hasInternetNetwork() {
        ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    }

    @Override
    public void initRecyclerView(String mode, String name) {
        Log.d(TAG, "initRecyclerView: Initializing Recycler View");

        ArrayList<Image> images = null;
        this.mode = mode;

        switch (mode) {
            case "collection": {
                images = getImagesFromCollection(name);
            }
            break;
            case "category": {
                images = getImageFromCategory(name);
            }
            break;
        }

        mView.initRecyclerView(images);

        //Init Recycler View
        if (!hasInternetNetwork() || images == null)
            mView.showNoNetwork();
        else
            mView.showPicturesList();
    }

    @Override
    public void updateRecyclerView(String name) {
        Log.d(TAG, "updateRecyclerView: Updating Recycler View");

        ArrayList<Image> images = null;
        switch (mode) {
            case "collection": {
                images = getImagesFromCollection(name);
            }
            break;
            case "category": {
                images = getImageFromCategory(name);
            }
            break;
        }

        //Init Recycler View
        if (!hasInternetNetwork() || images == null) mView.showNoNetwork();
        else {
            mView.updateRecyclerView(images);
            mView.showPicturesList();
        }
    }

    @Override
    public ArrayList<Image> getImagesFromCollection(String name) {
        Log.d(TAG, "getImagesFromCollection: Getting Images From Collection");

        ArrayList<Collection> allCollections = mSharedPrefsHelper.getCollections();
        ArrayList<Image> images = new ArrayList<>();

        boolean found = false;
        int i = 0;

        while (!found && i < allCollections.size()) {
            if (allCollections.get(i).getCollectionName().equals(name)) {
                images = allCollections.get(i).getCollectionPictures();
                found = true;
            } else i++;
        }

        return images;
    }

    @Override
    public ArrayList<Image> getImageFromCategory(String name) {

        //TODO: Get Category Images From Server Of the Category name passed as parameter

        Image[] categoryItem = gson.fromJson(readJSONFromAsset(), Image[].class);
        ArrayList<Image> recentImages = new ArrayList<>(Arrays.asList(categoryItem));

        ArrayList<Image> images = new ArrayList<>();

        for (Image image :
                recentImages) {
            if (image.getImageCategories().contains(name))
                images.add(image);
        }

        return images;
    }

    @Override
    public String getThumbnail(String mode, String name) {
        Log.d(TAG, "getThumbnail: Getting Thumbnail");

        switch (mode) {
            case "collection":
                if (!getImagesFromCollection(name).isEmpty())
                    return getImagesFromCollection(name).get(0).getImageUrl();
                break;
            case "category":
                return Objects.requireNonNull(getCategory(name)).getCategoryThumbnailUrl();
        }

        return null;
    }

    private Category getCategory(String categoryName) {

        //TODO: Get Category From Server Of the Category name passed as parameter

        for (Category category :
                mSharedPrefsHelper.getCategories()) {
            if (category.getCategoryName().equals(categoryName))
                return category;
        }

        return null;
    }

    private String readJSONFromAsset() {
        String json;
        try {
            InputStream is = mContext.getAssets().open("images.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public String listToString(ArrayList<Image> images) {
        return gson.toJson(images);
    }
}
