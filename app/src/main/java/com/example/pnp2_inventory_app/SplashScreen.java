package com.example.pnp2_inventory_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;
import android.widget.VideoView;

public class SplashScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Runnable r = new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, Login.class));
                finish();
            }
        };

        Handler h = new Handler();
        // The Runnable will be executed after the given delay time
        h.postDelayed(r, 4750); // will be delayed for 4.7 seconds
    }
}