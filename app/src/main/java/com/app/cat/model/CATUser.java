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

package com.app.cat.model;

import com.app.cat.util.HashGenerator;

import java.security.NoSuchAlgorithmException;

/**
 * CATUser model class represents an user model.
 *
 * @author Andreas Sekulski
 */
public class CATUser extends CATAccount {

    /**
     * Stored password from client.
     */
    private String HA1Password;

    /**
     * Create an cat owner class model. Password will be stored as an ha1.
     *
     * @param username Username from owner.
     * @param password Password from owner.
     * @param domain Domain url from owner.
     * @throws NoSuchAlgorithmException Throws an NoSuchAlgorithmException if password hash operation not supported.
     */
    public CATUser(String username, String password, String domain) throws NoSuchAlgorithmException {
        super(username, domain);
        this.HA1Password = HashGenerator.ha1(username, domain, password);
    }

    /**
     * Returns an ha1 password.
     * @return Ha1 password.
     */
    public String getHA1Password() {
        return HA1Password;
    }

    /**
     * Sets an password to this user and stores it as an HA1 hash.
     * @param password Password to store.
     * @throws NoSuchAlgorithmException Throws an NoSuchAlgorithmException if password hash operation not supported.
     */
    public void setHA1Password(String password) throws NoSuchAlgorithmException {
        this.HA1Password = HashGenerator.ha1(getUsername(), getDomain(), password);
    }
}
