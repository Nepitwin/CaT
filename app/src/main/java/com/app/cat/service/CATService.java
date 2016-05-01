package com.app.cat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.app.cat.linphone.LinphoneCATClient;
import com.app.cat.linphone.LinphoneCATVoIPService;

import org.linphone.core.LinphoneCoreException;

/**
 * Service class implementation for an cat client.
 *
 * @author Andreas Sekulski
 */
public class CATService extends Service {

    /**
     * Voice over IP service implementation for an client server communication.
     */
    private VoIPService voIPService;

    /**
     * Inner local binder class implementation for an cat servcie.
     *
     * @author Andreas Sekulski
     */
    public class LocalBinder extends Binder {
        /**
         * Gets service class from cat service.
         * @return Returns service class implementation.
         */
        CATService getService() {
            return CATService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            voIPService = new LinphoneCATVoIPService(LinphoneCATClient.getInstance().getCore());
            voIPService.start();
        } catch (LinphoneCoreException e) {
            // ToDo := Error handling in Android UI... Everytime the same... Donuts...
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(voIPService.isRunning()) {
            voIPService.stop();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("CATService", "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }
}
