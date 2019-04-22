package com.devalutix.wallpaperpro.presenters;

import com.devalutix.wallpaperpro.contracts.ImagesContract;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.ui.activities.ImagesActivity;

import java.util.ArrayList;

import javax.inject.Inject;

public class ImagesPresenter implements ImagesContract.Presenter {
    private static String TAG = "ImagesPresenter";

    //Declarations
    ImagesActivity mView;

    //Constructor
    public ImagesPresenter() {
    }

    //Essential Methods
    @Override
    public void attach(ImagesContract.View view) {
        mView = (ImagesActivity) view;
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
    public void initRecyclerView(String mode) {

    }

    @Override
    public ArrayList<Image> getImagesFromCollection() {
        return null;
    }

    @Override
    public ArrayList<Image> getImageFromCategory() {
        return null;
    }
}
