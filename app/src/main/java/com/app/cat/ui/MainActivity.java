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

import java.util.ArrayList;
import java.util.List;

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
     * Spinner entity to store IP's.
     */
    @Bind(R.id.spinnerIP)
    public Spinner spinnerIP;

    // SIP mockup user information
    private static final String username = "Doe";
    private static final String password = "doe";

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

        List<String> list = new ArrayList<String>();
        list.add("192.168.1.137");
        list.add("192.168.117.102");
        list.add("192.168.2.186");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIP.setAdapter(dataAdapter);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String domain = spinnerIP.getSelectedItem().toString();
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
