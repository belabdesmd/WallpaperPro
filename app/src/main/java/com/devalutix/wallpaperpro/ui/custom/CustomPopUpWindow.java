package com.devalutix.wallpaperpro.ui.custom;

import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.constraintlayout.widget.ConstraintLayout;

public class CustomPopUpWindow extends PopupWindow {

    private FrameLayout activity_container;

    public CustomPopUpWindow(View contentView, int width, int height, boolean focusable, FrameLayout activity_container) {
        super(contentView, width, height, focusable);
        this.activity_container = activity_container;
    }

    @Override
    public void dismiss() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //activity_container.getForeground().setAlpha(0);
        }
        super.dismiss();
    }
}
