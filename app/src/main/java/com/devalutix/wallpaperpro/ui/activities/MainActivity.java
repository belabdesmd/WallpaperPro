package com.devalutix.wallpaperpro.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.base.BaseApplication;
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
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private static final int REQUEST_WRITE_STORAGE = 1;
    private static String TAG = "MainActivity";

    //Declarations
    MVPComponent mvpComponent;
    @Inject
    MainPresenter mPresenter;
    private MainPagerAdapter mAdapter;

    //View Declarations
    @BindView(R.id.view_pager_main)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout_main)
    TabLayout mTab;
    @BindView(R.id.page_title)
    TextView title;
    @BindView(R.id.adView_main)
    AdView ad;

    //ClickListeners

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
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_icon))
                        .setImageResource(Config.tabIconsEnabled[tab.getPosition()]);
                tab.getCustomView().findViewById(R.id.tab_title).setVisibility(View.VISIBLE);
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
    }

    @Override
    public void showAddCollection() {
        hideAll();
    }

    @Override
    public void hideAll() {

    }

    @Override
    public void enableTabAt(int x) {
        TabLayout.Tab tab = mTab.getTabAt(x);

        for (int i = 0; i < 3; i++) {
            if (i == x) {
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_icon))
                        .setImageResource(Config.tabIconsEnabled[i]);
                tab.getCustomView().findViewById(R.id.tab_title).setVisibility(View.VISIBLE);

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

        fragments.add(new ExploreFragment());
        fragments.add(new CategoriesFragment());
        fragments.add(new FavoritesFragment());

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
