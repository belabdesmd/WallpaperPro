package com.devalutix.wallpaperpro.presenters;

import android.app.WallpaperManager;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.WallpaperContract;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.pojo.Wallpaper;
import com.devalutix.wallpaperpro.ui.activities.WallpaperActivity;
import com.devalutix.wallpaperpro.utils.ApiEndpointInterface;
import com.devalutix.wallpaperpro.utils.Config;
import com.devalutix.wallpaperpro.utils.GDPR;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WallpaperPresenter implements WallpaperContract.Presenter {

    /***************************************** Declarations ***************************************/
    private WallpaperActivity mView;
    private Gson gson;
    private SharedPreferencesHelper mSharedPrefsHelper;
    private ApiEndpointInterface apiService;
    private GDPR gdpr;
    private ArrayList<Wallpaper> wallpapers;

    /***************************************** Constructor ****************************************/
    public WallpaperPresenter(Gson gson, SharedPreferencesHelper sharedPreferencesHelper, GDPR gdpr, ApiEndpointInterface apiService) {
        this.gson = gson;
        mSharedPrefsHelper = sharedPreferencesHelper;
        this.gdpr = gdpr;
        this.apiService = apiService;
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(WallpaperContract.View view) {
        mView = (WallpaperActivity) view;
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
    public void loadInterstitialAd(InterstitialAd mInterstitialAd) {
        if (Config.ENABLE_AD_INTERSTITIAL) {
            if (mSharedPrefsHelper.isAdPersonalized())
                gdpr.loadPersonalizedInterstitialAd(mInterstitialAd);
            else
                gdpr.loadNonPersonalizedInterstitialAd(mInterstitialAd);
        }
    }

    @Override
    public void loadAd(AdView ad) {
        if (mSharedPrefsHelper.isAdPersonalized()) {
            gdpr.showPersonalizedAdBanner(ad);
        } else {
            gdpr.showNonPersonalizedAdBanner(ad);
        }
    }

    @Override
    public Wallpaper getWallpapers(int currentItem) {
        return wallpapers.get(currentItem);
    }

    @Override
    public void savePicture(Wallpaper wallpaper, int position) {
        Log.d("WallpaperPresenter", "savePicture: isEnabled" + mSharedPrefsHelper.isDownloadEnable());
        if (mSharedPrefsHelper.isDownloadEnable())
            Glide.with(mView)
                    .asBitmap()
                    .load(wallpaper.getWallpapers())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            mView.hideInfos();
                            apiService.updateDownloads(Config.TOKEN, wallpaper.getPk()).enqueue(new Callback<Wallpaper>() {
                                @Override
                                public void onResponse(@NonNull Call<Wallpaper> call, @NonNull Response<Wallpaper> response) {
                                    if (response.isSuccessful()) {
                                        wallpapers.set(position, response.body());
                                        mView.initInfos(position);
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<Wallpaper> call, @NonNull Throwable t) {

                                }
                            });
                            Toast.makeText(mView, mView.getResources().getString(R.string.downloading), Toast.LENGTH_SHORT).show();
                            saveWallpaperToInternalStorage(resource, wallpaper);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            Toast.makeText(mView, mView.getResources().getString(R.string.error_download), Toast.LENGTH_SHORT).show();
                        }
                    });
        else
            Toast.makeText(mView, mView.getResources().getString(R.string.error_download_permission), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sharePicture(Wallpaper wallpaper) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, mView.getResources().getString(R.string.app_name));
        String sAux = mView.getResources().getString(R.string.share_msg_1) + " " + wallpaper.getTitle()
                + " " + mView.getResources().getString(R.string.share_msg_2)
                + "\n" + "https://play.google.com/store/apps/details?id=" + mView.getPackageName();
        i.putExtra(Intent.EXTRA_TEXT, sAux);
        mView.startActivity(Intent.createChooser(i, "choose one"));
    }

    @Override
    public void setAsWallpaper(Wallpaper wallpaper) {
        Glide.with(mView)
                .asBitmap()
                .load(wallpaper.getWallpapers())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(mView);
                        try {
                            wallpaperManager.setBitmap(resource);
                            Toast.makeText(mView, mView.getResources().getString(R.string.set_wallpaper), Toast.LENGTH_SHORT).show();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        Toast.makeText(mView, mView.getResources().getString(R.string.set_wallpaper_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void openAddToFavoritesPopUp() {

        mView.showFavorite();

        Handler handler = new Handler();
        handler.postDelayed(() -> mView.hideFavorite(), 3000);

    }

    @Override
    public void setWallpapers(String wallpapers) {
        Wallpaper[] wallpaperUrlsArray = gson.fromJson(wallpapers, Wallpaper[].class);
        this.wallpapers = new ArrayList<>(Arrays.asList(wallpaperUrlsArray));
    }

    @Override
    public ArrayList<Wallpaper> getWallpapers() {
        return wallpapers;
    }

    @Override
    public ArrayList<Collection> getCollections() {
        ArrayList<Collection> collections = mSharedPrefsHelper.getCollections();
        if (collections == null) {
            collections = new ArrayList<>();
            collections.add(new Collection(Config.MY_FAVORITES_COLLECTION_NAME, new ArrayList<>()));
            mSharedPrefsHelper.saveCollections(collections);
        }
        collections.remove(0);
        return collections;
    }

    private void saveWallpaperToInternalStorage(Bitmap bitmap, Wallpaper wallpaper) {

        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(mView);

        // Initializing a new file
        // The bellow line return a directory in internal storage
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        if (!root.endsWith("/")) {
            root += "/";
        }
        File myDir = new File(root + mView.getResources().getString(R.string.app_name));
        myDir.mkdirs();

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String f_name = wallpaper.getTitle() + "_" + wallpaper.getPk() + ".jpg";

        // Create a file to save the wallpaper
        File file = new File(myDir, f_name);

        if (!file.exists())
            try {
                // Initialize a new OutputStream
                OutputStream stream;

                // If the output file exists, it can be replaced or appended to it
                stream = new FileOutputStream(file);

                // Compress the bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                // Flushes the stream
                stream.flush();

                // Closes the stream
                stream.close();

                Toast.makeText(wrapper, wrapper.getResources().getString(R.string.downloaded), Toast.LENGTH_SHORT).show();

            } catch (IOException e) // Catch the exception
            {
                e.printStackTrace();
            }
        else
            Toast.makeText(wrapper, wrapper.getResources().getString(R.string.wallpaper_already_downloaded), Toast.LENGTH_SHORT).show();

        mView.showInterstitialAd();
    }
}
