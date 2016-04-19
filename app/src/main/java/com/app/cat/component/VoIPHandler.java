package com.app.cat.component;

import android.os.Handler;

import org.linphone.core.LinphoneCore;

/**
 * Class to create an background service on an android device to update VoIP calls.
 *
 * @author Andreas Sekulski
 */
public class VoIPHandler implements Runnable {

    /**
     * Constant interval to call updates from an SIP server in ms.
     */
    private static final long NOTIFY_INTERVAL = 1000;

    /**
     * Handler to call this runnable periodically;
     */
    private Handler handler = new Handler();

    /**
     * ToDo : Class implementation from Linphone to call updates.
     */
    private LinphoneCore core;

    /**
     * Creates an Voice over IP event handler to update periodically an SIP server status.
     */
    public VoIPHandler(LinphoneCore core) {
        super();
        this.core = core;
    }

    @Override
    public void run() {
        core.iterate();
        // Call runnable again after an NOTIFY_INTERVAL
        handler.postDelayed(this, NOTIFY_INTERVAL);
    }

    /**
     * Method to stop running handler.
     */
    public void stopHandler() {
        handler.removeCallbacks(this);
    }
}
