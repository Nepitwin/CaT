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

package com.app.cat.ui.listener;

import android.os.Bundle;
import android.view.View;

import com.app.cat.client.CATClient;
import com.app.cat.model.CATFriend;
import com.app.cat.ui.CallActivity;
import com.app.cat.ui.adapter.TelephoneBookAdapter;
import com.app.cat.util.ApplicationContext;
import com.app.cat.util.PermissionManager;

/**
 * Video call listener implementation for an audio call click event.
 *
 * @author Andreas Sekulski, Dimitri Kotlovsky
 */
public class VideoCallListener implements View.OnClickListener {

    /**
     * Position which item was clicked.
     */
    private int position;

    /**
     * Model entity to get data.
     */
    private TelephoneBookAdapter adapter;

    /**
     * Cat client to communicate.
     */
    private CATClient client;

    /**
     * Audio call listener constructor to create an video call event.
     * @param position Position which model from adapter is clicked.
     * @param adapter Model adapter
     * @param client Client to start an video call.
     */
    public VideoCallListener(int position, TelephoneBookAdapter adapter, CATClient client) {
        this.position = position;
        this.adapter = adapter;
        this.client = client;
    }

    @Override
    public void onClick(View v) {

        CATFriend friend = adapter.getItem(position);

        // Try to call a friend
        if (!PermissionManager.havePermissions(PermissionManager.PERMISSIONS_AUDIO_CAMERA)) {
            PermissionManager.requestPermissions(
                    PermissionManager.PERMISSIONS_AUDIO_CAMERA,
                    PermissionManager.REQUEST_PERMISSIONS_OUTGOING_CALL);
            adapter.setCatFriend(friend);
            adapter.setVideoCall(true);
        } else {
            // Open Call Activity and call friend
            Bundle bundle = new Bundle();
            bundle.putInt(CallActivity.KEY_FRAGMENT_ID, CallActivity.FRAGMENT_OUTGOING_CALL);
            ApplicationContext.runIntentWithParams(ApplicationContext.ACTIVITY_CALL, bundle);
            client.callFriend(true, friend);
        }
    }
}
