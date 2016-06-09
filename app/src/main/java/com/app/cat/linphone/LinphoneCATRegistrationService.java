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

import android.app.ProgressDialog;
import android.os.Handler;

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
     * Dialog to show an registration process.
     */
    private ProgressDialog dialog;

    /**
     * Maximum number of tries.
     */
    private static final int MAX_TRIES = 5;

    private boolean isVisibleTBook;

    /**
     * Creates an Voice over IP event handler to update periodically an SIP server status.
     */
    public LinphoneCATRegistrationService(LinphoneProxyConfig proxyConfig) {
        super();
        this.proxyConfig = proxyConfig;
        this.counter = 0;
        this.isRunning = false;
        this.isVisibleTBook = true;
    }

    @Override
    public void run() {
        if(isRunning && !proxyConfig.isRegistered()) {
            if (counter == MAX_TRIES) {
                dialog.dismiss();

                if(isVisibleTBook) {
                    isVisibleTBook = false;
                    ApplicationContext.sendResult(ApplicationContext.MAIN_ACTIVITY_CLASS,
                            ApplicationContext.KEY_SHOW_ERROR_MESSAGE, "No internet connection.");
                }
            }
        } else if(proxyConfig.isRegistered()) {

            // Dismiss progress dialog
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if(!isVisibleTBook) {
                ApplicationContext.sendResult(ApplicationContext.MAIN_ACTIVITY_CLASS,
                        ApplicationContext.KEY_HIDE_ERROR_MESSAGE, "");
                isVisibleTBook = true;
                counter = 0;
            }
        }

        // Increase counter
        if(counter < MAX_TRIES) {
            counter++;
        }

        postDelayed(this, NOTIFY_INTERVAL);
    }

    @Override
    public void stop() {
        counter = 0;
        isRunning = false;
    }

    @Override
    public void start() {
        if(!isRunning()) {
            dialog = ApplicationContext.showProgressDialog("Login to Server", "Bla blub");
            if(dialog != null) {
                dialog.show();
            }

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