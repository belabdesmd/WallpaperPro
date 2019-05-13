package com.devalutix.wallpaperpro.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

public class SharedPreferencesHelper {
    private static final String TAG = "SharedPreferenceHelper";
    private static final String CATEGORIES = "Categories";
    private static final String COLLECTIONS = "Collections";

    //Declarations
    private SharedPreferences sharedPref;
    private Gson gson;

    //Constructor
    public SharedPreferencesHelper(SharedPreferences sharedPref, Gson gson) {
        this.sharedPref = sharedPref;
        this.gson = gson;
    }

    //Methods
    public void saveCategories(ArrayList<Category> categories) {
        String jsonToString = gson.toJson(categories);
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putString(CATEGORIES, jsonToString).apply();
    }

    public ArrayList<Category> getCategories() {
        if (isEmpty(CATEGORIES)) return null;
        else {
            String jsonCategory = sharedPref.getString(CATEGORIES, null);
            Category[] categoryItem = gson.fromJson(jsonCategory, Category[].class);
            return new ArrayList<Category>(Arrays.asList(categoryItem));
        }
    }


    public void saveCollections(ArrayList<Collection> collections) {
        String jsonToString = gson.toJson(collections);
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putString(COLLECTIONS, jsonToString).apply();
    }

    public ArrayList<Collection> getCollections() {
        if (isEmpty(COLLECTIONS)) return null;
        else {
            String jsonCollections = sharedPref.getString(COLLECTIONS, null);
            Collection[] collectionItem = gson.fromJson(jsonCollections, Collection[].class);
            return new ArrayList<Collection>(Arrays.asList(collectionItem));
        }
    }

    private boolean isEmpty(String mode) {
        return sharedPref.getString(mode, null) == null;
    }

    //Extra Functions
    public void firstTimeAskingPermission(String permission) {
        Log.d(TAG, "firstTimeAskingPermission: check");
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putBoolean(permission, false).apply();
    }

    public boolean isFirstTimeAskingPermission(String permission) {
        Log.d(TAG, "isFirstTimeAskingPermission.");
        return sharedPref != null && sharedPref.getBoolean(permission, true);
    }

    /**
     * Set Download Enabled if user Provides the Permission
     */
    public void setDownloadEnable(boolean enable) {
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putBoolean("Download", enable).apply();
    }

    /**
     * Check if Downloading Option Available
     */
    public boolean isDownloadEnable() {
        return sharedPref.getBoolean("Download", false);
    }

    /**
     * Set Dark Mode Enabled Or Disabled
     */
    public void setDarkModeEnable(boolean isEnabled) {
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putBoolean("DARK_MODE", isEnabled).apply();
    }

    /**
     * Check if Dark Mode is Enabled
     */
    public boolean isDarkModeEnabled() {
        return sharedPref.getBoolean("DARK_MODE", false);
    }
}
