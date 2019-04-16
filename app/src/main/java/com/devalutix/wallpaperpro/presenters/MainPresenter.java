package com.devalutix.wallpaperpro.presenters;

import com.devalutix.wallpaperpro.contracts.MainContract;
import com.devalutix.wallpaperpro.ui.activities.MainActivity;

import javax.inject.Inject;

public class MainPresenter implements MainContract.Presenter {
    private static String TAG = "MainPresenter";

    //Declarations
    MainActivity mView;

    //Constructor
    @Inject
    public MainPresenter() {
    }

    //Essential Methods
    @Override
    public void attach(MainContract.View view) {
        mView = (MainActivity) view;
    }

    @Override
    public void dettach() {
        mView = null;
    }

    @Override
    public boolean isAttached() {
        return !(mView == null);
    }

    //Methods
    @Override
    public void requestPermission() {

    }

    @Override
    public void initGDPR() {

    }
}
