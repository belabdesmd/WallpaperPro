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
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.presenters.ExplorePresenter;
import com.devalutix.wallpaperpro.ui.fragments.ExploreFragment;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder>{
    private static final String TAG = "ImagesAdapter";

    //Declarations
    private ExplorePresenter mPresenter;
    private ArrayList<Image> mImages;
    private ExploreFragment mView;

    public ImagesAdapter(ExplorePresenter mPresenter, ArrayList<Image> mImages, ExploreFragment mView) {
        this.mPresenter = mPresenter;
        this.mImages = mImages;
        this.mView = mView;
    }

    @NonNull
    @Override
    public ImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: CreatingViews.");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.images_recyclerview_item, parent, false);

        return new ImagesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Setting Views.");

        //Loading the Image
        Glide.with(mView)
                .load(mImages.get(position).getImageUrl())
                .fitCenter()
                //.placeholder(R.drawable.loading_spinner)
                .into(holder.image);

        //Click Listener
        final int finalPosition = position;
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.goToWallpaperActivity(finalPosition,mImages);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mImages == null) return 0;
        else return mImages.size();
    }

    /**
     * Clear All the ArrayList
     */
    public void clearAll() {
        if (mImages != null) mImages.clear();
        notifyDataSetChanged();
    }

    /**
     * Add the New ArrayList
     *
     * @param categories : the ArrayList to Add
     */
    public void addAll(ArrayList<Image> categories) {
        mImages = categories;
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
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
