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

import static com.mluansing.phonecalltimer.Constants.CURRENT_HOURS;
import static com.mluansing.phonecalltimer.Constants.CURRENT_MINUTES;
import static com.mluansing.phonecalltimer.Constants.CURRENT_SECONDS;

public class CountdownFragment extends Fragment {

    public static final String TAG = CountdownFragment.class.getSimpleName();

    int hours, mins, seconds;
    TextView hoursText, minsText, secondsText;

    private static final DecimalFormat FORMATTER_TIME = new DecimalFormat("00");

    public static CountdownFragment newInstance(int hours, int minutes, int seconds) {
        CountdownFragment fragment = new CountdownFragment();

        Bundle args = new Bundle();
        args.putInt(CURRENT_HOURS, hours);
        args.putInt(CURRENT_MINUTES, minutes);
        args.putInt(CURRENT_SECONDS, seconds);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_countdown, viewGroup, false);

        if (getArguments() != null) {
            hours = getArguments().getInt(CURRENT_HOURS);
            mins = getArguments().getInt(CURRENT_MINUTES);
            seconds = getArguments().getInt(CURRENT_SECONDS);
        }

        initializeCountdownView(view);

        return view;
    }

    private void initializeCountdownView(View view) {
        hoursText = view.findViewById(R.id.timer_hours);
        minsText = view.findViewById(R.id.timer_minutes);
        secondsText = view.findViewById(R.id.timer_seconds);

        hoursText.setText(FORMATTER_TIME.format(hours));
        minsText.setText(FORMATTER_TIME.format(mins));
        secondsText.setText(FORMATTER_TIME.format(seconds));
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return mins;
    }

    public int getSeconds() {
        return seconds;
    }

    public boolean decrementHours() {
        if (hours >= 1) {
            hours--;
            hoursText.setText(String.valueOf(hours));
            return true;
        } else {
            Log.e(TAG, "Could not decrement hoursText less than 0");
            return false;
        }
    }

    public boolean decrementMinutes() {
        if (mins >= 1) {
            mins--;
            minsText.setText(String.valueOf(mins));
            return true;
        } else {
            Log.e(TAG, "Could not decrement minutes less than 0");
            return false;
        }
    }

    public boolean decrementSeconds() {
        if (seconds >= 1) {
            seconds--;
            secondsText.setText(String.valueOf(seconds));
            return true;
        } else {
            Log.e(TAG, "Could not decrement secondsText less than 0");
            return false;
        }
    }

}
