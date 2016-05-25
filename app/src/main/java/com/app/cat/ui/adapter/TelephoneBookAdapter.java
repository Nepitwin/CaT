/*
 * Copyright (c) 2016.
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

import com.app.cat.R;
import com.app.cat.model.CATAccount;
import com.app.cat.util.ApplicationContext;
import com.app.cat.util.CatSettings;

import java.util.List;

/**
 * Class to create an list adapter for an telephone book list view.
 *
 * @author Andreas Sekulski
 */
public class TelephoneBookAdapter extends ArrayAdapter<CATAccount> {

    /**
     * Corresponding layout from adapter
     */
    private static final int LAYOUT = R.layout.layout_list_telephone_book;

    /**
     * Constructor to create an list key adapter.
     *
     * @param context Application context from adapter.
     * @param users List from all users to show.
     */
    public TelephoneBookAdapter(Context context, List<CATAccount> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(LAYOUT, parent, false);

        CATAccount user = getItem(position);

        // ToDo : Button interaction handling for audio and video calls
        Button audio = (Button) rowView.findViewById(R.id.buttonAudioCall);

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationContext.runIntent(ApplicationContext.ACTIVITY_CALL);
            }
        });

        Button video = (Button) rowView.findViewById(R.id.buttonVideoCall);

        audio.getBackground().setColorFilter(CatSettings.DEFAULT_BUTTON_COLOR, PorterDuff.Mode.MULTIPLY);
        video.getBackground().setColorFilter(CatSettings.DEFAULT_BUTTON_COLOR, PorterDuff.Mode.MULTIPLY);

        return rowView;
    }
}
