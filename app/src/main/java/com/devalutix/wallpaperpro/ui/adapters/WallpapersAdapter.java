package com.devalutix.wallpaperpro.ui.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.pojo.Wallpaper;
import com.devalutix.wallpaperpro.ui.activities.WallpapersActivity;
import com.devalutix.wallpaperpro.ui.fragments.ExploreFragment;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;

import java.util.ArrayList;

public class WallpapersAdapter extends RecyclerView.Adapter<WallpapersAdapter.ViewHolder> {

    /*************************************** Declarations *****************************************/
    private ArrayList<Wallpaper> mWallpapers;
    private ExploreFragment mView = null;
    private WallpapersActivity mView1 = null;

    /*************************************** Constructor ******************************************/
    public WallpapersAdapter(ArrayList<Wallpaper> mWallpapers, ExploreFragment mView) {
        this.mWallpapers = mWallpapers;
        this.mView = mView;
    }

    public WallpapersAdapter(ArrayList<Wallpaper> mWallpapers, WallpapersActivity mView1) {
        this.mWallpapers = mWallpapers;
        this.mView1 = mView1;
    }

    /*************************************** Methods **********************************************/
    @NonNull
    @Override
    public WallpapersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallpapers_recyclerview_item, parent, false);

        int width = parent.getMeasuredWidth() / 3;
        int height = (int) Math.round(width * 1.5);

        itemView.setLayoutParams(new RecyclerView.LayoutParams(width, height));

        return new WallpapersAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpapersAdapter.ViewHolder holder, int position) {
        Sprite chasingDots = new ChasingDots();
        chasingDots.setColor(R.color.colorAccent);
        holder.loading.setIndeterminateDrawable(chasingDots);

        //Loading the Wallpaper
        if (mView == null)
            Glide.with(mView1)
                    .load(mWallpapers.get(position).getWallpapers())
                    .fitCenter()
                    .error(R.drawable.error)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.loading.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.loading.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.wallpaper);
        else if (mView1 == null)
            Glide.with(mView)
                    .load(mWallpapers.get(position).getWallpapers())
                    .fitCenter()
                    .error(R.drawable.error)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.loading.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.loading.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.wallpaper);

        //Click Listener
        final int finalPosition = position;
        holder.container.setOnClickListener(v -> {

            if (mView == null)
                mView1.goToWallpaperActivity(finalPosition, mWallpapers);
            else if (mView1 == null)
                mView.getPresenter().goToWallpaperActivity(finalPosition, mWallpapers);
        });
    }

    @Override
    public int getItemCount() {
        if (mWallpapers == null) return 0;
        else return mWallpapers.size();
    }

    public void clearAll() {
        if (mWallpapers != null) mWallpapers.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Wallpaper> categories) {
        mWallpapers = categories;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView container;
        ImageView wallpaper;
        ProgressBar loading;

        ViewHolder(View v) {
            super(v);

            container = v.findViewById(R.id.wallpaper_item_container);
            wallpaper = v.findViewById(R.id.wallpaper_item);
            loading = v.findViewById(R.id.load);
        }
    }
}
