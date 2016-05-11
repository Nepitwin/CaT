package com.app.cat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.app.cat.R;
import com.app.cat.client.CATClient;
import com.app.cat.client.CATException;
import com.app.cat.model.CATFriend;
import com.app.cat.linphone.LinphoneCATClient;
import com.app.cat.model.CATUser;
import com.app.cat.model.CATAccount;
import com.app.cat.service.CATService;
import com.app.cat.ui.component.TelephoneBookAdapter;
import com.app.cat.util.ApplicationContext;
import com.app.cat.util.PropertiesLoader;

import org.linphone.core.LinphoneCoreException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
     * List view ui element for an telephone book.
     */
    @Bind(R.id.listViewTBook)
    public ListView listTBook;

    /**
     * Adapter which handles telephone book ui with corresponding user models.
     */
    private TelephoneBookAdapter telephoneBookAdapter;

    /**
     * CATUser model from an SIP-User.
     */
    private CATUser catUser;

    private CATFriend catFriend;

    /**
     * Configuration file to mockup user data from assets file.
     */
    private Map<String, String> configuration;

    /**
     * Service implementation which runs in background.
     */
    private Intent service;

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

        // Set UI application context
        ApplicationContext.setContext(this);

        // Starts an service in background
        service = new Intent(MainActivity.this, CATService.class);
        startService(service);

        // Mockup telephone book ui data.
        List<CATAccount> catAccounts = new ArrayList<CATAccount>();
        for(int i = 0; i < 10; i++) {
            catAccounts.add(new CATFriend("Mockup " + i, "Mockup Domain"));
        }
        telephoneBookAdapter = new TelephoneBookAdapter(this, catAccounts);
        listTBook.setAdapter(telephoneBookAdapter);

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

            catUser = new CATUser(
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
                client.register(catUser);
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

    @Override
    protected void onStop() {
        super.onStop();

        // Activity stops... kill background server
        // In productive this service runs all the time as an sub process.
        if(service != null) {
            stopService(service);
            service = null;
        }
    }
}
