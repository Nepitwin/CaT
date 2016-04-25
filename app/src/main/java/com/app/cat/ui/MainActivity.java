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
import com.app.cat.model.CATFriend;
import com.app.cat.linphone.LinphoneCATClient;
import com.app.cat.model.CATOwner;
import com.app.cat.util.PropertiesLoader;

import org.linphone.core.LinphoneCoreException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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

    /**
     * CATOwner model from an SIP-User.
     */
    private CATOwner catOwner;

    private CATFriend catFriend;

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
            // Get singleton object.
            int udp = 0;
            int tcp = 5060;
            int tls = 0;

            client = LinphoneCATClient.getInstance();
            client.setTransportType(udp, tcp, tls);

            configuration = PropertiesLoader.loadProperty(
                    this.getAssets().open("config.properties"),
                    Arrays.asList("username", "password", "domain", "friendUsername"));

            catOwner = new CATOwner(
                    configuration.get("username"),
                    configuration.get("password"),
                    configuration.get("domain"));

            catFriend = new CATFriend(configuration.get("friendUsername"),
                    configuration.get("domain"));

        } catch (IOException io) {
            // ToDo := Error handling in Android UI... Everytime the same... Donuts...
            Log.e("MainActivity", io.getMessage());
        } catch (LinphoneCoreException e) {
            // ToDo := Error handling in Android UI... Everytime the same... Donuts...
            Log.e("Init failed", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            // ToDo := Error handling in Android UI... Everytime the same... Donuts...
            e.printStackTrace();
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try {
                client.register(catOwner);
                client.addFriend(catFriend);
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
    }
}
