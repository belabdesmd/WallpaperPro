package com.devalutix.wallpaperpro.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.pojo.Wallpaper;
import com.devalutix.wallpaperpro.presenters.FavoritesPresenter;
import com.devalutix.wallpaperpro.ui.activities.WallpaperActivity;
import com.devalutix.wallpaperpro.ui.fragments.FavoritesFragment;
import com.devalutix.wallpaperpro.utils.Config;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

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

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collection_recyclerview_item, parent, false);

        return new FavoritesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.ViewHolder holder, int position) {

        Context context;

        if (mView != null) context = mView.getContext();
        else context = mView1;

        ArrayList<Wallpaper> wallpapers = mCollections.get(position).getCollectionWallpapers();

        assert context != null;
        if (!wallpapers.isEmpty()) {
            if (wallpapers.size() < 4) {
                Glide.with(context)
                        .load(mCollections.get(position).getCollectionWallpapers().get(0).getWallpapers())
                        .fitCenter()
                        .into(holder.preview);
                holder.previews.setVisibility(View.GONE);
                holder.preview.setVisibility(View.VISIBLE);
            }else{
                Glide.with(context)
                        .load(mCollections.get(position).getCollectionWallpapers().get(0).getWallpapers())
                        .fitCenter()
                        .into(holder.preview_1);
                Glide.with(context)
                        .load(mCollections.get(position).getCollectionWallpapers().get(1).getWallpapers())
                        .fitCenter()
                        .into(holder.preview_2);
                Glide.with(context)
                        .load(mCollections.get(position).getCollectionWallpapers().get(2).getWallpapers())
                        .fitCenter()
                        .into(holder.preview_3);
                Glide.with(context)
                        .load(mCollections.get(position).getCollectionWallpapers().get(3).getWallpapers())
                        .fitCenter()
                        .into(holder.preview_4);
                holder.previews.setVisibility(View.VISIBLE);
                holder.preview.setVisibility(View.GONE);
            }
        } else holder.preview.setBackgroundResource(R.drawable.gradient_color);


        //Set Category Name
        holder.name.setText(mCollections.get(position).getCollectionName());


        //Click Listener
        final int finalPosition = position;
        holder.container.setOnClickListener(v -> {
            if (mView1 != null) {
                mPresenter.add_removeWallpaper(mView1, mCollections.get(finalPosition).getCollectionName(), mView1.getImage());
                mView1.hideFavorite();
            } else if (mView != null)
                mView.goToWallpapers(mCollections.get(finalPosition).getCollectionName());
        });

        holder.container.setOnLongClickListener(view -> {
            if (mView != null) {
                if (!mCollections.get(position).getCollectionName().equals(mView.getResources().getString(R.string.MyFavorites)))
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
        ImageView preview;
        LinearLayout previews;
        ImageView preview_1;
        ImageView preview_2;
        ImageView preview_3;
        ImageView preview_4;
        TextView name;

        ViewHolder(View v) {
            super(v);

            container = v.findViewById(R.id.collection_item_container);
            preview = v.findViewById(R.id.collection_preview);
            previews = v.findViewById(R.id.previews);
            preview_1 = v.findViewById(R.id.preview_1);
            preview_2 = v.findViewById(R.id.preview_2);
            preview_3 = v.findViewById(R.id.preview_3);
            preview_4 = v.findViewById(R.id.preview_4);
            name = v.findViewById(R.id.collection_name);
        }
    }
}
