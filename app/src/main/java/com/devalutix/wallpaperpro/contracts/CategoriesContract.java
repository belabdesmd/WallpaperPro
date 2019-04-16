package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.devalutix.wallpaperpro.pojo.Category;

import java.util.ArrayList;

public interface CategoriesContract {

    interface Presenter extends BasePresenter<View> {

        void checkNetwork();

        void initRecyclerView();

        void updateRecyclerView(String mode);

        ArrayList<Category> getCategories();
    }

    interface View extends BaseView {

        void initRecyclerView(ArrayList<Category> categories);

        void updateRecyclerView(ArrayList<Category> categories);

        void showNoNetwork();

        void showCategoriesList();

        void goToImages(String categoryName);
    }

}
