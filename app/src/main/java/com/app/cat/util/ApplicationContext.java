package com.app.cat.util;

import android.app.Activity;
import android.content.Context;

/**
 * @author Andreas Sekulski
 *
 * Singleton utility class to get context from active activity.
 */
public class ApplicationContext {
    private static Activity gContext;

    public static void setContext( Activity activity) {
        gContext = activity;
    }

    public static Activity getActivity() {
        return gContext;
    }

    public static Context getContext() {
        return gContext;
    }
}
