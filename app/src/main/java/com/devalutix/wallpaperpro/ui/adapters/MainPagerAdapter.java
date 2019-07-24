package com.devalutix.wallpaperpro.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.utils.Config;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MainPagerAdapter extends FragmentPagerAdapter {

    /*************************************** Declarations *****************************************/
    private ArrayList<Fragment> fragments;
    private Context context;

    /*************************************** Constructor ******************************************/
    public MainPagerAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> fragments, Context context) {
        super(fm);
        this.fragments = fragments;
        this.context = context;
    }

    /*************************************** Methods **********************************************/
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public View getTabView(int position) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = view.findViewById(R.id.tab_title);
        tv.setText(Config.tabTitles[position]);
        ImageView img = view.findViewById(R.id.tab_icon);
        img.setImageResource(Config.tabIconsDisabled[position]);
        return view;
    }
}
