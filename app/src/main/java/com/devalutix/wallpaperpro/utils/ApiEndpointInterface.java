package com.devalutix.wallpaperpro.utils;

import com.devalutix.wallpaperpro.pojo.CategoryS;
import com.devalutix.wallpaperpro.pojo.ImageS;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiEndpointInterface {

    @GET("wallpapers/type=recent")
    Call<ArrayList<ImageS>> getRecentImages();

    @GET("wallpapers/type=popular")
    Call<ArrayList<ImageS>> getPopularImages();

    @GET("wallpapers/type=featured")
    Call<ArrayList<ImageS>> getFeaturedImages();

    @GET("categories")
    Call<ArrayList<CategoryS>> getCategories();

    @GET("wallpapers/category={category}")
    Call<ArrayList<ImageS>> getCategoryWallpapers(@Path("category") String category);

    @GET("search={query}")
    Call<ArrayList<ImageS>> searchWallpapers(@Path("query") String query);

    //TODO: Use Headers
    @PUT("wallpapers/update-downloads/{pk}")
    Call<ResponseBody> updateDownloads(@Path("query") int pk, @Body ImageS image);

}
