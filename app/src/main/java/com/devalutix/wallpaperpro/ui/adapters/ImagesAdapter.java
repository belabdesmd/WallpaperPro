package com.devalutix.wallpaperpro.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.pojo.Wallpaper;
import com.devalutix.wallpaperpro.ui.activities.ImagesActivity;
import com.devalutix.wallpaperpro.ui.fragments.ExploreFragment;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
    private static final String TAG = "ImagesAdapter";

    /*************************************** Declarations *****************************************/
    private ArrayList<Wallpaper> mImages;
    private ExploreFragment mView = null;
    private ImagesActivity mView1 = null;

    /*************************************** Constructor ******************************************/
    public ImagesAdapter(ArrayList<Wallpaper> mImages, ExploreFragment mView) {
        this.mImages = mImages;
        this.mView = mView;
    }

    public ImagesAdapter(ArrayList<Wallpaper> mImages, ImagesActivity mView1) {
        this.mImages = mImages;
        this.mView1 = mView1;
    }

    /*************************************** Methods **********************************************/
    @NonNull
    @Override
    public ImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: CreatingViews.");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.images_recyclerview_item, parent, false);

        int width = parent.getMeasuredWidth() / 3;
        int height = (int) Math.round(width * 1.5);

        itemView.setLayoutParams(new RecyclerView.LayoutParams(width, height));

        return new ImagesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Setting Views.");

        //Loading the Image
        if (mView == null)
            Glide.with(mView1)
                    .load(mImages.get(position).getImage())
                    .fitCenter()
                    //.placeholder(R.drawable.loading_spinner)
                    .into(holder.image);
        else if (mView1 == null)
            Glide.with(mView)
                    .load(mImages.get(position).getImage())
                    .fitCenter()
                    //.placeholder(R.drawable.loading_spinner)
                    .into(holder.image);

        //Click Listener
        final int finalPosition = position;
        holder.container.setOnClickListener(v -> {

            if (mView == null)
                mView1.goToWallpaperActivity(finalPosition, mImages);
            else if (mView1 == null)
                mView.goToWallpaperActivity(finalPosition, mImages);
        });
    }

    @Override
    public int getItemCount() {
        if (mImages == null) return 0;
        else return mImages.size();
    }

    public void clearAll() {
        if (mImages != null) mImages.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Wallpaper> categories) {
        mImages = categories;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView container;
        ImageView image;

        ViewHolder(View v) {
            super(v);

            container = v.findViewById(R.id.image_item_container);
            image = v.findViewById(R.id.image_item);
        }
    }
}
