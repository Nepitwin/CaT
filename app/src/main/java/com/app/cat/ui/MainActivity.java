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
import com.app.cat.util.PropertiesLoader;

import org.linphone.core.LinphoneCoreException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

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
    private String username;
    private String password;
    private String domain;

    // SIP mockup friend information
    private String friendUsername;

    /**
     * Configuration file to mockup user data from assets file.
     */
    private Map<String, String> configuration;

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

        try {
            configuration =  PropertiesLoader.loadProperty(
                    this.getAssets().open("config.properties"),
                    Arrays.asList("username", "password", "domain", "friendUsername"));

            // get configuration value and print it out
            username = configuration.get("username");
            password = configuration.get("password");
            domain = configuration.get("domain");
            friendUsername = configuration.get("friendUsername");

        } catch (IOException io) {
            // ToDo := Error handling in Android UI... Everytime the same... Donuts...
            Log.e("MainActivity", io.getMessage());
        }

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
