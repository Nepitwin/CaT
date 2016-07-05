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

package com.app.cat.client;

import android.content.Context;

import com.app.cat.model.CATFriend;
import com.app.cat.model.CATUser;

/**
 * Voice over IP CAT client interface, to handle all supported features like an audio or video call.
 *
 * @author Andreas Sekulski, Dimitri Kotlovsky
 */
public interface CATClient {

    /**
     * Adds an cat friend from contact book.
     * @param catFriend Friend from contact book to call.
     */
    void addCATFriend(CATFriend catFriend);

    /**
     * Register a given user to an SIP Server.
     * @param catUser User to try to register.
     */
    void register(CATUser catUser);

    /**
     * Unregister an user on an registered SIP-Server
     */
    void unregister();

    /**
     * Sets supported transport types to create an connection. If transport type will be set to 0
     * this type is not supported.
     * @param udp UDP transport port.
     * @param tcp TCP transport port.
     * @param tls TLS transport port.
     */
    void setTransportType(int udp, int tcp, int tls);

    /**
     * Try to call an friend.
     *
     * @param isVideoCall Is call an video (true) or audio call (false).
     * @param catFriend Friend to call.
     */
    void callFriend(boolean isVideoCall, CATFriend catFriend);

    /**
     * Accepts an incoming call if exists.
     */
    void acceptCall();

    /**
     * Decline an incoming call if exists.
     */
    void declineCall();

    /**
     * Sets an application context.
     * @param context Application context to set.
     */
    void setContext(Context context);

    /**
     * Indicates whether the current call is is an incoming call or not (that is an outgoing call).
     * @return <code>true</code> if the current call is an incoming call
     */
    Boolean isIncomingCall();

    /**
     * Indicates whether the current call is a video call oder not (that is an audio call).
     * @return <code>true</code> if the current call is a video call and <code>false</code> if it is
     *         an audio call
     */
    Boolean isVideoCall();
}
