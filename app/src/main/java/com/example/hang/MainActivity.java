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

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences prefs = getSharedPreferences(RegisterActivity.PREFS_NAME, Context.MODE_PRIVATE);
        boolean isRegistered = prefs.getBoolean(RegisterActivity.KEY_IS_REGISTERED, false);
        boolean fromRegister = getIntent().getBooleanExtra("from_register", false);

        if (!isRegistered && !fromRegister) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        Button btnEnter = findViewById(R.id.btnEnter);
        TextView tvSignUp = findViewById(R.id.tvSignUp);

        if (btnEnter != null) {
            btnEnter.setOnClickListener(v -> handleLogin());
        }

        if (tvSignUp != null) {
            tvSignUp.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            });
        }
    }

    private void handleLogin() {
        if (etUsername == null || etPassword == null) return;

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password required");
            return;
        }

        SharedPreferences prefs = getSharedPreferences(RegisterActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String savedUsername = prefs.getString(RegisterActivity.KEY_USERNAME, "");
        String savedPassword = prefs.getString(RegisterActivity.KEY_PASSWORD, "");

        if (username.equalsIgnoreCase(savedUsername) && Objects.equals(password, savedPassword)) {
            Toast.makeText(this, "Access Granted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else if (!savedUsername.isEmpty()) {
            Toast.makeText(this, "Access Denied: Invalid Credentials", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Welcome, " + username, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}