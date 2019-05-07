package com.devalutix.wallpaperpro.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.devalutix.wallpaperpro.contracts.ExploreContract;
import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.ui.fragments.ExploreFragment;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class ExplorePresenter implements ExploreContract.Presenter {
    private static String TAG = "ExplorePresenter";

    //Declarations
    private ExploreFragment mView;
    private Gson gson;
    private Context mContext;

    //Constructor
    public ExplorePresenter(Context mContext, Gson gson) {
        this.mContext = mContext;
        this.gson = gson;
    }

    //Essential Methods
    @Override
    public void attach(ExploreContract.View view) {
        mView = (ExploreFragment) view;
    }

    @Override
    public void dettach() {
        mView = null;
    }

    @Override
    public boolean isAttached() {
        return !(mView == null);
    }

    //Methods
    @Override
    public boolean checkNetwork() {
        ConnectivityManager conMgr =  (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    }

    @Override
    public void initRecyclerView() {
        ArrayList<Image> images = getRecent();

        if (!checkNetwork() || images == null) mView.showNoNetwork();
        else{
            mView.initRecyclerView(images);
            mView.showPicturesList();
        }
    }

    @Override
    public void updateRecyclerView(String mode) {
        ArrayList<Image> images = null;
        switch (mode){
            case "popular":
                images = getPopular();
                break;
            case "recent":
                images = getRecent();
                break;
            case "featured":
                images = getFeatured();
                break;
        }

        mView.updateRecyclerView(images);

        if (!checkNetwork() || images == null) mView.showNoNetwork();
        else mView.showPicturesList();
    }

    @Override
    public ArrayList<Image> getRecent() {
        ArrayList<Image> recentImages = new ArrayList<>();

        ArrayList<Category> imageCategories = new ArrayList<>();
        imageCategories.add(new Category("Nature","https://images.pexels.com/photos/1054289/pexels-photo-1054289.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));
        imageCategories.add(new Category("Sea","https://images.pexels.com/photos/1054289/pexels-photo-1054289.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));
        imageCategories.add(new Category("Sky","https://images.pexels.com/photos/1054289/pexels-photo-1054289.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));
        imageCategories.add(new Category("Mountains","https://images.pexels.com/photos/1054289/pexels-photo-1054289.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));

        ArrayList<String> imageTags = new ArrayList<>();
        imageTags.add("test1");
        imageTags.add("test2");
        imageTags.add("test3");
        imageTags.add("test4");
        imageTags.add("test5");

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/1054289/pexels-photo-1054289.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/1037992/pexels-photo-1037992.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/1308624/pexels-photo-1308624.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/1261728/pexels-photo-1261728.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/990826/pexels-photo-990826.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/853199/pexels-photo-853199.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/1054218/pexels-photo-1054218.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/1418116/pexels-photo-1418116.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/1307986/pexels-photo-1307986.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/983200/pexels-photo-983200.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/354939/pexels-photo-354939.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/988873/pexels-photo-988873.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/1429567/pexels-photo-1429567.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/924824/pexels-photo-924824.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/990824/pexels-photo-990824.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/990824/pexels-photo-990824.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/776656/pexels-photo-776656.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            recentImages.add(new Image("0",
                    "Image 1",
                    "https://images.pexels.com/photos/35857/amazing-beautiful-breathtaking-clouds.jpg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    326,
                    15,
                    imageCategories,
                    imageTags,
                    new SimpleDateFormat("yyyy-MM-dd").parse( "2009-12-31" ),
                    15,
                    12,
                    "Belfodil Abdessamed"
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return recentImages;
    }

    @Override
    public ArrayList<Image> getPopular() {
        return getRecent();
    }

    @Override
    public ArrayList<Image> getFeatured() {
        return getRecent();
    }

    @Override
    public String listToString(ArrayList<Image> images) {
        return gson.toJson(images);
    }
}
