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
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.os.Process;

import com.app.cat.linphone.LinphoneCATClient;
import com.app.cat.linphone.LinphoneCATVoIPService;

import org.linphone.core.LinphoneCoreException;

/**
 * Service class implementation for an cat client.
 *
 * @author Andreas Sekulski
 */
public class CATService extends Service {

    private Looper looper;
    private ServiceHandler mServiceHandler;

    /**
     * Voice over IP service implementation for an client server communication.
     */
    private VoIPService voIPService;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler implements Runnable {

        private int id;



        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            id = msg.arg1;
            run();
        }

        @Override
        public void run() {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                Thread.sleep(5000);
                Log.v("CATService", "Call");
                this.postDelayed(this, 1000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job

            // Not good ... is runnable in thread?!...
            stopSelf(id);
        }
    }

 //   /**
    //    * Inner local binder class implementation for an cat servcie.
    //  *
    //  * @author Andreas Sekulski
    //  */
  //  public class LocalBinder extends Binder {
    ///**
         //      * Gets service class from cat service.
    //     * @return Returns service class implementation.
    //    */
/*        CATService getService() {
            return CATService.this;
        }
    }*/

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
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        looper = thread.getLooper();
        mServiceHandler = new ServiceHandler(looper);

/*
        try {
            voIPService = new LinphoneCATVoIPService(LinphoneCATClient.getInstance());
            voIPService.start();
        } catch (LinphoneCoreException e) {
            // ToDo := Error handling in Android UI... Everytime the same... Donuts...
            e.printStackTrace();
        }
        */
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /*
        if(voIPService.isRunning()) {
            voIPService.stop();
        }*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("CATService", "Received start id " + startId + ": " + intent);

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);


        // If we get killed, after returning from here, restart
        return START_STICKY;
    }
}
