package com.mluansing.phonecalltimer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mluansing.phonecalltimer.util.SharedPreferenceUtil;

public class MainActivity extends AppCompatActivity {

    CountdownFragment countdownFragment;
    TimerFragment timerFragment;
    boolean isTimerRunning = false;

    // view
    ToggleButton toggleButton;
    SwitchCompat switchTimerMode;
    TextView modeDescription, labelTimer, labelCountdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeView();
        initializeTimerMode();
        initializeToggleButton();
    }

    private void initializeView() {
        toggleButton = findViewById(R.id.button_timer_start);
        modeDescription = findViewById(R.id.mode_description);
        switchTimerMode = findViewById(R.id.switch_timer_mode);
        labelCountdown = findViewById(R.id.label_countdown);
        labelTimer = findViewById(R.id.label_timer);
    }

    /**
     * initializes functionality of timer toggle button
     */
    private void initializeToggleButton() {
    }

    /**
     * initializes view mode based on user preferences
     */
    private void initializeTimerMode() {
        boolean isTimer = SharedPreferenceUtil.isDefaultTimer(MainActivity.this);

        changeTimerMode(isTimer);

        switchTimerMode.setChecked(isTimer);

        switchTimerMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferenceUtil.setDefaultTimer(MainActivity.this, b);
                changeTimerMode(b);
            }
        });
    }

    /**
     * replaces timer mode fragment
     *
     * @param isTimer true uses TimerFragment, false uses CountdownFragment
     */
    private void changeTimerMode(boolean isTimer) {
        if (isTimer) {
            timerFragment = new TimerFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_content, timerFragment).commit();
            modeDescription.setText(R.string.mode_timer_description);
            labelCountdown.setVisibility(View.INVISIBLE);
            labelTimer.setVisibility(View.VISIBLE);
        } else {
            countdownFragment = new CountdownFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_content, countdownFragment).commit();
            modeDescription.setText(R.string.mode_countdown_description);
            labelTimer.setVisibility(View.INVISIBLE);
            labelCountdown.setVisibility(View.VISIBLE);
        }
    }
}
