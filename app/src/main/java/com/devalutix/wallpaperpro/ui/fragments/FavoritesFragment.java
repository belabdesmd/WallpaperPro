package com.devalutix.wallpaperpro.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.FavoritesContract;
import com.devalutix.wallpaperpro.di.components.DaggerMVPComponent;
import com.devalutix.wallpaperpro.di.components.MVPComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.presenters.FavoritesPresenter;
import com.devalutix.wallpaperpro.ui.activities.ImagesActivity;
import com.devalutix.wallpaperpro.ui.activities.MainActivity;
import com.devalutix.wallpaperpro.ui.adapters.FavoritesAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class FavoritesFragment extends Fragment implements FavoritesContract.View {
    private static String TAG = "FavoritesFragment";
    private static final int COL_NUM = 2;

    //Declarations
    private MVPComponent mvpComponent;
    private FavoritesAdapter mAdapter;
    private ArrayList<Collection> collections;
    private FrameLayout activity_container;

    private AlertDialog mDialogAdd;
    private AlertDialog mDialogEdit;

    @Inject
    FavoritesPresenter mPresenter;

    //View Declarations
    @BindView(R.id.favorites_recyclerview)
    RecyclerView mRecyclerView;

    private CardView add_collection_popup;
    private EditText get_collection_name;
    private Button add_collection;

    private CardView edit_collection_popup;
    private EditText get_collection_name_edit;
    private Button edit_collection;

    //Constructor
    public FavoritesFragment() {
        // Required empty public constructor
    }

    //Essentials Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        activity_container = (FrameLayout) view.findViewById(R.id.favorites_container);

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
                    .applicationModule(new ApplicationModule(getActivity().getApplication()))
                    .mVPModule(new MVPModule(getActivity()))
                    .build();
        }
        return mvpComponent;
    }

    //Methods
    @Override
    public void initRecyclerView(ArrayList<Collection> collections) {

        //Set the List
        this.collections = collections;

        //Declarations
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(COL_NUM, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new FavoritesAdapter(mPresenter, collections, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new FavoritesFragment.MyItemDecoration());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initAddCollectionPopUp() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.add_collection_popup, null);

        //Create the Alert Dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setView(customView);
        mDialogAdd = alertDialogBuilder.create();

        //Init Views
        add_collection_popup = customView.findViewById(R.id.add_collection_popup);
        get_collection_name = customView.findViewById(R.id.add_collection_name);
        add_collection = customView.findViewById(R.id.done_adding);

        //Listeners
        add_collection.setOnClickListener(view -> {
            mPresenter.addCollection(new Collection(get_collection_name.getText().toString(), new ArrayList<Image>()));
            hideAddCollectionPopUp();
            get_collection_name.getText().clear();
            ((MainActivity) getActivity()).hideKeyboard();
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
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.edit_collection_popup, null);

        //Create the Alert Dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setView(customView);
        mDialogEdit = alertDialogBuilder.create();

        //Init Views
        edit_collection_popup = customView.findViewById(R.id.edit_collection_popup);
        get_collection_name_edit = customView.findViewById(R.id.edit_collection_name);
        edit_collection = customView.findViewById(R.id.done_editing);

        //Listeners
        edit_collection.setOnClickListener(view -> {
            mPresenter.editCollection(get_collection_name_edit.getText().toString());
            hideEditCollectionPopUp();
            get_collection_name_edit.getText().clear();
            ((MainActivity) getActivity()).hideKeyboard();
        });
        customView.findViewById(R.id.remove_collection).setOnClickListener(view -> {
            mPresenter.removeCollection();
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
    public void updateRecyclerView(ArrayList<Collection> collections) {
        this.collections = collections;

        //Deleting the List of the Categories
        mAdapter.clearAll();

        // Adding The New List of Categories
        mAdapter.addAll(collections);
    }

    @Override
    public void goToImages(String collectionName) {
        Intent goToImages = new Intent(getActivity(), ImagesActivity.class);

        goToImages.putExtra("name", collectionName);
        goToImages.putExtra("mode", "collection");
        goToImages.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goToImages);
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
    public void showEditCollectionPopUp() {
        mDialogEdit.show();
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
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            // only for the last one
            outRect.bottom = 48;
            outRect.right = 32;
            outRect.left = 32;
        }
    }
}
