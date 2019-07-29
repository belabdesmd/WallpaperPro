package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.devalutix.wallpaperpro.pojo.Category;

import java.util.ArrayList;

public interface CategoriesContract {

    interface Presenter extends BasePresenter<View> {

        void initRecyclerView();

        void updateRecyclerView();
    }

    interface View extends BaseView {

        void initRecyclerView(ArrayList<Category> categories);

        void updateRecyclerView(ArrayList<Category> categories);

        void initRetrySheet();

        void showNoNetwork();

        void showCategoriesList();

        void showEmptyCollection(String message);

        void showRetryCard(String message);

        void hideRetryCard();

        void goToImages(String categoryName);

        void refresh();
    }

}
