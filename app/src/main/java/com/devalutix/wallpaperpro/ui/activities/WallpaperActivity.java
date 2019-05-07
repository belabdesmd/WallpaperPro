package com.devalutix.wallpaperpro.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.WallpaperContract;
import com.devalutix.wallpaperpro.di.components.DaggerMVPComponent;
import com.devalutix.wallpaperpro.di.components.MVPComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.presenters.WallpaperPresenter;
import com.devalutix.wallpaperpro.ui.adapters.WallpaperPagerAdapter;
import com.devalutix.wallpaperpro.ui.fragments.WallpaperFragment;
import com.devalutix.wallpaperpro.utils.Config;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import java.util.ArrayList;

import javax.inject.Inject;

public class WallpaperActivity extends AppCompatActivity implements WallpaperContract.View {
    private static String TAG = "WallpaperActivity";

    //Declarations
    private MVPComponent mvpComponent;
    private ArrayList<Image> images;
    private InterstitialAd mInterstitialAd;
    private SlideUp slideUpInfo;
    private SlideUp slideUpFavorites;

    @Inject
    WallpaperPresenter mPresenter;

    //View Declarations
    @BindView(R.id.wallpaper_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.wallpaper_container)
    ConstraintLayout activity_container;
    @BindView(R.id.wallpaper_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.info_popup)
    CardView info_popup;
    @BindView(R.id.add_to_favorites_popup)
    CardView add_to_favorites_popup;

    @BindView(R.id.date_added)
    TextView date_added;
    @BindView(R.id.views)
    TextView views;
    @BindView(R.id.downloads)
    TextView downloads;
    @BindView(R.id.likes)
    TextView likes;
    @BindView(R.id.dislikes)
    TextView dislikes;
    @BindView(R.id.categories)
    TextView categories;
    @BindView(R.id.tags)
    TextView tags;
    @BindView(R.id.owner)
    TextView owner;

    //ClickListeners
    @OnClick(R.id.cancel_info)
    public void cancel() {
        hideInfos();
    }

    @OnClick(R.id.like_action)
    public void like() {
        mPresenter.likePicture();
    }

    @OnClick(R.id.dislike_action)
    public void dislike() {
        mPresenter.dislikePicture();
    }

    @OnClick(R.id.share_action)
    public void share() {
        mPresenter.sharePicture();
    }

    @OnClick(R.id.download_action)
    public void download() {
        mPresenter.savePicture(images.get(mViewPager.getCurrentItem()).getImageUrl());
    }

    @OnClick(R.id.popup_info_kicker)
    public void openPopUpInfo() {
        showInfos();
    }

    @OnClick(R.id.go_to_favorites)
    public void goToFavorites() {
        Intent goToFavorites = new Intent(WallpaperActivity.this, MainActivity.class);
        goToFavorites.putExtra("ToFavorites", true);
        startActivity(goToFavorites);
    }


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
        initInfos(mViewPager.getCurrentItem());

        //Init Info PopUp
        initPopUpInfos();

        //Init Add To Favorites PopUp
        initPopUpFavorite();

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

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                initInfos(position);
            }

            @Override
            public void onPageSelected(int position) {
                initInfos(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initPopUpInfos() {
        slideUpInfo = new SlideUpBuilder(info_popup)
                .withStartState(SlideUp.State.HIDDEN)
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withSlideFromOtherView(activity_container)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {

                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {

                    }
                })
                .build();
    }

    @Override
    public void initInfos(int position) {
        date_added.setText(images.get(position).getImageDateAdded().toString());
        views.setText("Views : "+images.get(position).getImageViews());
        downloads.setText("Downloads : "+images.get(position).getImageDownloads());
        likes.setText("Likes : "+images.get(position).getImageLikes());
        dislikes.setText("Dislikes : "+images.get(position).getImageDislikes());

        StringBuilder categories = new StringBuilder("Categories: ");
        for (Category category :
                images.get(position).getImageCategories()) {
            categories.append(" ").append(category.getCategoryName());
        }
        this.categories.setText(categories);

        StringBuilder tags = new StringBuilder("Tags: ");
        for (String tag :
                images.get(position).getImageTags()) {
            categories.append(" ").append(tag);
        }
        this.tags.setText(tags);

        owner.setText(images.get(position).getImageOwner());
    }

    @Override
    public void initPopUpFavorite() {
        slideUpFavorites = new SlideUpBuilder(add_to_favorites_popup)
                .withStartState(SlideUp.State.HIDDEN)
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withSlideFromOtherView(activity_container)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {

                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {

                    }
                })
                .build();
    }

    @Override
    public void initInterstitialAd() {

    }

    @Override
    public void showInfos() {
        slideUpInfo.show();
    }

    @Override
    public void hideInfos() {
        slideUpInfo.hide();
    }

    @Override
    public void showFavorite() {
        slideUpFavorites.show();
    }

    @Override
    public void hideFavorite() {
        slideUpFavorites.hide();
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
