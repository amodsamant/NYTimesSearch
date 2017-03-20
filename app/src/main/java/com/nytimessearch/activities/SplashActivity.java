package com.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * SplashActivity to display the splash screen with a delay. This is optimized
 * as there is no layout to inflate and it uses splash.xml
 */
public class SplashActivity extends AppCompatActivity {

    final static int DELAY = 750;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, SearchActivity.class);
            startActivity(intent);
            finish();
        }, DELAY);
    }
}
