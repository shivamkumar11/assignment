package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.activity.OnBackPressedCallback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

public class ProfileActivity extends AppCompatActivity {
    private ShimmerFrameLayout shimmerFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_status_bar_color));


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