package com.devalutix.wallpaperpro.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.devalutix.wallpaperpro.contracts.ImagesContract;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.ui.activities.ImagesActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ImagesPresenter implements ImagesContract.Presenter {
    private static String TAG = "ImagesPresenter";

    //Declarations
    private ImagesActivity mView;
    private Gson gson;
    private Context mContext;
    private String mode;
    private SharedPreferencesHelper mSharedPrefsHelper;

    //Constructor
    public ImagesPresenter(Gson gson, Context mContext, SharedPreferencesHelper mSharedPrefsHelper) {
        this.gson = gson;
        this.mContext = mContext;
        this.mSharedPrefsHelper = mSharedPrefsHelper;
    }

    //Essential Methods
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

    //Methods
    @Override
    public boolean checkNetwork() {
        ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    }

    @Override
    public void initRecyclerView(String mode, String name) {
        ArrayList<Image> images = null;
        this.mode = mode;

        switch (mode) {
            case "collection": {
                images = getImagesFromCollection(name);
                mView.showCollectionActions();
            }
            break;
            case "category": {
                images = getImageFromCategory(name);
                mView.hideCollectionActions();
            }
            break;
        }

        mView.initRecyclerView(images);

        //Init Recycler View
        if (!checkNetwork() || images == null) mView.showNoNetwork();
        else mView.showPicturesList();
    }

    @Override
    public void updateRecyclerView(String name) {
        ArrayList<Image> images = null;
        switch (mode) {
            case "collection": {
                images = getImagesFromCollection(name);
                mView.showCollectionActions();
            }
            break;
            case "category": {
                images = getImageFromCategory(name);
                mView.hideCollectionActions();
            }
            break;
        }

        //Set Page Name
        mView.setPageName(name);

        //Init Recycler View
        if (!checkNetwork() || images == null) mView.showNoNetwork();
        else {
            mView.updateRecyclerView(images);
            mView.showPicturesList();
        }
    }

    @Override
    public ArrayList<Image> getImagesFromCollection(String name) {

        ArrayList<Collection> allCollections = mSharedPrefsHelper.getCollections();
        ArrayList<Image> images = new ArrayList<>();

        boolean found = false;
        int i = 0;

        while (!found && i < allCollections.size()) {
            if (allCollections.get(i).getCollectioName().equals(name)) {
                images = allCollections.get(i).getCollectionPictures();
                found = true;
            } else i++;
        }

        return images;
    }

    @Override
    public ArrayList<Image> getImageFromCategory(String name) {
        //TODO: Delete Dummy Data

        Image[] collectionItem = gson.fromJson(readJSONFromAsset(), Image[].class);
        ArrayList<Image> recentImages = new ArrayList<Image>(Arrays.asList(collectionItem));

        ArrayList<Image> images = new ArrayList<>();

        for (Image image :
                recentImages) {
            if (image.getImageCategories().contains(name))
                images.add(image);
        }

        return images;
    }

    @Override
    public String listToString(ArrayList<Image> images) {
        return gson.toJson(images);
    }

    @Override
    public String getThumbnail(String mode, String name) {
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

    @Override
    public void removeCollection(String name) {
        ArrayList<Collection> allCollections = mSharedPrefsHelper.getCollections();
        allCollections.remove(new Collection(name, null));

        mSharedPrefsHelper.saveCollections(allCollections);
    }

    @Override
    public void editCollection(String name, String newName) {
        ArrayList<Collection> allCollections = mSharedPrefsHelper.getCollections();
        for (Collection collection :
                allCollections) {
            if (collection.getCollectioName().equals(name)) collection.setCollectioName(newName);
        }

        mSharedPrefsHelper.saveCollections(allCollections);
        mView.setPageName(newName);
    }

    private Category getCategory(String name) {
        for (Category category :
                mSharedPrefsHelper.getCategories()) {
            if (category.getCategoryName().equals(name))
                return category;
        }

        return null;
    }

    private String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("images.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
