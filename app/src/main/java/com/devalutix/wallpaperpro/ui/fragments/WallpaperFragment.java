package com.devalutix.wallpaperpro.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.presenters.WallpaperPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WallpaperFragment extends Fragment {
    public static final String TAG = "WallpaperFragment";

    /****************************************** Declarations **************************************/
    private String wallpaperImageUrl;
    private WallpaperPresenter mPresenter;

    /****************************************** View Declarations *********************************/
    @BindView(R.id.wallpaper)
    ImageView image;

    /****************************************** Constructor ***************************************/
    public WallpaperFragment() {
        //Require Empty Constructor
    }

    /****************************************** Essential Methods *********************************/
    public static WallpaperFragment newInstance(String image) {

        Bundle args = new Bundle();

        WallpaperFragment fragment = new WallpaperFragment();
        args.putString("image", image);
        fragment.setArguments(args);
        return fragment;
    }

    public void setPresenter(WallpaperPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Creating the View");

        View view = inflater.inflate(R.layout.one_wallpaper_fragment, container, false);
        ButterKnife.bind(this, view);

        assert getArguments() != null;
        wallpaperImageUrl = getArguments().getString("image");

        //Loading the Image
        Glide.with(this)
                .load(wallpaperImageUrl)
                .fitCenter()
                //.placeholder(R.drawable.loading_spinner)
                .into(image);

        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }
}
