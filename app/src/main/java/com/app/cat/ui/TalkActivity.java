package com.app.cat.ui;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.cat.R;
import com.app.cat.util.ApplicationContext;

import butterknife.ButterKnife;

public class TalkActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        // Set UI application context
        ApplicationContext.setContext(this);
    }
}
