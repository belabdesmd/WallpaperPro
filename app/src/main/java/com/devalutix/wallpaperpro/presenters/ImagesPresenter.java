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

import java.util.ArrayList;

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
        ConnectivityManager conMgr =  (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    }

    @Override
    public void initRecyclerView(String mode,String name) {
        ArrayList<Image> images = null;
        this.mode = mode;
        switch (mode){
            case "collection":{
                images = getImagesFromCollection(name);
                mView.showCollectionActions();
            }
                break;
            case "category":{
                images = getImageFromCategory(name);
                mView.hideCollectionActions();
            }
                break;
        }

        //Set Page Name
        mView.setPageName(name);

        //Init Recycler View
        if (!checkNetwork() || images == null) mView.showNoNetwork();
        else{
            mView.initRecyclerView(images);
            mView.showPicturesList();
        }
    }

    @Override
    public void updateRecyclerView(String name) {
        ArrayList<Image> images = null;
        switch (mode){
            case "collection":{
                images = getImagesFromCollection(name);
                mView.showCollectionActions();
            }
            break;
            case "category":{
                images = getImageFromCategory(name);
                mView.hideCollectionActions();
            }
            break;
        }

        //Set Page Name
        mView.setPageName(name);

        //Init Recycler View
        if (!checkNetwork() || images == null) mView.showNoNetwork();
        else{
            mView.updateRecyclerView(images);
            mView.showPicturesList();
        }
    }

    @Override
    public ArrayList<Image> getImagesFromCollection(String name) {

        //TODO: Get all Images From Database
        ArrayList<Image> allImages = new ArrayList<>();
        ArrayList<Image> images = new ArrayList<>();

        for (Image image: allImages) {
            if (image.getImageCategories().contains(name)) images.add(image);
        }

        return images;
    }

    @Override
    public ArrayList<Image> getImageFromCategory(String name) {
        ArrayList<Collection> allCollections =  mSharedPrefsHelper.getCollections();
        ArrayList<Image> images = new ArrayList<>();

        boolean found = false;
        int i = 0;

        while(!found && i < allCollections.size()){
            if (allCollections.get(i).getCollectioName().equals(name)){
                images = allCollections.get(i).getCollectionPictures();
                found = true;
            }else i++;
        }

        return images;
    }

    @Override
    public String listToString(ArrayList<Image> images) {
        return gson.toJson(images);
    }
}
