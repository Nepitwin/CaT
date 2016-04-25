package com.app.cat.model;

/**
 * Abstract class from an default user.
 */
public abstract class CATUser {

    /**
     * Username from client.
     */
    private String username;

    /**
     * SIP domain url.
     */
    private String domain;

    /**
     * Constructor to create an default cat user.
     * @param username Username from user.
     * @param domain Domain url from user.
     */
    public CATUser(String username, String domain) {
        this.username = username;
        this.domain = domain;
    }

    /**
     * Gets username from user.
     * @return String from username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username from user.
     * @param username Username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets domain from user sip url.
     * @return Domain from sip url.
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Sets domain from sip user.
     * @param domain Domain to set.
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * Get SIP account name with domain syntax.
     * @return SIP account url.
     */
    public String getSIPAccount() {
        return "sip:" + getUsername() + "@" + getDomain();
    }
}
