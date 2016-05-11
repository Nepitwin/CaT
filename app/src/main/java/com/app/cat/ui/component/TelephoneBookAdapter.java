package com.app.cat.ui.component;

import android.content.Context;
import android.graphics.Color;
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
