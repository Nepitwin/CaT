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

package com.app.cat.linphone;

import android.os.Handler;
import android.widget.Toast;

import com.app.cat.R;
import com.app.cat.service.VoIPService;
import com.app.cat.util.ApplicationContext;

import org.linphone.core.LinphoneProxyConfig;

/**
 * Class to create an background service on an android device to wait for the completion of the
 * server registration.
 *
 * @author Dimitri Kotlovsky, Andreas Sekulski
 */
public class LinphoneCATRegistrationService implements Runnable, VoIPService {

    /**
     * Constant interval to check registration status.
     */
    private static final long NOTIFY_INTERVAL = 1000;

    /**
     * Handler to call this runnable periodically;
     */
    private Handler handler;

    /**
     * Boolean indicator to loop.
     */
    private boolean isRunning;

    /**
     * LinphoneProxyConfig to check registration status.
     */
    private LinphoneProxyConfig proxyConfig;

    /**
     * Counter for counting the seconds while trying to register.
     */
    private int counter;

    /**
     * Creates an Voice over IP event handler to update periodically an SIP server status.
     */
    public LinphoneCATRegistrationService(LinphoneProxyConfig proxyConfig) {
        super();
        this.handler = new Handler();
        this.proxyConfig = proxyConfig;
        this.counter = 0;
        this.isRunning = false;
    }

    @Override
    public void run() {
        if(isRunning) {
            // Call runnable again after a NOTIFY_INTERVAL if registration didn't work
            if (!proxyConfig.isRegistered()) {
                counter++;
                handler.postDelayed(this, NOTIFY_INTERVAL);

                // Show a notification after 3 seconds of unsuccessful registration tries
                if (counter == 3) {
                    ApplicationContext.showToast(ApplicationContext.getStringFromRessources(
                            R.string.wait_for_registration), Toast.LENGTH_LONG);

                    // Show a special activity after 5 seconds and stop registration process
                } else if (counter >= 5) {
                    // ToDo: New activity with sad cat
                    ApplicationContext.showToast("STOP", Toast.LENGTH_LONG);
                    stop();
                }
            }
        }
    }

    @Override
    public void stop() {
        counter = 0;
        isRunning = false;
    }

    @Override
    public void start() {
        if(!isRunning()) {
            isRunning = true;
            counter = 0;
            run();
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}