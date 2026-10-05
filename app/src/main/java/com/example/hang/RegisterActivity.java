package com.example.hang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "HangmanPrefs";
    public static final String KEY_IS_REGISTERED = "is_registered";
    public static final String KEY_USERNAME = "registered_username";
    public static final String KEY_EMAIL = "registered_email";
    public static final String KEY_PASSWORD = "registered_password";

    private EditText etEmail;
    private EditText etDesiredUsername;
    private EditText etRegisterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        View mainView = findViewById(R.id.registerMain);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        etEmail = findViewById(R.id.etEmail);
        etDesiredUsername = findViewById(R.id.etDesiredUsername);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);

        Button btnBindSoul = findViewById(R.id.btnBindSoul);
        TextView tvReturnLogin = findViewById(R.id.tvReturnLogin);

        if (btnBindSoul != null) {
            btnBindSoul.setOnClickListener(v -> handleRegistration());
        }

        if (tvReturnLogin != null) {
            tvReturnLogin.setOnClickListener(v -> {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.putExtra("from_register", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }
    }

    private void handleRegistration() {
        if (etEmail == null || etDesiredUsername == null || etRegisterPassword == null) return;

        String email = etEmail.getText().toString().trim();
        String username = etDesiredUsername.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(username)) {
            etDesiredUsername.setError("Username is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etRegisterPassword.setError("Password is required");
            return;
        }

        // Save registration status and info in SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putBoolean(KEY_IS_REGISTERED, true)
                .putString(KEY_EMAIL, email)
                .putString(KEY_USERNAME, username)
                .putString(KEY_PASSWORD, password)
                .apply();

        Toast.makeText(this, "Soul Bound Successfully!", Toast.LENGTH_SHORT).show();

        // Navigate directly to Home screen
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}