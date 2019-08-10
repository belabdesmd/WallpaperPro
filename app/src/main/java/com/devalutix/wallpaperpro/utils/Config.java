package com.devalutix.wallpaperpro.utils;

import android.content.SharedPreferences;

import com.devalutix.wallpaperpro.R;

public class Config {
    private static final String NAME = "Name";
    private static final String EMAIL = "Email";

    private static final String GDPR = "Gdpr";
    private static final String BANNER = "Banner";
    private static final String INTERSTITIAL = "Interstitial";
    private static final String PUBLISHER = "Publisher";
    private static final String BANNER_ID = "Banner ID";
    private static final String INTERSTITIAL_ID = "Interstitial ID";

    private SharedPreferences preferences;

    public Config(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    /************************************** Basics ************************************************/
    public static String[] tabTitles = {"Explore", "Categories", "Favorites"};

    public static int[] tabIconsEnabled = {R.drawable.tab_explore_enabled,
            R.drawable.tab_category_enabled,
            R.drawable.tab_favorite_enabled};

    public static int[] tabIconsDisabled = {R.drawable.tab_explore_disabled,
            R.drawable.tab_category_disabled,
            R.drawable.tab_favorite_disabled};

    public static final String BASE_API_URL = "https://wallpaperprodemo.herokuapp.com/api/";

    public static final String TOKEN = "Token 7b5387fc9738d77146f05dc62e728f29331dc55e";

    public void setAdBannerId(String id) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(BANNER_ID, id).apply();
    }

    public void setAdInterstitialId(String id) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(INTERSTITIAL_ID, id).apply();
    }

    public String getAdBannerId() {
        return preferences.getString(BANNER_ID, "test");
    }

    public String getAdInterstitialId() {
        return preferences.getString(INTERSTITIAL_ID, "test");
    }

    /************************************ Developer Info ******************************************/

    public void setDeveloperName(String name) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(NAME, name).apply();
    }

    public void setDeveloperEmail(String email) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(EMAIL, email).apply();
    }

    public String getDeveloperName() {
        return preferences.getString(NAME, "Developer");
    }

    public String getDeveloperEmail() {
        return preferences.getString(EMAIL, "developer@gmail.com");
    }

    /************************************** Configs ***********************************************/

    public void setBannerEnabled(boolean enabled) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putBoolean(BANNER, enabled).apply();
    }

    public void setInterstitialEnabled(boolean enabled) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putBoolean(INTERSTITIAL, enabled).apply();
    }

    public void setGDPREnabled(boolean enabled) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putBoolean(GDPR, enabled).apply();
    }

    public void setPublisherId(String id) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(PUBLISHER, id).apply();
    }

    public boolean isBannerEnabled() {
        return preferences.getBoolean(BANNER, true);
    }

    public boolean isInterstitialEnabled() {
        return preferences.getBoolean(INTERSTITIAL, true);
    }

    public boolean isGDPREnabled() {
        return preferences.getBoolean(GDPR, true);
    }

    public String getPublisherId() {
        //TODO: Remove After Testings
        return preferences.getString(PUBLISHER, "pub-4679171106713552");
    }
}
