package com.devalutix.wallpaperpro.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.devalutix.wallpaperpro.contracts.CategoriesContract;
import com.devalutix.wallpaperpro.di.annotations.ApplicationContext;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.pojo.CategoryS;
import com.devalutix.wallpaperpro.ui.fragments.CategoriesFragment;
import com.devalutix.wallpaperpro.utils.ApiEndpointInterface;
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
    public boolean hasInternetConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    }

    @Override
    public void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: Initialising Recycler View");

        ArrayList<Category> categories = getCategories();

        //Init Recycler View
        mView.initRecyclerView(categories);

        //If There is no connection or there is a problem with the server: Show no Network
        if (!hasInternetConnection() || categories == null)
            mView.showNoNetwork();
        else
            mView.showCategoriesList();
    }

    @Override
    public void updateRecyclerView() {
        Log.d(TAG, "updateRecyclerView: Updating Recycler View");

        ArrayList<Category> categories = getCategories();

        //Update Recycler View
        mView.updateRecyclerView(categories);

        //If There is no connection or there is a problem with the server: Show no Network
        if (!hasInternetConnection() || categories == null)
            mView.showNoNetwork();
        else
            mView.showCategoriesList();
    }

    @Override
    public ArrayList<Category> getCategories() {

        ArrayList<CategoryS> categories;
        Call<ArrayList<CategoryS>> call = apiService.getCategories();

        call.enqueue(new Callback<ArrayList<CategoryS>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryS>> call, Response<ArrayList<CategoryS>> response) {
                //TODO: Get Response
                Toast.makeText(mView.getActivity(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                Toast.makeText(mView.getActivity(), "Message: " + response.message(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ArrayList<CategoryS>> call, Throwable t) {
                Toast.makeText(mView.getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Gson gson = new Gson();
        Category[] categoryItem = gson.fromJson(readJSONFromAsset(), Category[].class);

        return new ArrayList<>(Arrays.asList(categoryItem));
    }

    private String readJSONFromAsset() {
        String json;
        try {
            InputStream is = mContext.getAssets().open("categories.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
