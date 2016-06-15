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
import android.os.Looper;

import com.app.cat.service.VoIPService;

import org.linphone.core.LinphoneCore;

/**
 * Class to create an background service on an android device to update VoIPService calls.
 *
 * @author Andreas Sekulski
 */
public class LinphoneCATVoIPService extends Handler implements Runnable, VoIPService {

    /**
     * Constant interval to call updates from an SIP server in ms.
     */
    private static final long NOTIFY_INTERVAL = 50;

    /**
     * Corresponding client object to handle Client Server communication.
     */
    private LinphoneCore core;

    /**
     * Boolean indicator to loop.
     */
    private boolean isRunning;

    /**
     * Creates an Voice over IP event handler to update periodically an SIP server status.
     *
     * @param core Core form cat client to update periodically.
     * @param looper Run a message loop for a thread.
     */
    public LinphoneCATVoIPService(LinphoneCore core, Looper looper) {
        super(looper);
        this.core = core;
        this.isRunning = false;
    }

    @Override
    public void run() {
        if(isRunning) {
            core.iterate();

            // Call runnable again after a NOTIFY_INTERVAL
            this.postDelayed(this, NOTIFY_INTERVAL);
        }
    }

    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public void start() {
        if(!isRunning()) {
            post(this);
            isRunning = true;
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}