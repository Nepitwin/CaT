/*
 * Copyright (c) 2016.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.app.cat.ui.component;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.cat.R;

/**
 * Clock timer ui component to create an timer task.
 *
 * @author Andreas Sekulski
 */
public class ClockTimer extends LinearLayout {

    /**
     * Starting time from clock timer.
     */
    private long startTime;

    /**
     * Handler to call an thread to update ui.
     */
    private Handler handler = new Handler();

    /**
     * Default timer format 00:00:00 from clock counter.
     */
    private static String DEFAULT_TIMER_FORMAT = "%02d:%02d:%02d";

    /**
     * Update interval in ms.
     */
    private static int UPDATE_INTERVALL = 1000;

    /**
     * Textview counter to update counter interval.
     */
    private TextView counter;

    /**
     * Indicator if clock is running.
     */
    private boolean isRunning;

    /**
     * Default constructor to create clock timer.
     * @param context Context
     */
    public ClockTimer(Context context) {
        super(context);
        init(context);
        startTime = 0L;
    }

    /**
     * Default constructor to create clock timer.
     * @param context Context
     * @param attrs Attributes to set
     */
    public ClockTimer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Default constructor to create clock timer.
     * @param context Context
     * @param attrs Attributes to set
     * @param defStyleAttr Definition style attributes
     */
    public ClockTimer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Runs timer if not already running.
     */
    public void runTimer() {
        if(!isRunning) {
            startTime = SystemClock.uptimeMillis();
            handler.post(updateTimerThread);
            isRunning = true;
        }
    }

    /**
     * Stops timer if runs.
     */
    public void stopTimer() {
        if(isRunning) {
            handler.removeCallbacks(updateTimerThread);
             isRunning = false;
        }

    }

    /**
     * Runnable class to run an update timer thread.
     */
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            long timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            int secs = (int) (timeInMilliseconds / UPDATE_INTERVALL);
            int min = secs / 60;
            int hour = min / 60;

            hour = hour % 100;
            min = min % 60;
            secs = secs % 60;

            counter.setText(String.format(DEFAULT_TIMER_FORMAT, hour, min, secs));

            handler.postDelayed(this, UPDATE_INTERVALL);
        }
    };

    /**
     * Initialization for all objects.
     *
     * @param context Context.
     */
    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.clock_layout, this, true);
        counter = (TextView) view.findViewById(R.id.clockValue);
        isRunning = false;
    }
}
