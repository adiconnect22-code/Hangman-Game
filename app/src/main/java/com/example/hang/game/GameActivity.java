package com.example.hang.game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hang.R;
import com.example.hang.RegisterActivity;

import java.util.HashSet;
import java.util.Set;

public class GameActivity extends AppCompatActivity {

    private HangmanCanvasView hangmanCanvas;
    private TextView tvWordBlanks;
    private TextView tvWordHint;
    private TextView tvDestination;
    private TextView tvTether;
    private TextView tvRemaining;

    private LinearLayout keyboardRow1;
    private LinearLayout keyboardRow2;
    private LinearLayout keyboardRow3;

    private String secretWord = "GIRAFFE";
    private String secretHint = "Long neck tall animal";
    private String category = "ANIMALS";
    private String difficulty = "EASY";
    private String mode = "CLASSIC";

    private final Set<Character> guessedLetters = new HashSet<>();

    private int wrongCount = 0;
    private int tetherCount = 6;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        View mainView = findViewById(R.id.gameMain);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        hangmanCanvas = findViewById(R.id.hangmanCanvas);
        tvWordBlanks = findViewById(R.id.tvWordBlanks);
        tvWordHint = findViewById(R.id.tvWordHint);
        tvDestination = findViewById(R.id.tvDestination);
        tvTether = findViewById(R.id.tvTether);
        tvRemaining = findViewById(R.id.tvRemaining);

        keyboardRow1 = findViewById(R.id.keyboardRow1);
        keyboardRow2 = findViewById(R.id.keyboardRow2);
        keyboardRow3 = findViewById(R.id.keyboardRow3);

        View btnHintBulb = findViewById(R.id.btnHintBulb);
        if (btnHintBulb != null) {
            btnHintBulb.setOnClickListener(v -> revealHint());
        }

        // Read extras from ChooseFateActivity
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(ChooseFateActivity.EXTRA_CATEGORY)) {
                category = intent.getStringExtra(ChooseFateActivity.EXTRA_CATEGORY);
            }
            if (intent.hasExtra(ChooseFateActivity.EXTRA_DIFFICULTY)) {
                difficulty = intent.getStringExtra(ChooseFateActivity.EXTRA_DIFFICULTY);
            }
            if (intent.hasExtra(ChooseFateActivity.EXTRA_MODE)) {
                mode = intent.getStringExtra(ChooseFateActivity.EXTRA_MODE);
            }
        }

        WordBank.WordHint pair = WordBank.getRandomWordHint(category);
        secretWord = pair.word;
        secretHint = pair.hint;

        setupUI();
        buildQwertyKeyboard();

        if (mode != null && mode.toUpperCase().contains("TIMER")) {
            if (tvRemaining != null) {
                tvRemaining.setVisibility(View.VISIBLE);
            }
            startTimer();
        }
    }

    private void revealHint() {
        if (tvWordHint != null) {
            if (tvWordHint.getVisibility() != View.VISIBLE) {
                tvWordHint.setAlpha(0f);
                tvWordHint.setVisibility(View.VISIBLE);
                tvWordHint.animate().alpha(1f).setDuration(300).start();
            }
        }
        Toast.makeText(this, getString(R.string.hint_toast_format, secretHint), Toast.LENGTH_SHORT).show();
    }

    private void setupUI() {
        if (tvDestination != null) {
            String catShort = category.contains("-") ? category.split("-")[0].trim() : category;
            String diffShort = difficulty.contains("-") ? difficulty.split("-")[0].trim() : difficulty;
            tvDestination.setText(getString(R.string.destination_format, catShort.toUpperCase(), diffShort.toUpperCase()));
        }

        if (tvWordHint != null) {
            tvWordHint.setText(getString(R.string.clue_format, secretHint));
        }

        updateTetherDisplay();
        updateWordBlanks();
    }

    private void updateTetherDisplay() {
        if (tvTether != null) {
            tvTether.setText(getString(R.string.tether_format, tetherCount));
        }
    }

    private void updateWordBlanks() {
        StringBuilder builder = new StringBuilder();
        boolean hasUnguessed = false;

        for (int i = 0; i < secretWord.length(); i++) {
            char c = secretWord.charAt(i);
            if (guessedLetters.contains(c)) {
                builder.append(c);
            } else {
                builder.append("_");
                hasUnguessed = true;
            }
            if (i < secretWord.length() - 1) {
                builder.append(" ");
            }
        }

        if (tvWordBlanks != null) {
            tvWordBlanks.setText(builder.toString());
        }

        if (!hasUnguessed) {
            handleGameEnd(true);
        }
    }

    private void buildQwertyKeyboard() {
        String row1Keys = "QWERTYUIOP";
        String row2Keys = "ASDFGHJKL";
        String row3Keys = "ZXCVBNM";

        populateKeyRow(keyboardRow1, row1Keys);
        populateKeyRow(keyboardRow2, row2Keys);
        populateKeyRow(keyboardRow3, row3Keys);
    }

    private void populateKeyRow(LinearLayout rowLayout, String keys) {
        if (rowLayout == null) return;
        rowLayout.removeAllViews();

        int marginPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int heightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, getResources().getDisplayMetrics());

        for (char letter : keys.toCharArray()) {
            final char currentLetter = letter;
            Button keyButton = new Button(this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, heightPx, 1f);
            params.setMargins(marginPx, marginPx, marginPx, marginPx);
            keyButton.setLayoutParams(params);

            keyButton.setText(String.valueOf(currentLetter));
            keyButton.setTextColor(0xFFFFFFFF);
            keyButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            keyButton.setTypeface(Typeface.create("sans-serif-condensed", Typeface.BOLD));
            keyButton.setBackgroundResource(R.drawable.bg_key_normal);
            keyButton.setPadding(0, 0, 0, 0);

            keyButton.setOnClickListener(v -> onLetterClicked(currentLetter, keyButton));

            rowLayout.addView(keyButton);
        }
    }

    private void onLetterClicked(char letter, Button keyButton) {
        if (guessedLetters.contains(letter) || tetherCount <= 0) return;

        guessedLetters.add(letter);
        keyButton.setEnabled(false);

        if (secretWord.indexOf(letter) >= 0) {
            // Correct guess! Reveal letter on the blanks above!
            keyButton.setBackgroundResource(R.drawable.bg_key_correct);
            updateWordBlanks();

            if (tvWordBlanks != null) {
                tvWordBlanks.animate().scaleX(1.12f).scaleY(1.12f).setDuration(100).withEndAction(() ->
                        tvWordBlanks.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                ).start();
            }
        } else {
            // Wrong guess!
            keyButton.setBackgroundResource(R.drawable.bg_key_used);
            wrongCount++;
            tetherCount = Math.max(0, 6 - wrongCount);

            if (hangmanCanvas != null) {
                hangmanCanvas.setWrongGuesses(wrongCount);
                if (wrongCount >= 3) {
                    hangmanCanvas.setAtmosphereText("THE AIR IN THE CAGE IS COLD.");
                }
            }

            updateTetherDisplay();

            if (tetherCount <= 0) {
                handleGameEnd(false);
            }
        }
    }

    private void startTimer() {
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                if (tvRemaining != null) {
                    tvRemaining.setText(getString(R.string.remaining_format, seconds));
                }
            }

            @Override
            public void onFinish() {
                if (tvRemaining != null) {
                    tvRemaining.setText(getString(R.string.remaining_format, 0));
                }
                handleGameEnd(false);
            }
        }.start();
    }

    private void handleGameEnd(boolean won) {
        if (timer != null) {
            timer.cancel();
        }

        // Calculate and save scores in SharedPreferences
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.PREFS_NAME, Context.MODE_PRIVATE);
        int rituals = prefs.getInt("rituals_performed", 0) + 1;
        int essencesSaved = prefs.getInt("essences_saved", 0);
        int victimLosses = prefs.getInt("victim_losses", 0);
        int currentResonance = prefs.getInt("resonance", 0);
        int streak = prefs.getInt("rescue_streak", 0);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("rituals_performed", rituals);

        if (won) {
            essencesSaved++;
            streak++;
            int earnedPoints = 20 + (streak * 5);
            currentResonance += earnedPoints;

            String catUpper = (category != null) ? category.toUpperCase() : "ANIMALS";
            if (catUpper.contains("OBJECT")) {
                editor.putInt("solved_objects", prefs.getInt("solved_objects", 0) + 1);
            } else if (catUpper.contains("CELEBRITY") || catUpper.contains("CELEBRITIES")) {
                editor.putInt("solved_celebrities", prefs.getInt("solved_celebrities", 0) + 1);
            } else {
                editor.putInt("solved_animals", prefs.getInt("solved_animals", 0) + 1);
            }

            editor.putInt("essences_saved", essencesSaved);
            editor.putInt("rescue_streak", streak);
            editor.putInt("resonance", currentResonance);
        } else {
            victimLosses++;
            streak = 0;
            editor.putInt("victim_losses", victimLosses);
            editor.putInt("rescue_streak", streak);
        }
        editor.apply();

        // Launch GameOverActivity
        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra(GameOverActivity.EXTRA_WON, won);
        intent.putExtra(GameOverActivity.EXTRA_WORD, secretWord);
        intent.putExtra(GameOverActivity.EXTRA_CATEGORY, category);
        intent.putExtra(GameOverActivity.EXTRA_DIFFICULTY, difficulty);
        intent.putExtra(GameOverActivity.EXTRA_MODE, mode);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}