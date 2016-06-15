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
import com.app.cat.client.CATClient;
import com.app.cat.client.Multimedia;
import com.app.cat.model.CATFriend;
import com.app.cat.model.CATUser;
import com.app.cat.service.VoIPService;
import com.app.cat.util.ApplicationContext;

import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneAuthInfo;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreFactory;
import org.linphone.core.LinphoneCoreListener;
import org.linphone.core.LinphoneProxyConfig;
import org.linphone.core.PayloadType;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton LinphoneCATClient implementation to handle all SIP Client <-> Server communication.
 *
 * @author Andreas Sekulski, Dimitri Kotlovsky
 */
public class LinphoneCATClient implements CATClient {

    /**
     * Registration expiration time.
     */
    private static final int REGISTRATION_EXPIRATION_TIME = 2000;

    /**
     * Linphone core implementation.
     */
    private LinphoneCore core;

    /**
     * Factory to create an LinphoneCore.
     */
    private LinphoneCoreFactory coreFactory;

    /**
     * Proxy configuration from an SIP Server.
     */
    private LinphoneProxyConfig proxyConfig;

    /**
     * List from all friends to call.
     */
    private List<CATFriend> friendList;

    /**
     * Multimedia object to create video or audio calls.
     */
    private Multimedia multimedia;

    /**
     * Static instance from LinphoneCATClient
     */
    private static LinphoneCATClient instance;

    /**
     * Registration service.
     */
    private VoIPService registrationService;

    /**
     * Get instance method to create an singleton if nox already exists.
     *
     * @return Singleton LinphoneCATClient object.
     * @throws LinphoneCoreException Throws an LinphoneCoreException if instance creation failed.
     */
    public static LinphoneCATClient getInstance() throws LinphoneCoreException {

        if(instance == null) {
            instance = new LinphoneCATClient();
        }

        return instance;
    }

    /**
     * Default constructor to create an LinphoneCATClient singleton.
     */
    private LinphoneCATClient() throws LinphoneCoreException {

        // Create an factory instance
        coreFactory = LinphoneCoreFactory.instance();

        LinphoneCoreListener catServerListener = new LinphoneCATServerListener();
        core = coreFactory.createLinphoneCore(catServerListener, null);


        core.enableAdaptiveRateControl(false);
        for(PayloadType type : core.getAudioCodecs()) {
            core.enablePayloadType(type, false);
            if (type.getMime().equals("GSM")) {
                core.enablePayloadType(type, true);
                core.setPayloadTypeBitrate(type, 100);
            }
        }

        for(PayloadType type : core.getAudioCodecs()) {
            Log.v("params", type.getMime());
            Log.v("params", type.getRate() + "");
            Log.v("is enabled", "" + core.isPayloadTypeEnabled(type));
        }


        // Create an proxy configuration class from core.
        proxyConfig = core.createProxyConfig();
        proxyConfig.setExpires(REGISTRATION_EXPIRATION_TIME); // sets the registration expiration time

        // Starts the registration service
        registrationService = new LinphoneCATRegistrationService(proxyConfig);
    }

    @Override
    public void addCATFriend(CATFriend catFriend) {
        if(friendList == null) {
            friendList = new ArrayList<>();
        }

        friendList.add(catFriend);
    }

    @Override
    public void register(CATUser catUser) {
        String username = catUser.getUsername();
        String domain = catUser.getDomain();
        String password = catUser.getHA1Password();
        String sip = catUser.getSIPAccount();

        try {
            core.clearProxyConfigs();
            core.clearAuthInfos();
            core.enableKeepAlive(true);

            LinphoneAddress address = coreFactory.createLinphoneAddress(username, domain, password);

            LinphoneAuthInfo authInfo = coreFactory.createAuthInfo(
                    username, // Username
                    password,  // Password
                    null, // Realm
                    domain);

            // Enable config to register.
            proxyConfig = proxyConfig.enableRegister(true);

            proxyConfig.setIdentity(sip);
            proxyConfig.setAddress(address);
            proxyConfig.setProxy(address.getDomain());

            // Append CATUser information to core
            core.addAuthInfo(authInfo);
            core.addProxyConfig(proxyConfig);
            core.setDefaultProxyConfig(proxyConfig);

            // Start registration service
            registrationService.start();

        } catch (LinphoneCoreException e) {
            ApplicationContext.showToast(
                    ApplicationContext.getStringFromRessources(R.string.unknown_error_message),
                    Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
    }

    @Override
    public void unregister() {
        // Unregistration
        proxyConfig.edit(); // Start editing proxy configuration
        proxyConfig.enableRegister(false); // De-activate registration for this proxy config
        proxyConfig.done(); // Initiate REGISTER with expire = 0
    }

    @Override
    public void setTransportType(int udp, int tcp, int tls) {
        // Transports is an static class implementation from core.
        LinphoneCore.Transports transports = core.getSignalingTransportPorts();
        transports.tls = tls;
        transports.udp = udp;
        transports.tcp = tcp;
        core.setSignalingTransportPorts(transports);
    }

    @Override
    public void callFriend(boolean isVideoCall, CATFriend catFriend) {
        if(isVideoCall) {
            // ToDo Multimedia implementation for video call.
        } else {
            multimedia = new LinphoneCATAudio(core, coreFactory);
        }

        multimedia.callFriend(catFriend);
    }

    @Override
    public void acceptCall() {
        if(multimedia != null) {
            multimedia.acceptCall();
        }
    }

    @Override
    public void declineCall() {
        if(multimedia != null) {
            multimedia.declineCall();
            multimedia = null;
        }
    }

    @Override
    public Boolean isIncomingCall() {
        if (multimedia != null) {
            return multimedia.isIncomingCall();
        }

        return null;
    }

    /**
     * Sets an incoming call.
     * @param call Call to set.
     */
    public void incomingCall(boolean isVideoCall, LinphoneCall call) {
        if(isVideoCall) {
            // ToDo : Video call
        } else {
            multimedia = new LinphoneCATAudio(core, coreFactory, call);
        }
    }

    /**
     * Gets core from linphone.
     * @return Null if core not initialized otherwise an linphone core will be returned.
     */
    public LinphoneCore getCore() {
        return core;
    }
}
