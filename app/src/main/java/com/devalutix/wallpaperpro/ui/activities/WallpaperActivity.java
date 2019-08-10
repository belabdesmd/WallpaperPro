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
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
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
import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.pojo.Wallpaper;
import com.devalutix.wallpaperpro.presenters.CategoriesPresenter;
import com.devalutix.wallpaperpro.presenters.FavoritesPresenter;
import com.devalutix.wallpaperpro.presenters.WallpaperPresenter;
import com.devalutix.wallpaperpro.ui.adapters.CategoriesInfoAdapter;
import com.devalutix.wallpaperpro.ui.adapters.FavoritesAdapter;
import com.devalutix.wallpaperpro.ui.adapters.WallpaperPagerAdapter;
import com.devalutix.wallpaperpro.ui.fragments.WallpaperFragment;
import com.devalutix.wallpaperpro.utils.Config;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import static android.view.View.GONE;

public class WallpaperActivity extends AppCompatActivity implements WallpaperContract.View {
    private static final int COL_NUM = 2;
    private static final int REQUEST_WRITE_STORAGE = 1;
    private static final int COL_CAT_NUM = 3;

    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    private InterstitialAd mInterstitialAd;
    private BottomSheetBehavior info_popup_behavior;
    private BottomSheetBehavior add_to_favorite_popup_behavior;
    private CategoriesInfoAdapter mAdapter;

    @Inject
    WallpaperPresenter mPresenter;
    @Inject
    FavoritesPresenter favoritePresenter;
    @Inject
    CategoriesPresenter categoriesPresenter;

    /**************************************** View Declarations ***********************************/
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
    @BindView(R.id.collections_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty)
    TextView emptyCollectionLayout;

    @BindView(R.id.image_title)
    TextView titleTextView;
    @BindView(R.id.date_added)
    TextView dateTextView;
    @BindView(R.id.download_action)
    ImageView download;
    @BindView(R.id.downloads)
    TextView downloadsTextView;
    @BindView(R.id.categories_recycler_view)
    RecyclerView categoriesRecyclerView;
    @BindView(R.id.adView_wallpaper)
    AdView ad;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.share_action)
    public void share() {
        mPresenter.sharePicture(mPresenter.getWallpapers().get(mViewPager.getCurrentItem()));
        hideInfos();
    }

    @OnClick(R.id.download_wrapper)
    public void download() {
        int position = mViewPager.getCurrentItem();
        mPresenter.savePicture(mPresenter.getWallpapers().get(position), position);
    }

    @OnClick(R.id.set_as_wallpaper)
    public void set_wallpaper() {
        mPresenter.setAsWallpaper(mPresenter.getWallpapers().get(mViewPager.getCurrentItem()));
        hideInfos();
    }

    @OnClick(R.id.popup_info_kicker)
    public void openPopUpInfo(View v) {
        if (v.getTag().equals(0)) {
            showInfos();
            v.setTag(1);
        } else {
            hideInfos();
            v.setTag(0);
        }
    }

    @OnClick(R.id.go_to_favorites)
    public void goToFavorites() {
        Intent goToFavorites = new Intent(WallpaperActivity.this, MainActivity.class);
        goToFavorites.putExtra("ToFavorites", true);
        startActivity(goToFavorites);
    }

    /**************************************** Essential Methods ***********************************/
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

        //Init Info PopUp
        initPopUpInfos();
        initInfos(mViewPager.getCurrentItem());

        //Init Add To Favorites PopUp
        initPopUpFavorite();

        //Init Interstitial Ads
        initAdBanner();
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

    @Override
    protected void onPause() {
        super.onPause();
        ad.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dettach();
        ad.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ad.resume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wallpaper_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_STORAGE)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPresenter.grantDownload();
                int position = mViewPager.getCurrentItem();
                mPresenter.savePicture(mPresenter.getWallpapers().get(position), position);
            }
            else mPresenter.disableDownload();
    }

    /**************************************** Methods *********************************************/
    @Override
    public void setToolbar() {

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_wallpaper);
    }

    @Override
    public void initAdBanner() {
        if (mPresenter.getConfig().isBannerEnabled()) {
            ad.setAdUnitId(mPresenter.getConfig().getAdBannerId());
            mPresenter.loadAd(ad);
        } else {
            ad.setVisibility(GONE);
        }
    }

    @Override
    public void initInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(mPresenter.getConfig().getAdInterstitialId());
        mPresenter.loadInterstitialAd(mInterstitialAd);
    }

    @Override
    public void initViewPager() {
        WallpaperPagerAdapter adapter = new WallpaperPagerAdapter(getSupportFragmentManager());

        /*
         * Transferring the Extra From The Images Activity Into A List (String -> List)
         */
        mPresenter.setWallpapers(getIntent().getStringExtra("wallpapers"));

        //Creating the Fragments and Adding Them
        for (Wallpaper image :
                mPresenter.getWallpapers()) {
            //Creating New Fragment With a Picture
            WallpaperFragment frag = WallpaperFragment.newInstance(image.getWallpapers());
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initPopUpInfos() {

        info_popup_behavior = BottomSheetBehavior.from(info_popup);

        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(COL_CAT_NUM,
                StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new CategoriesInfoAdapter(this, categoriesPresenter, new ArrayList<>());
        categoriesRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new WallpaperActivity.CategoriesDecorator());
        categoriesRecyclerView.setAdapter(mAdapter);

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
                    theme.resolveAttribute(R.attr.cardColor, typedValue, true);
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

        Wallpaper wallpaper = mPresenter.getWallpapers().get(position);

        //Declarations
        String topic;
        StringBuilder topicInfo;
        String topicText;

        //Adding Date
        topic = getResources().getString(R.string.title_infos) + " ";
        topicInfo = new StringBuilder(wallpaper.getTitle());

        topicText = topic + topicInfo;

        Spannable spannable = new SpannableString(topicText);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), topic.length(), topic.length() + topicInfo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        titleTextView.setText(spannable, TextView.BufferType.SPANNABLE);

        //Adding Date
        topic = getResources().getString(R.string.date_added_infos) + " ";
        topicInfo = new StringBuilder(wallpaper.getDateAdded().substring(0,10));

        topicText = topic + topicInfo;

        Spannable spannable1 = new SpannableString(topicText);
        spannable1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), topic.length(), topic.length() + topicInfo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        dateTextView.setText(spannable1, TextView.BufferType.SPANNABLE);

        //Adding Downloads
        topicInfo = new StringBuilder(String.valueOf(wallpaper.getDownloads()));
        downloadsTextView.setText(topicInfo.toString());

        //Adding Categories
        ArrayList<String> categories = new ArrayList<>();
        for (Category category:
             wallpaper.getCategories()) {
            categories.add(category.getName());
        }

        mAdapter.clearAll();

        mAdapter.addAll(categories);
    }

    @Override
    public void initPopUpFavorite() {
        add_to_favorite_popup_behavior = BottomSheetBehavior.from(add_to_favorites_popup);

        //Declarations
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(COL_NUM, StaggeredGridLayoutManager.VERTICAL);
        ArrayList<Collection> collections = mPresenter.getCollections();
        FavoritesAdapter mAdapter = new FavoritesAdapter(favoritePresenter, collections, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new WallpaperActivity.MyItemDecoration());
        mRecyclerView.setAdapter(mAdapter);

        hideFavorite();

        if (!(collections.size() > 0))
            showEmptyCollection();
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
        favoritePresenter.add_removeWallpaper(this, getResources().getString(R.string.MyFavorites), mPresenter.getWallpapers(mViewPager.getCurrentItem()));
    }

    @Override
    public void hideFavorite() {
        add_to_favorite_popup_behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void showInterstitialAd() {
        if (mInterstitialAd.isLoaded())
            mInterstitialAd.show();
    }

    @Override
    public void showEmptyCollection() {
        mRecyclerView.setVisibility(View.GONE);
        emptyCollectionLayout.setVisibility(View.VISIBLE);
    }

    public Wallpaper getImage() {
        return mPresenter.getWallpapers(mViewPager.getCurrentItem());
    }

    public class MyItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                                   @NonNull RecyclerView.State state) {
            // only for the last one
            outRect.bottom = 48;
            outRect.right = 32;
            outRect.left = 32;
        }
    }

    public class CategoriesDecorator extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                                   @NonNull RecyclerView.State state) {
            // only for the last one
            outRect.bottom = 16;
            outRect.right = 32;
            outRect.left = 32;
        }
    }
}
