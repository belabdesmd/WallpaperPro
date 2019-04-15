package com.devalutix.wallpaperpro.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import android.os.Bundle;
import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.di.components.ApplicationComponent;

public class MainActivity extends AppCompatActivity {

    protected ApplicationComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set ButterKnife
        ButterKnife.bind(this);

        //Inject Component (Dependency Injection)
        mComponent.inject(this);
    }
}
