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
import android.util.Log;
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
public class LinphoneCATRegistrationService extends Handler implements Runnable, VoIPService {

    /**
     * Constant interval to check registration status.
     */
    private static final long NOTIFY_INTERVAL = 1000;

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
        this.proxyConfig = proxyConfig;
        this.counter = 0;
        this.isRunning = false;
    }

    @Override
    public void run() {

        if(isRunning && !proxyConfig.isRegistered()) {
            if (counter >= 5) {
                // Show a special activity after 5 seconds and stop registration process
                // ToDo: New activity with sad cat
                ApplicationContext.showToast("Registration not working check your internet connectivity.", Toast.LENGTH_LONG);
                stop();
            } else {
                counter++;
                postDelayed(this, NOTIFY_INTERVAL);
            }
        } else if(proxyConfig.isRegistered()) {
            ApplicationContext.showToast("We are in.", Toast.LENGTH_LONG);
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
            post(this);
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}