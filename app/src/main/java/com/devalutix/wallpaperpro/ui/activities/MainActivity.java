package com.devalutix.wallpaperpro.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.MainContract;
import com.devalutix.wallpaperpro.di.components.DaggerMVPComponent;
import com.devalutix.wallpaperpro.di.components.MVPComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.presenters.FavoritesPresenter;
import com.devalutix.wallpaperpro.presenters.MainPresenter;
import com.devalutix.wallpaperpro.ui.adapters.MainPagerAdapter;
import com.devalutix.wallpaperpro.ui.custom.CustomPopUpWindow;
import com.devalutix.wallpaperpro.ui.fragments.CategoriesFragment;
import com.devalutix.wallpaperpro.ui.fragments.ExploreFragment;
import com.devalutix.wallpaperpro.ui.fragments.FavoritesFragment;
import com.devalutix.wallpaperpro.utils.Config;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private static final int REQUEST_WRITE_STORAGE = 1;
    private static String TAG = "MainActivity";

    //Declarations
    MVPComponent mvpComponent;
    @Inject
    MainPresenter mPresenter;
    private MainPagerAdapter mAdapter;
    private FavoritesFragment favorites;
    private ExploreFragment explore;
    private CategoriesFragment categories;

    //View Declarations
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.view_pager_main)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout_main)
    TabLayout mTab;
    @BindView(R.id.page_title)
    TextView title;
    @BindView(R.id.add_collection)
    ImageView add_collection;
    @BindView(R.id.search)
    SearchView search;
    @BindView(R.id.adView_main)
    AdView ad;

    //ClickListeners
    @OnClick(R.id.add_collection)
    public void showAddCollectionPopUp(){
        favorites.showAddCollectionPopUp();
    }

    //Essentials Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set ButterKnife
        ButterKnife.bind(this);

        //Initialize Dagger For Application
        mvpComponent = getComponent();

        //Inject the Component Here
        mvpComponent.inject(this);

        //Attach View To Presenter
        mPresenter.attach(this);

        //Request Storage Permission
        mPresenter.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_WRITE_STORAGE);

        //Init Ad Banner
        initAdBanner();

        //Init View Pager
        initViewPager();

        //Init Tabs
        initTabLayout();

        //Set First Page Name
        title.setText(Config.tabTitles[0]);

        //TabLayout Listener
        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: {
                        title.setText(Config.tabTitles[0]);
                        showSearchBar();
                    }
                    break;
                    case 1: {
                        title.setText(Config.tabTitles[1]);
                        hideAll();
                    }
                    break;
                    case 2: {
                        title.setText(Config.tabTitles[2]);
                        showAddCollection();
                    }
                    break;
                }
                enableTabAt(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_icon))
                        .setImageResource(Config.tabIconsDisabled[tab.getPosition()]);
                tab.getCustomView().findViewById(R.id.tab_title).setVisibility(GONE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });

        //Navigation Menu OnClickListener
        ArrayList<Category> categories = mPresenter.getCategories();

        if (categories == null) {
            mNavigationView.getMenu().add(R.id.categories, Menu.FIRST, Menu.NONE, "None");
        } else {
            int i = 0;
            for (Category category :
                    categories) {
                mNavigationView.getMenu().add(R.id.categories, Menu.FIRST + i, Menu.NONE, category.getCategoryName());
                i++;
            }
        }


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(false);
                switch (item.getItemId()) {
                    //
                }
                // close drawer when item is tapped
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        if (getIntent().getBooleanExtra("ToFavorites", false))
            mViewPager.setCurrentItem(2);
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
        AdView ad1;
        if (Config.ENABLE_AD_BANNER) {
            if (Config.ENABLE_GDPR) {
                //If Gdpr enabled get the Variable banner ad from the Class Gdpr
                //else use the current one
                ad1 = mPresenter.getGDPR().getmAd();
            } else ad1 = ad;
            ad1.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AdView ad1;
        if (Config.ENABLE_AD_BANNER) {
            if (Config.ENABLE_GDPR) {
                //If Gdpr enabled get the Variable banner ad from the Class Gdpr
                //else use the current one
                ad1 = mPresenter.getGDPR().getmAd();
            } else ad1 = ad;
            ad1.destroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdView ad1;
        if (Config.ENABLE_AD_BANNER) {
            if (Config.ENABLE_GDPR) {
                //If Gdpr enabled get the Variable banner ad from the Class Gdpr
                //else use the current one
                ad1 = mPresenter.getGDPR().getmAd();
            } else ad1 = ad;
            ad1.resume();
        }
    }

    //Methods
    @Override
    public void initAdBanner() {
        if (Config.ENABLE_AD_BANNER) {
            if (Config.ENABLE_GDPR) {
                mPresenter.initGDPR(ad);
            } else {
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();
                ad.loadAd(adRequest);
            }
        } else {
            ad.setVisibility(GONE);
        }
    }

    @Override
    public void showSearchBar() {
        hideAll();
        search.setVisibility(VISIBLE);
    }

    @Override
    public void showAddCollection() {
        hideAll();
        add_collection.setVisibility(VISIBLE);
    }

    @Override
    public void hideAll() {
        search.setVisibility(GONE);
        add_collection.setVisibility(GONE);
    }

    @Override
    public void enableTabAt(int x) {
        TabLayout.Tab tab = mTab.getTabAt(x);

        for (int i = 0; i < 3; i++) {
            if (i == x) {
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_icon))
                        .setImageResource(Config.tabIconsEnabled[i]);
                tab.getCustomView().findViewById(R.id.tab_title).setVisibility(VISIBLE);

            } else {
                TabLayout.Tab tab1 = mTab.getTabAt(i);
                ((ImageView) tab1.getCustomView().findViewById(R.id.tab_icon))
                        .setImageResource(Config.tabIconsDisabled[i]);
                tab1.getCustomView().findViewById(R.id.tab_title).setVisibility(GONE);
            }
        }
    }

    @Override
    public void initViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        explore = new ExploreFragment();
        categories = new CategoriesFragment();
        favorites = new FavoritesFragment();

        fragments.add(explore);
        fragments.add(categories);
        fragments.add(favorites);

        mAdapter = new MainPagerAdapter(getSupportFragmentManager(), fragments, MainActivity.this);
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void initTabLayout() {
        mTab.setupWithViewPager(mViewPager);
        // Iterate over all tabs and set the custom view
        for (int i = 0; i < mTab.getTabCount(); i++) {
            TabLayout.Tab tab = mTab.getTabAt(i);
            tab.setCustomView(mAdapter.getTabView(i));
        }
        enableTabAt(0);
    }
}
