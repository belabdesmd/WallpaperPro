package com.devalutix.wallpaperpro.ui.adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class WallpaperPagerAdapter extends FragmentPagerAdapter {

    /*************************************** Declarations *****************************************/
    private final List<Fragment> mFragmentList = new ArrayList<>();

    /*************************************** Constructor ******************************************/
    public WallpaperPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    /*************************************** Methods **********************************************/
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment) {
        mFragmentList.add(fragment);
    }
}
