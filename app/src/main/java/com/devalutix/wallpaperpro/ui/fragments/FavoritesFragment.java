package com.devalutix.wallpaperpro.ui.fragments;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
import com.devalutix.wallpaperpro.ui.custom.CustomPopUpWindow;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

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
    private SlideUp slideUpAddCollection;
    private FrameLayout activity_container;

    @Inject
    FavoritesPresenter mPresenter;

    //View Declarations
    @BindView(R.id.add_collection_popup)
    CardView add_collection_popup;
    @BindView(R.id.favorites_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.add_collection_name)
    EditText get_collection_name;
    @BindView(R.id.done_adding)
    Button add_collection;

    //Click Listeners
    @OnClick(R.id.done_adding)
    public void add_collection() {
        mPresenter.addCollection(new Collection(get_collection_name.getText().toString(), new ArrayList<Image>()));
        cancel();
    }

    @OnClick(R.id.cancel_adding)
    public void cancel() {
        hideAddCollectionPopUp();
        get_collection_name.getText().clear();
        ((MainActivity) getActivity()).hideKeyboard();
    }

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
        slideUpAddCollection = new SlideUpBuilder(add_collection_popup)
                .withStartState(SlideUp.State.HIDDEN)
                .withStartGravity(Gravity.TOP)
                .withLoggingEnabled(true)
                .withSlideFromOtherView(activity_container)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {

                    }
                })
                .build();

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
        add_collection_popup.setVisibility(View.VISIBLE);
        add_collection_popup.bringToFront();
        slideUpAddCollection.show();
    }

    @Override
    public void hideAddCollectionPopUp() {
        slideUpAddCollection.hide();
        add_collection_popup.setVisibility(View.GONE);
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
