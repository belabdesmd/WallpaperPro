package com.devalutix.wallpaperpro.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.ImagesContract;
import com.devalutix.wallpaperpro.di.annotations.ApplicationContext;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.pojo.Wallpaper;
import com.devalutix.wallpaperpro.ui.activities.ImagesActivity;
import com.devalutix.wallpaperpro.utils.ApiEndpointInterface;
import com.devalutix.wallpaperpro.utils.Config;
import com.google.gson.Gson;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagesPresenter implements ImagesContract.Presenter {
    private static String TAG = "ImagesPresenter";

    /***************************************** Declarations ***************************************/
    private ImagesActivity mView;
    private Gson gson;
    private Context mContext;
    private String mode;
    private SharedPreferencesHelper mSharedPrefsHelper;
    private ApiEndpointInterface apiService;

    /***************************************** Constructor ****************************************/
    public ImagesPresenter(@ApplicationContext Context mContext, Gson gson, SharedPreferencesHelper mSharedPrefsHelper
            , ApiEndpointInterface apiService) {
        this.gson = gson;
        this.mContext = mContext;
        this.mSharedPrefsHelper = mSharedPrefsHelper;
        this.apiService = apiService;
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
    public void initRecyclerView(String mode, String name) {
        Log.d(TAG, "initRecyclerView: Initializing Recycler View");

        this.mode = mode;

        Call<ArrayList<Wallpaper>> call = null;

        switch (mode) {
            case "collection": {
                ArrayList<Wallpaper> images = getImagesFromCollection(name);
                mView.initRecyclerView(images);

                if (images.size() > 0) mView.showPicturesList();
                else mView.showEmptyCollection();
            }
            break;
            case "category": {
                call = apiService.getCategoryWallpapers(Config.TOKEN, name);
            }
            break;
            case "search": {
                call = apiService.searchWallpapers(Config.TOKEN, name);
            }
            break;
        }

        if (mode.equals("search") || mode.equals("category"))
            call.enqueue(new Callback<ArrayList<Wallpaper>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Wallpaper>> call,
                                       @NonNull Response<ArrayList<Wallpaper>> response) {
                    if (response.isSuccessful()) {
                        mView.initRecyclerView(response.body());
                        mView.showPicturesList();
                    } else {
                        mView.initRecyclerView(null);
                        mView.showNoNetwork();
                        (mView).showRetryCard(mView.getResources().getString(R.string.server_prblm_retry));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Wallpaper>> call, @NonNull Throwable t) {
                    mView.initRecyclerView(null);
                    mView.showNoNetwork();
                    mView.showRetryCard(mView.getResources().getString(R.string.net_prblm_retry));
                }
            });
    }

    @Override
    public void updateRecyclerView(String name) {
        Log.d(TAG, "updateRecyclerView: Updating Recycler View");

        Call<ArrayList<Wallpaper>> call = null;

        switch (mode) {
            case "collection": {
                ArrayList<Wallpaper> images = getImagesFromCollection(name);
                mView.updateRecyclerView(images);

                if (images.size() > 0) mView.showPicturesList();
                else mView.showEmptyCollection();
            }
            break;
            case "category": {
                call = apiService.getCategoryWallpapers(Config.TOKEN, name);
            }
            break;
            case "search": {
                call = apiService.searchWallpapers(Config.TOKEN, name);
            }
            break;
        }

        if (mode.equals("search") || mode.equals("category")) {
            assert call != null;
            call.enqueue(new Callback<ArrayList<Wallpaper>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Wallpaper>> call,
                                       @NonNull Response<ArrayList<Wallpaper>> response) {
                    if (response.isSuccessful()) {
                        mView.updateRecyclerView(response.body());
                        mView.showPicturesList();
                    } else {
                        mView.updateRecyclerView(null);
                        mView.showNoNetwork();
                        mView.showRetryCard(mView.getResources().getString(R.string.server_prblm_retry));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Wallpaper>> call, @NonNull Throwable t) {
                    mView.updateRecyclerView(null);
                    mView.showNoNetwork();
                    mView.showRetryCard(mView.getResources().getString(R.string.net_prblm_retry));
                }
            });
        }
    }

    @Override
    public ArrayList<Wallpaper> getImagesFromCollection(String name) {
        Log.d(TAG, "getImagesFromCollection: Getting Images From Collection");

        ArrayList<Collection> allCollections = mSharedPrefsHelper.getCollections();
        ArrayList<Wallpaper> images = new ArrayList<>();

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
    public String getMode() {
        return mode;
    }

    @Override
    public String listToString(ArrayList<Wallpaper> images) {
        return gson.toJson(images);
    }
}
