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

package com.app.cat.service;

import android.app.Service;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.widget.Toast;

import com.app.cat.R;
import com.app.cat.linphone.LinphoneCATClient;
import com.app.cat.linphone.LinphoneCATVoIPService;
import com.app.cat.util.ApplicationContext;

import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;

/**
 * Service class implementation for an cat client.
 *
 * @author Andreas Sekulski
 */
public class CATService extends Service {

    /**
     * Thread id for an service which is running.
     */
    private int threadID;

    /**
     * Voice over IP service implementation for an client server communication.
     */
    private VoIPService voIPService;

    /**
     * Default thread name from service. CAT communication system service
     */
    private static final String THREAD_NAME = "CATComService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread(THREAD_NAME, Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        try {
            // Get the HandlerThread's Looper and use it for our Handler
            LinphoneCore core = LinphoneCATClient.getInstance().getCore();
            voIPService = new LinphoneCATVoIPService(core, thread.getLooper());
        } catch (LinphoneCoreException e) {
            ApplicationContext.showToast(
                    ApplicationContext.getStringFromRessources(R.string.unknown_error_message),
                    Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Stop service thread
        if(voIPService.isRunning()) {
            voIPService.stop();
        }

        stopSelf(threadID);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Set thread id
        threadID = startId;

        // Run service handler
        voIPService.start();

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }
}
