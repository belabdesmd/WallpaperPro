package com.devalutix.wallpaperpro.ui.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.devalutix.wallpaperpro.pojo.Image;

import java.util.ArrayList;
import java.util.List;

public class WallpaperPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "WallpaperPagerAdapter";

    //Declarations
    private final List<Fragment> mFragmentList = new ArrayList<>();

    public WallpaperPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment) {
        Log.d(TAG, "addFrag: Adding the Fragment To the Adapter");

        mFragmentList.add(fragment);
    }
}
