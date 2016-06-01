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

package com.app.cat.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.app.cat.R;
import com.app.cat.client.CATClient;
import com.app.cat.client.CATException;
import com.app.cat.linphone.LinphoneCATClient;
import com.app.cat.model.CATFriend;
import com.app.cat.ui.CallActivity;
import com.app.cat.util.ApplicationContext;
import com.app.cat.util.CatSettings;

import org.linphone.core.LinphoneCoreException;

import java.util.List;

/**
 * Class to create an list adapter for an telephone book list view.
 *
 * @author Andreas Sekulski, Dimitri Kotlovsky
 */
public class TelephoneBookAdapter extends ArrayAdapter<CATFriend> {

    /**
     * Corresponding layout from adapter
     */
    private static final int LAYOUT = R.layout.layout_list_telephone_book;

    /**
     * Cat client instance.
     */
    private CATClient client;

    /**
     * Constructor to create an list key adapter.
     *
     * @param context Application context from adapter.
     * @param users List from all friends to show.
     */
    public TelephoneBookAdapter(Context context, List<CATFriend> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(LAYOUT, parent, false);

        try {
            client = LinphoneCATClient.getInstance();
        } catch (LinphoneCoreException e) {
            // ToDo := Error handling in Android UI... Everytime the same... Donuts...
            Log.e("TelephoneBookAdapter", e.getMessage());
        }

        final CATFriend catFriend = getItem(position);

        // ToDo : Button interaction handling for audio and video calls
        Button audio = (Button) rowView.findViewById(R.id.buttonAudioCall);

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Try to call an cool friend!
                try {
                    client.callFriend(catFriend);
                    Bundle bundle = new Bundle();
                    bundle.putInt(CallActivity.KEY_FRAGMENT_ID, CallActivity.FRAGMENT_CALL);
                    ApplicationContext.runIntentWithParams(ApplicationContext.ACTIVITY_CALL, bundle);
                } catch (CATException e) {
                    // ToDo := Error handling in Android UI... Everytime the same... Donuts...
                    e.printStackTrace();
                }
            }
        });

        Button video = (Button) rowView.findViewById(R.id.buttonVideoCall);

        audio.getBackground().setColorFilter(CatSettings.DEFAULT_BUTTON_COLOR, PorterDuff.Mode.MULTIPLY);
        video.getBackground().setColorFilter(CatSettings.DEFAULT_BUTTON_COLOR, PorterDuff.Mode.MULTIPLY);

        // Set name of the user
        TextView textview = (TextView) rowView.findViewById(R.id.textView);
        textview.setText(catFriend.getUsername());

        // ToDo: Consider changing the list items to LinphoneFriends instead of CATAccounts (would make things easier)
        // ToDo: Implement notifyDataSetChanged, so this is redrawn on changes
        // Analyse presence status
   /*     if (user instanceof CATFriend) {
            try {
                client = LinphoneCATClient.getInstance();
                LinphoneFriend linphoneFriend = client.getLinphoneFriend((CATFriend) user);

                // Display status
                if (linphoneFriend != null) {
                    PresenceModel model = linphoneFriend.getPresenceModel();
                    textview.setText(user.getUsername() + " [" + model.getActivity().getType().name() + "]");
                }
            } catch (LinphoneCoreException e) {
                // ToDo := Error handling in Android UI... Everytime the same... Donuts...
                Log.e("TelephoneBookAdapter", e.getMessage());
            }
        }*/

        return rowView;
    }
}
