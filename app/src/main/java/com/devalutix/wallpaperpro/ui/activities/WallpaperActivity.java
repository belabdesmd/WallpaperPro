package com.devalutix.wallpaperpro.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

import android.os.Bundle;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.base.BaseApplication;
import com.devalutix.wallpaperpro.contracts.MainContract;
import com.devalutix.wallpaperpro.contracts.WallpaperContract;
import com.devalutix.wallpaperpro.di.components.ApplicationComponent;
import com.devalutix.wallpaperpro.presenters.WallpaperPresenter;

import javax.inject.Inject;

public class WallpaperActivity extends AppCompatActivity implements WallpaperContract.View {
    private static String TAG = "WallpaperActivity";

    //Declarations
    protected ApplicationComponent mComponent;
    @Inject
    WallpaperPresenter mPresenter;

    //View Declarations

    //ClickListeners

    //Essentials Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        //Set ButterKnife
        ButterKnife.bind(this);

        //Inject Component (Dependency Injection)
        ((BaseApplication) getApplication()).getComponent().inject(this);

        //Attach View To Presenter
        mPresenter.attach(this);
    }

    //Methods
    @Override
    public void initViewPager() {

    }

    @Override
    public void initPopUpInfos() {

    }

    @Override
    public void initPopUpRate() {

    }

    @Override
    public void initPopUpFavorite() {

    }

    @Override
    public void initInterstitialAd() {

    }

    @Override
    public void showInfos() {

    }

    @Override
    public void hideInfos() {

    }

    @Override
    public void showRate() {

    }

    @Override
    public void hideRate() {

    }

    @Override
    public void showFavorite() {

    }

    @Override
    public void hideFavorite() {

    }

    @Override
    public void showInterstitialAd() {

    }
}
