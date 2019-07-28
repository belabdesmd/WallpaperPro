package com.devalutix.wallpaperpro.presenters;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.ExploreContract;
import com.devalutix.wallpaperpro.di.annotations.ApplicationContext;
import com.devalutix.wallpaperpro.pojo.Wallpaper;
import com.devalutix.wallpaperpro.ui.fragments.ExploreFragment;
import com.devalutix.wallpaperpro.utils.ApiEndpointInterface;
import com.devalutix.wallpaperpro.utils.Config;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExplorePresenter implements ExploreContract.Presenter {
    private static String TAG = "ExplorePresenter";

    /***************************************** Declarations ***************************************/
    private ExploreFragment mView;
    private Context mContext;
    private Gson gson;
    private ApiEndpointInterface apiService;
    private String mode = "recent";

    /***************************************** Constructor ****************************************/
    public ExplorePresenter(@ApplicationContext Context mContext, Gson gson, ApiEndpointInterface apiService) {
        this.mContext = mContext;
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
        Log.d(TAG, "initRecyclerView: Initialising Recycler View");

        Call<ArrayList<Wallpaper>> call = apiService.getRecentImages(Config.TOKEN);
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
                    mView.showRetryCard(mView.getResources().getString(R.string.server_prblm_retry));
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
    public void updateRecyclerView(String mode) {
        Log.d(TAG, "updateRecyclerView: Updating Recycler View");

        this.mode = mode;
        Call<ArrayList<Wallpaper>> call = null;

        switch (mode) {
            case "popular": {
                call = apiService.getPopularImages(Config.TOKEN);
            }
            break;
            case "recent": {
                call = apiService.getRecentImages(Config.TOKEN);
            }
            break;
            case "featured": {
                call = apiService.getFeaturedImages(Config.TOKEN);
            }
            break;
        }

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

    @Override
    public String listToString(ArrayList<Wallpaper> images) {
        return gson.toJson(images);
    }

    @Override
    public String getMode() {
        return mode;
    }
}
