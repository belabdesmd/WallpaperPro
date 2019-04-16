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
import com.devalutix.wallpaperpro.contracts.CategoriesContract;
import com.devalutix.wallpaperpro.di.components.ApplicationComponent;
import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.presenters.CategoriesPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

public class CategoriesFragment extends Fragment implements CategoriesContract.View {
    private static String TAG = "CategoriesFragment";

    //Declarations
    protected ApplicationComponent mComponent;
    @Inject
    CategoriesPresenter mPresenter;

    //View Declarations

    //ClickListeners

    //Constructor
    public CategoriesFragment() {
        // Required empty public constructor
    }

    //Essentials Methods
    public static CategoriesFragment newInstance(String param1, String param2) {
        CategoriesFragment fragment = new CategoriesFragment();
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
        View view = inflater.inflate(R.layout.fragment_category, container, false);

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
    public void initRecyclerView(ArrayList<Category> categories) {

    }

    @Override
    public void updateRecyclerView(ArrayList<Category> categories) {

    }

    @Override
    public void showNoNetwork() {

    }

    @Override
    public void showCategoriesList() {

    }

    @Override
    public void goToImages(String categoryName) {

    }

}
