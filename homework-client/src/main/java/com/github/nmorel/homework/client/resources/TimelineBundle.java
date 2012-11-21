package com.github.nmorel.homework.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface TimelineBundle
    extends ClientBundle
{
    public interface Style
        extends CssResource
    {
    }

    @Source( { "timeline.css" } )
    Style style();
}
