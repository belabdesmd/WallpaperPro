package com.devalutix.wallpaperpro.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.base.BaseApplication;
import com.devalutix.wallpaperpro.contracts.FavoritesContract;
import com.devalutix.wallpaperpro.di.components.DaggerMVPComponent;
import com.devalutix.wallpaperpro.di.components.MVPComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.presenters.FavoritesPresenter;
import com.devalutix.wallpaperpro.ui.activities.ImagesActivity;
import com.devalutix.wallpaperpro.ui.activities.WallpaperActivity;
import com.devalutix.wallpaperpro.ui.adapters.FavoritesAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

public class FavoritesFragment extends Fragment implements FavoritesContract.View {
    private static String TAG = "FavoritesFragment";
    private static final int COL_NUM = 3;

    //Declarations
    private MVPComponent mvpComponent;
    private FavoritesAdapter mAdapter;
    private ArrayList<Collection> collections;
    @Inject
    FavoritesPresenter mPresenter;

    //View Declarations
    @BindView(R.id.favorites_recyclerview)
    RecyclerView mRecyclerView;

    //ClickListeners

    //Constructor
    public FavoritesFragment() {
        // Required empty public constructor
    }

    //Essentials Methods
    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
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
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        //Init ButterKnife
        ButterKnife.bind(this, view);

        //Initialize Dagger For Application
        mvpComponent = getComponent();

        //Inject the Component Here
        mvpComponent.inject(this);

        //Attach View To Presenter
        mPresenter.attach(this);

        //Init Recycler View
        mPresenter.initRecyclerView();

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
    public void initRecyclerView(ArrayList<Collection> collections) {

        //Set the List
        this.collections = collections;

        //Declarations
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(COL_NUM, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new FavoritesAdapter(mPresenter, collections, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateRecyclerView(ArrayList<Collection> collections) {
        this.collections = collections;

        //Deleting the List of the Categories
        mAdapter.clearAll();

        // Adding The New List of Categories
        mAdapter.addAll(collections);
    }

    @Override
    public void goToImages(String collectionName) {
        Intent goToImages = new Intent(getActivity(), ImagesActivity.class);

        goToImages.putExtra("collection", collectionName);
        goToImages.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goToImages);
    }
}
