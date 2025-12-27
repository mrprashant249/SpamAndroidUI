package com.bb.spamfolder.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.spamfolder.R;
import com.bb.spamfolder.utils.Screens;

public class LoginActivity extends BaseActivity {
    private EditText etPhone;
    private EditText etPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Set click listeners
        btnLogin.setOnClickListener(v -> handleLogin());
        tvForgotPassword.setOnClickListener(v -> handleForgotPassword());
    }

    private void handleLogin() {
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.msg_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        Screens.showCustomScreen(mActivity, MainActivity.class);
    }

    private void handleForgotPassword() {
        Screens.showCustomScreen(mActivity, SuccessActivity.class);
    }
}
