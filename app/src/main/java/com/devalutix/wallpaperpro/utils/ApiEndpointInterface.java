package com.devalutix.wallpaperpro.utils;

import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.pojo.Wallpaper;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiEndpointInterface {

    @GET("wallpapers/type=recent")
    Call<ArrayList<Wallpaper>> getRecentImages(@Header("Authorization") String auth);

    @GET("wallpapers/type=popular")
    Call<ArrayList<Wallpaper>> getPopularImages(@Header("Authorization") String auth);

    @GET("wallpapers/type=featured")
    Call<ArrayList<Wallpaper>> getFeaturedImages(@Header("Authorization") String auth);

    @GET("categories")
    Call<ArrayList<Category>> getCategories(@Header("Authorization") String auth);

    @GET("wallpapers/category={category}")
    Call<ArrayList<Wallpaper>> getCategoryWallpapers(@Header("Authorization") String auth, @Path("category") String category);

    @GET("search={query}")
    Call<ArrayList<Wallpaper>> searchWallpapers(@Header("Authorization") String auth, @Path("query") String query);

    //TODO: Use Headers
    @PUT("wallpapers/update-downloads/{pk}")
    Call<Wallpaper> updateDownloads(@Header("Authorization") String auth, @Path("pk") int pk);

}
