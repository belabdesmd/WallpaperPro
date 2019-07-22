package com.devalutix.wallpaperpro.ui.activities;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.WallpaperContract;
import com.devalutix.wallpaperpro.di.components.DaggerMVPComponent;
import com.devalutix.wallpaperpro.di.components.MVPComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.presenters.FavoritesPresenter;
import com.devalutix.wallpaperpro.presenters.WallpaperPresenter;
import com.devalutix.wallpaperpro.ui.adapters.FavoritesAdapter;
import com.devalutix.wallpaperpro.ui.adapters.WallpaperPagerAdapter;
import com.devalutix.wallpaperpro.ui.fragments.WallpaperFragment;
import com.devalutix.wallpaperpro.utils.Config;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import java.util.ArrayList;

import javax.inject.Inject;

public class WallpaperActivity extends AppCompatActivity implements WallpaperContract.View {
    private static String TAG = "WallpaperActivity";
    private static final int COL_NUM = 2;

    //Declarations
    private MVPComponent mvpComponent;
    private ArrayList<Image> images;
    private InterstitialAd mInterstitialAd;
    private FavoritesAdapter mAdapter;
    private BottomSheetBehavior info_popup_behavior;
    private BottomSheetBehavior add_to_favorite_popup_behavior;

    @Inject
    WallpaperPresenter mPresenter;
    @Inject
    FavoritesPresenter favoritePresenter;

    //View Declarations
    @BindView(R.id.wallpaper_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.wallpaper_container)
    ConstraintLayout activity_container;
    @BindView(R.id.wallpaper_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.info_popup)
    CardView info_popup;
    @BindView(R.id.index)
    ImageView index;
    @BindView(R.id.add_to_favorites_popup)
    CardView add_to_favorites_popup;
    @BindView(R.id.collections_recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.image_title)
    TextView titleTextView;
    @BindView(R.id.date_added)
    TextView dateTextView;
    @BindView(R.id.download_action)
    ImageView download;
    @BindView(R.id.downloads)
    TextView downloadsTextView;
    @BindView(R.id.categories)
    TextView categoriesTextView;

    //ClickListeners
    @OnClick(R.id.share_action)
    public void share() {
        mPresenter.sharePicture(mViewPager.getCurrentItem());
    }

    @OnClick(R.id.download_wrapper)
    public void download() {
        mPresenter.savePicture(mViewPager.getCurrentItem());
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
        //Initialize Dagger For Application
        mvpComponent = getComponent();

        //Inject the Component Here
        mvpComponent.inject(this);

        //Set Dark Mode
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppDarkTheme);
        else setTheme(R.style.AppLightTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        //Set ButterKnife
        ButterKnife.bind(this);

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
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wallpaper_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: User Clicks on Options Item.");
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            case R.id.action_favorite: {
                mPresenter.openAddToFavoritesPopUp();
                return true;
            }
        }

        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initViewPager() {
        Log.d(TAG, "initViewPager: Initialising View Pager");
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
                hideInfos();
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

        info_popup_behavior = BottomSheetBehavior.from(info_popup);
        Context context = this;
        info_popup_behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset > 0) {
                    TypedValue typedValue = new TypedValue();
                    Resources.Theme theme = context.getTheme();
                    theme.resolveAttribute(R.attr.backgroundColor, typedValue, true);
                    @ColorInt int color = typedValue.data;
                    info_popup.setCardBackgroundColor(color);
                    index.setImageResource(R.drawable.index_popup);
                } else {
                    info_popup.setCardBackgroundColor(Color.TRANSPARENT);
                    index.setImageResource(R.drawable.index);
                }
            }
        });
    }

    @Override
    public void initInfos(int position) {

        //TODO: Visual Polish

        //Declarations
        String topic;
        String topicInfo;
        String topicText;

        //Adding Date
        topic = "Title: ";
        topicInfo = images.get(position).getImageTitle();

        topicText = topic + topicInfo;

        Spannable spannable = new SpannableString(topicText);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), topic.length(), topic.length() + topicInfo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        titleTextView.setText(spannable, TextView.BufferType.SPANNABLE);

        //Adding Date
        topic = "Date Added: ";
        topicInfo = images.get(position).getImageDateAdded().toString();

        topicText = topic + topicInfo;

        Spannable spannable1 = new SpannableString(topicText);
        spannable1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), topic.length(), topic.length() + topicInfo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        dateTextView.setText(spannable1, TextView.BufferType.SPANNABLE);

        //Adding Downloads
        topicInfo = String.valueOf(images.get(position).getImageDownloads());
        downloadsTextView.setText(topicInfo);

        //Adding Categories
        topic = "Categories: ";
        topicInfo = "";
        for (int i = 0; i < images.get(position).getImageCategories().size(); i++) {
            if (i != images.get(position).getImageCategories().size() - 1)
                topicInfo = topicInfo + images.get(position).getImageCategories().get(i) + ", ";
            else topicInfo = topicInfo + images.get(position).getImageCategories().get(i);
        }

        topicText = topic + topicInfo;

        Spannable spannable3 = new SpannableString(topicText);
        spannable3.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), topic.length(), topic.length() + topicInfo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        categoriesTextView.setText(spannable3, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void initPopUpFavorite() {
        add_to_favorites_popup.setMinimumHeight(300);
        add_to_favorite_popup_behavior = BottomSheetBehavior.from(add_to_favorites_popup);

        //Declarations
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(COL_NUM, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new FavoritesAdapter(favoritePresenter, mPresenter.getCollections(), this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new WallpaperActivity.MyItemDecoration());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initInterstitialAd() {
        //TODO: Init Interstitial Ad
    }

    @Override
    public void showInfos() {
        info_popup_behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void hideInfos() {
        info_popup_behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void showFavorite() {
        add_to_favorite_popup_behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void hideFavorite() {
        add_to_favorite_popup_behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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

    public Image getImage() {
        return images.get(mViewPager.getCurrentItem());
    }

    public class MyItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            // only for the last one
            outRect.bottom = 48;
            outRect.right = 32;
            outRect.left = 32;
        }
    }

}
