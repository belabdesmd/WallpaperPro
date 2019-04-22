package com.devalutix.wallpaperpro.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import javax.inject.Inject;

public class SharedPreferencesHelper {
    private static final String TAG = "SharedPreferenceHelper";

    //Declarations
    private SharedPreferences sharedPref;
    private Gson gson;

    public SharedPreferencesHelper(SharedPreferences sharedPref, Gson gson) {
        this.sharedPref = sharedPref;
        this.gson = gson;
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

    public void setDownloadEnable(boolean enable) {
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putBoolean("Download", enable).apply();
    }

    public boolean isDownloadEnable() {
        return sharedPref.getBoolean("Download", false);
    }
}
