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

import com.bumptech.glide.Glide;
import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.presenters.CategoriesPresenter;
import com.devalutix.wallpaperpro.ui.fragments.CategoriesFragment;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private static final String TAG = "CategoriesAdapter";

    /*************************************** Declarations *****************************************/
    private CategoriesPresenter mPresenter;
    private ArrayList<Category> mCategories;
    private CategoriesFragment mView;

    /*************************************** Constructor ******************************************/
    public CategoriesAdapter(CategoriesPresenter mPresenter, ArrayList<Category> mCategories, CategoriesFragment mView) {
        this.mPresenter = mPresenter;
        this.mCategories = mCategories;
        this.mView = mView;
    }

    /*************************************** Methods **********************************************/
    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: CreatingViews.");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_recyclerview_item, parent, false);

        return new CategoriesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Setting Views.");

        //Loading the Image
        Glide.with(mView)
                .load(mCategories.get(position).getImage())
                .fitCenter()
                //.placeholder(R.drawable.loading_spinner)
                .into(holder.image);

        //Set Category Name
        holder.name.setText(mCategories.get(position).getName());

        //Click Listener
        final int finalPosition = position;
        holder.container.setOnClickListener(v -> mView.goToImages(mCategories.get(finalPosition).getName()));
    }

    @Override
    public int getItemCount() {
        if (mCategories == null) return 0;
        else return mCategories.size();
    }

    public void clearAll() {
        if (mCategories != null) mCategories.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Category> categories) {
        mCategories = categories;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView container;
        ImageView image;
        TextView name;

        ViewHolder(View v) {
            super(v);

            container = v.findViewById(R.id.category_item_container);
            image = v.findViewById(R.id.category_image);
            name = v.findViewById(R.id.category_name);
        }
    }
}
