package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.devalutix.wallpaperpro.pojo.Image;
import java.util.ArrayList;

public interface ImagesContract {

    interface Presenter extends BasePresenter<ImagesContract.View> {

        boolean checkNetwork();

        void initRecyclerView(String mode,String name);

        void updateRecyclerView(String name);

        ArrayList<Image> getImagesFromCollection(String name);

        ArrayList<Image> getImageFromCategory(String name);

        String listToString(ArrayList<Image> images);

        String getThumbnail(String mode, String name);

        void removeCollection(String text);

        void editCollection(String toString, String toString1);
    }

    interface View extends BaseView {

        void setToolbar();

        void setPageName(String name);

        void setPageThumbnail(String thumbnail);

        void initRecyclerView(ArrayList<Image> images);

        void updateRecyclerView(ArrayList<Image> images);

        void goToWallpaperActivity(int position,ArrayList<Image> images);

        void showNoNetwork();

        void showPicturesList();

        void initEditCollectionPopUp();

        void showCollectionActions();

        void hideCollectionActions();

        void showAddCollectionPopUp();

        void hideAddCollectionPopUp();

        void enableDoneButton();

        void disableDoneButton();
    }
}
