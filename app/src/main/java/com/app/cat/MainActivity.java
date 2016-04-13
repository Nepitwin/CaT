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
            factory = LinphoneCoreFactory.instance();
            server = new CaTServerListener();
            core = factory.createLinphoneCore(server, null);
            proxyConfig = core.createProxyConfig();
            address = factory.createLinphoneAddress("sip:John@10.0.38.151");
            // String username, String userid, String passwd, String ha1, String realm, String domain
            authInfo = factory.createAuthInfo("John", null, "john", null, null, "10.0.38.151");
            core.addAuthInfo(authInfo);

            proxyConfig.setIdentity("sip:John@10.0.38.151");

            String serverAdress = address.getDomain();
            proxyConfig.setProxy(serverAdress);
            proxyConfig.enableRegister(true);

            core.addProxyConfig(proxyConfig);
            core.setDefaultProxyConfig(proxyConfig);

            Log.i("CaT", "Finished");

        } catch (LinphoneCoreException e) {
            e.printStackTrace();
        }
    }
}
