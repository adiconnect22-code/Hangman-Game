package com.example.hang;

import android.content.Context;
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

import java.util.Locale;

public class SoulRecordActivity extends AppCompatActivity {

    public static final String EXTRA_SUBJECT_ID = "subject_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_soul_record);

        View mainView = findViewById(R.id.soulRecordMain);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        TextView tvSubjectId = findViewById(R.id.tvSubjectId);
        TextView tvRitualsPerformed = findViewById(R.id.tvRitualsPerformed);
        TextView tvEssencesSaved = findViewById(R.id.tvEssencesSaved);
        TextView tvVictimLosses = findViewById(R.id.tvVictimLosses);
        TextView tvStabilityRate = findViewById(R.id.tvStabilityRate);
        TextView tvHighestResonance = findViewById(R.id.tvHighestResonance);
        TextView tvRescueStreak = findViewById(R.id.tvRescueStreak);

        TextView tvFlawlessRescues = findViewById(R.id.tvFlawlessRescues);
        TextView tvCriticalRescues = findViewById(R.id.tvCriticalRescues);
        TextView tvSalvationRate = findViewById(R.id.tvSalvationRate);

        TextView tvFastestRescue = findViewById(R.id.tvFastestRescue);
        TextView tvLetterPrecision = findViewById(R.id.tvLetterPrecision);

        SoulLineGraphView graphSoul = findViewById(R.id.graphSoul);
        TextView tvCatAnimalsCount = findViewById(R.id.tvCatAnimalsCount);
        TextView tvCatObjectsCount = findViewById(R.id.tvCatObjectsCount);
        TextView tvCatCelebritiesCount = findViewById(R.id.tvCatCelebritiesCount);

        Button btnReturnSurface = findViewById(R.id.btnReturnSurfaceSoul);

        // Fetch subject ID from Intent extra or SharedPreferences
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String savedUser = prefs.getString(RegisterActivity.KEY_USERNAME, "ADITHYA");
        String defaultUser = savedUser.trim().isEmpty() ? "ADITHYA" : savedUser.trim();

        String rawSubjectId = getIntent().getStringExtra(EXTRA_SUBJECT_ID);
        String subjectId = (rawSubjectId == null || rawSubjectId.trim().isEmpty()) ? defaultUser : rawSubjectId.trim();

        if (tvSubjectId != null) {
            tvSubjectId.setText(getString(R.string.subject_id_format, subjectId.toUpperCase()));
        }

        // If inspecting another user vs logged-in user:
        boolean isCurrentUser = subjectId.equalsIgnoreCase(defaultUser);
        int resonance = isCurrentUser ? prefs.getInt("resonance", 0) : (subjectId.equalsIgnoreCase("adi") ? 160 : 0);
        int victimLosses = isCurrentUser ? prefs.getInt("victim_losses", 0) : (subjectId.equalsIgnoreCase("adi") ? 6 : 0);
        int rituals = isCurrentUser ? prefs.getInt("rituals_performed", 0) : (subjectId.equalsIgnoreCase("adi") ? 10 : 0);
        int saved = isCurrentUser ? prefs.getInt("essences_saved", 0) : (subjectId.equalsIgnoreCase("adi") ? 4 : 0);
        int streak = isCurrentUser ? prefs.getInt("rescue_streak", 0) : 0;
        int stabilityRate = (rituals > 0) ? (saved * 100 / rituals) : 0;

        int flawless = isCurrentUser ? prefs.getInt("flawless_rescues", 0) : (subjectId.equalsIgnoreCase("adi") ? 2 : 0);
        int critical = isCurrentUser ? prefs.getInt("critical_rescues", 0) : (subjectId.equalsIgnoreCase("adi") ? 1 : 0);
        float fastestSec = isCurrentUser ? prefs.getFloat("fastest_rescue_sec", 0f) : (subjectId.equalsIgnoreCase("adi") ? 12.4f : 0f);

        int totalGuesses = isCurrentUser ? prefs.getInt("total_guesses", 0) : (subjectId.equalsIgnoreCase("adi") ? 45 : 0);
        int correctGuesses = isCurrentUser ? prefs.getInt("correct_guesses", 0) : (subjectId.equalsIgnoreCase("adi") ? 32 : 0);
        int letterPrecision = (totalGuesses > 0) ? (correctGuesses * 100 / totalGuesses) : 0;

        int solvedAnimals = isCurrentUser ? prefs.getInt("solved_animals", 0) : (subjectId.equalsIgnoreCase("adi") ? 2 : 0);
        int solvedObjects = isCurrentUser ? prefs.getInt("solved_objects", 0) : (subjectId.equalsIgnoreCase("adi") ? 1 : 0);
        int solvedCelebrities = isCurrentUser ? prefs.getInt("solved_celebrities", 0) : (subjectId.equalsIgnoreCase("adi") ? 1 : 0);

        if (tvRitualsPerformed != null) {
            tvRitualsPerformed.setText(getString(R.string.stat_rituals_performed, rituals));
        }

        if (tvEssencesSaved != null) {
            tvEssencesSaved.setText(getString(R.string.stat_essences_saved, saved));
        }

        if (tvVictimLosses != null) {
            tvVictimLosses.setText(getString(R.string.stat_victim_losses, victimLosses));
        }

        if (tvStabilityRate != null) {
            tvStabilityRate.setText(getString(R.string.stat_stability_rate, stabilityRate));
        }

        if (tvHighestResonance != null) {
            tvHighestResonance.setText(getString(R.string.stat_highest_resonance, resonance));
        }

        if (tvRescueStreak != null) {
            tvRescueStreak.setText(getString(R.string.stat_rescue_streak, streak));
        }

        // Ritual Mastery & Efficiency
        if (tvFlawlessRescues != null) {
            tvFlawlessRescues.setText(getString(R.string.stat_flawless_rescues, flawless));
        }

        if (tvCriticalRescues != null) {
            tvCriticalRescues.setText(getString(R.string.stat_critical_rescues, critical));
        }

        if (tvSalvationRate != null) {
            tvSalvationRate.setText(getString(R.string.stat_salvation_rate, stabilityRate));
        }

        // Speed & Precision
        if (tvFastestRescue != null) {
            String fastestStr = (fastestSec > 0f) ? String.format(Locale.getDefault(), "%.1f s", fastestSec) : "N/A";
            tvFastestRescue.setText(getString(R.string.stat_fastest_rescue, fastestStr));
        }

        if (tvLetterPrecision != null) {
            tvLetterPrecision.setText(getString(R.string.stat_letter_precision, letterPrecision));
        }

        if (graphSoul != null) {
            graphSoul.setGraphData(saved, victimLosses);
        }

        if (tvCatAnimalsCount != null) {
            tvCatAnimalsCount.setText(getString(R.string.cat_animals_count, solvedAnimals));
        }

        if (tvCatObjectsCount != null) {
            tvCatObjectsCount.setText(getString(R.string.cat_objects_count, solvedObjects));
        }

        if (tvCatCelebritiesCount != null) {
            tvCatCelebritiesCount.setText(getString(R.string.cat_celebrities_count, solvedCelebrities));
        }

        if (btnReturnSurface != null) {
            btnReturnSurface.setOnClickListener(v -> finish());
        }
    }
}