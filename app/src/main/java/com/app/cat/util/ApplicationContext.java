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

package com.app.cat.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.app.cat.ui.CallActivity;
import com.app.cat.ui.MainActivity;

/**
 * @author Andreas Sekulski
 *
 * Singleton utility class to get context from active activity.
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
     * Activity which is currently active.
     */
    private static Activity gContext;

    /**
     * Sets context from activity.
     * @param activity Activitiy which is currently showing.
     */
    public static void setContext( Activity activity) {
        gContext = activity;
    }

    /**
     * Gets activity which is currently active.
     * @return Activity which is open otherwise null if app is closed.
     */
    public static Activity getActivity() {
        return gContext;
    }

    /**
     * Get context from activity.
     * @return Application context from activity if is open otherwise null if app is closed.
     */
    public static Context getContext() {
        return gContext;
    }

    /**
     * Call an new intent to show up on screen.
     * @param aClass Class to shown from application context.
     */
    public static void runIntent(Class aClass) {
        Intent i = new Intent(gContext.getApplicationContext(), aClass);
        gContext.startActivity(i);
    }
}
