package com.devalutix.wallpaperpro.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.base.BaseApplication;
import com.devalutix.wallpaperpro.contracts.ImagesContract;
import com.devalutix.wallpaperpro.di.components.DaggerMVPComponent;
import com.devalutix.wallpaperpro.di.components.MVPComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.presenters.ImagesPresenter;
import com.devalutix.wallpaperpro.ui.adapters.ImagesAdapter;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import java.util.ArrayList;

import javax.inject.Inject;

public class ImagesActivity extends AppCompatActivity implements ImagesContract.View {
    private static String TAG = "ImagesActivity";
    private static final int COL_NUM = 3;

    //Declarations
    private MVPComponent mvpComponent;
    private ImagesAdapter mAdapter;
    private ArrayList<Image> images;
    private SlideUp slideUpEditCollection;
    @Inject
    ImagesPresenter mPresenter;

    //View Declarations
    @BindView(R.id.images_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.images_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_to_refresh_images)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.no_network_images1)
    ImageView noNetworkLayout;

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
        setContentView(R.layout.activity_images);

        //Set ButterKnife
        ButterKnife.bind(this);

        //Attach View To Presenter
        mPresenter.attach(this);

        //Set Toolbar
        setToolbar();

        //Set Page Name
        setPageName(getIntent().getStringExtra("mode"), getIntent().getStringExtra("name"));

        //init RecyclerView
        mRefresh.setRefreshing(true);
        mPresenter.initRecyclerView(getIntent().getStringExtra("mode"),
                getIntent().getStringExtra("name"));

        //Pull To Refresh Listener
        mRefresh.setOnRefreshListener(() -> mPresenter.updateRecyclerView(getIntent().getStringExtra("name")));
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
    public void setPageName(String mode, String name) {
        if (mode.equals("search"))
            mToolbar.setTitle("Search: " + name);
        else mToolbar.setTitle(name);
    }

    @Override
    public void initRecyclerView(ArrayList<Image> images) {

        //Set the List
        this.images = images;

        //Declarations
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(COL_NUM, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new ImagesAdapter(images, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new ImagesActivity.MyItemDecoration());
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
    public void goToWallpaperActivity(int position, ArrayList<Image> images) {
        if (!mRefresh.isRefreshing()) {
            Intent goToWallpaper = new Intent(this, WallpaperActivity.class);

            //Transferring the List
            String jsonImages = mPresenter.listToString(images);

            //Putting the Extras
            goToWallpaper.putExtra("current", position);
            goToWallpaper.putExtra("images", jsonImages);
            goToWallpaper.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(goToWallpaper);
        }
    }

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

    public class MyItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            // only for the last one
            outRect.bottom = 16;
            outRect.right = 16;
            outRect.left = 16;
        }
    }
}
