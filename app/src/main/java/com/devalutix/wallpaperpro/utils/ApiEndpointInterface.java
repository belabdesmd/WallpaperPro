package com.devalutix.wallpaperpro.utils;

import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.pojo.Wallpaper;
import com.devalutix.wallpaperpro.pojo.Config;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiEndpointInterface {

    @GET("config")
    Config getConfig(@Header("Authorization") String auth);

    @GET("wallpapers/type=recent")
    Call<ArrayList<Wallpaper>> getRecentWallpapers(@Header("Authorization") String auth);

    @GET("wallpapers/type=popular")
    Call<ArrayList<Wallpaper>> getPopularWallpapers(@Header("Authorization") String auth);

    @GET("wallpapers/type=featured")
    Call<ArrayList<Wallpaper>> getFeaturedWallpapers(@Header("Authorization") String auth);

    @GET("categories")
    Call<ArrayList<Category>> getCategories(@Header("Authorization") String auth);

    @GET("wallpapers/category={category}")
    Call<ArrayList<Wallpaper>> getCategoryWallpapers(@Header("Authorization") String auth, @Path("category") String category);

    @GET("search={query}")
    Call<ArrayList<Wallpaper>> searchWallpapers(@Header("Authorization") String auth, @Path("query") String query);

    @PUT("wallpapers/update-downloads/{pk}")
    Call<Wallpaper> updateDownloads(@Header("Authorization") String auth, @Path("pk") int pk);

}
