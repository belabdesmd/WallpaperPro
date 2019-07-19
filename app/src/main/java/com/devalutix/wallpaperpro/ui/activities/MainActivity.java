package com.devalutix.wallpaperpro.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.MainContract;
import com.devalutix.wallpaperpro.di.components.DaggerMVPComponent;
import com.devalutix.wallpaperpro.di.components.MVPComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.presenters.MainPresenter;
import com.devalutix.wallpaperpro.ui.adapters.MainPagerAdapter;
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
    private static String TAG = "MainActivity";
    private static final int REQUEST_WRITE_STORAGE = 1;

    //Declarations
    private MVPComponent mvpComponent;
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
    @BindView(R.id.page_title)
    TextView title;
    @BindView(R.id.view_pager_main)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout_main)
    TabLayout mTab;
    @BindView(R.id.add_collection)
    ImageView add_collection;
    @BindView(R.id.search)
    SearchView search;
    @BindView(R.id.adView_main)
    AdView ad;

    //ClickListeners
    @OnClick(R.id.add_collection)
    public void showAddCollectionPopUp() {
        favorites.showAddCollectionPopUp();
    }

    //Essentials Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Creating Views");

        //Initialize Dagger For Application
        mvpComponent = getComponent();
        //Inject the Component Here
        mvpComponent.inject(this);

        //Enable/Disable Dark Mode
        if (mPresenter.isDarkModeEnabled())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        //Set Dark Mode
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppDarkTheme);
        else setTheme(R.style.AppLightTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set ButterKnife
        ButterKnife.bind(this);

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

        //Set First Page Name/Show Search View
        title.setText(Config.tabTitles[0]);
        showSearchBar();

        //Search Bar Event Open/Close
        initSearchBar();

        //Init Dark Mode
        darkModeListener();

        //Navigation Menu OnClickListener
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //TODO: Add Drawer Menu Actions

                // close drawer when item is tapped
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        //Go To Favorites (After Saving a Picture)
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
    public void initSearchBar() {
        Log.d(TAG, "initSearchBar: Init Search Bar");

        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setVisibility(View.INVISIBLE);
            }
        });

        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                title.setVisibility(VISIBLE);
                return false;
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, "query: " + query, Toast.LENGTH_SHORT).show();
                mPresenter.searchImages(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void darkModeListener() {
        //Dark Mode
        MenuItem dark_mode_item = mNavigationView.getMenu().findItem(R.id.dark_mode);
        Switch dark_mode = (Switch) MenuItemCompat.getActionView(dark_mode_item);
        dark_mode.setChecked(mPresenter.isDarkModeEnabled());
        dark_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    mPresenter.enableDarkMode(true);
                    restartApp();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    mPresenter.enableDarkMode(false);
                    restartApp();
                }
            }
        });
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
    }

    @Override
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void restartApp() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
