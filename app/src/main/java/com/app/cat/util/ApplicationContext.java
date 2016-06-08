/*
 * This program is an Voice over IP client for Android devices.
 * Copyright (C) 2016 Andreas Sekulski, Dimitri Kotlovsky
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
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.app.cat.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.cat.ui.CallActivity;
import com.app.cat.ui.MainActivity;

/**
 * Static utility class to get context from active activity.
 *
 * @author Andreas Sekulski, Dimitri Kotlovsky
 */
public class ApplicationContext {

    /**
     * Main activity class.
     */
    public static Class ACTIVITY_MAIN = MainActivity.class;

    /**
     * Call activity class.
     */
    public static Class ACTIVITY_CALL = CallActivity.class;

    /**
     * Current activity which is running.
     */
    private static Activity activity;

    /**
     * Activity context.
     */
    private static Context context;

    /**
     * Sets the context from given activity.
     * @param gActivity Activity which will be set.
     */
    public static void setActivity(Activity gActivity) {
        activity = gActivity;
        context = activity.getApplicationContext();
    }

    /**
     * Call an new intent to show up on screen.
     * @param aClass Class to shown from application context.
     */
    public static void runIntent(Class aClass) {
        if(context != null) {
            Intent i = new Intent(context, aClass);
            activity.startActivity(i);
        }
    }

    /**
     * Call an new intent to show up on screen with an given bundle.
     * @param aClass Class to shown from application context.
     * @param bundle Bundle to set init params.
     */
    public static void runIntentWithParams(Class aClass, Bundle bundle) {
        if(context != null) {
            Intent i = new Intent(context, aClass);
            i.putExtras(bundle);
            activity.startActivity(i);
        }
    }

    /**
     * Returns the current activity.
     * @return current activity
     */
    public static Activity getCurrentActivity() {
        return activity;
    }

    /**
     * Closes current activity if it is not the MainActivity which should always be the last on
     * on the stack.
     */
    public static void closeCurrentActivity() {
        if (activity.getClass() != ACTIVITY_MAIN) {
            activity.finish();
        }
    }

    /**
     * Shows an toast with an given message.
     * @param message Message to show in an toast.
     * @param duration Visible duration from an toast.
     */
    public static void showToast(String message, int duration) {
        if(context != null) {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * Gets string from resource values.
     * @param id Id to get string.
     * @return Null if not exists otherwise an String.
     */
    public static String getStringFromRessources(int id) {
        String message;
        try {
            message = context.getResources().getString(id);
        } catch (Resources.NotFoundException ex) {
            message = null;
        }
        return message;
    }

    public static void adjustVolumeMAX() {
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        Log.v("Sound Current", "" + audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL));
        Log.v("Sound Max", "" + audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL));

        audioManager.setStreamVolume(
                AudioManager.STREAM_VOICE_CALL,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                AudioManager.FLAG_VIBRATE);

        Log.v("Sound Current", "" + audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL));
    }
}
