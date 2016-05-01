package com.app.cat.service;

/**
 * Voice over IP interface to handle server communication.
 *
 * @author Andreas Sekulski
 */
public interface VoIPService {

    /**
     * Stops an Voice over IP server communication.
     */
    void stop();

    /**
     * Starts an Voice over IP server communication.
     */
    void start();

    /**
     * Checks if VoIPService handler is currently running or not.
     * @return true if is running otherwise false.
     */
    boolean isRunning();
}