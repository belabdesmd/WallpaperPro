package com.devalutix.wallpaperpro.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.WallpaperContract;
import com.devalutix.wallpaperpro.di.components.DaggerMVPComponent;
import com.devalutix.wallpaperpro.di.components.MVPComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.presenters.WallpaperPresenter;
import com.devalutix.wallpaperpro.ui.adapters.WallpaperPagerAdapter;
import com.devalutix.wallpaperpro.ui.fragments.WallpaperFragment;
import com.devalutix.wallpaperpro.utils.Config;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

import javax.inject.Inject;

public class WallpaperActivity extends AppCompatActivity implements WallpaperContract.View {
    private static String TAG = "WallpaperActivity";

    //Declarations
    private MVPComponent mvpComponent;
    private ArrayList<Image> images;
    private InterstitialAd mInterstitialAd;
    @Inject
    WallpaperPresenter mPresenter;

    //View Declarations
    @BindView(R.id.wallpaper_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.wallpaper_viewpager)
    ViewPager mViewPager;

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

        //Set Toolbar
        setToolbar();

        //Init View Pager
        initViewPager();
        mViewPager.setCurrentItem(getIntent().getIntExtra("current", 0));

        //Init Interstitial Ads
        initInterstitialAd();
    }

    @Override
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
    public void setToolbar() {
        Log.d(TAG, "setToolbar: Setting the Toolbar.");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: User Clicks on Options Item.");
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initViewPager() {

        WallpaperPagerAdapter adapter = new WallpaperPagerAdapter(getSupportFragmentManager());

        /*
         * Transferring the Extra From The Images Activity Into A List (String -> List)
         */
        images = mPresenter.getImages(getIntent().getStringExtra("images"));

        //Creating the Fragments and Adding Them
        for (Image image :
                images) {
            //Creating New Fragment With a Picture
            WallpaperFragment frag = WallpaperFragment.newInstance(image.getImageUrl());
            frag.setPresenter(mPresenter);
            adapter.addFrag(frag);
        }

        //Adding the Adapter
        mViewPager.setAdapter(adapter);
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
        /*
         * if  Interstitial Ad is enabled and GDPR is disabled then int Intesrtitial ad
         * else Do nothing
         */
        if (Config.ENABLE_AD_INTERSTITIAL && !Config.ENABLE_GDPR) {
            //Gdpr Disabled and Interstitial Ad Enabled
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getResources().getString(R.string.ADMOB_INTERSTITIAL_AD_ID));
            mInterstitialAd.loadAd(new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build());
        }
    }
}
