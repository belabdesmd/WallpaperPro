package com.devalutix.wallpaperpro.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.devalutix.wallpaperpro.contracts.ExploreContract;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.ui.fragments.ExploreFragment;
import com.google.gson.Gson;

import java.util.ArrayList;

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
        return null;
    }

    @Override
    public ArrayList<Image> getPopular() {
        return null;
    }

    @Override
    public ArrayList<Image> getFeatured() {
        return null;
    }

    @Override
    public String listToString(ArrayList<Image> images) {
        return gson.toJson(images);
    }
}
