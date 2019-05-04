package com.devalutix.wallpaperpro.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

import android.os.Bundle;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.base.BaseApplication;
import com.devalutix.wallpaperpro.contracts.ImagesContract;
import com.devalutix.wallpaperpro.di.components.DaggerMVPComponent;
import com.devalutix.wallpaperpro.di.components.MVPComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.presenters.ImagesPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

public class ImagesActivity extends AppCompatActivity implements ImagesContract.View {
    private static String TAG = "ImagesActivity";

    //Declarations
    MVPComponent mvpComponent;
    @Inject
    ImagesPresenter mPresenter;

    //View Declarations

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
    public void setPageName(String name) {

    }

    @Override
    public void initRecyclerView(ArrayList<Image> images) {

    }

    @Override
    public void updateRecyclerView(ArrayList<Image> images) {

    }

    @Override
    public void goToWallpaperActivity(int position, ArrayList<Image> images) {

    }
}
