package com.app.cat.util;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.app.cat.R;

/**
 * Static utility class to request system permissions.
 *
 * @author Dimitri Kotlovsky
 */
public class PermissionManager {

    /**
     * Permission constant for recording audio.
     */
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;

    /**
     * Permission constant for reading contacts.
     */
    public static final String PERMISSION_READ_CONTACTS = Manifest.permission.READ_CONTACTS;

    /**
     * Permission constant for accessing the camera.
     */
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;

    /**
     * Result constant for requesting permissions for an incoming call.
     */
    public static final int REQUEST_PERMISSIONS_INCOMING_CALL = 4201;

    /**
     * Result constant for requesting permissions for an outgoing call.
     */
    public static final int REQUEST_PERMISSIONS_OUTGOING_CALL = 4202;

    /**
     * Result constant for requesting permissions to update the contacts list.
     */
    public static final int REQUEST_PERMISSIONS_UPDATE_CONTACTS = 4203;

    /**
     * Flag to determine whether the user was already prompted for permission or not.
     */
    public static boolean firstPermissionRequest = true;

    /**
     * Returns <code>true</code> if the given permission is granted for the application.
     * @param permission permission to be checked
     * @return <code>true</code> if the given permission is granted for the application
     */
    public static boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(ApplicationContext.getCurrentActivity(),
                permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requests the given permission from the user. Callback is send to the current activity bind to
     * the given result constant.
     * @param permission permission to be requested
     * @param resultConstant result constant to which the callback is bind
     */
    public static void requestPermission(String permission, int resultConstant) {
        // Should we show an explanation to the user?
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                ApplicationContext.getCurrentActivity(), permission)) {

            /* ToDo: Show explanation dialog for permissions instead of a toast message
               Show an explanation to the user *asynchronously* -- don't block
               this thread waiting for the user's response! After the user
               sees the explanation, try again to request the permission. */
            ApplicationContext.showToast(
                    ApplicationContext.getStringFromRessources(R.string.permission_denied),
                    Toast.LENGTH_LONG);
            ActivityCompat.requestPermissions(ApplicationContext.getCurrentActivity(),
                    new String[]{permission}, resultConstant);

        } else {

            // The user is prompted the first time
            if (firstPermissionRequest) {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(ApplicationContext.getCurrentActivity(),
                        new String[]{permission}, resultConstant);

            // The user has clicked "Never ask again" on the last permission dialog
            } else {

                // ToDo: This is a non functional workaround to show what is still to do here
                ApplicationContext.showToast(
                        ApplicationContext.getStringFromRessources(R.string.permission_denied),
                        Toast.LENGTH_LONG);
            }
        }
    }
}
