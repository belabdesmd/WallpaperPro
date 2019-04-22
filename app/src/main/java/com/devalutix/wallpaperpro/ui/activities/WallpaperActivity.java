package com.devalutix.wallpaperpro.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

import android.os.Bundle;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.base.BaseApplication;
import com.devalutix.wallpaperpro.contracts.WallpaperContract;
import com.devalutix.wallpaperpro.di.components.DaggerMVPComponent;
import com.devalutix.wallpaperpro.di.components.MVPComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.presenters.WallpaperPresenter;

import javax.inject.Inject;

public class WallpaperActivity extends AppCompatActivity implements WallpaperContract.View {
    private static String TAG = "WallpaperActivity";

    //Declarations
    MVPComponent mvpComponent;
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

        //Initialize Dagger For Application
        mvpComponent = getComponent();

        //Inject the Component Here
        mvpComponent.inject(this);

        //Attach View To Presenter
        mPresenter.attach(this);
    }

    public MVPComponent getComponent() {
        if (mvpComponent == null) {
            mvpComponent = DaggerMVPComponent
                    .builder()
                    .applicationModule(new ApplicationModule(getApplication()))
                    .mVPModule(new MVPModule(this))
                    .build();
        }
        return mvpComponent;
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
