package com.github.nmorel.homework.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Homework entry point
 */
public class Homework
    implements EntryPoint
{

    /**
     * This is the entry point method.
     */
    public void onModuleLoad()
    {
        RootPanel.get().add( new Label( "Hello World!" ) );
    }
}
