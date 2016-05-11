package com.app.cat.ui;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.cat.R;
import com.app.cat.util.ApplicationContext;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CallActivity extends AppCompatActivity {

    /**
     * Accept button.
     */
    @Bind(R.id.buttonCallAccept)
    Button accept;

    /**
     * Decline
     */
    @Bind(R.id.buttonCallDecline)
    Button decline;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        accept.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        decline.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationContext.runIntent(ApplicationContext.ACTIVITY_TALK);
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationContext.runIntent(ApplicationContext.ACTIVITY_MAIN);
            }
        });

        // Set UI application context
        ApplicationContext.setContext(this);
    }
}
