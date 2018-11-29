package com.mluansing.phonecalltimer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mluansing.phonecalltimer.modes.CountdownFragment;
import com.mluansing.phonecalltimer.modes.TimerFragment;
import com.mluansing.phonecalltimer.util.SharedPreferenceUtil;

import static com.mluansing.phonecalltimer.Constants.CURRENT_HOURS;
import static com.mluansing.phonecalltimer.Constants.CURRENT_MINUTES;
import static com.mluansing.phonecalltimer.Constants.CURRENT_SECONDS;
import static com.mluansing.phonecalltimer.Constants.DEFAULT_COUNTDOWN_HOURS;
import static com.mluansing.phonecalltimer.Constants.DEFAULT_COUNTDOWN_MINUTES;
import static com.mluansing.phonecalltimer.Constants.DEFAULT_COUNTDOWN_SECONDS;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1219;

    private static MainActivity instance;
    CountdownFragment countdownFragment;
    TimerFragment timerFragment;
    boolean isTimerRunning = false, isTimerMode;
    Handler timerHandler;
    Runnable secUpdater, minUpdater, hourUpdater;

    // view
    ToggleButton toggleButton;
    SwitchCompat switchTimerMode;
    View modeSwitchContainer;
    TextView modeDescription, labelTimer, labelCountdown;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        if (savedInstanceState != null) {
            if (!isTimerMode) {
                int hours = savedInstanceState.getInt(CURRENT_HOURS);
                int mins = savedInstanceState.getInt(CURRENT_MINUTES);
                int seconds = savedInstanceState.getInt(CURRENT_SECONDS);

                countdownFragment = CountdownFragment.newInstance(hours, mins, seconds);
                timerFragment = null;
            }
        }

        setContentView(R.layout.activity_main);

        initializeView();
        initializeTimerMode();
        initializeToggleButton();

        askUserForPermissions();

        // TODO: on rotation, resume timer where it was
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int hours, mins, secs;

        if (isTimerMode) {
            hours = timerFragment.getHours();
            mins = timerFragment.getMinutes();
            secs = timerFragment.getSeconds();


        } else {
            hours = countdownFragment.getHours();
            mins = countdownFragment.getMinutes();
            secs = countdownFragment.getSeconds();

        }

        outState.putInt(CURRENT_HOURS, hours);
        outState.putInt(CURRENT_MINUTES, mins);
        outState.putInt(CURRENT_SECONDS, secs);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    toggleButton.setEnabled(true);
                    toggleButton.setAlpha(1);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    toggleButton.setEnabled(false);
                    toggleButton.setAlpha((float) 0.7);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void initializeView() {
        toggleButton = findViewById(R.id.button_timer_start);
        modeSwitchContainer = findViewById(R.id.mode_switch_container);
        modeDescription = findViewById(R.id.mode_description);
        switchTimerMode = findViewById(R.id.switch_timer_mode);
        labelCountdown = findViewById(R.id.label_countdown);
        labelTimer = findViewById(R.id.label_timer);

        isTimerMode = SharedPreferenceUtil.isDefaultTimer(MainActivity.this);
    }

    /**
     * initializes functionality of timer toggle button
     */
    private void initializeToggleButton() {
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                // TODO: validate timer can be started / countdown time > 0
                isTimerRunning = b;

                if (!isTimerRunning) {
                    modeSwitchContainer.setVisibility(View.VISIBLE);

                    // cancel timer
                    KillCallManager.cancelTimer(MainActivity.this);

                    Log.d(TAG, "Cancelling timer");

                    resetClock();
                } else {
                    modeSwitchContainer.setVisibility(View.INVISIBLE);

                    if (isTimerMode) {
                        int hours = timerFragment.getHours();
                        int mins = timerFragment.getMinutes();
                        int secs = timerFragment.getSeconds();

                        Log.d(TAG, "Setting timer at " + hours + ":" + mins + ":" + secs + " AM / PM");

                        // set timer
                        KillCallManager.setTimer(MainActivity.this, hours, mins, secs);

                    } else {
                        int hours = countdownFragment.getHours();
                        int mins = countdownFragment.getMinutes();
                        int secs = countdownFragment.getSeconds();

                        Log.d(TAG, "Setting countdown for " + hours + ":" + mins + ":" + secs + " from now");

                        // start countdown
                        KillCallManager.setCountdown(MainActivity.this, hours, mins, secs);

                        showLiveClockChanges();
                    }
                }
            }
        });
    }

    private void showLiveClockChanges() {
        timerHandler = new Handler();

        secUpdater = new Runnable() {
            @Override
            public void run() {
                boolean success = countdownFragment.decrementSeconds();

                if (success) {
                    timerHandler.postDelayed(secUpdater, 1000);
                } else {
                    timerHandler.removeCallbacks(secUpdater);
                }
            }
        };
        timerHandler.post(secUpdater);

        minUpdater = new Runnable() {
            @Override
            public void run() {
                boolean success = countdownFragment.decrementMinutes();

                if (success) {
                    timerHandler.postDelayed(minUpdater, 60 * 1000);
                } else {
                    timerHandler.removeCallbacks(minUpdater);
                }
            }
        };
        timerHandler.post(minUpdater);

        hourUpdater = new Runnable() {
            @Override
            public void run() {
                boolean success = countdownFragment.decrementHours();

                if (success) {
                    timerHandler.postDelayed(hourUpdater, 60 * 60 * 1000);
                } else {
                    timerHandler.removeCallbacks(hourUpdater);
                }
            }
        };
        timerHandler.post(hourUpdater);

    }

    /**
     * initializes view mode based on user preferences
     */
    private void initializeTimerMode() {
        changeTimerMode();

        switchTimerMode.setChecked(isTimerMode);

        switchTimerMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isTimerMode = b;
                SharedPreferenceUtil.setDefaultTimer(MainActivity.this, isTimerMode);
                changeTimerMode();
            }
        });
    }

    /**
     * replaces timer mode fragment
     * <p>
     * isTimerMode true uses TimerFragment, false uses CountdownFragment
     */
    private void changeTimerMode() {
        if (isTimerMode) {
            if (timerFragment == null) {
                timerFragment = new TimerFragment();
                countdownFragment = null;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_content, timerFragment).commit();
            modeDescription.setText(R.string.mode_timer_description);
            labelCountdown.setVisibility(View.INVISIBLE);
            labelTimer.setVisibility(View.VISIBLE);
        } else {
            if (countdownFragment == null) {
                countdownFragment = CountdownFragment.newInstance(DEFAULT_COUNTDOWN_HOURS, DEFAULT_COUNTDOWN_MINUTES, DEFAULT_COUNTDOWN_SECONDS);
                timerFragment = null;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_content, countdownFragment).commit();
            modeDescription.setText(R.string.mode_countdown_description);
            labelTimer.setVisibility(View.INVISIBLE);
            labelCountdown.setVisibility(View.VISIBLE);
        }
    }

    private void askUserForPermissions() {
        // TODO: this doesn't automatically prompt in sdk 19
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {

                Toast.makeText(MainActivity.this, R.string.permissions_rationale, Toast.LENGTH_SHORT).show();

                toggleButton.setEnabled(false);
                toggleButton.setAlpha((float) 0.7);

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

                // MY_PERMISSIONS_REQUEST_READ_PHONE_STATE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // permission already granted
            toggleButton.setEnabled(true);
            toggleButton.setAlpha(1);
        }
    }

    public void resetClock() {
        countdownFragment = null;
        timerFragment = null;

        changeTimerMode();
        toggleButton.setChecked(false);

        // stop live clock updates
        if (timerHandler != null) {
            timerHandler.removeCallbacksAndMessages(null);
        }
    }
}