package com.mluansing.phonecalltimer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TimerFragment extends Fragment {

    TextView hours, mins, seconds;

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

        hours.setText(getString(R.string.timer_number_value, 12));
        mins.setText(getString(R.string.timer_number_value, 19));
        seconds.setText(getString(R.string.timer_number_value, 15));
    }

}
