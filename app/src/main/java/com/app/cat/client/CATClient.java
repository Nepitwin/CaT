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

import com.app.cat.model.CATFriend;
import com.app.cat.model.CATUser;

import org.linphone.core.LinphoneFriend;

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
     * Sets mobile cat user to client.
     * @param catUser User to set from device.
     */
    void setCATUser(CATUser catUser);

    /**
     * Register a given user to an SIP Server.
     * @return <code>true</code> if registration process was successful
     */
    boolean register();

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
     * Adds a friend with the given username and domain.
     * @param catFriend An friend to add.
     */
    void addFriend(CATFriend catFriend);

    /**
     * Enables the presence status.
     */
    void enablePresenceStatus();

    /**
     * Disables the presence status.
     */
    void disablePresenceStatus();

    /**
     * Try to call an friend.
     */
    void callFriend();

    /**
     * Sets the CATFriend to be called next.
     * @param catFriend Friend to be called next.
     */
    void setFriendToCall(CATFriend catFriend);

    /**
     * Accepts an incoming call if exists.
     */
    void acceptCall();

    /**
     * Decline an incoming call if exists.
     */
    void declineCall();

    /**
     * Request to update server information.
     */
    void updateServerInformation();

    /**
     * Returns the corresponding LinphoneFriend to the given CATFriend if he exists.
     * @param catFriend CATFriend
     * @return LinphoneFriend corresponding to the given CATFriend
     */
    LinphoneFriend getLinphoneFriend(CATFriend catFriend);
}
