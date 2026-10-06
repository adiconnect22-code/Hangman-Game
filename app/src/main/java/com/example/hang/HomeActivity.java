package com.example.hang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hang.game.ChooseFateActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        View mainView = findViewById(R.id.homeMain);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Check & Update Daily Streak
        DailyStreakActivity.checkAndUpdateDailyStreak(this);

        TextView tvActiveSubject = findViewById(R.id.tvActiveSubject);
        TextView tvHomeStreakCount = findViewById(R.id.tvHomeStreakCount);
        View btnStreakBadge = findViewById(R.id.btnStreakBadge);

        Button btnEnterVoid = findViewById(R.id.btnEnterVoid);
        Button btnSoulRecord = findViewById(R.id.btnSoulRecord);
        Button btnWallOfKills = findViewById(R.id.btnWallOfKills);
        Button btnRitualInsight = findViewById(R.id.btnRitualInsight);
        Button btnAlterEnvironment = findViewById(R.id.btnAlterEnvironment);

        // Fetch user preferences
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String savedUsername = prefs.getString(RegisterActivity.KEY_USERNAME, "ADITHYA");
        String username = savedUsername.trim().isEmpty() ? "ADITHYA" : savedUsername.trim();
        int currentStreak = prefs.getInt(DailyStreakActivity.KEY_DAILY_STREAK, 1);

        if (tvActiveSubject != null) {
            tvActiveSubject.setText(getString(R.string.active_subject_format, username.toUpperCase()));
        }

        if (tvHomeStreakCount != null) {
            tvHomeStreakCount.setText(getString(R.string.home_streak_format, currentStreak));
        }

        if (btnStreakBadge != null) {
            btnStreakBadge.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, DailyStreakActivity.class);
                startActivity(intent);
            });
        }

        if (btnEnterVoid != null) {
            btnEnterVoid.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, ChooseFateActivity.class);
                startActivity(intent);
            });
        }

        if (btnSoulRecord != null) {
            btnSoulRecord.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, SoulRecordActivity.class);
                intent.putExtra(SoulRecordActivity.EXTRA_SUBJECT_ID, username);
                startActivity(intent);
            });
        }

        if (btnWallOfKills != null) {
            btnWallOfKills.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            });
        }

        if (btnRitualInsight != null) {
            btnRitualInsight.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, HowToPlayActivity.class);
                startActivity(intent);
            });
        }

        if (btnAlterEnvironment != null) {
            btnAlterEnvironment.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.PREFS_NAME, Context.MODE_PRIVATE);
        int currentStreak = prefs.getInt(DailyStreakActivity.KEY_DAILY_STREAK, 1);
        TextView tvHomeStreakCount = findViewById(R.id.tvHomeStreakCount);
        if (tvHomeStreakCount != null) {
            tvHomeStreakCount.setText(getString(R.string.home_streak_format, currentStreak));
        }
    }
}