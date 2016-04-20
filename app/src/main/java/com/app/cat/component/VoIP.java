package com.app.cat.component;

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
}