package com.devalutix.wallpaperpro.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.base.BaseApplication;
import com.devalutix.wallpaperpro.contracts.ExploreContract;
import com.devalutix.wallpaperpro.di.components.DaggerMVPComponent;
import com.devalutix.wallpaperpro.di.components.MVPComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.presenters.ExplorePresenter;
import com.devalutix.wallpaperpro.ui.activities.WallpaperActivity;
import com.devalutix.wallpaperpro.ui.adapters.ImagesAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

public class ExploreFragment extends Fragment implements ExploreContract.View {
    private static String TAG = "ExploreFragment";
    private static final int COL_NUM = 3;

    //Declarations
    private MVPComponent mvpComponent;
    private ImagesAdapter mAdapter;
    private ArrayList<Image> images;
    @Inject
    ExplorePresenter mPresenter;

    //View Declarations
    @BindView(R.id.explore_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_to_refresh_explore)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.no_network_images)
    ImageView noNetworkLayout;

    //ClickListeners
    @OnClick(R.id.popular_filter)
    void popularFilter(){
        mRefresh.setRefreshing(true);
        mPresenter.updateRecyclerView("popular");
    }

    @OnClick(R.id.recent_filter)
    void recentFilter(){
        mRefresh.setRefreshing(true);
        mPresenter.updateRecyclerView("recent");
    }

    @OnClick(R.id.featured_filter)
    void featuredFilter(){
        mRefresh.setRefreshing(true);
        mPresenter.updateRecyclerView("featured");
    }

    //Constructor
    public ExploreFragment() {
        // Required empty public constructor
    }

    //Essentials Methods
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        //Init ButterKnife
        ButterKnife.bind(this, view);

        //Initialize Dagger For Application
        mvpComponent = getComponent();

        //Inject the Component Here
        mvpComponent.inject(this);

        //Attach View To Presenter
        mPresenter.attach(this);

        //init RecyclerView
        mRefresh.setRefreshing(true);
        mPresenter.initRecyclerView();

        //Pull To Refresh Listener
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.updateRecyclerView("recent");
            }
        });


        return view;
    }

    @Override
    public MVPComponent getComponent() {
        if (mvpComponent == null) {
            mvpComponent = DaggerMVPComponent
                    .builder()
                    .applicationModule(new ApplicationModule(getActivity().getApplication()))
                    .mVPModule(new MVPModule(getActivity()))
                    .build();
        }
        return mvpComponent;
    }

    //Methods

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
    public void goToWallpaperActivity(int position, ArrayList<Image> images) {
        if (!mRefresh.isRefreshing()){
            Intent goToWallpaper = new Intent(getActivity(), WallpaperActivity.class);

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
