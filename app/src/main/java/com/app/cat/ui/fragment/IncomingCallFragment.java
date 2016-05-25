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

package com.app.cat.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.cat.R;
import com.app.cat.ui.listener.IncomingCallFragmentListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * The Incoming Call Fragment represents the user interface for accepting or declining calls.
 * <p/>
 * Activities containing this fragment MUST implement the {@link IncomingCallFragmentListener}
 * interface.
 *
 * @author Dimitry Kotlovsky
 */
public class IncomingCallFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private IncomingCallFragmentListener mListener;

    /**
     * Accept button.
     */
    @Bind(R.id.buttonCallAccept)
    Button accept;

    /**
     * Decline button.
     */
    @Bind(R.id.buttonCallDecline)
    Button decline;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment.
     */
    public IncomingCallFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CallFragment newInstance(int columnCount) {
        CallFragment fragment = new CallFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incoming_call, container, false);
        ButterKnife.bind(this, view);

        accept.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        decline.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAcceptCall();
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDeclineCall();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IncomingCallFragmentListener) {
            mListener = (IncomingCallFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement IncomingCallFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
