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

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.cat.R;
import com.app.cat.linphone.LinphoneCATClient;
import com.app.cat.util.ApplicationContext;
import com.app.cat.util.CatSettings;

import org.linphone.core.LinphoneCoreException;
import org.linphone.mediastream.video.AndroidVideoWindowImpl;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoCallActivity extends AppCompatActivity {

    /**
     * View for the phone video of the call partner.
     */
    @Bind(R.id.videoSurface)
    SurfaceView mVideoView;

    /**
     * View for the captured video of the own phone camera.
     */
    @Bind(R.id.videoCaptureSurface)
    SurfaceView mCaptureView;

    /**
     * Hang Up button.
     */
    @Bind(R.id.buttonHangUp)
    Button hangUpButton;

    /**
     * View for the controls that are hidden on fullscreen.
     */
    @Bind(R.id.fullscreen_content_controls)
    View mControlsView;

    private AndroidVideoWindowImpl androidVideoWindowImpl;

    /**
     * Cat client instance.
     */
    LinphoneCATClient client;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private final Handler mHideHandler = new Handler();

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mVideoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };

    private boolean mVisible;

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videocall);

        // Set UI application context
        ApplicationContext.setActivity(this);

        // Get linphone client
        try {
            client = LinphoneCATClient.getInstance();
        } catch (LinphoneCoreException e) {
            ApplicationContext.showToast(
                    ApplicationContext.getStringFromRessources(R.string.unknown_error_message),
                    Toast.LENGTH_SHORT);
            e.printStackTrace();
        }

        // Fullscreen view handling
        mVisible = true;

        // Set up the user interaction to manually show or hide the system UI.
        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        hangUpButton.setOnTouchListener(mDelayHideTouchListener);

        // Set Button background color
        hangUpButton.getBackground().setColorFilter(CatSettings.HANGUP_BUTTON_COLOR,
                PorterDuff.Mode.MULTIPLY);

        // Decline Call if hang up button is clicked
        hangUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.declineCall();
            }
        });

        // Fix z order of the two video views
        fixZOrder(mVideoView, mCaptureView);

        // Bind the video views to the linphone video window implementation
        androidVideoWindowImpl = new AndroidVideoWindowImpl(mVideoView, mCaptureView,
                new AndroidVideoWindowImpl.VideoWindowListener() {

            public void onVideoRenderingSurfaceReady(AndroidVideoWindowImpl vw, SurfaceView surf) {
                mVideoView = surf;
                client.getCore().setVideoWindow(vw);
            }

            public void onVideoPreviewSurfaceReady(AndroidVideoWindowImpl vw, SurfaceView surf) {
                mCaptureView = surf;
                client.getCore().setPreviewWindow(mCaptureView);
            }

            public void onVideoRenderingSurfaceDestroyed(AndroidVideoWindowImpl vw) {}
            public void onVideoPreviewSurfaceDestroyed(AndroidVideoWindowImpl vw) {}
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (androidVideoWindowImpl != null) {
            synchronized (androidVideoWindowImpl) {
                client.getCore().setVideoWindow(androidVideoWindowImpl);
            }
        }
    }

    @Override
    public void onPause() {
        if (androidVideoWindowImpl != null) {
            synchronized (androidVideoWindowImpl) {
				/*
				 * this call will destroy native opengl renderer which is used by
				 * androidVideoWindowImpl
				 */
                client.getCore().setVideoWindow(null);
            }
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (androidVideoWindowImpl != null) {
            // Prevent linphone from crashing if correspondent hang up while you are rotating
            androidVideoWindowImpl.release();
            androidVideoWindowImpl = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mVideoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void fixZOrder(SurfaceView video, SurfaceView preview) {
        video.setZOrderOnTop(false);
        preview.setZOrderOnTop(true);
        preview.setZOrderMediaOverlay(true); // Needed to be able to display control layout over
    }
}
