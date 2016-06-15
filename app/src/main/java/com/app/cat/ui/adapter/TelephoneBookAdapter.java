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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cat.R;
import com.app.cat.client.CATClient;
import com.app.cat.linphone.LinphoneCATClient;
import com.app.cat.model.CATFriend;
import com.app.cat.ui.listener.AudioCallListener;
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
     * Cat friend to call.
     */
    private CATFriend catFriend;

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
            ApplicationContext.showToast(
                    ApplicationContext.getStringFromRessources(R.string.unknown_error_message),
                    Toast.LENGTH_SHORT);
            e.printStackTrace();
        }

        final CATFriend friend = getItem(position);

        Button audio = (Button) rowView.findViewById(R.id.buttonAudioCall);
        audio.setOnClickListener(new AudioCallListener(position, this, client));

        Button video = (Button) rowView.findViewById(R.id.buttonVideoCall);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ToDo : Button interaction handling for video calls
            }
        });

        audio.getBackground().setColorFilter(CatSettings.DEFAULT_BUTTON_COLOR, PorterDuff.Mode.MULTIPLY);
        video.getBackground().setColorFilter(CatSettings.DEFAULT_BUTTON_COLOR, PorterDuff.Mode.MULTIPLY);

        // Set name of the user
        TextView textview = (TextView) rowView.findViewById(R.id.textView);
        textview.setText(friend.getUsername());

        return rowView;
    }

    public void setCatFriend(CATFriend catFriend) {
        this.catFriend = catFriend;
    }

    public CATFriend getCatFriend() {
        return catFriend;
    }
}
