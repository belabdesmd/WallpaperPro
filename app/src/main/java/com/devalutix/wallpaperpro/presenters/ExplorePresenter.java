package com.devalutix.wallpaperpro.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.devalutix.wallpaperpro.contracts.ExploreContract;
import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.ui.fragments.ExploreFragment;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ExplorePresenter implements ExploreContract.Presenter {
    private static String TAG = "ExplorePresenter";

    //Declarations
    private ExploreFragment mView;
    private Gson gson;
    private Context mContext;

    //Constructor
    public ExplorePresenter(Context mContext, Gson gson) {
        this.mContext = mContext;
        this.gson = gson;
    }

    //Essential Methods
    @Override
    public void attach(ExploreContract.View view) {
        mView = (ExploreFragment) view;
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
    public void initRecyclerView() {
        ArrayList<Image> images = getRecent();

        mView.initRecyclerView(images);
        if (!checkNetwork() || images == null) mView.showNoNetwork();
        else mView.showPicturesList();
    }

    @Override
    public void updateRecyclerView(String mode) {
        ArrayList<Image> images = null;
        switch (mode){
            case "popular":
                images = getPopular();
                break;
            case "recent":
                images = getRecent();
                break;
            case "featured":
                images = getFeatured();
                break;
        }

        mView.updateRecyclerView(images);

        if (!checkNetwork() || images == null) mView.showNoNetwork();
        else mView.showPicturesList();
    }

    @Override
    public ArrayList<Image> getRecent() {
        Image[] collectionItem = gson.fromJson(readJSONFromAsset(), Image[].class);
        return new ArrayList<Image>(Arrays.asList(collectionItem));
    }

    @Override
    public ArrayList<Image> getPopular() {
        return getRecent();
    }

    @Override
    public ArrayList<Image> getFeatured() {
        return getRecent();
    }

    @Override
    public String listToString(ArrayList<Image> images) {
        return gson.toJson(images);
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
