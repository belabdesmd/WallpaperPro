package com.devalutix.wallpaperpro.base;

public interface BasePresenter<V extends BaseView> {

    void attach(V view);

    void dettach();

    boolean isAttached();
}
