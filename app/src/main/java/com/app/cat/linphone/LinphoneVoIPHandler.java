package com.app.cat.linphone;

import android.os.Handler;

import com.app.cat.client.VoIP;

import org.linphone.core.LinphoneCore;

/**
 * Class to create an background service on an android device to update VoIP calls.
 *
 * @author Andreas Sekulski
 */
public class LinphoneVoIPHandler implements Runnable, VoIP {

    /**
     * Constant interval to call updates from an SIP server in ms.
     */
    private static final long NOTIFY_INTERVAL = 100;

    /**
     * Handler to call this runnable periodically;
     */
    private Handler handler = new Handler();

    /**
     * Corresponding core object to handle Client Server communication.
     */
    private LinphoneCore core;

    /**
     * Boolean indicator to loop.
     */
    private boolean isRunning;

    /**
     * Creates an Voice over IP event handler to update periodically an SIP server status.
     */
    public LinphoneVoIPHandler(LinphoneCore core) {
        super();
        this.core = core;
        isRunning = false;
    }

    @Override
    public void run() {
        core.iterate();
        if(isRunning) {
            // Call runnable again after an NOTIFY_INTERVAL
            handler.postDelayed(this, NOTIFY_INTERVAL);
        }
    }

    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public void start() {
        if(!isRunning()) {
            isRunning = true;
            run();
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}