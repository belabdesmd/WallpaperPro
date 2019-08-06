package com.devalutix.wallpaperpro.models;

import android.content.SharedPreferences;
import android.util.Log;

import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class SharedPreferencesHelper {
    private static final String CATEGORIES = "Categories";
    private static final String COLLECTIONS = "Collections";
    private static final String DOWNLOAD = "Download";
    private static final String DARK_MODE = "Dark Mode";
    private static final String PERSONALIZED_ADS = "AD";
    private static final String FIRST = "First";

    /************************************* Declarations *******************************************/
    private SharedPreferences sharedPref;
    private Gson gson;

    /************************************* Constructor ********************************************/
    public SharedPreferencesHelper(SharedPreferences sharedPref, Gson gson) {
        this.sharedPref = sharedPref;
        this.gson = gson;
    }

    /************************************* Methods ***********************************************/

    private boolean isEmpty(String mode) {
        return sharedPref.getString(mode, null) == null;
    }

    //Categories
    public ArrayList<Category> getCategories() {
        if (isEmpty(CATEGORIES)) return null;
        else {
            String jsonCategory = sharedPref.getString(CATEGORIES, null);
            Category[] categoryItem = gson.fromJson(jsonCategory, Category[].class);
            return new ArrayList<>(Arrays.asList(categoryItem));
        }
    }

    //Collections
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
            return new ArrayList<>(Arrays.asList(collectionItem));
        }
    }

    /************************************* Extra Methods ******************************************/

    //Permissions
    public void firstTimeAskingPermission(String permission) {
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putBoolean(permission, false).apply();
    }

    public boolean isFirstTimeAskingPermission(String permission) {
        return sharedPref != null && sharedPref.getBoolean(permission, true);
    }

    //Download
    public void setDownloadEnable(boolean enable) {
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putBoolean(DOWNLOAD, enable).apply();
    }

    public boolean isDownloadEnable() {
        return sharedPref.getBoolean(DOWNLOAD, false);
    }

    //Dark Mode
    public void setDarkModeEnable(boolean isEnabled) {
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putBoolean(DARK_MODE, isEnabled).apply();
    }

    public boolean isDarkModeEnabled() {
        return sharedPref.getBoolean(DARK_MODE, false);
    }

    //Ad Personalization
    public void setAdPersonalized(boolean isPersonalized) {
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putBoolean(PERSONALIZED_ADS, isPersonalized).apply();
    }

    public boolean isAdPersonalized() {
        return sharedPref.getBoolean(PERSONALIZED_ADS, false);
    }

    //First Run
    public boolean isFirstRun() {
        return sharedPref.getBoolean(FIRST, true);
    }

    public void setFirstRun() {
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putBoolean(FIRST, false).apply();
    }
}
