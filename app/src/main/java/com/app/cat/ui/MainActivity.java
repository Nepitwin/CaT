/*
 * This program is an Voice over IP client for Android devices.
 * Copyright (C) 2016 Andreas Sekulski, Dimitri Kotlovsky
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.app.cat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cat.R;
import com.app.cat.client.CATClient;
import com.app.cat.linphone.LinphoneCATClient;
import com.app.cat.model.CATFriend;
import com.app.cat.model.CATUser;
import com.app.cat.service.CATService;
import com.app.cat.ui.adapter.TelephoneBookAdapter;
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

/**
 * The Main Activity represents the main user interface.
 *
 * @author Andreas Sekulski, Dimitri Kotlovsky
 */
public class MainActivity extends AppCompatActivity {

    /**
     * List view ui element for an telephone book.
     */
    @Bind(R.id.listViewTBook)
    public ListView listTBook;

    @Bind(R.id.testViewInfo)
    public TextView info;

    /**
     * Adapter which handles telephone book ui with corresponding user models.
     */
    private TelephoneBookAdapter telephoneBookAdapter;

    /**
     * CATUser model from an SIP-User.
     */
    private CATUser catUser;

    /**
     * Mpckup cat friend.
     */
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
        ApplicationContext.setActivity(this);

        // Mockup telephone book ui data.
        List<CATFriend> catAccounts = new ArrayList<CATFriend>();
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

            info.setText("User = " + configuration.get("username"));

            catFriend = new CATFriend(configuration.get("friendUsername"), configuration.get("domain"));
            catAccounts.add(catFriend);

            client.addCATFriend(catFriend);
            client.setCATUser(catUser);

            // Starts an service in background
            service = new Intent(MainActivity.this, CATService.class);
            startService(service);

        } catch (IOException io) {
            ApplicationContext.showToast(
                    ApplicationContext.getStringFromRessources(R.string.unknown_error_message),
                    Toast.LENGTH_SHORT);
        } catch (LinphoneCoreException e) {
            ApplicationContext.showToast(
                    ApplicationContext.getStringFromRessources(R.string.unknown_error_message),
                    Toast.LENGTH_SHORT);
        } catch (NoSuchAlgorithmException e) {
            ApplicationContext.showToast(
                    ApplicationContext.getStringFromRessources(R.string.unknown_error_message),
                    Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Activity stops... kill background server
        // In productive this service runs all the time as an sub process.
        if(service != null) {
            stopService(service);
            service = null;
        }
    }
}