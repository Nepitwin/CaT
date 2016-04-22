package com.app.cat.client;

/**
 * Voice over IP CAT client interface, to handle all supported features like an audio or video call.
 *
 * @author Andreas Sekulski
 */
public interface CATClient {

    /**
     * Register an given user to an SIP Server
     * @param username Username to register.
     * @param ha1 HA1 password.
     * @param realm If realm unequal domain set realm ip otherwise null.
     * @param domain Domain URL to an corresponding SIP-Server
     * @throws CATException Throws an CATException if settings invalid.
     */
    public void register(String username, String ha1, String realm, String domain) throws CATException;

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
}
