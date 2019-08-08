package com.devalutix.wallpaperpro.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.presenters.CategoriesPresenter;
import com.devalutix.wallpaperpro.ui.fragments.CategoriesFragment;

import java.util.ArrayList;

public class CategoriesInfoAdapter extends RecyclerView.Adapter<CategoriesInfoAdapter.ViewHolder> {
    private static final String TAG = "CategoriesAdapter";

    /*************************************** Declarations *****************************************/
    private CategoriesPresenter mPresenter;
    private ArrayList<String> categories;

    /*************************************** Constructor ******************************************/
    public CategoriesInfoAdapter(CategoriesPresenter mPresenter, ArrayList<String> categories) {
        this.mPresenter = mPresenter;
        this.categories = categories;
    }

    /*************************************** Methods **********************************************/
    @NonNull
    @Override
    public CategoriesInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: CreatingViews.");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_info_item, parent, false);

        return new CategoriesInfoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesInfoAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Setting Views.");

        holder.name.setText(categories.get(position));

        holder.container.setOnClickListener(v -> mPresenter.goToWallpapers(categories.get(position)));
    }

    @Override
    public int getItemCount() {
        if (categories == null) return 0;
        else return categories.size();
    }

    public void clearAll() {
        if (categories != null) categories.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<String> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView container;
        TextView name;

        ViewHolder(View v) {
            super(v);

            container = v.findViewById(R.id.category_container);
            name = v.findViewById(R.id.category);
        }
    }
}
