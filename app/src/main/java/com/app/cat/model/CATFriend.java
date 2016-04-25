package com.app.cat.model;

/**
 * CATFriend implementation from an sip friend.
 *
 * @author Andreas Sekulski
 */
public class CATFriend extends CATUser {

    /**
     * Constructor to create an cat friend.
     * @param friendUsername username from friend.
     * @param domain Domain url from friend.
     */
    public CATFriend(String friendUsername, String domain) {
        super(friendUsername, domain);
    }
}
