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
import org.linphone.core.PayloadType;

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

            address = factory.createLinphoneAddress("sip:doe@192.168.2.186");
            address.setTransport(LinphoneAddress.TransportType.LinphoneTransportTcp);

            // ToDo : HA1 Password will be generated from password
            // Store password Information from SIP Server as an HA1 password
            // Information about HA1 := http://www.opensips.org/Documentation/TipsFAQ
            authInfo = factory.createAuthInfo(
                    "Doe", // Username
                    null, // Userid for authetication digest (HA1)
                    "doe",  // Password
                    null, // HA1 --> md5(concat(username, ':', domain, ':', password)),
                    "192.168.2.186", // Realm
                    "192.168.2.186"); // Domain

            // Create an proxy configuration class from core.
            proxyConfig = core.createProxyConfig();

            // Method enableRegister returns an new LinphoneProxyConfig back...
            proxyConfig = proxyConfig.enableRegister(true);
            proxyConfig.setIdentity("sip:doe@192.168.2.186");
            proxyConfig.setAddress(address);
            proxyConfig.setProxy(address.getDomain());

            // Append authentication information to core
            core.addAuthInfo(authInfo);
            core.addProxyConfig(proxyConfig);
            core.setDefaultProxyConfig(proxyConfig);
            core.iterate();

            // Check if authentication was successfully...
            if(proxyConfig.isRegistered()) {
                Log.i("CaT", "Successfully log in to sip server");
            } else {
                Log.i("CaT", ":-(");
            }

        } catch (LinphoneCoreException e) {
            e.printStackTrace();
        }
    }
}
