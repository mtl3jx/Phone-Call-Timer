package com.mluansing.phonecalltimer.modes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mluansing.phonecalltimer.R;

import java.text.DecimalFormat;

public class CountdownFragment extends Fragment {

    public static final String TAG = CountdownFragment.class.getSimpleName();

    private static final int HOURS = 0, MINS = 0, SECONDS = 30;

    TextView hours, mins, seconds;

    private static final DecimalFormat FORMATTER_TIME = new DecimalFormat("00");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_countdown, viewGroup, false);

        initializeCountdownView(view);

        return view;
    }

    private void initializeCountdownView(View view) {
        hours = view.findViewById(R.id.timer_hours);
        mins = view.findViewById(R.id.timer_minutes);
        seconds = view.findViewById(R.id.timer_seconds);

        hours.setText(FORMATTER_TIME.format(HOURS));
        mins.setText(FORMATTER_TIME.format(MINS));
        seconds.setText(FORMATTER_TIME.format(SECONDS));
    }

    public int getHours() {
        return Integer.parseInt(hours.getText().toString());
    }

    public int getMinutes() {
        return Integer.parseInt(mins.getText().toString());
    }

    public int getSeconds() {
        return Integer.parseInt(seconds.getText().toString());
    }

    public boolean decrementHours() {
        int temp = getHours();
        if (temp >= 1) {
            hours.setText(String.valueOf(temp - 1));
            return true;
        } else {
            Log.e(TAG, "Could not decrement hours less than 0");
            return false;
        }
    }

    public boolean decrementMinutes() {
        int temp = getMinutes();
        if (temp >= 1) {
            mins.setText(String.valueOf(temp - 1));
            return true;
        } else {
            Log.e(TAG, "Could not decrement minutes less than 0");
            return false;
        }
    }

    public boolean decrementSeconds() {
        int temp = getSeconds();
        if (temp >= 1) {
            String newValue = String.valueOf(temp - 1);
            seconds.setText(newValue);
            return true;
        } else {
            Log.e(TAG, "Could not decrement seconds less than 0");
            return false;
        }
    }

}
