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
            proxyConfig = core.createProxyConfig();

            address = factory.createLinphoneAddress("sip:John@192.168.2.186");
            // String username, String userid, String passwd, String ha1, String realm, String domain
            authInfo = factory.createAuthInfo(
                    "John@192.168.2.186", // Username
                    null,  // UserID
                    "john",  // Password
                    null,  // Hash password
                    null, // Realm
                    "192.168.2.186"); // Domain

            // Method enableRegister returns an new LinphoneProxyConfig back...
            proxyConfig = proxyConfig.enableRegister(true);
            proxyConfig.setIdentity("sip:John@192.168.2.186");
            proxyConfig.setProxy(address.getDomain());

            core.addAuthInfo(authInfo);
            core.addProxyConfig(proxyConfig);
            core.setDefaultProxyConfig(proxyConfig);
            core.iterate();

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
