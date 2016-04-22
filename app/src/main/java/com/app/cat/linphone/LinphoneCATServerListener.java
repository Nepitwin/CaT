package com.app.cat.linphone;

import android.util.Log;

import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneAuthInfo;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCallStats;
import org.linphone.core.LinphoneChatMessage;
import org.linphone.core.LinphoneChatRoom;
import org.linphone.core.LinphoneContent;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreListener;
import org.linphone.core.LinphoneEvent;
import org.linphone.core.LinphoneFriend;
import org.linphone.core.LinphoneFriendList;
import org.linphone.core.LinphoneInfoMessage;
import org.linphone.core.LinphoneProxyConfig;
import org.linphone.core.PresenceModel;
import org.linphone.core.PublishState;
import org.linphone.core.SubscriptionState;

import java.nio.ByteBuffer;

/**
 * CaT communication server listener to respond on server messages.
 *
 * @author Andreas Sekulski
 */
public class LinphoneCATServerListener implements LinphoneCoreListener {

    @Override
    public void authInfoRequested(LinphoneCore linphoneCore, String s, String s1, String s2) {
        Log.i("Cat_Server", "--------------------------------");
        Log.i("Cat_Server", "authInfoRequested");
        Log.i("Cat_Server", s);
        Log.i("Cat_Server", s1);
        Log.i("Cat_Server", s2);
        Log.i("Login", "" + linphoneCore.getDefaultProxyConfig().isRegistered());
        Log.i("Cat_Server", "--------------------------------");
    }

    @Override
    public void callStatsUpdated(LinphoneCore linphoneCore, LinphoneCall linphoneCall, LinphoneCallStats linphoneCallStats) {
        Log.i("Cat_Server", "callStatsUpdated");
    }

    @Override
    public void newSubscriptionRequest(LinphoneCore linphoneCore, LinphoneFriend linphoneFriend, String s) {
        Log.i("Cat_Server", "newSubscriptionRequest");
    }

    @Override
    public void notifyPresenceReceived(LinphoneCore linphoneCore, LinphoneFriend linphoneFriend) {
        Log.i("Cat_Server", "--------------------------------");
        Log.i("Cat_Server", "notifyPresenceReceived");

        PresenceModel model = linphoneFriend.getPresenceModel();
        Log.i("Buddy basic status", linphoneFriend.getAddress() + "  ==>  " + model.getBasicStatus().name());
        Log.i("Buddy status", linphoneFriend.getAddress() + "  ==>  " + model.getActivity().getType().name());
        Log.i("Buddy statustext", linphoneFriend.getAddress() + "  ==>  " + model.getActivity().getDescription());
        if (model.getNote("en") != null) {
            Log.i("Buddy note", linphoneFriend.getAddress() + "  ==>  " + model.getNote("en").getContent());
        }
        Log.i("Cat_Server", "--------------------------------");
    }

    @Override
    public void dtmfReceived(LinphoneCore linphoneCore, LinphoneCall linphoneCall, int i) {
        Log.i("Cat_Server", "dtmfReceived");
    }

    @Override
    public void notifyReceived(LinphoneCore linphoneCore, LinphoneCall linphoneCall, LinphoneAddress linphoneAddress, byte[] bytes) {
        Log.i("Cat_Server", "notifyReceived");
    }

    @Override
    public void transferState(LinphoneCore linphoneCore, LinphoneCall linphoneCall, LinphoneCall.State state) {
        Log.i("Cat_Server", "transferState");
    }

    @Override
    public void infoReceived(LinphoneCore linphoneCore, LinphoneCall linphoneCall, LinphoneInfoMessage linphoneInfoMessage) {
        Log.i("Cat_Server", "infoReceived");
    }

    @Override
    public void subscriptionStateChanged(LinphoneCore linphoneCore, LinphoneEvent linphoneEvent, SubscriptionState subscriptionState) {
        Log.i("Cat_Server", "subscriptionStateChanged");
    }

    @Override
    public void publishStateChanged(LinphoneCore linphoneCore, LinphoneEvent linphoneEvent, PublishState publishState) {
        Log.i("Cat_Server", "--------------------------------");
        Log.i("Cat_Server", "publishStateChanged");
        Log.i("PublishStateChanged", linphoneEvent.getEventName());
        Log.i("Cat_Server", publishState.name());
        Log.i("Cat_Server", "--------------------------------");
    }

    @Override
    public void show(LinphoneCore linphoneCore) {
        Log.i("Cat_Server", "show");
    }

    @Override
    public void displayStatus(LinphoneCore linphoneCore, String s) {
        Log.i("Cat_Server", "--------------------------------");
        Log.i("Cat_Server", "displayStatus");
        Log.i("Cat_Server", s);
        Log.i("Cat_Server", "--------------------------------");
    }

    @Override
    public void displayMessage(LinphoneCore linphoneCore, String s) {
        Log.i("Cat_Server", "displayMessage");
    }

    @Override
    public void displayWarning(LinphoneCore linphoneCore, String s) {
        Log.i("Cat_Server", "displayWarning");
    }

    @Override
    public void fileTransferProgressIndication(LinphoneCore linphoneCore, LinphoneChatMessage linphoneChatMessage, LinphoneContent linphoneContent, int i) {
        Log.i("Cat_Server", "fileTransferProgressIndication");
    }

    @Override
    public void fileTransferRecv(LinphoneCore linphoneCore, LinphoneChatMessage linphoneChatMessage, LinphoneContent linphoneContent, byte[] bytes, int i) {
        Log.i("Cat_Server", "fileTransferRecv");
    }

    @Override
    public int fileTransferSend(LinphoneCore linphoneCore, LinphoneChatMessage linphoneChatMessage, LinphoneContent linphoneContent, ByteBuffer byteBuffer, int i) {
        return 0;
    }

    @Override
    public void globalState(LinphoneCore linphoneCore, LinphoneCore.GlobalState globalState, String s) {
        Log.i("Cat_Server", "globalState");
    }

    @Override
    public void registrationState(LinphoneCore linphoneCore, LinphoneProxyConfig linphoneProxyConfig, LinphoneCore.RegistrationState registrationState, String s) {
        Log.i("Cat_Server", "--------------------------------");
        Log.i("Cat_Server", "registrationState");

        LinphoneAuthInfo[] authInfos = linphoneCore.getAuthInfosList();
        for (LinphoneAuthInfo authInfo : authInfos) {
            Log.i("Cat_Server", "Username := " + authInfo.getUsername());
            Log.i("Cat_Server", "Password := " + authInfo.getPassword());
            Log.i("Cat_Server", "HA1 := " + authInfo.getHa1());
            Log.i("Cat_Server", "Domain := " + authInfo.getDomain());
            Log.i("Cat_Server", "Realm := " + authInfo.getRealm());
            Log.i("Cat_Server", "UserID := " + authInfo.getUserId());
        }
        Log.i("Cat_Server", "--------------------------------");
    }

    @Override
    public void configuringStatus(LinphoneCore linphoneCore, LinphoneCore.RemoteProvisioningState remoteProvisioningState, String s) {
        Log.i("Cat_Server", "configuringStatus");
    }

    @Override
    public void messageReceived(LinphoneCore linphoneCore, LinphoneChatRoom linphoneChatRoom, LinphoneChatMessage linphoneChatMessage) {
        Log.i("Cat_Server", "messageReceived");
    }

    @Override
    public void callState(LinphoneCore linphoneCore, LinphoneCall linphoneCall, LinphoneCall.State state, String s) {
        Log.i("Cat_Server", "callState");
    }

    @Override
    public void callEncryptionChanged(LinphoneCore linphoneCore, LinphoneCall linphoneCall, boolean b, String s) {
        Log.i("Cat_Server", "callEncryptionChanged");
    }

    @Override
    public void notifyReceived(LinphoneCore linphoneCore, LinphoneEvent linphoneEvent, String s, LinphoneContent linphoneContent) {
        Log.i("Cat_Server", "notifyReceived");
    }

    @Override
    public void isComposingReceived(LinphoneCore linphoneCore, LinphoneChatRoom linphoneChatRoom) {
        Log.i("Cat_Server", "isComposingReceived");
    }

    @Override
    public void ecCalibrationStatus(LinphoneCore linphoneCore, LinphoneCore.EcCalibratorStatus ecCalibratorStatus, int i, Object o) {
        Log.i("Cat_Server", "ecCalibrationStatus");
    }

    @Override
    public void uploadProgressIndication(LinphoneCore linphoneCore, int i, int i1) {
        Log.i("Cat_Server", "uploadProgressIndication");
    }

    @Override
    public void uploadStateChanged(LinphoneCore linphoneCore, LinphoneCore.LogCollectionUploadState logCollectionUploadState, String s) {
        Log.i("Cat_Server", "uploadStateChanged");
    }

    @Override
    public void friendListCreated(LinphoneCore linphoneCore, LinphoneFriendList linphoneFriendList) {
        Log.i("Cat_Server", "--------------------------------");
        Log.i("Cat_Server", "friendListCreated");
        for (LinphoneFriend friend : linphoneFriendList.getFriendList()) {
            Log.i("Cat_Server", "added buddy" + friend.getAddress());
        }
        Log.i("Cat_Server", "--------------------------------");
    }

    @Override
    public void friendListRemoved(LinphoneCore linphoneCore, LinphoneFriendList linphoneFriendList) {
        Log.i("Cat_Server", "friendListRemoved");
        Log.i("Cat_Server", "--------------------------------");
        Log.i("Cat_Server", "friendListRemoved");
        for (LinphoneFriend friend : linphoneFriendList.getFriendList()) {
            Log.i("Cat_Server", "removed buddy" + friend.getAddress());
        }
        Log.i("Cat_Server", "--------------------------------");
    }
}
