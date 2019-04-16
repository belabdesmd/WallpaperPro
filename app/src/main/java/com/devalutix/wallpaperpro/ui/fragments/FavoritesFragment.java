package com.devalutix.wallpaperpro.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.base.BaseApplication;
import com.devalutix.wallpaperpro.contracts.FavoritesContract;
import com.devalutix.wallpaperpro.di.components.ApplicationComponent;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.presenters.ExplorePresenter;
import com.devalutix.wallpaperpro.presenters.FavoritesPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

public class FavoritesFragment extends Fragment implements FavoritesContract.View {
    private static String TAG = "FavoritesFragment";

    //Declarations
    protected ApplicationComponent mComponent;
    @Inject
    FavoritesPresenter mPresenter;

    //View Declarations

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

        //Inject Component (Dependency Injection)
        ((BaseApplication) getActivity().getApplication()).getComponent().inject(this);

        //Attach View To Presenter
        mPresenter.attach(this);

        return view;
    }

    //Methods
    @Override
    public void initPopUpWindow() {

    }

    @Override
    public void initRecyclerView(ArrayList<Collection> collections) {

    }

    @Override
    public void updateRecyclerView(ArrayList<Collection> collections) {

    }

    @Override
    public void showPopUpWindow() {

    }

    @Override
    public void hidePopUpWindow() {

    }

    @Override
    public void goToImages(String collectionName) {

    }
}
