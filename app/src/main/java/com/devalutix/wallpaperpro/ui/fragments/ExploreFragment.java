package com.devalutix.wallpaperpro.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.base.BaseApplication;
import com.devalutix.wallpaperpro.contracts.ExploreContract;
import com.devalutix.wallpaperpro.di.components.DaggerMVPComponent;
import com.devalutix.wallpaperpro.di.components.MVPComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.presenters.ExplorePresenter;

import java.util.ArrayList;

import javax.inject.Inject;

public class ExploreFragment extends Fragment implements ExploreContract.View {
    private static String TAG = "ExploreFragment";

    //Declarations
    MVPComponent mvpComponent;
    @Inject
    ExplorePresenter mPresenter;

    //View Declarations

    //ClickListeners

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

        return view;
    }

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

    }

    @Override
    public void updateRecyclerView(ArrayList<Image> images) {

    }

    @Override
    public void showNoNetwork() {

    }

    @Override
    public void showPicturesList() {

    }

    @Override
    public void goToWallpaperActivity(int position, ArrayList<Image> images) {

    }
}
