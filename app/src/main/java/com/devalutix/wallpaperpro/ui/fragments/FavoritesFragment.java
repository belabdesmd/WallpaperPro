package com.devalutix.wallpaperpro.ui.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.FavoritesContract;
import com.devalutix.wallpaperpro.di.components.DaggerMVPComponent;
import com.devalutix.wallpaperpro.di.components.MVPComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.presenters.FavoritesPresenter;
import com.devalutix.wallpaperpro.ui.activities.WallpapersActivity;
import com.devalutix.wallpaperpro.ui.activities.MainActivity;
import com.devalutix.wallpaperpro.ui.adapters.FavoritesAdapter;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class FavoritesFragment extends Fragment implements FavoritesContract.View {
    private static final String TAG = "FavoritesFragment";
    private static final int COL_NUM = 2;

    /****************************************** Declarations **************************************/
    private MVPComponent mvpComponent;
    private FavoritesAdapter mAdapter;

    private AlertDialog mDialogAdd;
    private AlertDialog mDialogEdit;

    @Inject
    FavoritesPresenter mPresenter;

    /****************************************** View Declarations *********************************/
    @BindView(R.id.favorites_recycler_view)
    RecyclerView mRecyclerView;

    private EditText get_collection_name;
    private Button add_collection;

    private EditText get_collection_name_edit;
    private Button edit_collection;
    private ImageView remove_collection;

    /****************************************** Constructor ***************************************/
    public FavoritesFragment() {
        // Required empty public constructor
    }

    /****************************************** Essential Methods *********************************/
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        //Init ButterKnife
        ButterKnife.bind(this, view);

        //Initialize Dagger For Application
        mvpComponent = getComponent();

        //Inject the Component Here
        mvpComponent.inject(this);

        //Attach View To Presenter
        mPresenter.attach(this);

        //Init Recycler View
        mPresenter.initRecyclerView();

        //Init Add Collections PoUp
        initAddCollectionPopUp();
        initEditCollectionPopUp();

        return view;
    }

    @Override
    public MVPComponent getComponent() {
        if (mvpComponent == null) {
            mvpComponent = DaggerMVPComponent
                    .builder()
                    .applicationModule(new ApplicationModule(Objects.requireNonNull(getActivity()).getApplication()))
                    .mVPModule(new MVPModule(getActivity()))
                    .build();
        }
        return mvpComponent;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: resumed");
        if (mAdapter != null)
            mPresenter.updateRecyclerView();
        super.onResume();
    }

    /****************************************** Methods *******************************************/
    @Override
    public void initRecyclerView(ArrayList<Collection> collections) {

        //Declarations
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(COL_NUM,
                StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new FavoritesAdapter(mPresenter, collections, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new FavoritesFragment.MyItemDecoration());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initAddCollectionPopUp() {
        LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(getActivity())
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.add_collection_popup,
                null);

        //Create the Alert Dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setView(customView);
        mDialogAdd = alertDialogBuilder.create();

        Objects.requireNonNull(mDialogAdd.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //Init Views
        get_collection_name = customView.findViewById(R.id.add_collection_name);
        add_collection = customView.findViewById(R.id.done_adding);

        //Edit Color
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.primaryTextColor, typedValue, true);
        @ColorInt int color1 = typedValue.data;
        get_collection_name.setTextColor(color1);
        theme.resolveAttribute(R.attr.secondaryTextColor, typedValue, true);
        @ColorInt int color2 = typedValue.data;
        get_collection_name.setHintTextColor(color2);

        //Listeners
        add_collection.setOnClickListener(view -> {
            mPresenter.addCollection(new Collection(get_collection_name.getText().toString(),
                    new ArrayList<>()));
            hideAddCollectionPopUp();
            get_collection_name.getText().clear();
            ((MainActivity) getActivity()).hideKeyboard();
            mPresenter.updateRecyclerView();
        });
        customView.findViewById(R.id.cancel_adding).setOnClickListener(view -> {
            hideAddCollectionPopUp();
            get_collection_name.getText().clear();
            ((MainActivity) getActivity()).hideKeyboard();
        });

        //Disable Done Button
        disableDoneButton();

        //Getting Text
        get_collection_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) enableDoneButton();
                else disableDoneButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initEditCollectionPopUp() {
        LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(getActivity())
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.edit_collection_popup,
                null);

        //Create the Alert Dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setView(customView);
        mDialogEdit = alertDialogBuilder.create();

        Objects.requireNonNull(mDialogEdit.getWindow())
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //Init Views
        get_collection_name_edit = customView.findViewById(R.id.edit_collection_name);
        edit_collection = customView.findViewById(R.id.done_editing);
        remove_collection = customView.findViewById(R.id.remove_collection);

        //Edit Color
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.primaryTextColor, typedValue, true);
        @ColorInt int color1 = typedValue.data;
        get_collection_name_edit.setTextColor(color1);
        theme.resolveAttribute(R.attr.secondaryTextColor, typedValue, true);
        @ColorInt int color2 = typedValue.data;
        get_collection_name_edit.setHintTextColor(color2);

        customView.findViewById(R.id.cancel_editing).setOnClickListener(view -> {
            hideEditCollectionPopUp();
            get_collection_name_edit.getText().clear();
            ((MainActivity) getActivity()).hideKeyboard();
        });

        //Disable Done Button
        disableDoneButton();

        //Getting Text
        get_collection_name_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) enableDoneButton();
                else disableDoneButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void updateRecyclerView(ArrayList<Collection> collections) {

        //Deleting the List of the Categories
        mAdapter.clearAll();

        // Adding The New List of Categories
        mAdapter.addAll(collections);
    }

    @Override
    public void goToWallpapers(String collectionName) {
        Intent goToWallpapers = new Intent(getActivity(), WallpapersActivity.class);

        goToWallpapers.putExtra("name", collectionName);
        goToWallpapers.putExtra("mode", "collection");
        goToWallpapers.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goToWallpapers);
    }

    @Override
    public void showAddCollectionPopUp() {
        mDialogAdd.show();
    }

    @Override
    public void hideAddCollectionPopUp() {
        mDialogAdd.cancel();
    }

    @Override
    public void showEditCollectionPopUp(String collectionName) {
        mDialogEdit.show();
        get_collection_name_edit.setText(collectionName);
        remove_collection.setOnClickListener(view -> {
            mPresenter.removeCollection(collectionName);
            hideEditCollectionPopUp();
            mPresenter.updateRecyclerView();
        });
        edit_collection.setOnClickListener(view -> {
            mPresenter.editCollection(collectionName, get_collection_name_edit.getText().toString());
            hideEditCollectionPopUp();
            get_collection_name_edit.getText().clear();
            ((MainActivity) Objects.requireNonNull(getActivity())).hideKeyboard();
            mPresenter.updateRecyclerView();
        });
    }

    @Override
    public void hideEditCollectionPopUp() {
        mDialogEdit.cancel();
    }

    @Override
    public void enableDoneButton() {
        add_collection.setBackground(getResources().getDrawable(R.drawable.filter_on));
        add_collection.setEnabled(true);
    }

    @Override
    public void disableDoneButton() {
        add_collection.setBackground(getResources().getDrawable(R.drawable.disabled_button));
        add_collection.setEnabled(false);
    }

    public class MyItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                                   @NonNull RecyclerView.State state) {
            // only for the last one
            outRect.bottom = 48;
            outRect.right = 32;
            outRect.left = 32;
        }
    }
}
