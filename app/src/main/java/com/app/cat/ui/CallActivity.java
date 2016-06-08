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

package com.app.cat.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.app.cat.R;
import com.app.cat.client.CATClient;
import com.app.cat.linphone.LinphoneCATClient;
import com.app.cat.ui.fragment.CallFragment;
import com.app.cat.ui.fragment.IncomingCallFragment;
import com.app.cat.ui.listener.CallFragmentListener;
import com.app.cat.ui.listener.IncomingCallFragmentListener;
import com.app.cat.util.ApplicationContext;
import com.app.cat.util.PermissionManager;

import org.linphone.core.LinphoneCoreException;

import butterknife.ButterKnife;

/**
 * The Call Activity represents the user interface for outgoing or incoming calls.
 *
 * @author Andreas Sekulski, Dimitri Kotlovsky
 */
public class CallActivity extends AppCompatActivity implements CallFragmentListener,
        IncomingCallFragmentListener, ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * The Fragment Transaction object supports the replacement of fragments.
     */
    FragmentTransaction fragmentTransaction;

    public static final int FRAGMENT_CALL = 1;

    public static final int FRAGMENT_INCOMING_CALL = 2;

    public static final String KEY_FRAGMENT_ID = "FRAGMENT";

    private boolean grantedPermissions = false;

    int fragment;

    /**
     * Cat client instance.
     */
    private CATClient client;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        // Set UI application context
        ApplicationContext.setActivity(this);

        try {
            client = LinphoneCATClient.getInstance();
        } catch (LinphoneCoreException e) {
            ApplicationContext.showToast(
                    ApplicationContext.getStringFromRessources(R.string.unknown_error_message),
                    Toast.LENGTH_SHORT);
        }

        Bundle bundle = getIntent().getExtras();
        fragment = bundle.getInt(KEY_FRAGMENT_ID);

        // Load the right fragment
        if (savedInstanceState == null) {
            if(fragment == FRAGMENT_CALL) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction().
                        add(R.id.callUIContainer, new CallFragment());
            } else if(fragment == FRAGMENT_INCOMING_CALL) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction().
                        add(R.id.callUIContainer, new IncomingCallFragment());
            } else {
                ApplicationContext.showToast(
                        ApplicationContext.getStringFromRessources(R.string.unknown_error_message),
                        Toast.LENGTH_SHORT);
            }
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Try to call the chosen friend if the activity is opened with the CallFragment
        if(fragment == FRAGMENT_CALL) {
            // Try to call a friend
            if (!PermissionManager.hasPermission(PermissionManager.PERMISSION_RECORD_AUDIO)) {
                PermissionManager.requestPermission(
                        PermissionManager.PERMISSION_RECORD_AUDIO,
                        PermissionManager.REQUEST_PERMISSIONS_OUTGOING_CALL);
            } else {
                client.callFriend();
            }
        }
    }

    @Override
    public void onAcceptCall() {
        // Request permission to record audio before accepting the call
        if (!PermissionManager.hasPermission(PermissionManager.PERMISSION_RECORD_AUDIO)) {
            PermissionManager.requestPermission(
                    PermissionManager.PERMISSION_RECORD_AUDIO,
                    PermissionManager.REQUEST_PERMISSIONS_INCOMING_CALL);

        // Permission is already granted - accept the call
        } else {
            switchFragments();
            client.acceptCall();
        }
    }

    @Override
    public void onDeclineCall() {
        client.declineCall();
    }

    @Override
    public void onHangUp() {
        onDeclineCall();
    }

    /**
     * Switches from {@link IncomingCallFragment} to {@link CallFragment} because user accepted the
     * call.
     */
    private void switchFragments() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction().
                replace(R.id.callUIContainer, new CallFragment());
        fragmentTransaction.commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        // Workaround for handling FragmentTransactions due to callbacks instead of user interaction
        if (grantedPermissions) {
            switchFragments();
        } else {
            grantedPermissions = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Process request results
        switch (requestCode) {
            case PermissionManager.REQUEST_PERMISSIONS_INCOMING_CALL: {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.length > 0)
                    && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    grantedPermissions = true;
                    client.acceptCall(); // ToDo: What happens, if meanwhile the call timed out?
                } else {
                    onDeclineCall();
                    PermissionManager.firstPermissionRequest = false;
                    ApplicationContext.showToast(ApplicationContext.getStringFromRessources(
                            R.string.permission_denied),
                            Toast.LENGTH_LONG);
                }
                return;
            }
            case PermissionManager.REQUEST_PERMISSIONS_OUTGOING_CALL: {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.length > 0)
                    && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    client.callFriend();
                } else {
                    client.setFriendToCall(null);
                    finish(); // close activity //ToDo: final??
                    PermissionManager.firstPermissionRequest = false;
                    ApplicationContext.showToast(ApplicationContext.getStringFromRessources(
                            R.string.permission_denied),
                            Toast.LENGTH_LONG);
                }
                return;
            }
        }
    }
}
