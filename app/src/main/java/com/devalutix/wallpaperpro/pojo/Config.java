package com.devalutix.wallpaperpro.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Config {

    @SerializedName("banner_id")
    @Expose
    private String banner_id;
    @SerializedName("interstitial_id")
    @Expose
    private String interstitial_id;

    @SerializedName("banner")
    @Expose
    private boolean banner;
    @SerializedName("interstitial")
    @Expose
    private boolean interstitial;
    @SerializedName("gdpr")
    @Expose
    private boolean gdpr;

    @SerializedName("publisher_id")
    @Expose
    private String publisher_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;

    public String getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public String getInterstitial_id() {
        return interstitial_id;
    }

    public void setInterstitial_id(String interstitial_id) {
        this.interstitial_id = interstitial_id;
    }

    public boolean isBanner() {
        return banner;
    }

    public void setBanner(boolean banner) {
        this.banner = banner;
    }

    public boolean isInterstitial() {
        return interstitial;
    }

    public void setInterstitial(boolean interstitial) {
        this.interstitial = interstitial;
    }

    public boolean isGdpr() {
        return gdpr;
    }

    public void setGdpr(boolean gdpr) {
        this.gdpr = gdpr;
    }

    public String getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(String publisher_id) {
        this.publisher_id = publisher_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
