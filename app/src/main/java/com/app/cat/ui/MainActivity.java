package com.app.cat.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.app.cat.R;
import com.app.cat.client.CATClient;
import com.app.cat.client.CATException;
import com.app.cat.linphone.LinphoneCATClient;
import com.app.cat.util.HashGenerator;

import org.linphone.core.LinphoneCoreException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

        Properties prop = new Properties();
        InputStream input = null;

        try {
            // Mockup user...
            input = this.getAssets().open("config.properties");
            // load a properties file
            prop.load(input);

            // get the property value and print it out
            username = prop.getProperty("username");
            password = prop.getProperty("password");
            domain = prop.getProperty("domain");
            friendUsername = prop.getProperty("friendUsername");
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
