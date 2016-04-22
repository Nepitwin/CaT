package com.app.cat.client;

/**
 * CATException if an error occured.
 *
 * @author Andreas Sekulski
 */
public class CATException extends Exception {

    /**
     * Stores error message.
     * @param message Error message to store.
     */
    public CATException(String message) {
        super(message);
    }
}
