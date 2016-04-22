package com.app.cat.util;

import android.app.Activity;
import android.content.Context;

/**
 * @author Andreas Sekulski
 *
 * Singleton utility class to get context from active activity.
 */
public class ApplicationContext {

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
}
