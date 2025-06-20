package com.example.assignment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class UserDetailsActivity extends AppCompatActivity {
    TextInputEditText etName, etBio, etReferral;
    AutoCompleteTextView dropdownInterest;
    CheckBox cbYes, cbNo;
    Button btnCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_details);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        Window window = getWindow();
        // Make sure icons are white
        window.getDecorView().setSystemUiVisibility(0);


        // Initialize fields
                etName = findViewById(R.id.et_name);
                etBio = findViewById(R.id.et_bio);
                etReferral = findViewById(R.id.et_referral);
                dropdownInterest = findViewById(R.id.dropdown_interest);
                cbYes = findViewById(R.id.cb_yes);
                cbNo = findViewById(R.id.cb_no);
                btnCreate = findViewById(R.id.btn_create_account);

                // Setup dropdown values
                String[] interests = {"Technology", "Sports", "Music", "Travel", "Art", "Fitness", "Finance", "Gaming", "Books", "Photography","Music","Coding","Dancing"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, interests);
                dropdownInterest.setAdapter(adapter);

                // checkbox behavior
                cbYes.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) cbNo.setChecked(false);
                });

                cbNo.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) cbYes.setChecked(false);
                });

                // Create account logic
                btnCreate.setOnClickListener(v -> {
                    String name = Objects.requireNonNull(etName.getText()).toString().trim();
                    String bio = Objects.requireNonNull(etBio.getText()).toString().trim();
                    String referral = Objects.requireNonNull(etReferral.getText()).toString().trim();
                    String interest = dropdownInterest.getText().toString().trim();
                    boolean knowsAward = cbYes.isChecked();
                    boolean wantsToKnow = cbNo.isChecked();

                    // Validation
                    if (name.isEmpty()) {
                        showToast("Please enter your name");
                        return;
                    }

                    if (interest.isEmpty()) {
                        showToast("Please select your interest");
                        return;
                    }

                    if (!knowsAward && !wantsToKnow) {
                        showToast("Please answer if you know about the award");
                        return;
                    }

                    showToast("Account created successfully!");
                    startActivity(new Intent(UserDetailsActivity.this, ProfileActivity.class));

                    System.out.println("Name: " + name);
                    System.out.println("Bio: " + bio);
                    System.out.println("Interest: " + interest);
                    System.out.println("Referral: " + referral);
                    System.out.println("Knows Award: " + knowsAward);
                });
            }

            private void showToast(String msg) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        }
