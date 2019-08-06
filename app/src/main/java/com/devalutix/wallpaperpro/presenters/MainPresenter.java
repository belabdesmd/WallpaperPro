package com.devalutix.wallpaperpro.presenters;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.MainContract;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.ui.activities.WallpapersActivity;
import com.devalutix.wallpaperpro.ui.activities.MainActivity;
import com.devalutix.wallpaperpro.utils.Config;
import com.devalutix.wallpaperpro.utils.GDPR;
import com.devalutix.wallpaperpro.utils.PermissionUtil;
import com.google.android.gms.ads.AdView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;

import java.util.Objects;

public class MainPresenter implements MainContract.Presenter {
    private static String TAG = "MainPresenter";

    /***************************************** Declarations ***************************************/
    private MainActivity mView;
    private PermissionUtil mPermissionUtil;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private GDPR gdpr;

    /***************************************** Constructor ****************************************/
    public MainPresenter(PermissionUtil mPermissionUtil, SharedPreferencesHelper sharedPreferencesHelper,
                         GDPR gdpr) {
        this.mPermissionUtil = mPermissionUtil;
        this.sharedPreferencesHelper = sharedPreferencesHelper;
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
    public void requestPermission(String permission, int permissionRequest) {
        mPermissionUtil.checkPermission(mView, permission,
                new PermissionUtil.PermissionAskListener() {
                    @Override
                    public void onNeedPermission() {
                        Log.d(TAG, "onNeedPermission: Demand Permission");
                        ActivityCompat.requestPermissions(
                                mView,
                                new String[]{permission},
                                permissionRequest
                        );
                    }

                    @Override
                    public void onPermissionPreviouslyDenied() {
                        Log.d(TAG, "onPermissionPreviouslyDenied: Permission denied");
                        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                            final int BUTTON_NEGATIVE = -2;
                            final int BUTTON_POSITIVE = -1;

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case BUTTON_NEGATIVE:
                                        //which = -2
                                        dialog.dismiss();
                                        break;
                                    case BUTTON_POSITIVE:
                                        //which = -1
                                        ActivityCompat.requestPermissions(
                                                mView,
                                                new String[]{permission},
                                                permissionRequest
                                        );
                                        dialog.dismiss();
                                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                                mView, permission))
                                            onPermissionGranted();
                                }
                            }
                        };
                        new AlertDialog.Builder(mView)
                                .setMessage(mView.getResources().getString(R.string.permission_msg))
                                .setTitle(mView.getResources().getString(R.string.permission_title))
                                .setPositiveButton(mView.getResources().getString(R.string.permission_positive), listener)
                                .setNegativeButton(mView.getResources().getString(R.string.permission_negative), listener)
                                .create()
                                .show();
                        //show a dialog explaining permission and then request permission
                    }

                    @Override
                    public void onPermissionDisabled() {
                        Log.d(TAG, "onPermissionDisabled: Permission Denied");
                        //Not Granted
                        sharedPreferencesHelper.setDownloadEnable(false);
                    }

                    @Override
                    public void onPermissionGranted() {
                        Log.d(TAG, "onPermissionGranted: Permission Granted");
                        //Granted
                        sharedPreferencesHelper.setDownloadEnable(true);
                    }
                });
    }

    @Override
    public void checkGDPRConsent() {
        if (Config.ENABLE_GDPR) {
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
        Log.d(TAG, "grantDownload: Enabling Download");
        sharedPreferencesHelper.setDownloadEnable(true);
    }

    @Override
    public void disableDownload() {
        Log.d(TAG, "disableDownload: Disabling Download");
        sharedPreferencesHelper.setDownloadEnable(false);
    }

    @Override
    public void showDrawerMenuIndex() {
        if (sharedPreferencesHelper.isFirstRun()) {
            sharedPreferencesHelper.setFirstRun();
            AlertDialog.Builder alert = new AlertDialog.Builder(mView, R.style.CustomDialogTheme)
                    .setTitle("Notice")
                    .setMessage("To Show The Side Menu Drag From The Left Side Of the Screen")
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> mView.mDrawerLayout.openDrawer(GravityCompat.START))
                    .setIcon(R.drawable.info);

            AlertDialog dialog = alert.create();
            Objects.requireNonNull(dialog.getWindow()).setGravity(Gravity.TOP | Gravity.START);
            dialog.show();
        }
    }
}
