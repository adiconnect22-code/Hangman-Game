package com.example.hang.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hang.R;

public class ChooseFateActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "extra_category";
    public static final String EXTRA_DIFFICULTY = "extra_difficulty";
    public static final String EXTRA_MODE = "extra_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_fate);

        View mainView = findViewById(R.id.chooseFateMain);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        RadioGroup rgCategory = findViewById(R.id.rgCategory);
        RadioGroup rgDifficulty = findViewById(R.id.rgDifficulty);
        RadioGroup rgMode = findViewById(R.id.rgMode);
        Button btnEnterRoom = findViewById(R.id.btnEnterRoom);

        if (btnEnterRoom != null) {
            btnEnterRoom.setOnClickListener(v -> {
                String catText = getSelectedText(rgCategory, "ANIMALS");
                String diffText = getSelectedText(rgDifficulty, "EASY");
                String modeText = getSelectedText(rgMode, "CLASSIC");

                Intent intent = new Intent(ChooseFateActivity.this, GameActivity.class);
                intent.putExtra(EXTRA_CATEGORY, catText);
                intent.putExtra(EXTRA_DIFFICULTY, diffText);
                intent.putExtra(EXTRA_MODE, modeText);
                startActivity(intent);
            });
        }
    }

    private String getSelectedText(RadioGroup rg, String defaultVal) {
        if (rg != null) {
            int checkedId = rg.getCheckedRadioButtonId();
            RadioButton rb = findViewById(checkedId);
            if (rb != null) {
                return rb.getText().toString();
            }
        }
        return defaultVal;
    }
}