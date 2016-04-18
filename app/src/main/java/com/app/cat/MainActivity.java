package com.app.cat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.app.cat.component.CaTServerListener;

import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneAuthInfo;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreFactory;
import org.linphone.core.LinphoneCoreListener;
import org.linphone.core.LinphoneProxyConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinphoneCore core;
        LinphoneProxyConfig proxyConfig;
        LinphoneAddress address;
        LinphoneCoreFactory factory;
        LinphoneAuthInfo authInfo;
        LinphoneCoreListener server;


        try {
            // Create an factory instance
            factory = LinphoneCoreFactory.instance();

            // SIP-Server listener implementation
            server = new CaTServerListener();

            core = factory.createLinphoneCore(server, null);

            // Username, Domain, DisplayName
            address = factory.createLinphoneAddress("Doe", "192.168.2.186", "Doe");

            // Combination from Username, Password and Domain stores password clear.
            authInfo = factory.createAuthInfo(
                    "Doe", // Username
                    "doe",  // Password
                    null, // Realm
                    "192.168.2.186"); // Domain

            // Create an proxy configuration class from core.
            proxyConfig = core.createProxyConfig();

            // Method enableRegister returns an new LinphoneProxyConfig back...
            proxyConfig = proxyConfig.enableRegister(true);
            proxyConfig.setIdentity("sip:Doe@192.168.2.186");
            proxyConfig.setAddress(address);
            proxyConfig.setProxy(address.getDomain());

            // Transports is an static class implementation from core.
            /*
            LinphoneCore.Transports transports = core.getSignalingTransportPorts();
            transports.tls = 0;
            transports.udp = 0;
            transports.tcp = 5060;
            core.setSignalingTransportPorts(transports);
            */

            // Append authentication information to core
            core.addAuthInfo(authInfo);
            core.addProxyConfig(proxyConfig);
            core.setDefaultProxyConfig(proxyConfig);

            boolean isRegistered = proxyConfig.isRegistered();

            // Example Registration
            while (!isRegistered) {
                core.iterate();
                isRegistered = proxyConfig.isRegistered();
            }

            // Example Unregistration
            proxyConfig.edit(); /*start editing proxy configuration*/
            proxyConfig.enableRegister(false); /*de-activate registration for this proxy config*/
            proxyConfig.done(); /*initiate REGISTER with expire = 0*/

            while (isRegistered) {
                core.iterate();
                isRegistered = proxyConfig.isRegistered();
            }

            Log.i("CaT", "Done");

        } catch (LinphoneCoreException e) {
            e.printStackTrace();
        }
    }
}
