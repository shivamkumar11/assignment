package com.example.assignment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.activity.OnBackPressedCallback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

public class ProfileActivity extends AppCompatActivity {
    private ShimmerFrameLayout shimmerFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        Window window = getWindow();

        // Make sure icons are white
        window.getDecorView().setSystemUiVisibility(0);


        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(0x000000)
                .setHighlightColor(0xCCFFFFFF)
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setTilt(-20f)
                .setDuration(3000)
                .setIntensity(0.05f)
                .setDropoff(0.05f)
                .setRepeatCount(0)
                .build();

        shimmerFrameLayout = findViewById(R.id.view7);
        shimmerFrameLayout.setShimmer(shimmer);
        shimmerFrameLayout.startShimmer();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        shimmerFrameLayout.stopShimmer(); // to prevent leaks
        super.onPause();
    }

    @Override
    protected void onResume() {
        shimmerFrameLayout.startShimmer();
        super.onResume();
    }



}