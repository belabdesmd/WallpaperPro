package com.devalutix.wallpaperpro.presenters;

import android.app.WallpaperManager;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
    private static String TAG = "WallpaperPresenter";

    /***************************************** Declarations ***************************************/
    private WallpaperActivity mView;
    private Gson gson;
    private SharedPreferencesHelper mSharedPrefsHelper;
    private ApiEndpointInterface apiService;
    private GDPR gdpr;
    private ArrayList<Wallpaper> images;

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
    public Wallpaper getImage(int currentItem) {
        return images.get(currentItem);
    }

    @Override
    public void savePicture(Wallpaper image, int position) {
        if (mSharedPrefsHelper.isDownloadEnable())
            Glide.with(mView)
                    .asBitmap()
                    .load(image.getImage())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            mView.hideInfos();
                            apiService.updateDownloads(Config.TOKEN, image.getPk()).enqueue(new Callback<Wallpaper>() {
                                @Override
                                public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                                    if (response.isSuccessful()){
                                        images.set(position, response.body());
                                        mView.initInfos(position);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Wallpaper> call, Throwable t) {

                                }
                            });
                            Toast.makeText(mView, mView.getResources().getString(R.string.downloading), Toast.LENGTH_SHORT).show();
                            saveImageToInternalStorage(resource);
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
    public void sharePicture(Wallpaper image) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, mView.getResources().getString(R.string.app_name));
        String sAux = mView.getResources().getString(R.string.share_msg_1) + image.getTitle()
                + mView.getResources().getString(R.string.share_msg_2)
                + mView.getResources().getString(R.string.app_name);
        i.putExtra(Intent.EXTRA_TEXT, sAux);
        mView.startActivity(Intent.createChooser(i, "choose one"));
    }

    @Override
    public void setAsWallpaper(Wallpaper image) {
        Glide.with(mView)
                .asBitmap()
                .load(image.getImage())
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
    public void setImages(String images) {
        Wallpaper[] imageUrlsArray = gson.fromJson(images, Wallpaper[].class);
        this.images = new ArrayList<>(Arrays.asList(imageUrlsArray));
    }

    @Override
    public ArrayList<Wallpaper> getImages() {
        return images;
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

    private void saveImageToInternalStorage(Bitmap bitmap) {

        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(mView);

        // Initializing a new file
        // The bellow line return a directory in internal storage
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/DCIM/" + mView.getResources().getString(R.string.app_name));
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String f_name = mView.getResources().getString(R.string.app_name) + "_Image-" + n + ".jpg";

        // Create a file to save the image
        File file = new File(myDir, f_name);

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

        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(file.getAbsolutePath());

        // Return the saved image Uri
        Log.d(TAG, "saveImageToInternalStorage: " + savedImageURI);

        mView.showInterstitialAd();
    }
}
