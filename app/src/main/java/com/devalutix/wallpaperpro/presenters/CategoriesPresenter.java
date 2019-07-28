package com.devalutix.wallpaperpro.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.CategoriesContract;
import com.devalutix.wallpaperpro.di.annotations.ApplicationContext;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.ui.fragments.CategoriesFragment;
import com.devalutix.wallpaperpro.utils.ApiEndpointInterface;
import com.devalutix.wallpaperpro.utils.Config;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesPresenter implements CategoriesContract.Presenter {
    private static String TAG = "CategoriesPresenter";

    /***************************************** Declarations ***************************************/
    private CategoriesFragment mView;
    private Context mContext;
    private SharedPreferencesHelper mSharedPrefsHelper;
    private ApiEndpointInterface apiService;

    /***************************************** Constructor ****************************************/
    public CategoriesPresenter(@ApplicationContext Context mContext, SharedPreferencesHelper mSharedPrefsHelper,
                               ApiEndpointInterface apiService) {
        this.mContext = mContext;
        this.mSharedPrefsHelper = mSharedPrefsHelper;
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
        Log.d(TAG, "initRecyclerView: Initialising Recycler View");

        Call<ArrayList<Category>> call = apiService.getCategories(Config.TOKEN);

        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Category>> call,
                                   @NonNull Response<ArrayList<Category>> response) {
                if (response.isSuccessful()) {
                    mView.initRecyclerView(response.body());
                    mView.showCategoriesList();
                } else {
                    mView.initRecyclerView(null);
                    mView.showNoNetwork();
                    mView.showRetryCard(mView.getResources().getString(R.string.server_prblm_retry));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                mView.initRecyclerView(null);
                mView.showNoNetwork();
                mView.showRetryCard(mView.getResources().getString(R.string.net_prblm_retry));
            }
        });
    }

    @Override
    public void updateRecyclerView() {
        Log.d(TAG, "updateRecyclerView: Updating Recycler View");

        Call<ArrayList<Category>> call = apiService.getCategories(Config.TOKEN);

        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Category>> call,
                                   @NonNull Response<ArrayList<Category>> response) {
                if (response.isSuccessful()) {
                    mView.updateRecyclerView(response.body());
                    mView.showCategoriesList();
                } else {
                    mView.updateRecyclerView(null);
                    mView.showNoNetwork();
                    mView.showRetryCard(mView.getResources().getString(R.string.server_prblm_retry));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Category>> call, @NonNull Throwable t) {
                mView.updateRecyclerView(null);
                mView.showNoNetwork();
                mView.showRetryCard(mView.getResources().getString(R.string.net_prblm_retry));
            }
        });
    }
}
