package com.devalutix.wallpaperpro.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.base.BaseApplication;
import com.devalutix.wallpaperpro.contracts.ImagesContract;
import com.devalutix.wallpaperpro.di.components.DaggerMVPComponent;
import com.devalutix.wallpaperpro.di.components.MVPComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.presenters.ImagesPresenter;
import com.devalutix.wallpaperpro.ui.adapters.ImagesAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

public class ImagesActivity extends AppCompatActivity implements ImagesContract.View {
    private static String TAG = "ImagesActivity";
    private static final int COL_NUM = 3;

    //Declarations
    private MVPComponent mvpComponent;
    private ImagesAdapter mAdapter;
    private ArrayList<Image> images;
    @Inject
    ImagesPresenter mPresenter;

    //View Declarations
    @BindView(R.id.images_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.images_page_title)
    TextView title;
    @BindView(R.id.images_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_to_refresh_images)
    SwipeRefreshLayout mRefresh;

    @BindView(R.id.collection_tools)
    LinearLayout tools;
    @BindView(R.id.no_network_images1)
    ImageView noNetworkLayout;

    //ClickListeners

    //Essentials Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

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

        //init RecyclerView
        mRefresh.setRefreshing(true);
        mPresenter.initRecyclerView(getIntent().getStringExtra("mode"),
                getIntent().getStringExtra("name"));

        //Pull To Refresh Listener
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.updateRecyclerView("recent");
            }
        });
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
    public void setPageName(String name) {
        title.setText(name);
    }

    @Override
    public void initRecyclerView(ArrayList<Image> images) {

        //Set the List
        this.images = images;

        //Declarations
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(COL_NUM, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new ImagesAdapter(images, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateRecyclerView(ArrayList<Image> images) {

        this.images = images;

        //Deleting the List of the Categories
        mAdapter.clearAll();

        // Adding The New List of Categories
        mAdapter.addAll(images);

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

    }

    @Override
    public void showPicturesList() {
        mRefresh.setRefreshing(false);

        mRecyclerView.setVisibility(View.VISIBLE);
        noNetworkLayout.setVisibility(View.GONE);
    }

    @Override
    public void showCollectionActions() {
        tools.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCollectionActions() {
        tools.setVisibility(View.GONE);
    }

    @Override
    public void goToWallpaperActivity(int position, ArrayList<Image> images) {
        if (!mRefresh.isRefreshing()){
            Intent goToWallpaper = new Intent(this, WallpaperActivity.class);

            //Transferring the List
            String jsonImages = mPresenter.listToString(images);

            //Putting the Extras
            goToWallpaper.putExtra("current",position);
            goToWallpaper.putExtra("images",jsonImages);
            goToWallpaper.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(goToWallpaper);
        }
    }
}
