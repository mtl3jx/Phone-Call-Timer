package com.mluansing.phonecalltimer.modes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mluansing.phonecalltimer.R;

import java.text.DecimalFormat;

public class TimerFragment extends Fragment {

    private static final int HOURS = 15, MINS = 0, SECONDS = 0;

    TextView hours, mins, seconds;

    private static final DecimalFormat FORMATTER_TIME = new DecimalFormat("00");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, viewGroup, false);

        initializeTimerView(view);

        return view;
    }

    private void initializeTimerView(View view) {
        hours = view.findViewById(R.id.timer_hours);
        mins = view.findViewById(R.id.timer_minutes);
        seconds = view.findViewById(R.id.timer_seconds);

        hours.setText(getString(R.string.timer_number_value, String.valueOf(HOURS)));
        mins.setText(getString(R.string.timer_number_value, FORMATTER_TIME.format(MINS)));
        seconds.setText(getString(R.string.timer_number_value, FORMATTER_TIME.format(SECONDS)));
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

}
