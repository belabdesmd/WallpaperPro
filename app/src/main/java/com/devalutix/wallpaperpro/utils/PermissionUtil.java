package com.devalutix.wallpaperpro.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.ui.activities.MainActivity;

import androidx.core.app.ActivityCompat;

public class PermissionUtil {

    private SharedPreferencesHelper sharedPreferencesHelper;

    public PermissionUtil(SharedPreferencesHelper sharedPreferencesHelper) {
        this.sharedPreferencesHelper = sharedPreferencesHelper;
    }

    /*
     * Check if version is marshmallow and above.
     * Used in deciding to ask runtime permission
     * */
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

    public void checkPermission(Context context, String permission, PermissionAskListener listener) {
        /*
         *
         * if permission is not granted
         *
         * */
        if (shouldAskPermission(context, permission)) {
            /*
             *
             * If permission denied previously
             *
             * */
            if (ActivityCompat.shouldShowRequestPermissionRationale((MainActivity) context, permission)) {
                listener.onPermissionPreviouslyDenied();
            } else {
                /*
                 *
                 *  Permission denied or first time requested
                 *
                 * */
                if (sharedPreferencesHelper.isFirstTimeAskingPermission(permission)) {
                    sharedPreferencesHelper.firstTimeAskingPermission(permission);
                    listener.onNeedPermission();
                } else {
                    /*
                     * Handle the feature without permission or ask user to manually allow permission
                     * */
                    listener.onPermissionDisabled();
                }
            }
        } else {
            listener.onPermissionGranted();
        }
    }

    /*
     * Callback on various cases on checking permission
     *
     * 1.  Below M, runtime permission not needed. In that case onPermissionGranted() would be called.
     *     If permission is already granted, onPermissionGranted() would be called.
     *
     * 2.  Above M, if the permission is being asked first time onNeedPermission() would be called.
     *
     * 3.  Above M, if the permission is previously asked but not granted, onPermissionPreviouslyDenied()
     *     would be called.
     *
     * 4.  Above M, if the permission is disabled by device policy or the user checked "Never ask again"
     *     check box on previous request permission, onPermissionDisabled() would be called.
     * */

    public interface PermissionAskListener {
        /*
         * Callback to ask permission
         * */
        void onNeedPermission();

        /*
         * Callback on permission denied
         * */
        void onPermissionPreviouslyDenied();

        /*
         * Callback on permission "Never show again" checked and denied
         * */
        void onPermissionDisabled();

        /*
         * Callback on permission granted
         * */
        void onPermissionGranted();
    }
}
