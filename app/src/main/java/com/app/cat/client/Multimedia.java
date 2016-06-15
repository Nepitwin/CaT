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

/**
 * Multimedia interface for streaming components like video or audio.
 *
 * @author Andreas Sekulski
 */
public interface Multimedia {

    /**
     * Try to call an given friend.
     * @param catFriend Friend to call.
     */
    void callFriend(CATFriend catFriend);

    /**
     * Accepts an call if exists.
     */
    void acceptCall();

    /**
     * Decline an call if exists.
     */
    void declineCall();

    /**
     * Returns <code>true</code> if the current call is an incoming call.
     * @return <code>true</code> if the current call is an incoming call
     */
    boolean isIncomingCall();
}
