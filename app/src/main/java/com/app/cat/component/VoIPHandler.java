package com.app.cat.component;

import android.os.Handler;
import android.util.Log;

import org.linphone.core.LinphoneCore;

/**
 * Class to create an background service on an android device to update VoIP calls.
 *
 * @author Andreas Sekulski
 */
public class VoIPHandler implements Runnable, VoIP {

    /**
     * Constant interval to call updates from an SIP server in ms.
     */
    private static final long NOTIFY_INTERVAL = 100;

    /**
     * Handler to call this runnable periodically;
     */
    private Handler handler = new Handler();

    /**
     * ToDo : Class implementation from Linphone to call updates.
     */
    private LinphoneCore core;

    /**
     * Boolean indicator to loop.
     */
    private boolean loop;

    /**
     * Creates an Voice over IP event handler to update periodically an SIP server status.
     */
    public VoIPHandler(LinphoneCore core) {
        super();
        this.core = core;
        loop = true;
    }

    @Override
    public void run() {
        core.iterate();

        if(loop) {
            // Call runnable again after an NOTIFY_INTERVAL
            handler.postDelayed(this, NOTIFY_INTERVAL);
        }
    }

    @Override
    public void stop() {
        loop = false;
    }

    @Override
    public void start() {
        loop = true;
        run();
    }
}