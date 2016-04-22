package com.app.cat.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.app.cat.R;
import com.app.cat.client.CATClient;
import com.app.cat.client.CATException;
import com.app.cat.linphone.LinphoneCATClient;
import com.app.cat.util.HashGenerator;

import org.linphone.core.LinphoneCoreException;

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

    // SIP mockup user information
    private static final String username = "Doe";
    private static final String password = "doe";
    private static final String domain = "192.168.1.137";
    //private static final String domain = "192.168.2.186"; // dimi: 192.168.1.137   andy: [192.168.117.102; 192.168.2.186]

    // SIP mockup friend information
    private static final String friendUsername = "John";

    /**
     * Cat client instance.
     */
    private CATClient client;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    client.register(username, HashGenerator.ha1(username, domain, password), null, domain);
                    client.addFriend(friendUsername, domain);
                    // ToDo := Presence should wait until adding friends is done !!!
                    client.enablePresenceStatus();
                } catch (CATException e) {
                    // ToDo := Error handling in Android UI... Everytime the same... Donuts...
                    e.printStackTrace();
                }
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.disablePresenceStatus();
                // ToDo := Unregister should wait until presence is done !!!
                client.unregister();
            }
        });

        try {
            // Get singleton object.
            client = LinphoneCATClient.getInstance();
        } catch (LinphoneCoreException e) {
            // ToDo := Exception handling in Android UI
            Log.e("Init failed", e.getMessage());
        }
    }
}
