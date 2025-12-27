package com.bb.spamfolder.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.bb.spamfolder.R;

public class SuccessActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        Button btnClose = findViewById(R.id.btnClose);
        TextView tvBackToLogin = findViewById(R.id.tvBackToLogin);

        // Set click listeners
        btnClose.setOnClickListener(v -> handleCloseApp());
        tvBackToLogin.setOnClickListener(v -> handleBackToLogin());
    }

    private void handleCloseApp() {
        finishAffinity();
    }

    private void handleBackToLogin() {
        finish();
    }
}
