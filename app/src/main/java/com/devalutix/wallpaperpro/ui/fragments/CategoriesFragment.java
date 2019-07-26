package com.devalutix.wallpaperpro.ui.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.CategoriesContract;
import com.devalutix.wallpaperpro.di.components.DaggerMVPComponent;
import com.devalutix.wallpaperpro.di.components.MVPComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.presenters.CategoriesPresenter;
import com.devalutix.wallpaperpro.ui.activities.ImagesActivity;
import com.devalutix.wallpaperpro.ui.activities.MainActivity;
import com.devalutix.wallpaperpro.ui.adapters.CategoriesAdapter;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

public class CategoriesFragment extends Fragment implements CategoriesContract.View {
    private static String TAG = "CategoriesFragment";

    /****************************************** Declarations **************************************/
    private MVPComponent mvpComponent;
    private CategoriesAdapter mAdapter;
    private ArrayList<Category> categories;
    @Inject
    CategoriesPresenter mPresenter;

    /****************************************** View Declarations *********************************/
    @BindView(R.id.category_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_to_refresh_categories)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.no_network_category)
    ImageView noNetworkLayout;

    /****************************************** Constructor ***************************************/
    public CategoriesFragment() {
        // Required empty public constructor
    }

    /****************************************** Essential Methods *********************************/
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        //Init ButterKnife
        ButterKnife.bind(this, view);

        //Initialize Dagger For Application
        mvpComponent = getComponent();

        //Inject the Component Here
        mvpComponent.inject(this);

        //Attach View To Presenter
        mPresenter.attach(this);

        //Init Recycler View
        mRefresh.setRefreshing(true);
        mPresenter.initRecyclerView();

        //When Pulling To Refresh Listener
        mRefresh.setOnRefreshListener(() -> {
            ((MainActivity) getActivity()).hideRetryCard();
            mPresenter.updateRecyclerView();
        });

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

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.updateRecyclerView();
    }

    /****************************************** Methods *******************************************/
    @Override
    public void initRecyclerView(ArrayList<Category> categories) {

        //Set the List
        this.categories = categories;

        //Declarations
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new CategoriesAdapter(mPresenter, categories, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new CategoriesFragment.MyItemDecoration());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateRecyclerView(ArrayList<Category> categories) {
        this.categories = categories;

        //Deleting the List of the Categories
        mAdapter.clearAll();

        // Adding The New List of Categories
        mAdapter.addAll(categories);

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
    public void showCategoriesList() {
        mRefresh.setRefreshing(false);

        mRecyclerView.setVisibility(View.VISIBLE);
        noNetworkLayout.setVisibility(View.GONE);

    }

    @Override
    public void goToImages(String categoryName) {
        Intent goToWallpaper = new Intent(getActivity(), ImagesActivity.class);

        //Putting the Extras
        goToWallpaper.putExtra("name", categoryName);
        goToWallpaper.putExtra("mode", "category");
        goToWallpaper.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goToWallpaper);
    }

    @Override
    public void refresh() {
        ((MainActivity) getActivity()).hideRetryCard();
        mRefresh.setRefreshing(false);
        mPresenter.updateRecyclerView();
    }

    public class MyItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                                   @NonNull RecyclerView.State state) {
            // only for the last one
            outRect.bottom = 32;
            outRect.right = 24;
            outRect.left = 24;
        }
    }
}
