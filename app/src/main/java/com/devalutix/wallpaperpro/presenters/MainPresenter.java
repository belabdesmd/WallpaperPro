package com.devalutix.wallpaperpro.presenters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.Gravity;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.MainContract;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.ui.activities.WallpapersActivity;
import com.devalutix.wallpaperpro.ui.activities.MainActivity;
import com.devalutix.wallpaperpro.utils.Config;
import com.devalutix.wallpaperpro.utils.GDPR;
import com.google.android.gms.ads.AdView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;

import java.util.Objects;

public class MainPresenter implements MainContract.Presenter {

    /***************************************** Declarations ***************************************/
    private MainActivity mView;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private GDPR gdpr;
    private Config config;

    /***************************************** Constructor ****************************************/
    public MainPresenter(SharedPreferencesHelper sharedPreferencesHelper,Config config, GDPR gdpr) {
        this.sharedPreferencesHelper = sharedPreferencesHelper;
        this.config = config;
        this.gdpr = gdpr;
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(MainContract.View view) {
        mView = (MainActivity) view;
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
    public void refreshConfig(){
        //TODO: Config Call
    }

    @Override
    public void requestPermission(String permission, int permissionRequest) {
        if (shouldAskPermission(mView, permission))
            if (sharedPreferencesHelper.isFirstTimeAskingPermission(permission)){
                sharedPreferencesHelper.firstTimeAskingPermission(permission);
                ActivityCompat.requestPermissions(
                        mView,
                        new String[]{permission},
                        permissionRequest
                );
            }
    }

    @Override
    public void checkGDPRConsent() {
        if (config.isGDPREnabled()) {
            gdpr.checkForConsent();
        }
    }

    @Override
    public void loadAd(AdView ad) {
        if (sharedPreferencesHelper.isAdPersonalized()) {
            gdpr.showPersonalizedAdBanner(ad);
        } else {
            gdpr.showNonPersonalizedAdBanner(ad);
        }
    }

    @Override
    public void searchWallpapers(String query) {
        Intent goToWallpaper = new Intent(mView, WallpapersActivity.class);

        //Putting the Extras
        goToWallpaper.putExtra("name", query);
        goToWallpaper.putExtra("mode", "search");
        goToWallpaper.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mView.startActivity(goToWallpaper);
    }

    @Override
    public void enableDarkMode(boolean isChecked) {
        sharedPreferencesHelper.setDarkModeEnable(isChecked);
    }

    @Override
    public boolean isDarkModeEnabled() {
        return sharedPreferencesHelper.isDarkModeEnabled();
    }

    @Override
    public void grantDownload() {
        sharedPreferencesHelper.setDownloadEnable(true);
    }

    @Override
    public void disableDownload() {
        sharedPreferencesHelper.setDownloadEnable(false);
    }

    @Override
    public Config getConfig(){
        return config;
    }

    @Override
    public void showDrawerMenuIndex() {
        if (sharedPreferencesHelper.isFirstRun()) {
            sharedPreferencesHelper.setFirstRun();
            AlertDialog.Builder alert = new AlertDialog.Builder(mView, R.style.CustomDialogTheme)
                    .setTitle(mView.getResources().getString(R.string.notice))
                    .setMessage(mView.getResources().getString(R.string.drawer_info))
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> mView.mDrawerLayout.openDrawer(GravityCompat.START))
                    .setIcon(R.drawable.info);

            AlertDialog dialog = alert.create();
            Objects.requireNonNull(dialog.getWindow()).setGravity(Gravity.TOP | Gravity.START);
            dialog.show();
        }
    }

    private boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    private boolean shouldAskPermission(Context context, String permission) {
        if (shouldAskPermission()) {
            int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
            return permissionResult != PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }
}
