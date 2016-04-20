package com.app.cat;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.app.cat.component.CaTServerListener;
import com.app.cat.component.VoIP;
import com.app.cat.component.VoIPHandler;

import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneAuthInfo;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreFactory;
import org.linphone.core.LinphoneCoreListener;
import org.linphone.core.LinphoneProxyConfig;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    /**
     * Login button.
     */
    @Bind(R.id.buttonLogin)
    public Button buttonLogin;

    /**
     * Logout button.
     */
    @Bind(R.id.buttonLogout)
    public Button buttonLogout;

    // ToDo := Class implementation from Linphone (Interface + Implementation)
    LinphoneCore core;
    LinphoneProxyConfig proxyConfig;
    LinphoneAddress address;
    LinphoneCoreFactory factory;
    LinphoneAuthInfo authInfo;
    LinphoneCoreListener server;
    VoIP voIP;

    boolean isLoginClicked;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String username = "Doe";
        final String password = "doe";
        final String domain = "192.168.117.102";
        final String sip = "sip:" + username + "@" + domain;

        try {
            // Default init

            // Create an factory instance
            factory = LinphoneCoreFactory.instance();
            // SIP-Server listener implementation
            server = new CaTServerListener();
            core = factory.createLinphoneCore(server, null);
            // Create an proxy configuration class from core.
            proxyConfig = core.createProxyConfig();

            voIP = new VoIPHandler(core);
            voIP.start();

            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ToDo: Class implementation
                    // Example Registration
                    try {
                        if(!isLoginClicked) {

                            Log.i("Clicked", "Login motherfucker");

                            // Username, Domain, DisplayName
                            address = factory.createLinphoneAddress(username, domain, username);

                            // Combination from Username, Password and Domain stores password clear.
                            authInfo = factory.createAuthInfo(
                                    username, // Username
                                    password,  // Password
                                    null, // Realm
                                    domain); // Domain

                            // Method enableRegister returns an new LinphoneProxyConfig back...
                            proxyConfig = proxyConfig.enableRegister(true);
                            proxyConfig.setIdentity(sip);
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

                            // ToDo : Check if addAuthInfo and ProxyConfig must be deleted.
                            // Append authentication information to core
                            core.addAuthInfo(authInfo);
                            core.addProxyConfig(proxyConfig);
                            core.setDefaultProxyConfig(proxyConfig);

                            Log.i("Login", "Fuck yeahhh" + proxyConfig.isRegistered());

                            isLoginClicked = true;

                        } else {
                            proxyConfig.edit(); // Start editing proxy configuration
                            proxyConfig.enableRegister(true); // Activate registration for this proxy config
                            proxyConfig.done(); // Initiate REGISTER with expire = 0
                        }
                    } catch (LinphoneCoreException e) {
                        e.printStackTrace();
                    }
                }
            });

            buttonLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i("Clicked", "Logout motherfucker");

                    // Example Unregistration
                    proxyConfig.edit(); // Start editing proxy configuration
                    proxyConfig.enableRegister(false); // De-activate registration for this proxy config
                    proxyConfig.done(); // Initiate REGISTER with expire = 0

                }
            });

        } catch (LinphoneCoreException e) {
            e.printStackTrace();
        }
    }
}
