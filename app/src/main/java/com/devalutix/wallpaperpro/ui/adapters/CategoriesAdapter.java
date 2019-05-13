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

    //Declarations
    private CategoriesPresenter mPresenter;
    private ArrayList<Category> mCategories;
    private CategoriesFragment mView;

    public CategoriesAdapter(CategoriesPresenter mPresenter, ArrayList<Category> mCategories, CategoriesFragment mView) {
        this.mPresenter = mPresenter;
        this.mCategories = mCategories;
        this.mView = mView;
    }

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
                .load(mCategories.get(position).getCategoryIconUrl())
                .fitCenter()
                //.placeholder(R.drawable.loading_spinner)
                .into(holder.image);

        //Set Category Name
        holder.name.setText(mCategories.get(position).getCategoryName());

        //Click Listener
        final int finalPosition = position;
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.goToImages(mCategories.get(finalPosition).getCategoryName());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCategories == null) return 0;
        else return mCategories.size();
    }

    /**
     * Clear All the ArrayList
     */
    public void clearAll() {
        if (mCategories != null) mCategories.clear();
        notifyDataSetChanged();
    }

    /**
     * Add the New ArrayList
     *
     * @param categories : the ArrayList to Add
     */
    public void addAll(ArrayList<Category> categories) {
        mCategories = categories;
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
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
