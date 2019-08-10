package com.devalutix.wallpaperpro.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.devalutix.wallpaperpro.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WallpaperFragment extends Fragment {

    /****************************************** View Declarations *********************************/
    @BindView(R.id.wallpaper)
    ImageView image;

    /****************************************** Constructor ***************************************/
    public WallpaperFragment() {
        //Require Empty Constructor
    }

    /****************************************** Essential Methods *********************************/
    public static WallpaperFragment newInstance(String wallpaper) {

        Bundle args = new Bundle();

        WallpaperFragment fragment = new WallpaperFragment();
        args.putString("image", wallpaper);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.one_wallpaper_fragment, container, false);
        ButterKnife.bind(this, view);

        assert getArguments() != null;
        String wallpaperImageUrl = getArguments().getString("image");

        //Loading the Wallpaper
        Glide.with(this)
                .load(wallpaperImageUrl)
                .fitCenter()
                .error(R.drawable.error)
                .into(image);

        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }
}
