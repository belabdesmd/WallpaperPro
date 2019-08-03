package com.devalutix.wallpaperpro.presenters;

import androidx.annotation.NonNull;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.WallpapersContract;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.pojo.Wallpaper;
import com.devalutix.wallpaperpro.ui.activities.WallpapersActivity;
import com.devalutix.wallpaperpro.utils.ApiEndpointInterface;
import com.devalutix.wallpaperpro.utils.Config;
import com.google.gson.Gson;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WallpapersPresenter implements WallpapersContract.Presenter {

    /***************************************** Declarations ***************************************/
    private WallpapersActivity mView;
    private Gson gson;
    private String mode;
    private SharedPreferencesHelper mSharedPrefsHelper;
    private ApiEndpointInterface apiService;

    /***************************************** Constructor ****************************************/
    public WallpapersPresenter(Gson gson, SharedPreferencesHelper mSharedPrefsHelper
            , ApiEndpointInterface apiService) {
        this.gson = gson;
        this.mSharedPrefsHelper = mSharedPrefsHelper;
        this.apiService = apiService;
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(WallpapersContract.View view) {
        mView = (WallpapersActivity) view;
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
        this.mode = mode;

        Call<ArrayList<Wallpaper>> call = null;

        switch (mode) {
            case "collection": {
                ArrayList<Wallpaper> wallpapers = getWallpapersFromCollection(name);
                mView.initRecyclerView(wallpapers);

                if (wallpapers.size() > 0) mView.showPicturesList();
                else mView.showEmptyCollection(mView.getResources().getString(R.string.empty_collection));
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

                        assert response.body() != null;
                        if (response.body().size() > 0) mView.showPicturesList();
                        else {
                            if (mode.equals("search"))
                                mView.showEmptyCollection(mView.getResources().getString(R.string.empty_search));
                            else mView.showEmptyCollection(mView.getResources().getString(R.string.empty_category));
                        }
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
        Call<ArrayList<Wallpaper>> call = null;

        switch (mode) {
            case "collection": {
                ArrayList<Wallpaper> wallpapers = getWallpapersFromCollection(name);
                mView.updateRecyclerView(wallpapers);

                if (wallpapers.size() > 0) mView.showPicturesList();
                else mView.showEmptyCollection(mView.getResources().getString(R.string.empty_collection));
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

                        assert response.body() != null;
                        if (response.body().size() > 0) mView.showPicturesList();
                        else {
                            if (mode.equals("search"))
                                mView.showEmptyCollection(mView.getResources().getString(R.string.empty_search));
                            else mView.showEmptyCollection(mView.getResources().getString(R.string.empty_category));
                        }
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
    public ArrayList<Wallpaper> getWallpapersFromCollection(String name) {

        ArrayList<Collection> allCollections = mSharedPrefsHelper.getCollections();
        ArrayList<Wallpaper> wallpapers = new ArrayList<>();

        boolean found = false;
        int i = 0;

        while (!found && i < allCollections.size()) {
            if (allCollections.get(i).getCollectionName().equals(name)) {
                wallpapers = allCollections.get(i).getCollectionPictures();
                found = true;
            } else i++;
        }

        return wallpapers;
    }

    @Override
    public String getMode() {
        return mode;
    }

    @Override
    public String listToString(ArrayList<Wallpaper> wallpapers) {
        return gson.toJson(wallpapers);
    }
}
