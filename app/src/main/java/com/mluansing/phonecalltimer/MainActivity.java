package com.mluansing.phonecalltimer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

import com.mluansing.phonecalltimer.util.SharedPreferenceUtil;

public class MainActivity extends AppCompatActivity {

    SwitchCompat switchTimerMode;
    CountdownFragment countdownFragment;
    TimerFragment timerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeTimerMode();
    }

    /**
     * initializes view mode based on user preferences
     */
    private void initializeTimerMode() {
        boolean isTimer = SharedPreferenceUtil.isDefaultTimer(MainActivity.this);

        changeTimerMode(isTimer);

        switchTimerMode = findViewById(R.id.switch_timer_mode);
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
        } else {
            countdownFragment = new CountdownFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_content, countdownFragment).commit();
        }
    }
}
