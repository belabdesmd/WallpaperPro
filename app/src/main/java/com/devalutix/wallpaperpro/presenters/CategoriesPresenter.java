package com.devalutix.wallpaperpro.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.devalutix.wallpaperpro.contracts.CategoriesContract;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.ui.fragments.CategoriesFragment;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class CategoriesPresenter implements CategoriesContract.Presenter {
    private static String TAG = "CategoriesPresenter";

    //Declarations
    private CategoriesFragment mView;
    private Context mContext;
    private SharedPreferencesHelper mSharedPrefsHelper;

    //Constructor
    public CategoriesPresenter(Context mContext, SharedPreferencesHelper mSharedPrefsHelper) {
        this.mContext = mContext;
        this.mSharedPrefsHelper = mSharedPrefsHelper;
    }

    //Essential Methods
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

    //Methods
    @Override
    public boolean checkNetwork() {
        ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    }

    @Override
    public void initRecyclerView() {
        ArrayList<Category> categories = getCategories();

        if (!checkNetwork() || categories == null) mView.showNoNetwork();
        else {
            mView.initRecyclerView(categories);
            mView.showCategoriesList();
        }
    }

    @Override
    public void updateRecyclerView() {
        ArrayList<Category> categories = getCategories();

        if (!checkNetwork() || categories == null) mView.showNoNetwork();
        else {
            mView.updateRecyclerView(categories);
            mView.showCategoriesList();
        }
    }

    @Override
    public ArrayList<Category> getCategories() {

        //TODO: Remove Dummy Data

        Gson gson = new Gson();
        Category[] categoryItem = gson.fromJson(readJSONFromAsset(), Category[].class);
        ArrayList<Category> categories = new ArrayList<Category>(Arrays.asList(categoryItem));

        if (!checkNetwork() || categories == null) categories = mSharedPrefsHelper.getCategories();
        else {
            mSharedPrefsHelper.saveCategories(categories);
        }

        return categories;
    }

    private String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("categories.json");
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
