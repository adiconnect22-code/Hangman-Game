package com.example.hang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private static class PlayerScore {
        final String name;
        final int resonance;
        final int victimLosses;
        final boolean isCurrentUser;

        PlayerScore(String name, int resonance, int victimLosses, boolean isCurrentUser) {
            this.name = name;
            this.resonance = resonance;
            this.victimLosses = victimLosses;
            this.isCurrentUser = isCurrentUser;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leaderboard);

        View mainView = findViewById(R.id.leaderboardMain);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        LinearLayout leaderboardList = findViewById(R.id.leaderboardList);
        Button btnReturnSurface = findViewById(R.id.btnReturnSurfaceWall);

        // Fetch current logged-in user
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String savedUser = prefs.getString(RegisterActivity.KEY_USERNAME, "adithya");
        String username = savedUser.trim().isEmpty() ? "adithya" : savedUser.trim();
        int userResonance = prefs.getInt("resonance", 0);
        int userVictimLosses = prefs.getInt("victim_losses", 0);

        List<PlayerScore> players = new ArrayList<>();

        if (username.equalsIgnoreCase("adi")) {
            // Merge logged-in user with 'adi'
            int highestResonance = Math.max(160, userResonance);
            players.add(new PlayerScore("adi", highestResonance, userVictimLosses > 0 ? userVictimLosses : 6, true));
        } else {
            players.add(new PlayerScore("adi", 160, 6, false));
            players.add(new PlayerScore(username, userResonance, userVictimLosses, true));
        }

        // Sort by resonance descending, then victimLosses ascending
        players.sort((p1, p2) -> {
            if (p2.resonance != p1.resonance) {
                return Integer.compare(p2.resonance, p1.resonance);
            }
            return Integer.compare(p1.victimLosses, p2.victimLosses);
        });

        if (leaderboardList != null) {
            leaderboardList.removeAllViews();

            int dp14 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics());
            int dp12 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());

            for (PlayerScore player : players) {
                int rank = players.indexOf(player) + 1;

                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setPadding(dp14, dp14, dp14, dp14);
                row.setClickable(true);
                row.setFocusable(true);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, dp12);
                row.setLayoutParams(params);

                if (player.isCurrentUser) {
                    row.setBackgroundResource(R.drawable.bg_leaderboard_user_row);
                } else {
                    row.setBackgroundResource(R.drawable.bg_leaderboard_row);
                }

                TextView tvText = new TextView(this);
                tvText.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                tvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                tvText.setLetterSpacing(0.04f);
                tvText.setTypeface(Typeface.create("sans-serif-condensed", Typeface.BOLD));

                if (player.isCurrentUser) {
                    tvText.setTextColor(0xFFFF9900); // Gold/orange highlight
                } else {
                    tvText.setTextColor(0xFFCCCCCC); // Light silver
                }

                tvText.setText(getString(R.string.leaderboard_row_format, rank, player.name, player.resonance, player.victimLosses));
                row.addView(tvText);

                // On clicking any player row, open Soul Record profile
                final String targetSubject = player.name;
                row.setOnClickListener(v -> {
                    Intent intent = new Intent(LeaderboardActivity.this, SoulRecordActivity.class);
                    intent.putExtra(SoulRecordActivity.EXTRA_SUBJECT_ID, targetSubject);
                    startActivity(intent);
                });

                leaderboardList.addView(row);
            }
        }

        if (btnReturnSurface != null) {
            btnReturnSurface.setOnClickListener(v -> finish());
        }
    }
}