package com.app.cat.linphone;

import android.util.Log;

import com.app.cat.client.CATClient;
import com.app.cat.client.CATException;
import com.app.cat.model.CATFriend;
import com.app.cat.model.CATUser;

import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneAuthInfo;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreFactory;
import org.linphone.core.LinphoneCoreListener;
import org.linphone.core.LinphoneFriend;
import org.linphone.core.LinphoneFriendList;
import org.linphone.core.LinphoneProxyConfig;
import org.linphone.core.PresenceActivityType;
import org.linphone.core.PresenceModel;

/**
 * Singleton LinphoneCATClient implementation to handle all SIP Client <-> Server communication.
 *
 * @author Andreas Sekulski, Dimitry Kotlovsky
 */
public class LinphoneCATClient implements CATClient {

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
     * Cat server listener implementation to handle client server communication.
     */
    private LinphoneCoreListener catServerListener;

    /**
     * Static instance from LinphoneCATClient
     */
    private static LinphoneCATClient instance;

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

        // Create an proxy configuration class from core.
        proxyConfig = core.createProxyConfig();
    }

    @Override
    public void register(CATUser catUser) throws CATException {

        Log.i("Clicked", "Login motherfucker");

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

            // ToDo : Check if addAuthInfo and ProxyConfig must be deleted.
            // Append CATUser information to core
            core.addAuthInfo(authInfo);
            core.addProxyConfig(proxyConfig);
            core.setDefaultProxyConfig(proxyConfig);
        } catch (LinphoneCoreException e) {
            throw new CATException(e.getMessage());
        }
    }

    @Override
    public void unregister() {
        Log.i("Clicked", "Logout motherfucker");

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
    public void addFriend(CATFriend catFriend) throws CATException {

        // Generate sip URL from username and domain.
        String friendSIP = catFriend.getSIPAccount();

        try {
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
            core.addFriend(friend);

            // ToDo : Check what "LinphoneFriendList" is doing exactly. Probably saving a buddy list on the server.
            LinphoneFriendList friendList = core.createLinphoneFriendList();
            friendList.addFriend(friend);

        } catch (LinphoneCoreException e) {
            throw new CATException(e.getMessage());
        }
    }

    @Override
    public void enablePresenceStatus() {
        PresenceModel model = coreFactory.createPresenceModel();
        //model.setBasicStatus(PresenceBasicStatus.Open); // doesn't fuckig work ??!!!?
        model.setActivity(PresenceActivityType.Busy, "I'm busy asshole."); // Zoiper doesn't understand activities !!!
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

    /**
     * Gets core from linphone.
     * @return Null if core not initialized otherwise an linphone core will be returned.
     */
    public LinphoneCore getCore() {
        return core;
    }
}
