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
     * Permissions constant for recording audio and accessing the camera.
     */
    public static final String[] PERMISSIONS_AUDIO_CAMERA = new String[]{
            PermissionManager.PERMISSION_RECORD_AUDIO,
            PermissionManager.PERMISSION_CAMERA};

    /**
     * Permissions constant for recording audio, accessing the camera and reading contacts.
     */
    public static final String[] PERMISSION_AUDIO_CAMERA_CONTACTS = new String[]{
            PermissionManager.PERMISSION_RECORD_AUDIO,
            PermissionManager.PERMISSION_READ_CONTACTS,
            PermissionManager.PERMISSION_CAMERA};

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
     * Returns <code>true</code> if the given permissions are granted for the application.
     * @param permissions permissions to be checked
     * @return <code>true</code> if the given permissions are granted for the application
     */
    public static boolean havePermissions(String[] permissions) {
        boolean havePermissions = true;
        for(int i = 0; (i < permissions.length) && havePermissions; i++) {
            havePermissions = ContextCompat.checkSelfPermission(ApplicationContext
                    .getCurrentActivity(), permissions[i]) == PackageManager.PERMISSION_GRANTED;
        }
        return havePermissions;
    }

    /**
     * Requests the given permissions from the user. Callback is send to the current activity
     * bind to the given result constant.
     * @param permissions permissions to be requested
     * @param resultConstant result constant to which the callback is bind
     */
    public static void requestPermissions(String[] permissions, int resultConstant) {

        // Should we show an explanation to the user?
        boolean shouldShowRequestPermissionRationale = false;
        for(int i = 0; (i < permissions.length) && !shouldShowRequestPermissionRationale; i++) {
            shouldShowRequestPermissionRationale = ActivityCompat
                    .shouldShowRequestPermissionRationale(ApplicationContext
                            .getCurrentActivity(), permissions[i]);
        }

        if (shouldShowRequestPermissionRationale) {
            /* ToDo: Show explanation dialog for permissions instead of a toast message
               Show an explanation to the user *asynchronously* -- don't block
               this thread waiting for the user's response! After the user
               sees the explanation, try again to request the permission. */
            ApplicationContext.showToast(
                    ApplicationContext.getStringFromRessources(R.string.permission_denied),
                    Toast.LENGTH_LONG);
            ActivityCompat.requestPermissions(ApplicationContext.getCurrentActivity(),
                    permissions, resultConstant);

        } else {

            // The user is prompted the first time
            if (firstPermissionRequest) {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(ApplicationContext.getCurrentActivity(),
                        permissions, resultConstant);

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
