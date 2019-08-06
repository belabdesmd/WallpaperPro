package com.devalutix.wallpaperpro.presenters;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.ExploreContract;
import com.devalutix.wallpaperpro.pojo.Wallpaper;
import com.devalutix.wallpaperpro.ui.activities.WallpaperActivity;
import com.devalutix.wallpaperpro.ui.fragments.ExploreFragment;
import com.devalutix.wallpaperpro.utils.ApiEndpointInterface;
import com.devalutix.wallpaperpro.utils.Config;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExplorePresenter implements ExploreContract.Presenter {

    /***************************************** Declarations ***************************************/
    private ExploreFragment mView;
    private Gson gson;
    private ApiEndpointInterface apiService;
    private String mode = "recent";

    /***************************************** Constructor ****************************************/
    public ExplorePresenter(Gson gson, ApiEndpointInterface apiService) {
        this.gson = gson;
        this.apiService = apiService;
    }

    /***************************************** Essential Methods **********************************/
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

    /***************************************** Methods ********************************************/
    @Override
    public void initRecyclerView() {

        Call<ArrayList<Wallpaper>> call = apiService.getRecentWallpapers(Config.TOKEN);

        call.enqueue(new Callback<ArrayList<Wallpaper>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Wallpaper>> call,
                                   @NonNull Response<ArrayList<Wallpaper>> response) {
                if (response.isSuccessful()) {
                    //Init Categories List and Show It
                    mView.initRecyclerView(response.body());
                    mView.showWallpapersList();

                    //Show No Images In Case there is none
                    assert response.body() != null;
                    if (response.body().size() > 0) mView.showWallpapersList();
                    else mView.showEmptyCollection(mView.getResources().getString(R.string.empty_images));
                } else {
                    //Init Recycler View With Null and Show No Network + The Problem
                    mView.initRecyclerView(null);
                    mView.showNoNetwork();
                    mView.showRetryCard(mView.getResources().getString(R.string.server_prblm_retry));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Wallpaper>> call, @NonNull Throwable t) {
                //Init Recycler View With Null and Show No Network + The Problem
                mView.initRecyclerView(null);
                mView.showNoNetwork();
                mView.showRetryCard(mView.getResources().getString(R.string.net_prblm_retry));
            }
        });
    }

    @Override
    public void updateRecyclerView(String mode) {

        this.mode = mode;
        Call<ArrayList<Wallpaper>> call = null;

        switch (mode) {
            case "popular": {
                call = apiService.getPopularWallpapers(Config.TOKEN);
            }
            break;
            case "recent": {
                call = apiService.getRecentWallpapers(Config.TOKEN);
            }
            break;
            case "featured": {
                call = apiService.getFeaturedWallpapers(Config.TOKEN);
            }
            break;
        }

        assert call != null;
        call.enqueue(new Callback<ArrayList<Wallpaper>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Wallpaper>> call,
                                   @NonNull Response<ArrayList<Wallpaper>> response) {
                if (response.isSuccessful()) {
                    //Update Categories List and Show It
                    mView.updateRecyclerView(response.body());
                    mView.showWallpapersList();

                    //Show No Images In Case there is none
                    assert response.body() != null;
                    if (response.body().size() > 0) mView.showWallpapersList();
                    else mView.showEmptyCollection(mView.getResources().getString(R.string.empty_images));
                } else {
                    //Update Recycler View With Null and Show No Network + The Problem
                    mView.updateRecyclerView(null);
                    mView.showNoNetwork();
                    mView.showRetryCard(mView.getResources().getString(R.string.server_prblm_retry));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Wallpaper>> call, @NonNull Throwable t) {
                //Update Recycler View With Null and Show No Network + The Problem
                mView.updateRecyclerView(null);
                mView.showNoNetwork();
                mView.showRetryCard(mView.getResources().getString(R.string.net_prblm_retry));
            }
        });
    }

    @Override
    public String listToString(ArrayList<Wallpaper> wallpapers) {
        //Transfer the Wallpapers List into a String to Send it to the WallpaperActivity
        return gson.toJson(wallpapers);
    }

    @Override
    public String getMode() {
        return mode;
    }

    @Override
    public void goToWallpaperActivity(int position, ArrayList<Wallpaper> wallpapers) {
        if (!mView.isRefreshing()) {
            Intent goToWallpaper = new Intent(mView.getActivity(), WallpaperActivity.class);

            //Transferring the List
            String jsonWallpapers = listToString(wallpapers);

            //Putting the Extras
            goToWallpaper.putExtra("current", position);
            goToWallpaper.putExtra("wallpapers", jsonWallpapers);
            goToWallpaper.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mView.startActivity(goToWallpaper);
        }
    }
}
