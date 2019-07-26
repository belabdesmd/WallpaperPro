package com.devalutix.wallpaperpro.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.devalutix.wallpaperpro.contracts.ExploreContract;
import com.devalutix.wallpaperpro.di.annotations.ApplicationContext;
import com.devalutix.wallpaperpro.pojo.CategoryS;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.pojo.ImageS;
import com.devalutix.wallpaperpro.ui.activities.MainActivity;
import com.devalutix.wallpaperpro.ui.fragments.ExploreFragment;
import com.devalutix.wallpaperpro.utils.ApiEndpointInterface;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExplorePresenter implements ExploreContract.Presenter {
    private static String TAG = "ExplorePresenter";

    /***************************************** Declarations ***************************************/
    private ExploreFragment mView;
    private Context mContext;
    private Gson gson;
    private ApiEndpointInterface apiService;
    private String mode = "recent";

    /***************************************** Constructor ****************************************/
    public ExplorePresenter(@ApplicationContext Context mContext, Gson gson, ApiEndpointInterface apiService) {
        this.mContext = mContext;
        this.gson = gson;
        this.apiService = apiService;
    }

    /***************************************** Essential Methods **********************************/
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

    /***************************************** Methods ********************************************/
    //TODO: Remove
    @Override
    public boolean hasInternetAccess() {
        ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    }

    @Override
    public void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: Initialising Recycler View");

        ArrayList<Image> images = getRecent();
        Call<ArrayList<ImageS>> call = apiService.getRecentImages();
        call.enqueue(new Callback<ArrayList<ImageS>>() {
            @Override
            public void onResponse(Call<ArrayList<ImageS>> call, Response<ArrayList<ImageS>> response) {
                Toast.makeText(mView.getActivity(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()){
                //mView.initRecyclerView(response.body());
                //mView.showPicturesList();
                }else{
                //mView.initRecyclerView(null);
                //mView.showNoNetwork();
                ((MainActivity) mView.getActivity()).showRetryCard("Server Problem");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ImageS>> call, Throwable t) {
                Toast.makeText(mView.getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                //mView.initRecyclerView(null);
                //mView.showNoNetwork();
                ((MainActivity) mView.getActivity()).showRetryCard("Internet Connection Problem");
            }
        });

        //Init Recycler View
        mView.initRecyclerView(images);

        //If There is no connection or there is a problem with the server: Show no Network
        if (!hasInternetAccess() || images == null) mView.showNoNetwork();
        else mView.showPicturesList();
    }

    @Override
    public void updateRecyclerView(String mode) {
        Log.d(TAG, "updateRecyclerView: Updating Recycler View");
        this.mode = mode;
        ArrayList<Image> images = null;
        Call<ArrayList<ImageS>> call = null;

        switch (mode) {
            case "popular": {
                images = getPopular();
                call = apiService.getPopularImages();
            }
            break;
            case "recent": {
                images = getRecent();
                call = apiService.getRecentImages();
            }
            break;
            case "featured": {
                images = getFeatured();
                call = apiService.getFeaturedImages();
            }
            break;
        }

        call.enqueue(new Callback<ArrayList<ImageS>>() {
            @Override
            public void onResponse(Call<ArrayList<ImageS>> call, Response<ArrayList<ImageS>> response) {
                Toast.makeText(mView.getActivity(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()){
                //mView.updateRecyclerView(response.body());
                //mView.showPicturesList();
                }else{
                //mView.updateRecyclerView(null);
                //mView.showNoNetwork();
                ((MainActivity) mView.getActivity()).showRetryCard("Server Problem");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ImageS>> call, Throwable t) {
                Toast.makeText(mView.getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                //mView.updateRecyclerView(null);
                //mView.showNoNetwork();
                ((MainActivity) mView.getActivity()).showRetryCard("Internet Connection Problem");
            }
        });

        //Update Recycler View
        mView.updateRecyclerView(images);

        //If There is no connection or there is a problem with the server: Show no Network
        if (!hasInternetAccess() || images == null)
            mView.showNoNetwork();
        else
            mView.showPicturesList();
    }

    @Override
    public ArrayList<Image> getRecent() {
        Image[] collectionItem = gson.fromJson(readJSONFromAsset(), Image[].class);
        return new ArrayList<>(Arrays.asList(collectionItem));
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

    @Override
    public String getMode() {
        return mode;
    }

    //TODO: Remove
    private String readJSONFromAsset() {
        String json;
        try {
            InputStream is = mContext.getAssets().open("images.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
