package com.example.assignment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout loadingOverlay;
    private TextInputLayout inputLayoutContact, inputLayoutPassword;
    private TextInputEditText editTextContact, editTextPassword;
    private EditText editTextPhoneNumber;
    private LinearLayout phoneLayout;
    private CountryCodePicker countryCodePicker;
    private MaterialButton btnLogin;
    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    private LottieAnimationView lottieLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_login_screen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        loadingOverlay = findViewById(R.id.loadingOverlay);
        lottieLoadingView = findViewById(R.id.lottieLoadingView);


        inputLayoutContact = findViewById(R.id.inputLayoutContact);
        editTextContact = findViewById(R.id.editTextContact);
        inputLayoutPassword = findViewById(R.id.inputLayoutPassword);
        editTextPassword = findViewById(R.id.editTextPassword);
        phoneLayout = findViewById(R.id.phoneLayout);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        btnLogin = findViewById(R.id.btnLogin);
        ShimmerFrameLayout shimmerFrameLayout = findViewById(R.id.shimmerLayout);


        // Detect input type (number or email)
        editTextContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(LoginActivity.this, R.color.black));

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String input = s.toString().trim();
                if (input.isEmpty()) {
                    showEmailField();
                    inputLayoutPassword.setVisibility(View.GONE);
                    return;
                }

                char firstChar = input.charAt(0);
                if (Character.isDigit(firstChar)) {
                    showPhoneField(input);
                } else if (Character.isLetter(firstChar)) {
                    showEmailField();
                }
            }
        });

        // Allow switching back from phone to email
        editTextPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                if (input.isEmpty()) {
                    showEmailField();
                    editTextContact.setText("");
                } else if (!Character.isDigit(input.charAt(0))) {
                    showEmailField();
                    editTextContact.setText(input);
                    editTextContact.setSelection(input.length());
                    editTextContact.requestFocus();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean a = validateInputs();
                if (a == true) {
                    hideKeyboard();
                    loadingOverlay.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() -> {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        finish();
                    }, 2000);
                    //  startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                }

            }
        });
    }

    private void showPhoneField(String number) {
        inputLayoutContact.setVisibility(View.GONE);
        phoneLayout.setVisibility(View.VISIBLE);
        inputLayoutPassword.setVisibility(View.GONE);

        // ✅ Clear password when switching to phone
        editTextPassword.setText("");

        editTextPhoneNumber.setText(number);
        editTextPhoneNumber.setSelection(number.length());
        editTextPhoneNumber.requestFocus();
    }

    private void showEmailField() {
        inputLayoutContact.setVisibility(View.VISIBLE);
        phoneLayout.setVisibility(View.GONE);
        inputLayoutPassword.setVisibility(View.VISIBLE);
    }

    private boolean validateInputs() {
        String emailOrPhone = Objects.requireNonNull(editTextContact.getText()).toString().trim();
        String phone = editTextPhoneNumber.getText().toString().trim();
        String password = Objects.requireNonNull(editTextPassword.getText()).toString().trim();

        if (phoneLayout.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(phone)) {
                editTextPhoneNumber.setError("Phone number required");
                return false;
            }

            String fullPhoneNumber = countryCodePicker.getSelectedCountryCodeWithPlus() + phone;

            if (phone.length() < 8) {
                editTextPhoneNumber.setError("Phone number too short");
                return false;
            }

            if (!phone.matches("\\d+")) {
                editTextPhoneNumber.setError("Enter valid numeric phone number");
                return false;
            }

            try {
                String fullNumber = countryCodePicker.getSelectedCountryCodeWithPlus() + phone;
                Phonenumber.PhoneNumber parsedNumber = phoneUtil.parse(fullNumber, countryCodePicker.getSelectedCountryNameCode());

                if (!phoneUtil.isValidNumber(parsedNumber)) {
                    editTextPhoneNumber.setError("Invalid phone number for selected country");
                    return false;
                } else {
                    //Toast.makeText(this, "✅ Valid phone login: " + fullNumber, Toast.LENGTH_SHORT).show();

                    return true;
                }

            } catch (Exception e) {
                editTextPhoneNumber.setError("Invalid number");
            }
        } else {
            if (TextUtils.isEmpty(emailOrPhone)) {
                inputLayoutContact.setError("Email is required");
                return false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailOrPhone).matches()) {
                inputLayoutContact.setError("Enter a valid email");
                return false;
            } else {
                inputLayoutContact.setError(null);
            }

            if (TextUtils.isEmpty(password)) {
                inputLayoutPassword.setError("Password is required");
                return false;
            } else if (!isStrongPassword(password)) {
                inputLayoutPassword.setError("Min 6 chars with upper, lower, number & symbol");
                return false;
            } else {
                inputLayoutPassword.setError(null);
            }

            //  Toast.makeText(this, "✅ Valid email login", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private boolean isStrongPassword(String password) {
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$";
        return password.matches(pattern);
    }
}
