package com.example.hang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_MUSIC = "pref_music";
    public static final String KEY_SFX = "pref_sfx";
    public static final String KEY_VIBRATION = "pref_vibration";
    public static final String KEY_DARK_THEME = "pref_dark_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        View mainView = findViewById(R.id.settingsMain);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        CheckBox cbMusic = findViewById(R.id.cbMusic);
        CheckBox cbSfx = findViewById(R.id.cbSfx);
        CheckBox cbVibration = findViewById(R.id.cbVibration);
        CheckBox cbDarkTheme = findViewById(R.id.cbDarkTheme);

        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnReturnSurface = findViewById(R.id.btnReturnSurface);

        SharedPreferences prefs = getSharedPreferences(RegisterActivity.PREFS_NAME, Context.MODE_PRIVATE);

        if (cbMusic != null) {
            cbMusic.setChecked(prefs.getBoolean(KEY_MUSIC, true));
            cbMusic.setOnCheckedChangeListener((buttonView, isChecked) ->
                    prefs.edit().putBoolean(KEY_MUSIC, isChecked).apply());
        }

        if (cbSfx != null) {
            cbSfx.setChecked(prefs.getBoolean(KEY_SFX, true));
            cbSfx.setOnCheckedChangeListener((buttonView, isChecked) ->
                    prefs.edit().putBoolean(KEY_SFX, isChecked).apply());
        }

        if (cbVibration != null) {
            cbVibration.setChecked(prefs.getBoolean(KEY_VIBRATION, true));
            cbVibration.setOnCheckedChangeListener((buttonView, isChecked) ->
                    prefs.edit().putBoolean(KEY_VIBRATION, isChecked).apply());
        }

        if (cbDarkTheme != null) {
            cbDarkTheme.setChecked(prefs.getBoolean(KEY_DARK_THEME, true));
            cbDarkTheme.setOnCheckedChangeListener((buttonView, isChecked) ->
                    prefs.edit().putBoolean(KEY_DARK_THEME, isChecked).apply());
        }

        // Sever Subject Session (Logout)
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                prefs.edit().putBoolean(RegisterActivity.KEY_IS_REGISTERED, false).apply();
                Toast.makeText(this, "Session Severed (Logged Out)", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }

        // Return to Surface (Home)
        if (btnReturnSurface != null) {
            btnReturnSurface.setOnClickListener(v -> finish());
        }
    }
}