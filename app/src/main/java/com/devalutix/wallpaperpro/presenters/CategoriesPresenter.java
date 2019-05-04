package com.devalutix.wallpaperpro.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.devalutix.wallpaperpro.contracts.CategoriesContract;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.ui.fragments.CategoriesFragment;

import java.util.ArrayList;

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
        ConnectivityManager conMgr =  (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        ArrayList<Category> categories = null;

        if (!checkNetwork() || categories == null) categories = mSharedPrefsHelper.getCategories();
        else{
            mSharedPrefsHelper.saveCategories(categories);
        }
        return categories;
    }
}
