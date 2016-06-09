package com.app.cat.ui.listener;

/**
 * Listener for interactions with the Call Fragment.
 * <p/>
 * This interface must be implemented by activities that contain the "Call Fragment"
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 *
 * @author Dimitri Kotlovsky
 */
public interface OutgoingCallFragmentListener {

    /**
     * Is invoked when user hangs up the call.
     */
    void onHangUp();
}
