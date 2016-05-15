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

package com.app.cat.model;

/**
 * Abstract class from an default user.
 */
public abstract class CATAccount {

    /**
     * Username from client.
     */
    private String username;

    /**
     * SIP domain url.
     */
    private String domain;

    /**
     * Constructor to create an default cat account.
     * @param username Username from user.
     * @param domain Domain url from user.
     */
    public CATAccount(String username, String domain) {
        this.username = username;
        this.domain = domain;
    }

    /**
     * Gets username from account.
     * @return String from username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username from account.
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
     * Sets domain from sip account.
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
