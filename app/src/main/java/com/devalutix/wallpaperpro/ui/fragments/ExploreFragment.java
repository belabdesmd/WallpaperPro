package com.devalutix.wallpaperpro.ui.fragments;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.ExploreContract;
import com.devalutix.wallpaperpro.di.components.DaggerMVPComponent;
import com.devalutix.wallpaperpro.di.components.MVPComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.pojo.Wallpaper;
import com.devalutix.wallpaperpro.presenters.ExplorePresenter;
import com.devalutix.wallpaperpro.ui.adapters.WallpapersAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

public class ExploreFragment extends Fragment implements ExploreContract.View {
    private static final int COL_NUM = 3;

    /****************************************** Declarations **************************************/
    private MVPComponent mvpComponent;
    private WallpapersAdapter mAdapter;
    private String mode = null;
    @Inject
    ExplorePresenter mPresenter;
    private BottomSheetBehavior retry_behavior;

    /****************************************** View Declarations *********************************/
    @BindView(R.id.explore_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_to_refresh_explore)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.no_network_images)
    ImageView noNetworkLayout;
    @BindView(R.id.empty_explore)
    TextView emptyCollectionLayout;

    @BindView(R.id.recent_filter)
    Button recent;
    @BindView(R.id.popular_filter)
    Button popular;
    @BindView(R.id.featured_filter)
    Button featured;

    //Retry
    @BindView(R.id.retry_card)
    ConstraintLayout retry_card;
    @BindView(R.id.retry_msg)
    TextView retry_msg;


    /****************************************** Click Listeners ***********************************/
    @OnClick(R.id.popular_filter)
    void popularFilter() {
        mRefresh.setRefreshing(true);
        enableFilter(0);
        mode = "popular";
        mPresenter.updateRecyclerView(mode);
    }

    @OnClick(R.id.recent_filter)
    void recentFilter() {
        mRefresh.setRefreshing(true);
        enableFilter(1);
        mode = "recent";
        mPresenter.updateRecyclerView(mode);
    }

    @OnClick(R.id.featured_filter)
    void featuredFilter() {
        mRefresh.setRefreshing(true);
        enableFilter(2);
        mode = "featured";
        mPresenter.updateRecyclerView(mode);
    }

    @OnClick(R.id.retry)
    void retry() {
        refresh();
    }

    /****************************************** Constructor ***************************************/
    public ExploreFragment() {
        // Required empty public constructor
    }

    /****************************************** Essential Methods *********************************/
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        enableFilter(0);
        mode = "recent";
        mRefresh.setRefreshing(true);
        mPresenter.initRecyclerView();

        //Init Retry
        initRetrySheet();

        //When Pulling To Refresh Listener
        mRefresh.setOnRefreshListener(this::refresh);

        return view;
    }

    @Override
    public MVPComponent getComponent() {
        if (mvpComponent == null) {
            mvpComponent = DaggerMVPComponent
                    .builder()
                    .applicationModule(new ApplicationModule(Objects.requireNonNull(getActivity()).getApplication()))
                    .mVPModule(new MVPModule(getActivity()))
                    .build();
        }
        return mvpComponent;
    }

    /****************************************** Methods *******************************************/
    @Override
    public void initRecyclerView(ArrayList<Wallpaper> wallpapers) {

        //Declarations
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(COL_NUM, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new WallpapersAdapter(wallpapers, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new MyItemDecoration());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateRecyclerView(ArrayList<Wallpaper> wallpapers) {

        //Deleting the List of the Categories
        mAdapter.clearAll();

        // Adding The New List of Categories
        mAdapter.addAll(wallpapers);

        /*
         * Stop Refreshing the Animations
         */
        mRefresh.setRefreshing(false);
    }

    @Override
    public void initRetrySheet() {
        retry_behavior = BottomSheetBehavior.from(retry_card);
        hideRetryCard();
    }

    @Override
    public void showNoNetwork() {
        mRefresh.setRefreshing(false);

        mRecyclerView.setVisibility(View.GONE);
        noNetworkLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void showWallpapersList() {
        mRefresh.setRefreshing(false);

        mRecyclerView.setVisibility(View.VISIBLE);
        noNetworkLayout.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyCollection(String message) {
        mRefresh.setRefreshing(false);

        mRecyclerView.setVisibility(View.GONE);
        noNetworkLayout.setVisibility(View.GONE);

        emptyCollectionLayout.setText(message);
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
    public void enableFilter(int position) {
        disableAllFilters();
        switch (position) {
            case 0: {
                popular.setBackground(getResources().getDrawable(R.drawable.filter_on));
                popular.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            break;
            case 1: {
                recent.setBackground(getResources().getDrawable(R.drawable.filter_on));
                recent.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            break;
            case 2: {
                featured.setBackground(getResources().getDrawable(R.drawable.filter_on));
                featured.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            break;
        }
    }

    @Override
    public void disableAllFilters() {
        recent.setBackground(getResources().getDrawable(R.drawable.filter_off));
        recent.setTextColor(getResources().getColor(R.color.colorAccent));


        popular.setBackground(getResources().getDrawable(R.drawable.filter_off));
        popular.setTextColor(getResources().getColor(R.color.colorAccent));


        featured.setBackground(getResources().getDrawable(R.drawable.filter_off));
        featured.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void refresh() {
        hideRetryCard();
        mRefresh.setRefreshing(true);
        mPresenter.updateRecyclerView(mPresenter.getMode());
    }

    @Override
    public boolean isRefreshing() {
        return mRefresh.isRefreshing();
    }

    @Override
    public ExplorePresenter getPresenter() {
        return mPresenter;
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
