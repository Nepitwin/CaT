package com.app.cat.client;

/**
 * Voice over IP interface to handle server communication.
 *
 * @author Andreas Sekulski
 */
public interface VoIP {

    /**
     * Stops an Voice over IP server communication.
     */
    void stop();

    /**
     * Starts an Voice over IP server communication.
     */
    void start();

    /**
     * Checks if VoIP handler is currently running or not.
     * @return true if is running otherwise false.
     */
    boolean isRunning();
}