package com.devalutix.wallpaperpro.utils;

import com.devalutix.wallpaperpro.R;

/**********************************
 © 2018 Sam Dev
 ALL RIGHTS RESERVED
 ***********************************/
public class Config {

    /**
     * Set this Boolean true if you want to enable Ad Banners or false
     * To disable Ad Banners
     */
    public static final boolean ENABLE_AD_BANNER = true;
    /**
     * Set this Boolean true if you want to enable Interstitial ad or false
     * To disable Interstitial ad
     */
    public static final boolean ENABLE_AD_INTERSTITIAL = true;
    /**
     * Set this Boolean true if you want to enable GDPR or false
     * To disable GDPR, You can disable GDPR if the app isn't targeting a EU Country
     *
     * If You Set this boolean as true, you need to select the ad technology providers used
     * from your admob account
     * Go to Admob > Blocking Control "in the side menu" > Choose EU User Consent
     * then choose "Custom set of ad technology providers"
     */
    public static final boolean ENABLE_GDPR = false;

    /**
     * Add Your Email Here, That email is the email that will receive
     * the Messages from the app users
     */
    public static final String YOUR_EMAIL = "";
    /**
     * Add Your Publisher Id, You can find it on your settings at your admob account
     */
    public static final String PUBLISHER_ID = "pub-4679171106713552";

    public static final String MY_FAVORITES_COLLECTION_NAME = "My Favorites";

    public static final String BASE_API_URL = "https://wallpaperprodemo.herokuapp.com/api/";

    public static final String TOKEN = "Token 7b5387fc9738d77146f05dc62e728f29331dc55e";

    public static String[] tabTitles = {"Explore", "Categories", "Favorites"};


    public static int[] tabIconsEnabled = {R.drawable.tab_explore_enabled,
            R.drawable.tab_category_enabled,
            R.drawable.tab_favorite_enabled};

    public static int[] tabIconsDisabled = {R.drawable.tab_explore_disabled,
            R.drawable.tab_category_disabled,
            R.drawable.tab_favorite_disabled};
}
