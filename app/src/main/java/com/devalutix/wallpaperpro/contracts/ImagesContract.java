package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.devalutix.wallpaperpro.pojo.Image;
import java.util.ArrayList;

public interface ImagesContract {

    interface Presenter extends BasePresenter<ImagesContract.View> {

        void initRecyclerView(String mode);

        ArrayList<Image> getImagesFromCollection();

        ArrayList<Image> getImageFromCategory();
    }

    interface View extends BaseView {

        void setPageName(String name);

        void initRecyclerView(ArrayList<Image> images);

        void updateRecyclerView(ArrayList<Image> images);

        void goToWallpaperActivity(int position,ArrayList<Image> images);

    }
}
