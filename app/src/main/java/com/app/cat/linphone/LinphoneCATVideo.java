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

import android.util.Log;
import android.widget.Toast;

import com.app.cat.R;
import com.app.cat.client.Multimedia;
import com.app.cat.model.CATFriend;
import com.app.cat.util.ApplicationContext;

import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCallParams;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreFactory;

/**
 * Audio call implementation class.
 *
 * @author Andreas Sekulski, Dimitri Kotlovsky
 */
public class LinphoneCATVideo implements Multimedia {

    /**
     * Linphone core implementation.
     */
    private LinphoneCore core;

    /**
     * Factory to create an LinphoneCore.
     */
    private LinphoneCoreFactory coreFactory;

    /**
     * Instance for an call.
     */
    private LinphoneCall call;

    /**
     * Determines whether this call is an incoming or outgoing call.
     */
    private boolean isIncomingCall;

    /**
     * Linphone CAT video call constructor.
     * @param core LinphoneCore
     * @param coreFactory Factory from linphone core.
     */
    public LinphoneCATVideo(LinphoneCore core, LinphoneCoreFactory coreFactory) {
        this.core = core;
        this.coreFactory = coreFactory;
        this.isIncomingCall = false;
    }

    /**
     * Linphone CAT audio call constructor.
     * @param core LinphoneCore
     * @param coreFactory Factory from linphone core.
     * @param call Incoming call to store.
     */
    public LinphoneCATVideo(LinphoneCore core, LinphoneCoreFactory coreFactory, LinphoneCall call) {
        this.core = core;
        this.coreFactory = coreFactory;
        this.call = call;
        this.isIncomingCall = true;
    }

    @Override
    public void callFriend(CATFriend catFriend) {

        // Set connection parameters
        LinphoneCallParams params = core.createCallParams(null);
        params.setVideoEnabled(true);
        //params.setAudioBandwidth(0); // disable limitation
        params.enableLowBandwidth(false);
        //params.enableRealTimeText(true);

        try {
            call = core.inviteAddressWithParams(coreFactory.createLinphoneAddress(
                    catFriend.getSIPAccount()), params);

            call.enableCamera(true);
            LinphoneCallParams params2 = call.getCurrentParamsCopy();
            Log.v("params", "video enabled: --> " + params2.getVideoEnabled());
            Log.v("params", "low bandwidth: --> " + params2.isLowBandwidthEnabled());
            Log.v("params", "real time text: --> " + params2.realTimeTextEnabled());
            Log.v("params", "video codec: --> " + params2.getUsedVideoCodec());

        } catch (LinphoneCoreException e) {
            ApplicationContext.showToast(
                    ApplicationContext.getStringFromRessources(R.string.unknown_error_message),
                    Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
    }

    @Override
    public void acceptCall() {

        LinphoneCallParams params = core.createCallParams(call);

        //boolean isLowBandwidthConnection = !LinphoneUtils.isHighBandwidthConnection(LinphoneService.instance().getApplicationContext());

        if (params != null)  {
            params.enableLowBandwidth(false);
            params.setVideoEnabled(true);
            //params.enableAudioMulticast(true);
            //params.setAudioBandwidth(40);
        } else {
            Log.v("Call", "Params not working");
        }

        if (params != null) {
            Log.v("params", "low bandwidth: --> " + params.isLowBandwidthEnabled());
            Log.v("params", "video enabled: --> " + params.getVideoEnabled());
            Log.v("params", "audio codec: --> " + params.getUsedAudioCodec());
        } else {
            Log.v("params", "BLACKKKK HAWKKKKK DOWNNNNNNNNNNN I REPEAT BLACK HAWK DOWN");
        }

        try {
            Log.v("Call", "Volume : " + call.getPlayVolume());
            core.acceptCallWithParams(call, params);
            call.enableCamera(true);
        } catch (LinphoneCoreException e) {
            ApplicationContext.showToast(
                    ApplicationContext.getStringFromRessources(R.string.unknown_error_message),
                    Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
    }

    @Override
    public void declineCall() {
        if(call != null) {
            core.terminateCall(call);
            call = null;
        }
    }

    @Override
    public boolean isIncomingCall() {
        return this.isIncomingCall;
    }
}