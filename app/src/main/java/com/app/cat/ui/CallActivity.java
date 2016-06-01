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

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.app.cat.R;
import com.app.cat.client.CATException;
import com.app.cat.linphone.LinphoneCATClient;
import com.app.cat.ui.fragment.CallFragment;
import com.app.cat.ui.fragment.IncomingCallFragment;
import com.app.cat.ui.listener.CallFragmentListener;
import com.app.cat.ui.listener.IncomingCallFragmentListener;
import com.app.cat.util.ApplicationContext;

import org.linphone.core.LinphoneCoreException;

import butterknife.ButterKnife;

/**
 * The Call Activity represents the user interface for outgoing or incoming calls.
 *
 * @author Andreas Sekulski, Dimitri Kotlovsky
 */
public class CallActivity extends AppCompatActivity implements CallFragmentListener, IncomingCallFragmentListener {

    /**
     * The Fragment Transaction object supports the replacement of fragments.
     */
    FragmentTransaction fragmentTransaction;

    public static final int FRAGMENT_CALL = 1;

    public static final int FRAGMENT_INCOMING_CALL = 2;

    public static final String KEY_FRAGMENT_ID = "FRAGMENT";


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        Bundle bundle = getIntent().getExtras();
        int fragment = bundle.getInt(KEY_FRAGMENT_ID);

        if (savedInstanceState == null) {
            if(fragment == FRAGMENT_CALL) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction().add(R.id.callUIContainer, new CallFragment());
            } else if(fragment == FRAGMENT_INCOMING_CALL) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction().add(R.id.callUIContainer, new IncomingCallFragment());
            } else {
                // ToDo : WTF Error
            }
            fragmentTransaction.commit();
        }

        // Set UI application context
        ApplicationContext.setContext(this);
    }

    @Override
    public void onAcceptCall() {
        try {
            LinphoneCATClient.getInstance().acceptCall();
            fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.callUIContainer, new CallFragment());
            fragmentTransaction.commit();
        } catch (LinphoneCoreException e) {
            // ToDo := Error handling in Android UI... Everytime the same... Donuts...
            e.printStackTrace();
        } catch (CATException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeclineCall() {
        try {
            LinphoneCATClient.getInstance().declineCall();
        } catch (LinphoneCoreException e) {
            // ToDo := Error handling in Android UI... Everytime the same... Donuts...
            e.printStackTrace();
        }
    }

    @Override
    public void onHangUp() {
        onDeclineCall();
        ApplicationContext.runIntent(ApplicationContext.ACTIVITY_MAIN);
    }
}
