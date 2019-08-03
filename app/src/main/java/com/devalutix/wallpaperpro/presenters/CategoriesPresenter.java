package com.devalutix.wallpaperpro.presenters;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.CategoriesContract;
import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.ui.activities.WallpapersActivity;
import com.devalutix.wallpaperpro.ui.fragments.CategoriesFragment;
import com.devalutix.wallpaperpro.utils.ApiEndpointInterface;
import com.devalutix.wallpaperpro.utils.Config;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesPresenter implements CategoriesContract.Presenter {

    /***************************************** Declarations ***************************************/
    private CategoriesFragment mView;
    private ApiEndpointInterface apiService;

    /***************************************** Constructor ****************************************/
    public CategoriesPresenter(ApiEndpointInterface apiService) {
        this.apiService = apiService;
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(CategoriesContract.View view) {
        mView = (CategoriesFragment) view;
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

        Call<ArrayList<Category>> call = apiService.getCategories(Config.TOKEN);

        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Category>> call,
                                   @NonNull Response<ArrayList<Category>> response) {
                if (response.isSuccessful()) {
                    //Init Categories List and Show It
                    mView.initRecyclerView(response.body());
                    mView.showCategoriesList();

                } else {
                    //Init Recycler View With Null and Show No Network + The Problem
                    mView.initRecyclerView(null);
                    mView.showNoNetwork();
                    mView.showRetryCard(mView.getResources().getString(R.string.server_prblm_retry));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Category>> call, @NonNull Throwable t) {
                //Init Recycler View With Null and Show No Network + The Problem
                mView.initRecyclerView(null);
                mView.showNoNetwork();
                mView.showRetryCard(mView.getResources().getString(R.string.net_prblm_retry));
            }
        });
    }

    @Override
    public void updateRecyclerView() {

        Call<ArrayList<Category>> call1 = apiService.getCategories(Config.TOKEN);

        call1.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Category>> call,
                                   @NonNull Response<ArrayList<Category>> response) {
                if (response.isSuccessful()) {
                    //Init Categories List and Show It
                    mView.updateRecyclerView(response.body());
                    mView.showCategoriesList();
                } else {
                    //Init Recycler View With Null and Show No Network + The Problem
                    mView.updateRecyclerView(null);
                    mView.showNoNetwork();
                    mView.showRetryCard(mView.getResources().getString(R.string.server_prblm_retry));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Category>> call, @NonNull Throwable t) {
                //Init Recycler View With Null and Show No Network + The Problem
                mView.updateRecyclerView(null);
                mView.showNoNetwork();
                mView.showRetryCard(mView.getResources().getString(R.string.net_prblm_retry));
            }
        });
    }

    @Override
    public void goToWallpapers(String categoryName) {
        Intent goToWallpaper = new Intent(mView.getActivity(), WallpapersActivity.class);

        //Putting the Extras
        goToWallpaper.putExtra("name", categoryName);
        goToWallpaper.putExtra("mode", "category");
        goToWallpaper.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mView.startActivity(goToWallpaper);
    }
}
