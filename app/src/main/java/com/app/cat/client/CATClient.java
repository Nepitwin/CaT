package com.app.cat.client;

import com.app.cat.model.CATFriend;
import com.app.cat.model.CATOwner;

/**
 * Voice over IP CAT client interface, to handle all supported features like an audio or video call.
 *
 * @author Andreas Sekulski, Dimitry Kotlovsky
 */
public interface CATClient {

    /**
     * Register a given user to an SIP Server.
     * @param catOwner CATOwner model from an client to register to an sip server.
     * @throws CATException
     */
    public void register(CATOwner catOwner) throws CATException;

    /**
     * Unregister an user on an registered SIP-Server
     */
    public void unregister();

    /**
     * Sets supported transport types to create an connection. If transport type will be set to 0
     * this type is not supported.
     * @param udp UDP transport port.
     * @param tcp TCP transport port.
     * @param tls TLS transport port.
     */
    public void setTransportType(int udp, int tcp, int tls);

    /**
     * Adds a friend with the given username and domain.
     * @param catFriend An friend to add.
     * @throws CATException Throws an CATException if settings are invalid.
     */
    public void addFriend(CATFriend catFriend) throws CATException;

    /**
     * Enables the presence status.
     */
    public void enablePresenceStatus();

    /**
     * Disables the presence status.
     */
    public void disablePresenceStatus();
}
