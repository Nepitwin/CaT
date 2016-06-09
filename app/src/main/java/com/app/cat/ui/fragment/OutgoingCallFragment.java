package com.app.cat.ui.fragment;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.cat.R;
import com.app.cat.ui.listener.OutgoingCallFragmentListener;
import com.app.cat.util.CatSettings;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * The Outgoing Call Fragment represents the user interface for waiting or declining calls.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OutgoingCallFragmentListener}
 * interface.
 *
 * @author Dimitri Kotlovsky
 */
public class OutgoingCallFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OutgoingCallFragmentListener mListener;

    /**
     * Hang Up button.
     */
    @Bind(R.id.buttonHangUp)
    Button hangUp;

    @Bind(R.id.textViewOutgoingCallName)
    TextView textViewName;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment.
     */
    public OutgoingCallFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OutgoingCallFragment newInstance(int columnCount) {
        OutgoingCallFragment fragment = new OutgoingCallFragment();
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
        View view = inflater.inflate(R.layout.fragment_outgoing_call, container, false);
        ButterKnife.bind(this, view);

        hangUp.getBackground().setColorFilter(CatSettings.HANGUP_BUTTON_COLOR, PorterDuff.Mode.MULTIPLY);

        hangUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onHangUp();
            }
        });

        textViewName.setText("Irgendwas muss hier rein... mhmm donuts...");

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OutgoingCallFragmentListener) {
            mListener = (OutgoingCallFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OutgoingCallFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}