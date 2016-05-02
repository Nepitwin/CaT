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
    private String password;

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
        this.password = HashGenerator.ha1(username, domain, password);
    }

    /**
     * Returns an ha1 password.
     * @return Ha1 password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets an password to this user and stores it as an ha1 hash.
     * @param password Password to store.
     * @throws NoSuchAlgorithmException Throws an NoSuchAlgorithmException if password hash operation not supported.
     */
    public void setPassword(String password) throws NoSuchAlgorithmException {
        this.password = HashGenerator.ha1(getUsername(), getDomain(), password);
    }
}
