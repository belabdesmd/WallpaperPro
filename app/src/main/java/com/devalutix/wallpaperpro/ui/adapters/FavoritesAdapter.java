package com.devalutix.wallpaperpro.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.presenters.FavoritesPresenter;
import com.devalutix.wallpaperpro.ui.activities.WallpaperActivity;
import com.devalutix.wallpaperpro.ui.fragments.FavoritesFragment;
import com.devalutix.wallpaperpro.utils.Config;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private static final String TAG = "FavoritesAdapter";

    /*************************************** Declarations *****************************************/
    private FavoritesPresenter mPresenter;
    private ArrayList<Collection> mCollections;
    private FavoritesFragment mView = null;
    private WallpaperActivity mView1 = null;

    /*************************************** Constructor ******************************************/
    public FavoritesAdapter(FavoritesPresenter mPresenter, ArrayList<Collection> mCollections, FavoritesFragment mView) {
        this.mPresenter = mPresenter;
        this.mCollections = mCollections;
        this.mView = mView;
    }

    public FavoritesAdapter(FavoritesPresenter mPresenter, ArrayList<Collection> mCollections, WallpaperActivity mView) {
        this.mPresenter = mPresenter;
        this.mCollections = mCollections;
        this.mView1 = mView;
    }

    /*************************************** Methods **********************************************/
    @NonNull
    @Override
    public FavoritesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: CreatingViews.");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collection_recyclerview_item, parent, false);

        return new FavoritesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Setting Views.");

        if (mView != null) {
            if (!mCollections.get(position).getCollectionPictures().isEmpty())
                Glide.with(mView)
                        .load(mCollections.get(position).getCollectionPictures().get(0).getImage())
                        .fitCenter()
                        //.placeholder(R.drawable.loading_spinner)
                        .into(holder.thumbnail);
        } else if (mView1 != null) {
            if (!mCollections.get(position).getCollectionPictures().isEmpty())
                Glide.with(mView1)
                        .load(mCollections.get(position).getCollectionPictures().get(0).getImage())
                        .fitCenter()
                        //.placeholder(R.drawable.loading_spinner)
                        .into(holder.thumbnail);
        }


        //Set Category Name
        holder.name.setText(mCollections.get(position).getCollectionName());


        //Click Listener
        final int finalPosition = position;
        holder.container.setOnClickListener(v -> {
            if (mView1 != null) {
                mPresenter.addImageToCollection(mCollections.get(finalPosition).getCollectionName(), mView1.getImage());
                Toast.makeText(mView1, mView1.getResources().getString(R.string.wallpaper_added_msg) + mCollections.get(finalPosition).getCollectionName(), Toast.LENGTH_SHORT).show();
                mView1.hideFavorite();
            } else if (mView != null)
                mView.goToImages(mCollections.get(finalPosition).getCollectionName());
        });

        holder.container.setOnLongClickListener(view -> {
            if (mView != null) {
                if (!mCollections.get(position).getCollectionName().equals(Config.MY_FAVORITES_COLLECTION_NAME))
                    mView.showEditCollectionPopUp(mCollections.get(position).getCollectionName());
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        if (mCollections == null) return 0;
        else return mCollections.size();
    }

    public void clearAll() {
        if (mCollections != null) mCollections.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Collection> collections) {
        mCollections = collections;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView container;
        ImageView thumbnail;
        TextView name;

        ViewHolder(View v) {
            super(v);

            container = v.findViewById(R.id.collection_item_container);
            thumbnail = v.findViewById(R.id.collection_thumbnail);
            name = v.findViewById(R.id.collection_name);
        }
    }
}
