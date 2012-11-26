package com.github.nmorel.homework.client.ui;

/**
 * Represents the state a view can take
 * 
 * @author Nicolas Morel
 */
public enum State
{
    /**
     * Default state. Usually when the user see the view for the first time.
     */
    DEFAULT,

    /**
     * Used when a request to the server is made to show to the user that a request is in progess.
     */
    LOADING,

    /**
     * After a request to the server has returned. Usually show a list of results.
     */
    LOADED,

    /**
     * After a request to the server has returned in error.
     */
    ERROR;
}
