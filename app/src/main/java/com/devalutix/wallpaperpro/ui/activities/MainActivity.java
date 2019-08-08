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
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.google.android.gms.ads.AdView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private static String TAG = "MainActivity";
    private static final int REQUEST_WRITE_STORAGE = 1;

    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    @Inject
    MainPresenter mPresenter;
    private MainPagerAdapter mAdapter;
    private ExploreFragment explore;
    private CategoriesFragment categories;
    private FavoritesFragment favorites;
    private boolean doubleBackToExitPressedOnce = false;

    /**************************************** View Declarations ***********************************/
    //Drawer Menu
    @BindView(R.id.drawer_layout)
    public DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    //Page Title
    @BindView(R.id.page_title)
    TextView title;

    //View Pages
    @BindView(R.id.view_pager_main)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout_main)
    TabLayout mTab;

    //Actions
    @BindView(R.id.add_collection)
    ImageView add_collection;
    @BindView(R.id.search)
    SearchView search;
    @BindView(R.id.adView_main)
    AdView ad;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.add_collection)
    public void showAddCollectionPopUp() {
        favorites.showAddCollectionPopUp();
    }


    /**************************************** Essential Methods ***********************************/
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

        //Check For Consent
        mPresenter.checkGDPRConsent();

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

        //Drawer Menu Index
        mPresenter.showDrawerMenuIndex();

        //Navigation Menu OnClickListener
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            switch (id) {
                case R.id.our_apps_menu:
                    ourApps();
                    break;
                case R.id.rate_app_menu:
                    rateApp();
                    break;
                case R.id.share_app_menu:
                    shareApp();
                    break;
                case R.id.send_feedback_menu:
                    sendFeedback();
                    break;
            }

            // close drawer when item is tapped
            mDrawerLayout.closeDrawers();
            return true;
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
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: paused");
        super.onPause();
        ad.pause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: destroyed");
        super.onDestroy();
        ad.destroy();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: resumed");
        super.onResume();
        ad.resume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_STORAGE)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                mPresenter.grantDownload();
            else mPresenter.disableDownload();
    }

    /**************************************** Methods *********************************************/
    @Override
    public void darkModeListener() {
        //Dark Mode
        MenuItem dark_mode_item = mNavigationView.getMenu().findItem(R.id.dark_mode);
        Switch dark_mode = (Switch) MenuItemCompat.getActionView(dark_mode_item);
        dark_mode.setChecked(mPresenter.isDarkModeEnabled());
        dark_mode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                mPresenter.enableDarkMode(true);
                restartApp();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                mPresenter.enableDarkMode(false);
                restartApp();
            }
        });
    }

    @Override
    public void initAdBanner() {
        if (Config.ENABLE_AD_BANNER) {
            mPresenter.loadAd(ad);
        } else {
            ad.setVisibility(GONE);
        }
    }

    @Override
    public void initSearchBar() {
        Log.d(TAG, "initSearchBar: Init Search Bar");
        EditText searchEditText = search.findViewById(R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorAccent));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorAccent));

        search.setOnSearchClickListener(v -> title.setVisibility(View.INVISIBLE));

        search.setOnCloseListener(() -> {
            title.setVisibility(VISIBLE);
            return false;
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search.onActionViewCollapsed();
                mPresenter.searchWallpapers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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
        mViewPager.setOffscreenPageLimit(0);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        explore.refresh();
                        break;
                    case 1:
                        categories.refresh();
                        break;
                }
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
    public void initTabLayout() {
        mTab.setupWithViewPager(mViewPager);
        // Iterate over all tabs and set the custom view
        for (int i = 0; i < mTab.getTabCount(); i++) {
            TabLayout.Tab tab = mTab.getTabAt(i);
            assert tab != null;
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
                ((ImageView) Objects.requireNonNull(tab.getCustomView()).findViewById(R.id.tab_icon))
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
                assert tab != null;
                ((ImageView) Objects.requireNonNull(tab.getCustomView()).findViewById(R.id.tab_icon))
                        .setImageResource(Config.tabIconsEnabled[i]);
                tab.getCustomView().findViewById(R.id.tab_title).setVisibility(VISIBLE);

            } else {
                TabLayout.Tab tab1 = mTab.getTabAt(i);
                assert tab1 != null;
                ((ImageView) Objects.requireNonNull(tab1.getCustomView()).findViewById(R.id.tab_icon))
                        .setImageResource(Config.tabIconsDisabled[i]);
                tab1.getCustomView().findViewById(R.id.tab_title).setVisibility(GONE);
            }
        }
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

    /**************************************** Menu Actions ****************************************/
    private void ourApps() {
        String developerName = getResources().getString(R.string.developer_name);
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=" + developerName)));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=" + developerName + "&hl=en")));
        }
    }

    private void rateApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());

        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private void shareApp() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        String sAux = getString(R.string.share_app_message) + "\n";
        sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName();
        i.putExtra(Intent.EXTRA_TEXT, sAux);
        startActivity(Intent.createChooser(i, "choose one"));
    }

    private void sendFeedback() {
        String[] TO = {getResources().getString(R.string.developer_mail)};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback For " + getResources().getString(R.string.app_name));
        String message = "Message:\n\n---\n";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            message = message + "App Version : " + version + "\n";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        message = message + "Android Version : " + android.os.Build.VERSION.SDK_INT + "\n";
        message = message + "Device Brand : " + Build.MANUFACTURER + "\n";
        message = message + "Device Model : " + Build.MODEL;

        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
