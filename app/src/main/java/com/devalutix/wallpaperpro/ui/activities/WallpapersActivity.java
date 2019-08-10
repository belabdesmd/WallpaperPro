package com.devalutix.wallpaperpro.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.WallpapersContract;
import com.devalutix.wallpaperpro.di.components.DaggerMVPComponent;
import com.devalutix.wallpaperpro.di.components.MVPComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.pojo.Wallpaper;
import com.devalutix.wallpaperpro.presenters.WallpapersPresenter;
import com.devalutix.wallpaperpro.ui.adapters.WallpapersAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

public class WallpapersActivity extends AppCompatActivity implements WallpapersContract.View {
    private static final int COL_NUM = 3;

    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    private WallpapersAdapter mAdapter;
    @Inject
    WallpapersPresenter mPresenter;
    private BottomSheetBehavior retry_behavior;

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.wallpapers_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.wallpapers_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_to_refresh_wallpapers)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.no_network_wallpapers)
    ImageView noNetworkLayout;
    @BindView(R.id.empty)
    ImageView emptyCollectionLayout;

    //Retry
    @BindView(R.id.retry_card)
    ConstraintLayout retry_card;
    @BindView(R.id.retry_msg)
    TextView retry_msg;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.retry)
    public void retry() {
        refresh();
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
        setContentView(R.layout.activity_wallpapers);

        //Set ButterKnife
        ButterKnife.bind(this);

        //Attach View To Presenter
        mPresenter.attach(this);

        //init UI
        initUI();
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
    protected void onDestroy() {
        mPresenter.dettach();
        super.onDestroy();
    }

    /**************************************** Methods *********************************************/
    private void initUI() {
        //Set Toolbar
        setToolbar();

        //Init Retry Sheet
        initRetrySheet();

        //Set Page Name
        setPageName(getIntent().getStringExtra("name"));

        //init RecyclerView
        mRefresh.setRefreshing(true);
        mPresenter.initRecyclerView(getIntent().getStringExtra("mode"),
                getIntent().getStringExtra("name"));

        //Pull To Refresh Listener
        mRefresh.setColorSchemeResources(R.color.colorAccent);
        mRefresh.setOnRefreshListener(this::refresh);
    }

    @Override
    public void setToolbar() {
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPageName(String name) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(name);
    }

    @Override
    public void initRecyclerView(ArrayList<Wallpaper> wallpapers) {

        //Declarations
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(COL_NUM, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new WallpapersAdapter(wallpapers, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new WallpapersActivity.MyItemDecoration());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initRetrySheet() {
        retry_behavior = BottomSheetBehavior.from(retry_card);
        hideRetryCard();
    }

    @Override
    public void updateRecyclerView(ArrayList<Wallpaper> wallpapers) {

        if (mAdapter != null) {
            //Deleting the List of the Categories
            mAdapter.clearAll();

            // Adding The New List of Categories
            mAdapter.addAll(wallpapers);
        }

        /*
         * Stop Refreshing the Animations
         */
        mRefresh.setRefreshing(false);
    }

    @Override
    public void showNoNetwork() {
        mRefresh.setRefreshing(false);

        mRecyclerView.setVisibility(View.GONE);
        noNetworkLayout.setVisibility(View.VISIBLE);
        emptyCollectionLayout.setVisibility(View.GONE);

    }

    @Override
    public void showPicturesList() {
        mRefresh.setRefreshing(false);

        mRecyclerView.setVisibility(View.VISIBLE);
        noNetworkLayout.setVisibility(View.GONE);
        emptyCollectionLayout.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyCollection(String message) {
        mRefresh.setRefreshing(false);

        mRecyclerView.setVisibility(View.GONE);
        noNetworkLayout.setVisibility(View.GONE);
        emptyCollectionLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRetryCard(String message) {
        retry_msg.setText(message);
        retry_behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void hideRetryCard() {
        retry_behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void goToWallpaperActivity(int position, ArrayList<Wallpaper> wallpapers) {
        if (!mRefresh.isRefreshing()) {
            Intent goToWallpaper = new Intent(this, WallpaperActivity.class);

            //Transferring the List
            String jsonImages = mPresenter.listToString(wallpapers);

            //Putting the Extras
            goToWallpaper.putExtra("current", position);
            goToWallpaper.putExtra("images", jsonImages);
            goToWallpaper.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(goToWallpaper);
        }
    }

    @Override
    public void refresh() {
        hideRetryCard();
        mRefresh.setRefreshing(false);
        mPresenter.updateRecyclerView(mPresenter.getMode());
    }

    public class MyItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                                   @NonNull RecyclerView.State state) {
            // only for the last one
            outRect.bottom = 16;
            outRect.right = 16;
            outRect.left = 16;
        }
    }
}
