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
import com.app.cat.model.CATFriend;
import com.app.cat.model.CATUser;
import com.app.cat.service.VoIPService;
import com.app.cat.util.ApplicationContext;

import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneAuthInfo;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCallParams;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreFactory;
import org.linphone.core.LinphoneCoreListener;
import org.linphone.core.LinphoneFriend;
import org.linphone.core.LinphoneProxyConfig;
import org.linphone.core.PayloadType;
import org.linphone.core.PresenceActivityType;
import org.linphone.core.PresenceModel;

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
     * User address Information.
     */
    private LinphoneAddress address;

    /**
     * CATUser information from user which contains registered SIP username, ha1 etc.
     */
    private LinphoneAuthInfo authInfo;

    /**
     * LinphoneCall variable to indicate if an call is set.
     */
    private LinphoneCall linphoneCall;

    /**
     * Cat server listener implementation to handle client server communication.
     */
    private LinphoneCoreListener catServerListener;

    /**
     * List from all friends to call.
     */
    private List<CATFriend> friendList;

    /**
     * Cat user from device.
     */
    private CATUser catUser;

    /**
     * CATFriend variable to indicate who is going to be called next.
     */
    private CATFriend catFriend;

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
        catServerListener = new LinphoneCATServerListener();

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
    public void setCATUser(CATUser catUser) {
        this.catUser = catUser;
        Log.v("Constructor", "Try registration");
        register();
    }

    @Override
    public void register() {
        if(catUser != null) {
            String username = catUser.getUsername();
            String domain = catUser.getDomain();
            String password = catUser.getPassword();
            String sip = catUser.getSIPAccount();

            try {
                core.clearProxyConfigs();
                core.clearAuthInfos();
                core.enableKeepAlive(true);

                address = coreFactory.createLinphoneAddress(username, domain, password);

                authInfo = coreFactory.createAuthInfo(
                        username, // Username
                        password,  // Password
                        null, // Realm
                        domain); // Domain

                // Enable config to register.
                proxyConfig = proxyConfig.enableRegister(true);

                proxyConfig.setIdentity(sip);
                proxyConfig.setAddress(address);
                proxyConfig.setProxy(address.getDomain());

                // Append CATUser information to core
                core.addAuthInfo(authInfo);
                core.addProxyConfig(proxyConfig);
                core.setDefaultProxyConfig(proxyConfig);

                registrationService.start();

                // ToDo: Registration Service is not creating a new thread properly
                // Start registration service
                //registrationService.start();

                // Wait until registration is finished.
                //while(!proxyConfig.isRegistered() && registrationService.isRunning()) {
               // while(!proxyConfig.isRegistered() ) { //&& registrationService.isRunning()) {
               //     updateServerInformation();
               // }

            } catch (LinphoneCoreException e) {
                ApplicationContext.showToast(
                        ApplicationContext.getStringFromRessources(R.string.unknown_error_message),
                        Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
        } else {
            ApplicationContext.showToast(
                    ApplicationContext.getStringFromRessources(R.string.unknown_error_message),
                    Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void unregister() {
        // Unregistration
        proxyConfig.edit(); // Start editing proxy configuration
        proxyConfig.enableRegister(false); // De-activate registration for this proxy config
        proxyConfig.done(); // Initiate REGISTER with expire = 0

        // Wait until unregistration is finished.
        //while(proxyConfig.isRegistered()) {
        //    updateServerInformation();
        //}
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
    public void addFriend(CATFriend catFriend) {

        // Generate sip URL from username and domain.
        String friendSIP = catFriend.getSIPAccount();

        try {

            // Debug-mode: clearing all friends
            for (LinphoneFriend friend : core.getFriendList()) {
                friend.edit(); // start editing friend
                friend.enableSubscribes(false); // disable subscription for this friend
                friend.done(); // commit changes triggering an UNSUBSCRIBE message
                core.removeFriend(friend);
                Log.i("Clear friends", "Friend is deleted ==> " + friend.getName());
            }/*
            for (LinphoneFriendList list : core.getFriendLists()) {
                Log.i("Clear friend lists", "List is deleted ==> " + list.toString());
                core.removeFriendList(list);
            }*/

            // Buddy list
            LinphoneFriend friend = coreFactory.createLinphoneFriend(friendSIP); /* creates friend object for buddy joe */
            friend.enableSubscribes(true); /* configure this friend to emit SUBSCRIBE message after being added to LinphoneCore */
            friend.setIncSubscribePolicy(LinphoneFriend.SubscribePolicy.SPAccept); /* accept Incoming subscription request for this friend */
            core.addFriend(friend); /* add my friend to the buddy list, initiate SUBSCRIBE message */

            // ToDo : Check what "LinphoneFriendList" is doing exactly. Probably saving a buddy list on the server.
            //LinphoneFriendList friendList = core.createLinphoneFriendList();
            //friendList.addFriend(friend);

        } catch (LinphoneCoreException e) {
            ApplicationContext.showToast(
                    ApplicationContext.getStringFromRessources(R.string.unknown_error_message),
                    Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
    }

    @Override
    public void enablePresenceStatus() {
        PresenceModel model = coreFactory.createPresenceModel();
        //model.setBasicStatus(PresenceBasicStatus.Open); // doesn't fuckig work ??!!!?
        model.setActivity(PresenceActivityType.Online, "I'm busy asshole."); // Zoiper doesn't understand activities !!!
        model.addNote("Away", "en"); // But Zoiper is able to read notes ;)
        core.setPresenceModel(model);
        proxyConfig.edit();
        proxyConfig.enablePublish(true);
        proxyConfig.done();
    }

    @Override
    public void disablePresenceStatus() {
        PresenceModel model = core.getPresenceModel();
        model.clearNotes();
        model.clearActivities();
        model.setActivity(PresenceActivityType.Offline, "offline");
        core.setPresenceModel(model);
    }

    @Override
    public void callFriend() {
        if(catFriend != null) {
            LinphoneCallParams params = core.createCallParams(null);
            params.setVideoEnabled(false);
            params.enableLowBandwidth(false);
            //params.setAudioBandwidth(40);
            try {
                setLinphoneCall(core.inviteAddressWithParams(coreFactory.
                        createLinphoneAddress(catFriend.getSIPAccount()), params));
//                setLinphoneCall(core.invite(coreFactory.
//                        createLinphoneAddress(catFriend.getSIPAccount())));
            } catch (LinphoneCoreException e) {
                ApplicationContext.showToast(
                        ApplicationContext.getStringFromRessources(R.string.unknown_error_message),
                        Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setFriendToCall(CATFriend catFriend) {
        this.catFriend = catFriend;
    }

    @Override
    public void acceptCall() {
        if(linphoneCall != null) {

            LinphoneCallParams params = core.createCallParams(linphoneCall);

            //boolean isLowBandwidthConnection = !LinphoneUtils.isHighBandwidthConnection(LinphoneService.instance().getApplicationContext());

            if(params != null)  {
                params.enableLowBandwidth(false);
                //params.enableAudioMulticast(true);
                //params.setAudioBandwidth(40);
            } else {
                Log.v("Call", "Params not working");
            }

            if(params != null) {
                Log.v("params", "PARAAMAMMMMMMMMMMMMMMMMMMMMMSSSSSSSSSSSSSSSS FOUNDDDDDDDDDD SCOTTTTYYYYYY");

                Log.v("params", "media encryption: --> " + params.getMediaEncryption().toString());
                Log.v("params", "audio codec: --> " + params.getUsedAudioCodec());

            } else {
                Log.v("params", "BLACKKKK HAWKKKKK DOWNNNNNNNNNNN I REPEAT BLACK HAWK DOWN");
            }


            try {
                Log.v("Call", "Volume : " + linphoneCall.getPlayVolume());
                core.acceptCallWithParams(linphoneCall, params);
            } catch (LinphoneCoreException e) {
                ApplicationContext.showToast(
                        ApplicationContext.getStringFromRessources(R.string.unknown_error_message),
                        Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void declineCall() {
        if(linphoneCall != null) {
            core.terminateCall(linphoneCall);
            linphoneCall = null;
        }

        if (catFriend != null) {
            catFriend = null;
        }
    }

    @Override
    public LinphoneFriend getLinphoneFriend(CATFriend catFriend) {
        return core.findFriendByAddress(catFriend.getSIPAccount());
    }

    /**
     * Gets core from linphone.
     * @return Null if core not initialized otherwise an linphone core will be returned.
     */
    public LinphoneCore getCore() {
        return core;
    }

    /**
     * Sets a linphone call.
     * @param call Call to set.
     */
    public void setLinphoneCall(LinphoneCall call) {
        linphoneCall = call;
    }
}
